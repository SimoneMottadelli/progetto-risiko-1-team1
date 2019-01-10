package com.drisk.technicalservice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonHelper {
	
	private static final String TERRITORIES = "territories";
	private static final String CONTINENTS = "continents";
	private static final String DIFFICULTY = "difficulty";
	private static final String NAME = "name";
	private static final String NEIGHBOURHOOD = "neighbourhood";
	private static final String MEMBERSHIP = "membership";
	
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
	
	private static List<String> getListFromJson(JsonObject gameConfig, String memberName) throws SyntaxException {
		List<String> list = new LinkedList<>();
		JsonArray array = gameConfig.getAsJsonArray(memberName);
		if (array == null)
			throw new SyntaxException("Syntax error: " + memberName + " keyword must exists");

		for(JsonElement name : array) {
			String nameFormatted = name.toString().replace("\"", "");
			if(!list.contains(nameFormatted))
				list.add(nameFormatted);
		}
		return list;
	}
	
	public static List<String> getTerritoriesFromJson(JsonObject gameConfig) throws SyntaxException {
		return getListFromJson(gameConfig, TERRITORIES);
	}
	
	public List<String> getContinentsFromJson(JsonObject gameConfig) throws SyntaxException {
		return getListFromJson(gameConfig, CONTINENTS);
	}
	
	/*public static List<String> getNumberOfTanksFromJson(JsonObject gameConfig) throws SyntaxException {
		return getListFromJson(gameConfig, NUMBEROFTANKS);
	}*/
	
	private static Map<String, List<String>> getRelationshipFromJson(JsonObject gameConfig, String relation) throws SyntaxException {
		Map<String, List<String>> map = new HashMap<>();
		JsonArray array = gameConfig.getAsJsonArray(relation);
		if (array == null)
			throw new SyntaxException("Syntax error: " + relation + " keyword must exists");
		for(int i = 0; i < array.size(); ++i) {
			JsonObject obj = array.get(i).getAsJsonObject();
			JsonElement continentElement = obj.get(NAME); 
			if (continentElement == null)
				throw new SyntaxException("Syntax error: in membership or neighbourhood object there is an unknown keyword." + 
											" It should contain the following keywords: \"name\" and \"territories\"");
			String continentName = continentElement.toString().replace("\"", "");
			List<String> territoriesNames = getTerritoriesFromJson(obj);
			map.put(continentName, territoriesNames);
		}
		return map;
	}
	
	public Map<String, List<String>> getNeighbourhoodFromJson(JsonObject gameConfig) throws SyntaxException {
		return getRelationshipFromJson(gameConfig, NEIGHBOURHOOD);
	}
	
	public Map<String, List<String>> getMembershipFromJson(JsonObject gameConfig) throws SyntaxException {
		return getRelationshipFromJson(gameConfig, MEMBERSHIP);
	}
}
