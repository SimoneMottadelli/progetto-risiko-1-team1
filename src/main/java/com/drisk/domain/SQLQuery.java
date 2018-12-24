package com.drisk.domain;

public final class SQLQuery {
	
	public static String extractContinents(String difficulty) {
		String query = "SELECT * FROM continent as c JOIN map_continent" 
				+ " as mc ON c.name = mc.continent JOIN map as m ON mc.map = m.difficulty WHERE"
				+ " m.difficulty = '" + difficulty + "'";
		return query;
	}
	
	public static String extractTerritories(String difficulty) {
		String query = "SELECT * FROM territory as t JOIN territory_map as tm ON t.name = tm.territory"
				+ " JOIN map as m on tm.map = m.difficulty WHERE m.difficulty = '" + difficulty + "'";
		return query;
	}
	
	public static String extractNeighbours(String difficulty) {
		String query = "SELECT * FROM neighbourhood as n JOIN map as m on n.map = m.difficulty"
				+ " WHERE m.difficulty = '" + difficulty + "'";
		return query;
	}
	
}
