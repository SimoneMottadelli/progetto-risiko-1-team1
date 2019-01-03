package com.drisk.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import com.drisk.technicalservice.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MapTest {
	
	@Test
	public void createMapTest() {
		Map.getInstance();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class); 
			System.out.println(obj);
			Map.getInstance().createMap(obj);
		} catch (FileNotFoundException | SyntaxException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void createContinentTest() {
		assertEquals(3, Map.getInstance().getContinents().size());
	}
	
	@Test
	public void createTerritoriesTest() {
		assertEquals(25, Map.getInstance().getTerritories().size());
		Continent c = Map.getInstance().findContinentByName("europe");
		assertEquals(7, c.getTerritories().size());
		Territory t = Map.getInstance().findTerritoryByName("congo");
		assertEquals(3, t.getNeighbours().size());
		t = Map.getInstance().findTerritoryByName("egypt");
		assertTrue(t.getNeighbours().contains(new Territory("north africa")));
	}
	
	@Test
	public void toJsonTest() {
		JsonObject obj = Map.getInstance().toJson();
		assertEquals("easy", obj.get("difficulty").toString().replace("\"", ""));
		assertEquals(3, obj.getAsJsonArray("continents").size());
		assertEquals(25, obj.getAsJsonArray("neighbourhood").size());
	}
	
}
