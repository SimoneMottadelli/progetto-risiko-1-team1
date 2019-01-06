package com.drisk.technicalservice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.drisk.domain.Continent;
import com.drisk.domain.MapManager;
import com.drisk.domain.MatchManager;
import com.drisk.domain.Player;
import com.drisk.domain.Territory;
import com.drisk.domain.TerritoryCard;
import com.drisk.domain.TerritoryCardSymbol;
import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
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
	private static final String OWNER = "owner";
	private static final String CURRENT_PLAYER_COLOR = "currentPlayersColor";
	private static final String CURRENT_PHASE_ID = "currentPhaseId";
	private static final String PLAYERS = "players";
	private static final String CARDS = "cards";
	private static final String SYMBOL = "symbol";
	
	public static String difficultyFromJson(JsonObject gameConfig) {
		return gameConfig.get(DIFFICULTY).getAsString();
	}
	
	public static JsonObject createResponseJson(int responseCode, String responseMessage) {
		JsonObject obj = new JsonObject();
		obj.addProperty("responseCode", responseCode);
		obj.addProperty("responseMessage", responseMessage);
		return obj;
	}
	
	public static JsonObject parseJson(String body) {
		Gson converter = new Gson();
		return converter.fromJson(body, JsonObject.class);
	}
	
	public static JsonObject gameManagerToJson(String color, Integer phaseId, List<Player> players) {
		JsonObject result = new JsonObject();
		result.addProperty(CURRENT_PLAYER_COLOR, color);
		result.addProperty(CURRENT_PHASE_ID, phaseId);
		JsonArray playersArray = new JsonArray();
		for(Player p : players) {
			playersArray.add(p.toJson());
		}
		result.add(PLAYERS, playersArray);
		return result;
	}
	
	public static JsonObject playerToJson(String nickname, String color, int availableTanks, boolean ready) {
		JsonObject jsonPlayer = new JsonObject();
		jsonPlayer.addProperty("nickname", nickname);
		jsonPlayer.addProperty("availableTanks", availableTanks);
		jsonPlayer.addProperty("color", color.toUpperCase());
		jsonPlayer.addProperty("ready", ready);
		return jsonPlayer;
	}
	
	public static JsonObject matchManagerToJson(List<Player> players) {
		JsonObject result = new JsonObject();
		JsonArray jsonArrayPlayers = new JsonArray();
		for(Player p : players)
			jsonArrayPlayers.add(p.toJson());
		result.add(PLAYERS, jsonArrayPlayers);
		result.addProperty("mapReady", MatchManager.getInstance().isGameConfigured());
		return result;
	}
	
	public static TerritoryCard[] getTrisFromJson(JsonObject obj) {
		TerritoryCard[] tris = new TerritoryCard[3];
		JsonArray cards = obj.getAsJsonArray(CARDS);
		int i = 0;
		for(JsonElement cardObj : cards) {
			JsonObject card = cardObj.getAsJsonObject();
			Territory t = MapManager.getInstance().findTerritoryByName(card.get(NAME).toString().toLowerCase());
			tris[i++] = new TerritoryCard(t, TerritoryCardSymbol.valueOf(card.get(SYMBOL).toString().toUpperCase()));
		}
		return tris;
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
	
	public static Map<String, List<String>> getNeighbourhoodFromJson(JsonObject gameConfig) throws SyntaxException {
		return getRelationshipFromJson(gameConfig, NEIGHBOURHOOD);
	}
	
	public static Map<String, List<String>> getMembershipFromJson(JsonObject gameConfig) throws SyntaxException {
		return getRelationshipFromJson(gameConfig, MEMBERSHIP);
	}
	
	private static JsonArray continentsToJson(List<Continent> continents) {
		JsonArray continentsArray = new JsonArray();
		for(Continent c : continents) {
			JsonObject name = new JsonObject();
			name.addProperty(NAME, c.getName());
			if(c.findPlayer() == null)
				name.addProperty(OWNER, "noOne");
			else
				name.addProperty(OWNER, c.findPlayer().getColor().toString());
			continentsArray.add(name);
		}
		return continentsArray;
	}
	
	private static JsonArray membershipToJson(List<Continent> continents) {
		JsonArray membershipArray = new JsonArray();
		for(Continent c : continents) {
			JsonObject obj = new JsonObject();
			obj.addProperty(NAME, c.getName());
			JsonArray terrArray = new JsonArray();
			for(Territory t : c.getTerritories())
				terrArray.add(t.getName());
			obj.add(TERRITORIES, terrArray);
			membershipArray.add(obj);
		}
		return membershipArray;
	}
	
	private static JsonArray territoriesToJson(List<Territory> territories) {
		JsonArray territoriesArray = new JsonArray();
		for(Territory t : territories) {
			JsonObject obj = new JsonObject();
			obj.addProperty(NAME, t.getName());
			obj.addProperty(OWNER, t.findPlayer().getColor().toString());
			obj.addProperty(NUMBEROFTANKS, t.getNumberOfTanks());
			territoriesArray.add(obj);
		}
		return territoriesArray;
	}
	
	private static JsonArray neighbourhoodToJson(List<Territory> territories) {
		JsonArray neighbourhoodArray = new JsonArray();
		for(Territory t : territories) {
			JsonObject obj = new JsonObject();
			obj.addProperty(NAME, t.getName());
			JsonArray neighboursArray = new JsonArray();
			for(Territory neighbour : t.getNeighbours())
				neighboursArray.add(neighbour.getName());
			obj.add(TERRITORIES, neighboursArray);
			neighbourhoodArray.add(obj);
		}
		return neighbourhoodArray;
	}
	
	public static JsonObject mapToJson(String difficulty, List<Continent> continents, List<Territory> territories) {
		JsonObject result = new JsonObject();
		result.addProperty(DIFFICULTY, difficulty);
		result.add(CONTINENTS, continentsToJson(continents));
		result.add(TERRITORIES, territoriesToJson(territories));
		result.add(MEMBERSHIP, membershipToJson(continents));
		result.add(NEIGHBOURHOOD, neighbourhoodToJson(territories));
		return result;
	}

}
