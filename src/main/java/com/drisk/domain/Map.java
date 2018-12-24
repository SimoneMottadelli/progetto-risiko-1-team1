package com.drisk.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Map {

	private Map map;
	private String difficulty;
	private List<Continent> continents;
	private List<Territory> territories;
	
	private Map() {
		difficulty = null;
		continents = new ArrayList<>();
		territories = new ArrayList<>();
	}

	public Map getInstance() {
		if (map == null)
			map = new Map();
		return map;
	}

	public void createMap(String difficulty) {
		setDifficulty(difficulty);
		Connection c = null;
		try {
			c = DriverManager.getConnection("jdbc:sqlite:driskdb.db");
			Statement state = c.createStatement();
			ResultSet result = state.executeQuery("select name from continent");
			while(result.next()) {
				System.out.println(result.getString("name"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	
	public List<Continent> getContinents() {
		return continents;
	}

	
	public void setContinents(List<Continent> continents) {
		this.continents = continents;
	}
	
}
