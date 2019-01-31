package com.drisk.controller;

import java.net.DatagramSocket;
import java.net.InetAddress;
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
import com.drisk.domain.LobbyManager;
import com.drisk.domain.MapManager;
import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@Controller
public class LobbyController {

	private static final String SESSION_ATTRIBUTE_COLOR = "color";
	private static final String IS_NOT_A_PLAYER = "You are not a player";
	private JsonHelper helper = new JsonHelper();

	@PostMapping("/join")
	@ResponseBody
	public JsonObject join(HttpServletRequest request) {
		LobbyManager mm = LobbyManager.getInstance();
		if (GameManager.getInstance().isGameStarted())
			return helper.createResponseJson(-1, "The game has already started!");
		else if (mm.isMatchFull())
			return helper.createResponseJson(-1, "There are enough players!");
		else {
			HttpSession session = request.getSession(false);
			if (!isAPlayer(session) || mm.findPlayerByColor((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR)) == null) {
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
		if (!isAPlayer(request.getSession(false)))
			return helper.createResponseJson(-1, IS_NOT_A_PLAYER);
		try {
			String body = request.getReader().lines().collect(Collectors.joining());
			JsonObject gameConfig = helper.parseJson(body);
			MapManager.getInstance().createMap(gameConfig);
			LobbyManager.getInstance().configGame(gameConfig);
			return helper.createResponseJson(0, "Configuration correctly added");
		} catch (JsonSyntaxException e) {
			return helper.createResponseJson(-1, "Syntax error: cannot parse json object");
		} catch (Exception e) {
			return helper.createResponseJson(-1, e.getMessage());
		}
	}

	@GetMapping("/info")
	public SseEmitter handleSse() {
		SseEmitter emitter = new SseEmitter();
		JsonObject obj = LobbyManager.getInstance().toJson();
		try {
			obj.addProperty("gamePage", "http://" + getIp() + ":8080/drisk/pages/game.html");
			emitter.send(obj);
		} catch (Exception ex) {
			emitter.completeWithError(ex);
		}
		emitter.complete();
		return emitter;
	}
	
	@GetMapping("/lobbyPage")
	@ResponseBody
	public JsonObject resetGame() {
		GameManager.getInstance().resetGame();
		return helper.createResponseJson(0, "http://" + getIp() + ":8080/drisk");
	}
	
	private String getIp() {
		try(final DatagramSocket socket = new DatagramSocket()){
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			return socket.getLocalAddress().getHostAddress();
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping("/exit")
	@ResponseBody
	public JsonObject exit(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (!isAPlayer(session))
			return helper.createResponseJson(-1, IS_NOT_A_PLAYER);
		LobbyManager.getInstance().exitGame((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR));
		session.invalidate();
		return helper.createResponseJson(0, "You've exited from the game!");
	}

	private void tryToStartGame() {
		LobbyManager mm = LobbyManager.getInstance();
		if (mm.isEveryoneReady() && MapManager.getInstance().getMap().isReady() && mm.areThereAtLeastTwoPlayers())
			LobbyManager.getInstance().initGame();
	}

	@GetMapping("/ready")
	@ResponseBody
	public JsonObject ready(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (!isAPlayer(session))
			return helper.createResponseJson(-1, IS_NOT_A_PLAYER);
		LobbyManager.getInstance().setPlayerReady((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR), true);
		tryToStartGame();
		return helper.createResponseJson(0, "The game will start when everyone is ready!");
	}

	@GetMapping("/notready")
	@ResponseBody
	public JsonObject notReady(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (!isAPlayer(session))
			return helper.createResponseJson(-1, IS_NOT_A_PLAYER);
		LobbyManager.getInstance().setPlayerReady((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR), false);
		return helper.createResponseJson(0, "The game will start when everyone is ready!");
	}

	private boolean isAPlayer(HttpSession session) {
		return session != null && LobbyManager.getInstance()
				.findPlayerByColor((ColorEnum) session.getAttribute(SESSION_ATTRIBUTE_COLOR)) != null;
	}

}
