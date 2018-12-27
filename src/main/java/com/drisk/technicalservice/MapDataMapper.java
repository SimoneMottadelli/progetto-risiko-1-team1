package com.drisk.technicalservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MapDataMapper {
	
	public static final String DBCONNECTION = "jdbc:sqlite:driskdb.db";
	
	private MapDataMapper() {}
	
	public static List<String> getContinentsNames(String difficulty) {
		List<String> continentsNames = new LinkedList<>();
		try (Connection connection = DriverManager.getConnection(DBCONNECTION);
				Statement state = connection.createStatement()) {
			String query = SQLQuery.extractContinents(difficulty);
			try (ResultSet result = state.executeQuery(query)) {
				while(result.next()) {
					String contientName = result.getString("name");
					continentsNames.add(contientName);
				}				
			}
		}
		catch (SQLException e) {
			String msg = "Failed to execute getContinentsNames method in MapMapper: SQLException";
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, msg);
		}
		return continentsNames;
	}
	
	public static List<String[]> getTerritoriesAndContinentsNames(String difficulty) {
		List<String[]> territoriesAndContinentsNames = new LinkedList<>();
		try (Connection connection = DriverManager.getConnection(DBCONNECTION); 
				Statement state = connection.createStatement()) {
			String query = SQLQuery.extractTerritoriesAndContinents(difficulty);
			try (ResultSet result = state.executeQuery(query)) {
				while(result.next()) {
					String territoryName = result.getString("territory");
					String continentName = result.getString("continent");
					String[] territoryAndContinent = new String[2];
					territoryAndContinent[0] = territoryName;
					territoryAndContinent[1] = continentName;
					territoriesAndContinentsNames.add(territoryAndContinent);
				}		
			}
		}
		catch (SQLException e) {
			String msg = "Failed to execute getTerritoriesAndContinentsNames method in MapMapper: SQLException";
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, msg);
		}
		return territoriesAndContinentsNames;
	}
	
	public static List<String[]> getTerritoriesAndNeighboursNames(String difficulty) {
		List<String[]> territoriesAndNeighboursNames = new LinkedList<>();
		try (Connection connection = DriverManager.getConnection(DBCONNECTION); 
				Statement state = connection.createStatement()) {
			String query = SQLQuery.extractTerritoriesAndNeighbours(difficulty);
			try(ResultSet result = state.executeQuery(query)) {
				while(result.next()) {
					String territoryName = result.getString("territory");
					String neighbourName = result.getString("neighbour");
					String[] territoryAndNeighbour = new String[2];
					territoryAndNeighbour[0] = territoryName;
					territoryAndNeighbour[1] = neighbourName;
					territoriesAndNeighboursNames.add(territoryAndNeighbour);
				}	
			}
		}
		catch (SQLException e) {
			String msg = "Failed to execute getTerritoriesAndContinentsNames method in MapMapper: SQLException";
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, msg);
		}
		return territoriesAndNeighboursNames;
	}

}
