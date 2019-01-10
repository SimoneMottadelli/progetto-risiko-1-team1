package com.drisk.controller;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.drisk.domain.game.Color;
import com.drisk.domain.lobby.LobbyManager;
import com.drisk.domain.map.MapManager;
import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@Controller
public class MatchController {
	
    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();
    private static final String SESSION_ATTRIBUTE_COLOR = "color";
    private static final String IS_NOT_A_PLAYER = "You are not a player";
    private JsonHelper helper = new JsonHelper();
	
	@PostMapping("/join")
	@ResponseBody
	public JsonObject join(HttpServletRequest request) {
		LobbyManager mm = LobbyManager.getInstance();
		if(mm.isMatchStarted())
			return helper.createResponseJson(-1, "The match has already started!");
		else if(mm.isMatchFull())
			return helper.createResponseJson(-1, "There are enough players!");
		else {
			HttpSession session = request.getSession(false);
			if(!isAPlayer(session)) {
				mm.joinGame(request.getParameter("name").trim());
				session = request.getSession();
				session.setAttribute(SESSION_ATTRIBUTE_COLOR, mm.findLastPlayerColor());
				return helper.createResponseJson(0, "You've joined the game!");
			}
			else
				return helper.createResponseJson(0, "Welcome back to joining room");
		}
	}
	
	@PostMapping("/gameConfig")
	@ResponseBody
	public JsonObject gameConfig(HttpServletRequest request) {
		if(!isAPlayer(request.getSession(false)))
			return helper.createResponseJson(-1, IS_NOT_A_PLAYER);
		try {
			String body = request.getReader().lines().collect(Collectors.joining());
			MapManager.getInstance().createMap(helper.parseJson(body));
			return helper.createResponseJson(0, "Map correctly added");
		} 
		catch (JsonSyntaxException e)
		{
			return helper.createResponseJson(-1, "Syntax error: cannot parse json object");
		}
		catch (Exception e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
	}
    	     
	@GetMapping("/info")
    public SseEmitter handleSse() {
		SseEmitter emitter = new SseEmitter();
		nonBlockingService.execute(() -> {
			try {
				JsonObject obj = LobbyManager.getInstance().toJson();
				obj.addProperty("mapReady", MapManager.getInstance().isMapReady());
				emitter.send(obj);
				emitter.complete();	
			} catch (Exception ex) {
				emitter.completeWithError(ex);
			}
		});
		return emitter;
	}
	
	@GetMapping("/exit")
	@ResponseBody
	public JsonObject exit(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(!isAPlayer(session))
			return helper.createResponseJson(-1, IS_NOT_A_PLAYER);
		LobbyManager.getInstance().exitGame((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR));
		session.invalidate();
		return helper.createResponseJson(0, "You've exited from the game!");
	}
	
	private void tryToStartGame(){
		LobbyManager mm = LobbyManager.getInstance();
		if (mm.isEveryoneReady() && MapManager.getInstance().isMapReady() && mm.areThereAtLeastTwoPlayers())
			LobbyManager.getInstance().initGame();
	}
	
	@GetMapping("/ready")
	@ResponseBody
	public JsonObject ready(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(!isAPlayer(session))
			return helper.createResponseJson(-1, IS_NOT_A_PLAYER);
		LobbyManager.getInstance().setPlayerReady((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR), true);
		tryToStartGame();
		return helper.createResponseJson(0, "The game will start when everyone is ready!");
	}
	
	@GetMapping("/notready")
	@ResponseBody
	public JsonObject notReady(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(!isAPlayer(session))
			return helper.createResponseJson(-1, IS_NOT_A_PLAYER);
		LobbyManager.getInstance().setPlayerReady((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR), false);
		return helper.createResponseJson(0, "The game will start when everyone is ready!");
	}
	
	private boolean isAPlayer(HttpSession session) {
		return session != null;
	}
	
}
