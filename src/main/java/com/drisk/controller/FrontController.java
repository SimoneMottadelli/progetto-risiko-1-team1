package com.drisk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FrontController {

	@RequestMapping(value="/prova")
	@ResponseBody
	public String Prova() {
		return "ciao1";
	}
	
}
