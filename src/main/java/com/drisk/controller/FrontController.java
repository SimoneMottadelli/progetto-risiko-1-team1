package com.drisk.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drisk.domain.Continent;
import com.drisk.domain.Map;
import com.drisk.domain.Territory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Controller
public class FrontController {
	
	public Continent x;
	
	public FrontController() {
		x = new Continent("asia");
		Map.getInstance().createMap("easy");
	}

	@RequestMapping(value="/prova")
	@ResponseBody
	public String prova() {
		Gson x1 = new Gson();
		Map map = Map.getInstance();
		return x1.toJson(map);
	}
	
}
