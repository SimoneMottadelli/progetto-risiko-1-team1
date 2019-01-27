package com.drisk.technicalservice;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonHelper {
	
	private static final String DIFFICULTY = "difficulty";
	
	public String difficultyFromJson(JsonObject gameConfig) {
		return gameConfig.get(DIFFICULTY).getAsString();
	}
	
	public JsonObject createResponseJson(int responseCode, String responseMessage) {
		JsonObject obj = new JsonObject();
		obj.addProperty("responseCode", responseCode);
		obj.addProperty("responseMessage", responseMessage);
		return obj;
	}
	
	public JsonObject parseJson(String body) {
		Gson converter = new Gson();
		return converter.fromJson(body, JsonObject.class);
	}

}
