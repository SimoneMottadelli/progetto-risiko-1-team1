package com.drisk.controller;


import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.drisk.domain.MatchManager;
import com.drisk.domain.Player;
import com.drisk.domain.Turn;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class MatchController {
	
	@PostMapping(value="/join")
	@ResponseBody
	public String join(HttpServletRequest request) {		
		MatchManager mm = MatchManager.getInstance();
		if(MatchManager.getInstance().isMatchStarted())
			return "The match has already started!";
		else if(mm.isMatchFull())
			return "There are enough players!";
		else {
			mm.joinGame(request.getParameter("name"));
			return "You've joined the game!";
		}
	}
	
	
	@GetMapping(value="/players")
	@ResponseBody
	public JsonObject getPlayers(HttpServletResponse response) {
		JsonArray jsonArrayPlayers = new JsonArray();
		for(Player p : MatchManager.getInstance().getPlayers())
			jsonArrayPlayers.add(p.toJson());
		JsonObject jsonResult = new JsonObject();
		jsonResult.add("playersArray", jsonArrayPlayers);
		return jsonResult;
	}
	
	
	@GetMapping(value="/startGame")
	@ResponseBody
	public String startGame() {
		MatchManager mm = MatchManager.getInstance();
		if (!mm.isMatchStarted()) {
			mm.startGame();
			return "Game's just started!";
		}
		return "The game has already begun!";
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
