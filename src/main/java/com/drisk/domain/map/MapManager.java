package com.drisk.domain.map;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.drisk.domain.exceptions.SyntaxException;
import com.drisk.domain.game.Player;
import com.drisk.technicalservice.FileLoader;
import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonObject;

public class MapManager {

	private static MapManager instance;
	private Map map;

	private static final String SYNTAXERROR = "SyntaxError: ";
	
	private MapManager() {
		map = new Map();
	}

	public static MapManager getInstance() {
		if (instance == null)
			instance = new MapManager();
		return instance;
	}
	
	/**
	 * It allows to create map with specific features through JsonObject configuration object
	 * @param gameConfig JsonObject with map configuration
	 * @throws SyntaxException if the map difficulty is custom and there is an error during the creation with that JsonObject
	 * @throws FileNotFoundException if the map difficulty is not custom but the file with map is not found
	 */
	public void createMap(JsonObject gameConfig) throws SyntaxException, FileNotFoundException {
		map = new Map();
		map.setDifficulty(new JsonHelper().difficultyFromJson(gameConfig));
		if(map.getDifficulty().equals(DifficultyEnum.CUSTOM))
			createMapComponents(gameConfig);
		else
			createMapComponents(new FileLoader().readDefaultMapFile(map.getDifficulty()));
		map.setReady(true);
	}
	
	/**
	 * It allow to create map components, like continents, territories and neighbours
	 * @param gameConfig JsonObject with map configuration
	 * @throws SyntaxException if some map components isn't written correctly
	 */
	private void createMapComponents(JsonObject gameConfig) throws SyntaxException {
		JsonHelper helper = new JsonHelper();
		createContinents(helper.getContinentsFromJson(gameConfig));
		createTerritories(helper.getMembershipFromJson(gameConfig));
		createNeighbours(helper.getNeighbourhoodFromJson(gameConfig));
	}
	
	
	/**
	 * It allows to create continents with their name
	 * @param continentsNames List<String> with continents's names
	 */
	private void createContinents(List<String> continentsNames) {
		for(String continentName : continentsNames) {
			Continent c = new Continent(continentName);
			map.addContinent(c);
		}	
	}

	/**
	 * It allows to create territories with their name and their continent's name owner
	 * @param relation Map<String, List<String>> with continent name as key and territories list as value
	 */
	private void createTerritories(java.util.Map<String, List<String>> relation) throws SyntaxException {
		for(java.util.Map.Entry<String, List<String>> entry : relation.entrySet()) {
			Continent c = findContinentByName(entry.getKey());
			if (c == null)
				throw new SyntaxException(SYNTAXERROR + entry.getKey() + " is not a valid continent");
			for(String territoryName : relation.get(c.getName())) {
				Territory t = new Territory(territoryName);
				c.addTerritory(t);
			}
		}
	}
	
	private void createNeighbours(java.util.Map<String, List<String>> relation) throws SyntaxException {
		for(java.util.Map.Entry<String, List<String>> entry : relation.entrySet()) {
			Territory t = findTerritoryByName(entry.getKey());
			if (t == null)
				throw new SyntaxException(SYNTAXERROR + entry.getKey() + " is not a valid territory");
			
			for(String neighbourName : relation.get(t.getName())) {
				Territory neighbour = findTerritoryByName(neighbourName);
				if (neighbour == null)
					throw new SyntaxException(SYNTAXERROR + neighbourName + 
							" is not a valid neighbour territory for " + t.getName());
				t.addNeighbour(neighbour);
				neighbour.addNeighbour(t);
			}
		}
	}
	
	/**
	 * It allow to retrieve a continent by his name
	 * @param continentName String name of contitent
	 * @return Continent with continentName or null if it doesn't exist
	 */
	public Continent findContinentByName(String continentName) {
		for(Continent c : map.getContinents())
			if(c.getName().equals(continentName))
				return c;
		return null;
	}
	
	/**
	 * It allows to spread territories to players
	 * @param players List<Player> of players
	 */
	public void initPlayersTerritories(List<Player> players) {
		Collections.shuffle(getMapTerritories());
		int i = 0;
		for(Territory t : getMapTerritories()) {
			t.setOwner(players.get(i % players.size()));
			++i;
		}
	}
	
	public List<Territory> getMapTerritories(Player player) {
		List<Territory> territoriesOwned = new LinkedList<>();
		for(Territory t : getMapTerritories())
			if(t.getOwner().equals(player))
				territoriesOwned.add(t);
		return territoriesOwned;
	}
	
	/**
	 * It allow to retrieve a territory by his name
	 * @param territoryName String name of territory
	 * @return Territory with territoryName or null if it doesn't exist
	 */
	public Territory findTerritoryByName(String territoryName) {
		for(Continent c : map.getContinents())
			for(Territory t : c.getTerritories())
				if(t.getName().equals(territoryName))
					return t;
		return null;
	}
	
	public int getNumberOfTerritories() {
		return getMapTerritories().size();
	}
	
	public List<Territory> getMapTerritories() {
		return map.getTerritories();
	}

	public List<Continent> getMapContinents() {
		return map.getContinents();
	}
	
	public String getSVGMap() throws IOException {
		return new FileLoader().readSVGMapFile(map.getDifficulty());
	}
	
	public JsonObject toJson() {
		return map.toJson();
	}

	public boolean isMapReady() {
		return map.isReady();
	}
	
	public DifficultyEnum getMapDifficulty() {
		return map.getDifficulty();
	}	
}
