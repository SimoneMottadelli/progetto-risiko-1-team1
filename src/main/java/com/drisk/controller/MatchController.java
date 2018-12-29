package com.drisk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drisk.domain.MatchManager;
import com.drisk.domain.Turn;

@Controller
public class MatchController {
	
	private int x = 0;
	
	@PostMapping(value="/join")
	@ResponseBody
	public String join(HttpServletRequest request, HttpServletResponse response) {
		MatchManager mm = MatchManager.getInstance();
		if(mm.isMatchStarted())
			return "match started";
		else if(mm.isMatchFull())
			return "match full";
		else {
			String playerName = request.getParameter("name");
			mm.joinGame(playerName);
			x++;
			return  playerName + " has joined the game! Number of players: " + x;
		}
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
