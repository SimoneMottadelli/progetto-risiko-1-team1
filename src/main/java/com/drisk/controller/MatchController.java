package com.drisk.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.drisk.domain.MatchManager;
import com.drisk.domain.Player;
import com.drisk.domain.Turn;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class MatchController {
	
    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();
	
	@PostMapping(value="/join")
	@ResponseBody
	public String join(HttpServletRequest request) {		
		MatchManager mm = MatchManager.getInstance();
		if(mm.isMatchStarted())
			return "The match has already started!";
		else if(mm.isMatchFull())
			return "There are enough players!";
		else {
			mm.joinGame(request.getParameter("name").trim());
			return "You've joined the game!";
		}
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
		MatchManager.getInstance().exitGame(request.getParameter("name"));
		if (MatchManager.getInstance().isEveryoneReady()) {
			MatchManager.getInstance().startGame();
		}
		return "You've exited from the game!";
	}
	
	
	@PostMapping(value="/ready")
	@ResponseBody
	public String ready(HttpServletRequest request) {
		MatchManager.getInstance().setPlayerReady(request.getParameter("name"), true);
		if (MatchManager.getInstance().isEveryoneReady()) {
			MatchManager.getInstance().startGame();
		}
		return "The game will start when everyone is ready!";
	}
	
	@PostMapping(value="/notready")
	@ResponseBody
	public String notReady(HttpServletRequest request) {
		MatchManager.getInstance().setPlayerReady(request.getParameter("name"), false);
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
