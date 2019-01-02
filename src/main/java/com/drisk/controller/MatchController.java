package com.drisk.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.drisk.domain.Color;
import com.drisk.domain.MatchManager;
import com.drisk.domain.Player;
import com.drisk.domain.Turn;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class MatchController {
	
    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();
    private static final String SESSION_ATTRIBUTE = "color";
	
	@PostMapping(value="/join")
	@ResponseBody
	public JsonObject join(HttpServletRequest request) {
		MatchManager mm = MatchManager.getInstance();
		if(mm.isMatchStarted())
			return createResponseObj(-1, "The match has already started!");
		else if(mm.isMatchFull())
			return createResponseObj(-1, "There are enough players!");
		else {
			HttpSession session = request.getSession(false);
			if(session == null) {
				mm.joinGame(request.getParameter("name").trim());
				session = request.getSession();
				session.setAttribute(SESSION_ATTRIBUTE, mm.getPlayers().get(mm.getPlayers().size() - 1).getColor());
				return createResponseObj(0, "You've joined the game!");
			}
			else
				return createResponseObj(-1, "You've already joined to the game");
		}
	}
	
	private JsonObject createResponseObj(int responseCode, String responseMessage) {
		JsonObject obj = new JsonObject();
		obj.addProperty("responseCode", responseCode);
		obj.addProperty("responseMessage", responseMessage);
		return obj;
	}
    	     
	@GetMapping("/players")
    public SseEmitter handleSse(HttpServletResponse response) {
		SseEmitter emitter = new SseEmitter();
		nonBlockingService.execute(() -> {
			try {
				JsonArray jsonArrayPlayers = new JsonArray();
				for(Player p : MatchManager.getInstance().getPlayers())
					jsonArrayPlayers.add(p.toJson());
				JsonObject jsonResult = new JsonObject();
				jsonResult.add("playersArray", jsonArrayPlayers);				
				emitter.send(jsonResult);
				emitter.complete();	
			} catch (Exception ex) {
				emitter.completeWithError(ex);
			}
		});
		return emitter;
    }   
	
	
	@PostMapping(value="/exit")
	@ResponseBody
	public String exit(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		MatchManager.getInstance().exitGame((Color) session.getAttribute(SESSION_ATTRIBUTE));
		session.invalidate();
		if (MatchManager.getInstance().isEveryoneReady()) {
			MatchManager.getInstance().startGame();
		}
		return "You've exited from the game!";
	}
	
	@PostMapping(value="/ready")
	@ResponseBody
	public String ready(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		MatchManager.getInstance().setPlayerReady((Color) session.getAttribute(SESSION_ATTRIBUTE), true);
		if (MatchManager.getInstance().isEveryoneReady()) {
			MatchManager.getInstance().startGame();
		}
		return "The game will start when everyone is ready!";
	}
	
	@PostMapping(value="/notready")
	@ResponseBody
	public String notReady(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		MatchManager.getInstance().setPlayerReady((Color) session.getAttribute(SESSION_ATTRIBUTE), false);
		return "The game will start when everyone is ready!";
	}
	
	
	@GetMapping(value="/nextPhase")
	@ResponseBody
	public String nextPhase() {
		MatchManager mm = MatchManager.getInstance();
		if (mm.isMatchStarted()) {
			Turn.getInstance().getCurrentPhase().nextPhase();
			return "Going to the next phase!";
		}
		return "Couldn't go to the next phase!";
	}
	
	
	@GetMapping(value="/playPhase")
	@ResponseBody
	public String playPhase() {
		MatchManager mm = MatchManager.getInstance();
		if (mm.isMatchStarted()) {
			Turn.getInstance().getCurrentPhase().startPhase();
			return "playing the phase...";
		}
		return "Couldn't play the phase!";
	}
	
}
