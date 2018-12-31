package com.drisk.technicalservice;

public final class SQLQuery {
	
	private SQLQuery() {}
	
	public static String extractContinents(String difficulty) {
		return "SELECT * FROM continent as c JOIN map_continent" 
				+ " as mc ON c.name = mc.continent JOIN map as m ON mc.map = m.difficulty WHERE"
				+ " m.difficulty = '" + difficulty + "'";
	}
	
	public static String extractTerritoriesAndContinents(String difficulty) {
		return "SELECT * FROM territory as t JOIN territory_map as tm ON t.name = tm.territory"
				+ " JOIN map as m on tm.map = m.difficulty WHERE m.difficulty = '" + difficulty + "'";
	}
	
	public static String extractTerritoriesAndNeighbours(String difficulty) {
		return "SELECT * FROM neighbourhood as n JOIN map as m on n.map = m.difficulty"
				+ " WHERE m.difficulty = '" + difficulty + "'";
	}
	
	public static String extractTerritoryCard(String difficulty) {
		return "SELECT territoryName, cardSymbol " +
				"FROM ((map as m join map_continent as mc on m.difficulty = mc.map) " +
						"join territory as t on mc.continent = t.continent) " +
						"join territory_card as tc on tc.territoryName = t.name " +
				"WHERE m.difficulty = '" + difficulty + "'";
	}
	
	public static String extractMissionCard(String difficulty) {
		return "SELECT * " + 
				"FROM mission_card " +
				"WHERE difficulty = '" + difficulty + "'";
	}
	
}
