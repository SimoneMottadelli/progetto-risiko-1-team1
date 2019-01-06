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
import com.drisk.domain.Player;
import com.drisk.domain.TankManager;
import com.drisk.domain.TurnManager;
import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonObject;

@Controller
public class GameController {
	
	private ExecutorService nonBlockingService = Executors.newCachedThreadPool();
	private static final String SESSION_ATTRIBUTE_COLOR = "color";
	private static final String OK = "OK";
	private static final String IS_NOT_YOUR_TURN = "It's not your turn, wait for it...";
	private static final String NOT_A_PLAYER = "You are not a player because you haven't a color assigned";
	
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
	public synchronized JsonObject placeTanks(HttpServletRequest request) {
		try {
			String body = request.getReader().lines().collect(Collectors.joining());
			JsonObject obj = JsonHelper.parseJson(body);
			Player p = GameManager.getInstance().findPlayerByColor((Color) request.getSession(false).getAttribute(SESSION_ATTRIBUTE_COLOR));
			int numOfTanks = obj.getAsJsonPrimitive("numOfTanks").getAsInt();
			if(p.getAvailableTanks() < numOfTanks)
				return JsonHelper.createResponseJson(-1, "You don't have " + numOfTanks + " tanks to place");
			else {
				String territoryName = obj.getAsJsonPrimitive("where").getAsString();
				TankManager.getInstance().placeTanks(MapManager.getInstance().findTerritoryByName(territoryName), p.placeTanks(numOfTanks));
				tryToStartGame();
				return JsonHelper.createResponseJson(0, OK);
			}
		} catch (IOException e) {
			return JsonHelper.createResponseJson(-1, e.getMessage());
		}
	}
	
	private void tryToStartGame() {
		if(GameManager.getInstance().areAllTanksPlaced())
			GameManager.getInstance().startGame();
	}
	
	@GetMapping("/getColorFromSession")
	@ResponseBody
	public synchronized JsonObject getColorSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(isAPlayer(session))
			return JsonHelper.createResponseJson(0, session.getAttribute(SESSION_ATTRIBUTE_COLOR).toString());
		else
			return JsonHelper.createResponseJson(-1, NOT_A_PLAYER);
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
			return JsonHelper.createResponseJson(-1, NOT_A_PLAYER);
		if(!isMyTurn((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR)))
			return JsonHelper.createResponseJson(-1, IS_NOT_YOUR_TURN);
		try {
			String body = request.getReader().lines().collect(Collectors.joining());
			JsonObject obj = JsonHelper.parseJson(body);
			TurnManager.getInstance().playPhase(obj);
			return JsonHelper.createResponseJson(0, OK);
		} catch (IOException e){
			return JsonHelper.createResponseJson(-1, e.getMessage());
		}
	}
	
	private boolean isMyTurn(Color color) {
		return color.equals(TurnManager.getInstance().getCurrentPlayer().getColor());
	}
	
	private boolean isAPlayer(HttpSession session) {
		return session != null;
	}
	
	@GetMapping(value="/nextPhase")
	@ResponseBody
	public synchronized JsonObject nextPhase(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(!isAPlayer(session))
			return JsonHelper.createResponseJson(-1, NOT_A_PLAYER);
		if(!isMyTurn((Color) session.getAttribute(SESSION_ATTRIBUTE_COLOR)))
			return JsonHelper.createResponseJson(-1, IS_NOT_YOUR_TURN);
		TurnManager.getInstance().getCurrentPhase().nextPhase();
		return JsonHelper.createResponseJson(0, OK);
	}
	
	/*
	@GetMapping(value="/playPhase")
	@ResponseBody
	public synchronized String playPhase() {
		MatchManager mm = MatchManager.getInstance();
		if (mm.isMatchStarted()) {
			TurnManager.getInstance().getCurrentPhase().playPhase();
			return "playing the phase...";
		}
		return "Couldn't play the phase!";
	}
	*/
	
}
