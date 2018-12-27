package com.drisk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drisk.domain.Map;
import com.google.gson.JsonObject;

@Controller
public class FrontController {
	
	@RequestMapping(value="/prova")
	@ResponseBody
	public JsonObject prova() {
		return Map.getInstance().toJson();
	}
	
}
