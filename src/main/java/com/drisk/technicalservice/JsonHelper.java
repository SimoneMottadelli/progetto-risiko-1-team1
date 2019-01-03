package com.drisk.technicalservice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.drisk.domain.Continent;
import com.drisk.domain.Territory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonHelper {
	
	private JsonHelper() {}
	
	private static final String TERRITORIES = "territories";
	private static final String CONTINENTS = "continents";
	private static final String DIFFICULTY = "difficulty";
	private static final String NAME = "name";
	private static final String NEIGHBOURHOOD = "neighbourhood";
	private static final String MEMBERSHIP = "membership";
	private static final String NUMBEROFTANKS = "numberOfTanks";
	
	public static String difficultyFromJson(JsonObject gameConfig) {
		return gameConfig.get(DIFFICULTY).getAsString();
	}
	
	private static List<String> getListFromJson(JsonObject gameConfig, String memberName) throws SyntaxException {
		List<String> list = new LinkedList<>();
		JsonArray array = gameConfig.getAsJsonArray(memberName);
		if (array == null)
			throw new SyntaxException("Syntax error: " + memberName + " is not valid keyword for a map");

		for(JsonElement name : array) {
			String nameFormatted = name.toString().replace("\"", "");
			if(!list.contains(nameFormatted))
				list.add(nameFormatted);
		}
		return list;
	}
	
	public static List<String> getTerritoriesFromJson(JsonObject gameConfig) throws SyntaxException{
		return getListFromJson(gameConfig, TERRITORIES);
	}
	
	public static List<String> getContinentsFromJson(JsonObject gameConfig) throws SyntaxException{
		return getListFromJson(gameConfig, CONTINENTS);
	}
	
	private static Map<String, List<String>> getRelationshipFromJson(JsonObject gameConfig, String relation) throws SyntaxException {
		Map<String, List<String>> map = new HashMap<>();
		JsonArray array = gameConfig.getAsJsonArray(relation);
		if (array == null)
			throw new SyntaxException("Syntax error: " + relation + " is not valid keyword for a map");
		for(int i = 0; i < array.size(); ++i) {
			JsonObject obj = array.get(i).getAsJsonObject();
			String continentName = obj.get(NAME).toString().replace("\"", "");
			List<String> territoriesNames = getTerritoriesFromJson(obj);
			map.put(continentName, territoriesNames);
		}
		return map;
	}
	
	public static Map<String, List<String>> getNeighbourhoodFromJson(JsonObject gameConfig) throws SyntaxException {
		return getRelationshipFromJson(gameConfig, NEIGHBOURHOOD);
	}
	
	public static Map<String, List<String>> getMembershipFromJson(JsonObject gameConfig) throws SyntaxException {
		return getRelationshipFromJson(gameConfig, MEMBERSHIP);
	}
	
	public static JsonObject mapToJson(String difficulty, List<Continent> continents, List<Territory> territories) {
		JsonObject result = new JsonObject();
		JsonArray continentsArray = new JsonArray();
		JsonArray territoriesArray = new JsonArray();
		JsonArray membershipArray = new JsonArray();
		JsonArray neighbourhoodArray = new JsonArray();
		for(Continent c : continents) {
			continentsArray.add(c.getName());
			JsonObject obj = new JsonObject();
			obj.addProperty(NAME, c.getName());
			JsonArray terrArray = new JsonArray();
			for(Territory t : c.getTerritories())
				terrArray.add(t.getName());
			obj.add(TERRITORIES, terrArray);
			membershipArray.add(obj);
		}
		for(Territory t : territories) {
			JsonObject obj = new JsonObject();
			obj.addProperty(NAME, t.getName());
			obj.addProperty(NUMBEROFTANKS, t.getNumberOfTanks());
			territoriesArray.add(obj);
			JsonObject neighbourObj = new JsonObject();
			neighbourObj.addProperty(NAME, t.getName());
			JsonArray neighboursArray = new JsonArray();
			for(Territory neighbour : t.getNeighbours())
				neighboursArray.add(neighbour.getName());
			neighbourObj.add(TERRITORIES, neighboursArray);
			neighbourhoodArray.add(neighbourObj);
		}
		result.addProperty(DIFFICULTY, difficulty);
		result.add(CONTINENTS, continentsArray);
		result.add(TERRITORIES, territoriesArray);
		result.add(MEMBERSHIP, membershipArray);
		result.add(NEIGHBOURHOOD, neighbourhoodArray);
		return result;
	}
	
}
