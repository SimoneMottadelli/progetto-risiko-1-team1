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

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.game.ColorEnum;
import com.drisk.domain.game.GameManager;
import com.drisk.domain.game.TankManager;
import com.drisk.domain.map.DifficultyEnum;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.turn.TurnManager;
import com.drisk.technicalservice.FileLoader;
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
	
	@GetMapping("/territories")
    public SseEmitter getMapTerritories() {
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
	
	@GetMapping("/map")
	@ResponseBody
    public JsonObject handleSseMap() {
		JsonObject responseObj = MapManager.getInstance().toJson();
		try {
			responseObj.addProperty("mapSVG", MapManager.getInstance().getSVGMap());
			return helper.createResponseJson(0, responseObj.toString());
		} catch (IOException e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
    }
	
	@GetMapping("/turnStatus")
	public SseEmitter handleSseTurn() {
		SseEmitter emitter = new SseEmitter();
		nonBlockingService.execute(() -> {
			try {
				emitter.send(TurnManager.getInstance().toJson());
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
			ColorEnum color = (ColorEnum) request.getSession(false).getAttribute(SESSION_ATTRIBUTE_COLOR);
			TankManager.getInstance().tryToPlaceTanks(GameManager.getInstance().findPlayerByColor(color), MapManager.getInstance().findTerritoryByName(territoryName), numOfTanks);
		} catch (RequestNotValidException e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
		GameManager.getInstance().tryToStartGame();
		ColorEnum playerColor = (ColorEnum) request.getSession(false).getAttribute(SESSION_ATTRIBUTE_COLOR);
		return helper.createResponseJson(0, GameManager.getInstance().findPlayerByColor(playerColor).toJson().toString());
	}
	
	@GetMapping("/playerInfo")
	@ResponseBody
	public JsonObject getPlayerInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(!isAPlayer(session))
			return helper.createResponseJson(-1, NOT_A_PLAYER);
		return GameManager.getInstance().findPlayerByColor((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR)).toJson();
	}
	
	@PostMapping("/playPhase")
	@ResponseBody
	public JsonObject playPhase(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(!isAPlayer(session))
			return helper.createResponseJson(-1, NOT_A_PLAYER);
		if(!TurnManager.getInstance().isPlayerTurn((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR)))
			return helper.createResponseJson(-1, IS_NOT_YOUR_TURN);
		String body;
		try {
			body = request.getReader().lines().collect(Collectors.joining());
		} catch (IOException e){
			return helper.createResponseJson(-1, e.getMessage());
		}
		try {
			TurnManager.getInstance().playPhase(helper.parseJson(body));
		} catch (RequestNotValidException e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
		return helper.createResponseJson(0, TurnManager.getInstance().getCurrentPlayer().toJson().toString());
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
		if(!TurnManager.getInstance().isPlayerTurn((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR)))
			return helper.createResponseJson(-1, IS_NOT_YOUR_TURN);
		TurnManager.getInstance().getCurrentPhase().nextPhase();
		return helper.createResponseJson(0, OK);
	}

}
