package com.drisk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drisk.domain.GameManager;
import com.drisk.domain.Map;
import com.drisk.domain.MatchManager;
import com.google.gson.JsonObject;

@Controller
public class FrontController {
	
	//http://localhost:8080/drisk/Contr/join
	
	@RequestMapping(value="/prova2")
	public String join() {
		MatchManager mm = MatchManager.getInstance();
		String page;
		if(mm.isMatchStarted())
			page = "pagineGiocoGiaIniziato.html";
		else if(mm.isMatchFull())
			page = "paginaGiocoPieno.html";
		else {
			mm.joinGame();
			page = "paginaGioco.html";
		}
		return page;
	}
	
	@GetMapping(value="/prova")
	@ResponseBody
	public JsonObject prova() {
		Map.getInstance().createMap("easy");
		return Map.getInstance().toJson();
	}
	
}
