package com.drisk.controller;

import java.io.IOException;
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
import com.drisk.domain.GameManager;
import com.drisk.domain.MapManager;
import com.drisk.domain.TankManager;
import com.drisk.domain.TurnManager;
import com.drisk.domain.exceptions.ExceededAvailableTanksException;
import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonObject;

@Controller
public class GameController {
	
	private ExecutorService nonBlockingService = Executors.newCachedThreadPool();
	private static final String SESSION_ATTRIBUTE_COLOR = "color";
	private static final String OK = "OK";
	private static final String IS_NOT_YOUR_TURN = "It's not your turn, wait for it...";
	private static final String NOT_A_PLAYER = "You are not a player because you haven't a color assigned";
	private JsonHelper helper = new JsonHelper();
	
	@GetMapping("/map")
    public SseEmitter handleSseMap() {
		SseEmitter emitter = new SseEmitter();
		nonBlockingService.execute(() -> {
			try {
				JsonObject map = MapManager.getInstance().toJson();
				emitter.send(map);
				emitter.complete();	
			} catch (Exception ex) {
				emitter.completeWithError(ex);
			}
		});
		return emitter;
    }
	
	@GetMapping("/turnStatus")
	public SseEmitter handleSseTurn() {
		SseEmitter emitter = new SseEmitter();
		nonBlockingService.execute(() -> {
			try {
				emitter.send(GameManager.getInstance().toJson());
				emitter.complete();	
			} catch (Exception ex) {
				emitter.completeWithError(ex);
			}
		});
		return emitter;
	}
	
	@PostMapping("/initialTanksPlacement")
	@ResponseBody
	public JsonObject placeTanks(HttpServletRequest request) {
		if(!isAPlayer(request.getSession(false)))
			return helper.createResponseJson(-1, NOT_A_PLAYER);
		String body;
		try {
			body = request.getReader().lines().collect(Collectors.joining());
		} catch (IOException e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
		JsonObject obj = helper.parseJson(body);
		int numOfTanks = obj.getAsJsonPrimitive("numOfTanks").getAsInt();
		try {
			String territoryName = obj.getAsJsonPrimitive("where").getAsString();
			TankManager.getInstance().placeTanks(MapManager.getInstance().findTerritoryByName(territoryName), numOfTanks);
		} catch (ExceededAvailableTanksException e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
		GameManager.getInstance().tryToStartGame();
		Color playerColor = (Color) request.getSession(false).getAttribute(SESSION_ATTRIBUTE_COLOR);
		return helper.createResponseJson(0, GameManager.getInstance().findPlayerByColor(playerColor).toJson().toString());
	}

	
	@GetMapping("/getColorFromSession")
	@ResponseBody
	public JsonObject getColorSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(isAPlayer(session))
			return helper.createResponseJson(0, session.getAttribute(SESSION_ATTRIBUTE_COLOR).toString());
		else
			return helper.createResponseJson(-1, NOT_A_PLAYER);
	}
	
	
	@GetMapping("/test")
	@ResponseBody
	public String test(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Color c = (Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR);
		return c.toString();
	}
	
	@PostMapping("/playPhase")
	@ResponseBody
	public JsonObject playPhase(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(!isAPlayer(session))
			return helper.createResponseJson(-1, NOT_A_PLAYER);
		if(!TurnManager.getInstance().isPlayerTurn((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR)))
			return helper.createResponseJson(-1, IS_NOT_YOUR_TURN);
		String body;
		try {
			body = request.getReader().lines().collect(Collectors.joining());
		} catch (IOException e){
			return helper.createResponseJson(-1, e.getMessage());
		}
		TurnManager.getInstance().playPhase(helper.parseJson(body));
		return helper.createResponseJson(0, OK);
	}
	
	private boolean isAPlayer(HttpSession session) {
		return session != null;
	}
	
	@GetMapping("/nextPhase")
	@ResponseBody
	public JsonObject nextPhase(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(!isAPlayer(session))
			return helper.createResponseJson(-1, NOT_A_PLAYER);
		if(!TurnManager.getInstance().isPlayerTurn((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR)))
			return helper.createResponseJson(-1, IS_NOT_YOUR_TURN);
		TurnManager.getInstance().getCurrentPhase().nextPhase();
		return helper.createResponseJson(0, OK);
	}

}
