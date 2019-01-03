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

import com.drisk.domain.Color;
import com.drisk.domain.MatchManager;
import com.drisk.domain.Player;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@Controller
public class MatchController {
	
    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();
    private static final String SESSION_ATTRIBUTE_COLOR = "color";
	
	@PostMapping(value="/join")
	@ResponseBody
	public JsonObject join(HttpServletRequest request) {
		MatchManager mm = MatchManager.getInstance();
		if(mm.isMatchStarted())
			return createResponseJson(-1, "The match has already started!");
		else if(mm.isMatchFull())
			return createResponseJson(-1, "There are enough players!");
		else {
			HttpSession session = request.getSession(false);
			if(session == null) {
				mm.joinGame(request.getParameter("name").trim());
				session = request.getSession();
				session.setAttribute(SESSION_ATTRIBUTE_COLOR, mm.findLastPlayerColor());
				return createResponseJson(0, "You've joined the game!");
			}
			else
				return createResponseJson(-1, "You've already joined to the game");
		}
	}
	
	private JsonObject createResponseJson(int responseCode, String responseMessage) {
		JsonObject obj = new JsonObject();
		obj.addProperty("responseCode", responseCode);
		obj.addProperty("responseMessage", responseMessage);
		return obj;
	}
	
	@PostMapping("/gameConfig")
	@ResponseBody
	public JsonObject gameConfig(HttpServletRequest request) {
		try {
			String body = request.getReader().lines().collect(Collectors.joining());
			Gson converter = new Gson();
			JsonObject obj = converter.fromJson(body, JsonObject.class);
			MatchManager.getInstance().setGameConfig(obj);
			return createResponseJson(0, "gameConfig correctly parsed");
		} 
		catch (JsonSyntaxException e)
		{
			return createResponseJson(-1, "Syntax error: cannot parse json object");
		}
		catch (Exception e) {
			return createResponseJson(-1, e.getMessage());
		}
	}
    	     
	@GetMapping("/info")
    public SseEmitter handleSse() {
		SseEmitter emitter = new SseEmitter();
		nonBlockingService.execute(() -> {
			try {
				JsonObject jsonResponse = createResponsePlayersJson();
				jsonResponse.addProperty("mapReady", MatchManager.getInstance().isGameConfigured());
				emitter.send(jsonResponse);
				emitter.complete();	
			} catch (Exception ex) {
				emitter.completeWithError(ex);
			}
		});
		return emitter;
    }
	
	// used by handleSse for creating the response
	private JsonObject createResponsePlayersJson() {
		JsonArray jsonArrayPlayers = new JsonArray();
		for(Player p : MatchManager.getInstance().getPlayers())
			jsonArrayPlayers.add(p.toJson());
		JsonObject jsonResult = new JsonObject();
		jsonResult.add("playersArray", jsonArrayPlayers);
		return jsonResult;
	}
	
	
	@GetMapping(value="/exit")
	@ResponseBody
	public String exit(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		MatchManager.getInstance().exitGame((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR));
		session.invalidate();
		return "You've exited from the game!";
	}
	
	private void tryToStartGame(){
		if (MatchManager.getInstance().isEveryoneReady() && MatchManager.getInstance().isGameConfigured())
			MatchManager.getInstance().startGame();
	}
	
	@GetMapping(value="/ready")
	@ResponseBody
	public JsonObject ready(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		MatchManager.getInstance().setPlayerReady((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR), true);
		tryToStartGame();
		return createResponseJson(0, "The game will start when everyone is ready!");
	}
	
	@GetMapping(value="/notready")
	@ResponseBody
	public JsonObject notReady(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		MatchManager.getInstance().setPlayerReady((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR), false);
		return createResponseJson(0, "The game will start when everyone is ready!");
	}
	
}
