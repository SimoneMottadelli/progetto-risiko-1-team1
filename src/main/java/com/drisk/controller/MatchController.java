package com.drisk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drisk.domain.MatchManager;

@Controller
public class MatchController {

	
	@GetMapping(value="/join")
	@ResponseBody
	public String join() {
		MatchManager mm = MatchManager.getInstance();
		if(mm.isMatchStarted())
			return "match started";
		else if(mm.isMatchFull())
			return "match full";
		else {
			mm.joinGame();
			return "ok";
		}
	}
}
