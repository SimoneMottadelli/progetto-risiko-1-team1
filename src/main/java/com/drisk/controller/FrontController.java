package com.drisk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drisk.domain.GameManager;
import com.drisk.domain.Map;
import com.google.gson.JsonObject;

@Controller
public class FrontController {
	
	//http://localhost:8080/drisk/Contr/join
	
	@RequestMapping(value="/join")
	public String join() {
		GameManager gm = GameManager.getInstance();
		
		return "nome_pagine.html o .jsp";
	}
	
	@RequestMapping(value="/prova")
	@ResponseBody
	public JsonObject prova() {
		return Map.getInstance().toJson();
	}
	
}
