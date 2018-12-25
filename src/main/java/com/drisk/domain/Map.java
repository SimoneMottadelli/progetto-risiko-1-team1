package com.drisk.domain;

import java.util.ArrayList;
import java.util.List;

import com.drisk.technicalservice.MapDataMapper;

public class Map {

	private static Map instance;
	private String difficulty;
	private List<Continent> continents;
	private List<Territory> territories;
	
	private Map() {
		difficulty = null;
		continents = new ArrayList<>();
		territories = new ArrayList<>();
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

	private void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	
	public List<Continent> getContinents() {
		return continents;
	}
	
	public List<Territory> getTerritories() {
		return territories;
	}

	public void setContinents(List<Continent> continents) {
		this.continents = continents;
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
			Continent continent = findContinentByName(territoryAndContinentName[1]);
			Territory territory = new Territory(territoryName, continent);
			addTerritory(territory);
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
	
	private void addTerritory(Territory territory) {
		if(!territories.contains(territory))
			territories.add(territory);
	}
	
	public Continent findContinentByName(String continentName) {
		for(Continent c : continents)
			if(c.getName().equals(continentName))
				return c;
		return null;
	}
	
	public Territory findTerritoryByName(String territoryName) {
		for(Territory t : territories)
			if(t.getName().equals(territoryName))
				return t;
		return null;
	}
	
}
