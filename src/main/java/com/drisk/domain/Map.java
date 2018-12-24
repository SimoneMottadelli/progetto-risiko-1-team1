package com.drisk.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

	public void createMap(String difficulty) throws SQLException {
		setDifficulty(difficulty);
		try (Connection c = DriverManager.getConnection("jdbc:sqlite:driskdb.db")) {
			Statement state = c.createStatement();
			createContinents(state);
			createTerritories(state);
			createNeighbour(state);				
		}
	}
	
	private void createNeighbour(Statement state) throws SQLException {
		String query = SQLQuery.extractNeighbours(difficulty);
		try (ResultSet result = state.executeQuery(query)) {
			while(result.next()) {
				String territoryName = result.getString("territory");
				String neighbourName = result.getString("neighbour");
				Territory t = findTerritoryByName(territoryName);
				t.addNeighbour(findTerritoryByName(neighbourName));
			}		
		}
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
	
	private void createTerritories(Statement state) throws SQLException {
		String query = SQLQuery.extractTerritories(difficulty);
		try (ResultSet result = state.executeQuery(query)) {
			while(result.next()) {
				String territoryName = result.getString("name");
				String continentName = result.getString("continent");
				Territory t = new Territory(territoryName, findContinentByName(continentName));
				addTerritory(t);
			}	
		}
	}
	
	private void createContinents(Statement state) throws SQLException {
		String query = SQLQuery.extractContinents(difficulty);
		try (ResultSet result = state.executeQuery(query)) {
			while(result.next()) {
				String continentName = result.getString("name");
				Continent c = new Continent(continentName);
				addContinent(c);
			}		
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
