package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

import com.drisk.technicalservice.MapDataMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Map {

	private static Map instance;
	private String difficulty;
	private LinkedList<Continent> continents;
	
	private Map() {
		difficulty = null;
		continents = new LinkedList<>();
	}

	public static Map getInstance() {
		if (instance == null)
			instance = new Map();
		return instance;
	}
	
	public void createMap(String difficuly) {
		setDifficulty(difficuly);
		createContinents();
		createTerritories();
		createNeighbours();
	}
	
	
	private void createContinents() {
		List<String> continentsName = MapDataMapper.getContinentsNames(difficulty);
		for(String continentName : continentsName) {
			Continent c = new Continent(continentName);
			addContinent(c);
		}	
	}
	
	private void createTerritories() {
		List<String[]> territoriesAndContinentsNames = MapDataMapper.getTerritoriesAndContinentsNames(difficulty);
		for(String[] territoryAndContinentName : territoriesAndContinentsNames) {
			String territoryName = territoryAndContinentName[0];
			Territory territory = new Territory(territoryName);
			Continent continent = findContinentByName(territoryAndContinentName[1]);
			continent.addTerritory(territory);
		}	
	}
	
	private void createNeighbours() {
		List<String[]> territoriesAndNeighboursName = MapDataMapper.getTerritoriesAndNeighboursNames(difficulty);
		for(String[] territoryAndNeighbourName : territoriesAndNeighboursName) {
			String territoryName = territoryAndNeighbourName[0];
			String neighbourName = territoryAndNeighbourName[1];
			Territory t = findTerritoryByName(territoryName);
			t.addNeighbour(findTerritoryByName(neighbourName));
		}
	}
	
	private void addContinent(Continent continent) {
		if(!continents.contains(continent))
			continents.add(continent);
	}
	
	
	public Continent findContinentByName(String continentName) {
		for(Continent c : continents)
			if(c.getName().equals(continentName))
				return c;
		return null;
	}
	
	public Territory findTerritoryByName(String territoryName) {
		for(Continent c : continents)
			for(Territory t : c.getTerritories())
				if(t.getName().equals(territoryName))
					return t;
		return null;
	}
	
	private void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
	public List<Territory> getTerritories() {
		List<Territory> territories = new LinkedList<>();
		for(Continent c : continents)
			territories.addAll(c.getTerritories());
		return territories;
	}

	public LinkedList<Continent> getContinents() {
		return continents;
	}
	
	public JsonObject toJson() {
		JsonObject jsonMap = new JsonObject();
		jsonMap.addProperty("difficulty", difficulty);
		JsonArray arrayContinents = new JsonArray();
		for(Continent c : continents)
			arrayContinents.add(c.toJson());
		jsonMap.add("continents", arrayContinents);
		return jsonMap;
	}
	
}
