package com.drisk.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.drisk.domain.ColorEnum;
import com.drisk.domain.GameManager;
import com.drisk.domain.MapManager;
import com.drisk.domain.TankManager;
import com.drisk.domain.TurnManager;
import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class GameController {
	
	private static final String SESSION_ATTRIBUTE_COLOR = "color";
	private static final String IS_NOT_YOUR_TURN = "It's not your turn, wait for it...";
	private static final String NOT_A_PLAYER = "You are not a player because you haven't a color assigned";
	private JsonHelper helper = new JsonHelper();
	
	@GetMapping("/territories")
    public SseEmitter getMapTerritories() {
		SseEmitter emitter = new SseEmitter();
		JsonArray territories = MapManager.getInstance().getMap().toJson().getAsJsonArray("territories");
		try {
			emitter.send(territories);
		} catch (IOException ex) {
			emitter.completeWithError(ex);
		}
		emitter.complete();	
				
		return emitter;
    }
	
	@GetMapping("/map")
	@ResponseBody
    public JsonObject getMap() {
		JsonObject responseObj = null;
		if (MapManager.getInstance().getMap() != null && MapManager.getInstance().getMap().isReady())
			responseObj = MapManager.getInstance().getMap().toJson();
		try {
			if (responseObj != null) {
				responseObj.addProperty("mapSVG", MapManager.getInstance().getSVGMap());
				return helper.createResponseJson(0, responseObj.toString());
			}
			return  helper.createResponseJson(-1, "You are not a player!");
		} catch (IOException e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
    }
	
	@GetMapping("/turnStatus")
	public SseEmitter handleSseTurn() {
		SseEmitter emitter = new SseEmitter();
		try {
			JsonObject result = TurnManager.getInstance().toJson();
			if(GameManager.getInstance().getColorOfWinner() != null)
				result.addProperty("winner", GameManager.getInstance().getColorOfWinner().toString());
			emitter.send(result);
		} catch (IOException ex) {
			emitter.completeWithError(ex);
		}
		emitter.complete();	
		return emitter;
	}
	
	@PostMapping("/initialTanksPlacement")
	@ResponseBody
	public JsonObject initialPlaceTanks(HttpServletRequest request) {
		if(!isAPlayer(request.getSession(false)))
			return helper.createResponseJson(-1, NOT_A_PLAYER);
		if (TurnManager.getInstance().getCurrentPhase() != null)
			return helper.createResponseJson(-1, "The initial tanks placement is finished");
		String body;
		try {
			body = request.getReader().lines().collect(Collectors.joining());
		} catch (IOException e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
		JsonObject obj = helper.parseJson(body);
		int numOfTanks = helper.parseJson(body).getAsJsonPrimitive("numOfTanks").getAsInt();
		try {
			String territoryName = obj.getAsJsonPrimitive("territory").getAsString();
			ColorEnum color = (ColorEnum) request.getSession(false).getAttribute(SESSION_ATTRIBUTE_COLOR);
			TankManager.getInstance().tryToPlaceTanks(GameManager.getInstance().findPlayerByColor(color), MapManager.getInstance().getMap().findTerritoryByName(territoryName), numOfTanks);
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
		return helper.createResponseJson(0, GameManager.getInstance().findPlayerByColor((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR)).toJson().toString());
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
		return session != null && GameManager.getInstance().findPlayerByColor((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR)) != null;
	}
	
	@GetMapping("/nextPhase")
	@ResponseBody
	public JsonObject nextPhase(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(!isAPlayer(session))
			return helper.createResponseJson(-1, NOT_A_PLAYER);
		if(!TurnManager.getInstance().isPlayerTurn((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR)))
			return helper.createResponseJson(-1, IS_NOT_YOUR_TURN);
		String playerStatus = TurnManager.getInstance().getCurrentPlayer().toJson().toString();
		try {
			TurnManager.getInstance().getCurrentPhase().nextPhase();
			return helper.createResponseJson(0, playerStatus);
		} catch (RequestNotValidException e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
	}
	
	@GetMapping("/exitGame")
	@ResponseBody
	public JsonObject exit(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (!isAPlayer(session))
			return helper.createResponseJson(-1, NOT_A_PLAYER);
		GameManager.getInstance().exitGame((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR));
		session.invalidate();
		return helper.createResponseJson(0, "You've exited from the game!");
	}

}
