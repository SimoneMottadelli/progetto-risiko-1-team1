package com.drisk.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.drisk.domain.exceptions.SyntaxException;
import com.drisk.technicalservice.FileLoader;
import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MapManager {

	private static MapManager instance;
	private com.drisk.domain.Map map;

	private static final String SYNTAXERROR = "SyntaxError: ";
	private static final String TERRITORIES = "territories";
	private static final String CONTINENTS = "continents";
	private static final String NAME = "name";
	private static final String NEIGHBOURHOOD = "neighbourhood";
	private static final String MEMBERSHIP = "membership";

	private MapManager() {
		map = new com.drisk.domain.Map();
	}

	public static MapManager getInstance() {
		if (instance == null)
			instance = new MapManager();
		return instance;
	}

	public com.drisk.domain.Map getMap() {
		return map;
	}

	/**
	 * It allows to create map with specific features through JsonObject
	 * configuration object
	 * 
	 * @param gameConfig JsonObject with map configuration
	 * @throws SyntaxException       if the map difficulty is custom and there is an
	 *                               error during the creation with that JsonObject
	 * @throws FileNotFoundException if the map difficulty is not custom but the
	 *                               file with map is not found
	 */
	public void createMap(JsonObject gameConfig) throws SyntaxException, FileNotFoundException {
		map = new com.drisk.domain.Map();
		map.setDifficulty(new JsonHelper().difficultyFromJson(gameConfig));
		if (map.getDifficulty().equals(DifficultyEnum.CUSTOM))
			createMapComponents(gameConfig);
		else
			createMapComponents(new FileLoader().readDefaultMapFile(map.getDifficulty().toString().toLowerCase()));
		map.setReady(true);
	}

	/**
	 * It allow to create map components, like continents, territories and
	 * neighbours
	 * 
	 * @param gameConfig JsonObject with map configuration
	 * @throws SyntaxException if some map components isn't written correctly
	 */
	private void createMapComponents(JsonObject gameConfig) throws SyntaxException {
		createContinents(getContinentsFromJson(gameConfig));
		createTerritories(getMembershipFromJson(gameConfig));
		createNeighbours(getNeighbourhoodFromJson(gameConfig));
	}

	/**
	 * It allows to create continents with their name
	 * 
	 * @param continentsNames List<String> with continents's names
	 */
	private void createContinents(List<String> continentsNames) {
		for (String continentName : continentsNames) {
			Continent c = new Continent(continentName);
			map.addContinent(c);
		}
	}

	/**
	 * It allows to create territories with their name and their continent's name
	 * owner
	 * 
	 * @param relation Map<String, List<String>> with continent name as key and
	 *                 territories list as value
	 */
	private void createTerritories(java.util.Map<String, List<String>> relation) throws SyntaxException {
		for (java.util.Map.Entry<String, List<String>> entry : relation.entrySet()) {
			Continent c = map.findContinentByName(entry.getKey());
			if (c == null)
				throw new SyntaxException(SYNTAXERROR + entry.getKey() + " is not a valid continent");
			for (String territoryName : relation.get(c.getName())) {
				Territory t = new Territory(territoryName);
				c.addTerritory(t);
			}
		}
	}

	private void createNeighbours(java.util.Map<String, List<String>> relation) throws SyntaxException {
		for (java.util.Map.Entry<String, List<String>> entry : relation.entrySet()) {
			Territory t = map.findTerritoryByName(entry.getKey());
			if (t == null)
				throw new SyntaxException(SYNTAXERROR + entry.getKey() + " is not a valid territory");

			for (String neighbourName : relation.get(t.getName())) {
				Territory neighbour = map.findTerritoryByName(neighbourName);
				if (neighbour == null)
					throw new SyntaxException(
							SYNTAXERROR + neighbourName + " is not a valid neighbour territory for " + t.getName());
				t.addNeighbour(neighbour);
				neighbour.addNeighbour(t);
			}
		}
	}

	public void initPlayersTerritories(List<Player> players) {
		Collections.shuffle(map.getTerritories());
		int i = 0;
		for (Territory t : map.getTerritories()) {
			t.setOwner(players.get(i % players.size()));
			++i;
		}
	}

	public List<Territory> getPlayerTerritories(Player player) {
		List<Territory> territoriesOwned = new LinkedList<>();
		for (Territory t : map.getTerritories())
			if (t.getOwner().equals(player))
				territoriesOwned.add(t);
		return territoriesOwned;
	}

	/**
	 * It allow to retrieve a territory by his name
	 * 
	 * @param territoryName String name of territory
	 * @return Territory with territoryName or null if it doesn't exist
	 */

	public String getSVGMap() throws IOException {
		return new FileLoader().readSVGMapFile(map.getDifficulty());
	}

	public static void destroy() {
		instance = null;
	}

	public DifficultyEnum getMapDifficulty() {
		return map.getDifficulty();
	}

	private List<String> getListFromJson(JsonObject gameConfig, String memberName) throws SyntaxException {
		List<String> list = new LinkedList<>();
		JsonArray array = gameConfig.getAsJsonArray(memberName);
		if (array == null)
			throw new SyntaxException("Syntax error: " + memberName + " keyword must exists");

		for (JsonElement name : array) {
			String nameFormatted = name.toString().replace("\"", "");
			if (!list.contains(nameFormatted))
				list.add(nameFormatted);
		}
		return list;
	}

	private List<String> getTerritoriesFromJson(JsonObject gameConfig) throws SyntaxException {
		return getListFromJson(gameConfig, TERRITORIES);
	}

	private List<String> getContinentsFromJson(JsonObject gameConfig) throws SyntaxException {
		return getListFromJson(gameConfig, CONTINENTS);
	}

	private java.util.Map<String, List<String>> getRelationshipFromJson(JsonObject gameConfig, String relation)
			throws SyntaxException {
		Map<String, List<String>> map = new HashMap<>();
		JsonArray array = gameConfig.getAsJsonArray(relation);
		if (array == null)
			throw new SyntaxException("Syntax error: " + relation + " keyword must exists");
		for (int i = 0; i < array.size(); ++i) {
			JsonObject obj = array.get(i).getAsJsonObject();
			JsonElement continentElement = obj.get(NAME);
			if (continentElement == null)
				throw new SyntaxException(
						"Syntax error: in membership or neighbourhood object there is an unknown keyword."
								+ " It should contain the following keywords: \"name\" and \"territories\"");
			String continentName = continentElement.toString().replace("\"", "");
			List<String> territoriesNames = getTerritoriesFromJson(obj);
			map.put(continentName, territoriesNames);
		}
		return map;
	}

	private java.util.Map<String, List<String>> getNeighbourhoodFromJson(JsonObject gameConfig) throws SyntaxException {
		return getRelationshipFromJson(gameConfig, NEIGHBOURHOOD);
	}

	private java.util.Map<String, List<String>> getMembershipFromJson(JsonObject gameConfig) throws SyntaxException {
		return getRelationshipFromJson(gameConfig, MEMBERSHIP);
	}

}
