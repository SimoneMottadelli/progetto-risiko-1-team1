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
	
	// if no exception is thrown, map will be set to ready
	public void createMap(JsonObject gameConfig) throws SyntaxException, FileNotFoundException {
		map = new Map();
		map.setDifficulty(new JsonHelper().difficultyFromJson(gameConfig));
		if(map.getDifficulty().equals(DifficultyEnum.CUSTOM))
			createMapComponents(gameConfig);
		else
			createMapComponents(new FileLoader().readDefaultMapFile(map.getDifficulty()));
		map.setReady(true);
	}
	
	public JsonObject getSVGMap() throws IOException {
		return new FileLoader().readSVGMapFile(map.getDifficulty());
	}
	
	private void createMapComponents(JsonObject gameConfig) throws SyntaxException {
		JsonHelper helper = new JsonHelper();
		createContinents(helper.getContinentsFromJson(gameConfig));
		createTerritories(helper.getMembershipFromJson(gameConfig));
		createNeighbours(helper.getNeighbourhoodFromJson(gameConfig));
	}
	
	private void createContinents(List<String> continentsNames) {
		for(String continentName : continentsNames) {
			Continent c = new Continent(continentName);
			map.addContinent(c);
		}	
	}
	
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
	
	public Continent findContinentByName(String continentName) {
		for(Continent c : map.getContinents())
			if(c.getName().equals(continentName))
				return c;
		return null;
	}
	
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
