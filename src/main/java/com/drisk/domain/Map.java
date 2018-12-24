package com.drisk.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Map {

	private String difficulty;
	private List<Continent> continents;
	private List<Territory> territories;
	
	public Map(String difficulty) {
		this.difficulty = difficulty;
		continents = new ArrayList<>();
		territories = new ArrayList<>();
	}
	
	public void createMap() {
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

	
	
}
