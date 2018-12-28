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

public class CardDataMapper {
	
	public static final String DBCONNECTION = "jdbc:sqlite:driskdb.db";
	
	private CardDataMapper() {}
	
	public static List<String[]> getTerritoryCard(String difficulty) {
		List<String[]> territoryCard = new LinkedList<>();
		try (Connection connection = DriverManager.getConnection(DBCONNECTION); 
				Statement state = connection.createStatement()) {
			String query = SQLQuery.extractTerritoryCard(difficulty);
			try (ResultSet result = state.executeQuery(query)) {
				while(result.next()) {
					String territoryName = result.getString("territoryName");
					String continentName = result.getString("cardSymbol");
					String[] territoryInfo = new String[2];
					territoryInfo[0] = territoryName;
					territoryInfo[1] = continentName;
					territoryCard.add(territoryInfo);
				}		
			}
		}
		catch (SQLException e) {
			String msg = "Failed to execute getTerritoriesAndContinentsNames method in MapMapper: SQLException";
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, msg);
		}
		return territoryCard;
	}
	
	public static List<String> getMissionCard(String difficulty) {
		List<String> missionCard = new LinkedList<>();
		try (Connection connection = DriverManager.getConnection(DBCONNECTION); 
				Statement state = connection.createStatement()) {
			String query = SQLQuery.extractMissionCard(difficulty);
			try (ResultSet result = state.executeQuery(query)) {
				while(result.next()) {
					missionCard.add(result.getString("text"));
				}
			}
		}
		catch (SQLException e) {
			String msg = "Failed to execute getTerritoriesAndContinentsNames method in MapMapper: SQLException";
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, msg);
		}
		return missionCard;
	}
}
