package com.drisk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FrontController1 {

	@RequestMapping(value="/prova1")
	@ResponseBody
	public String prova() {
		return "ciao";
	}
	
}
