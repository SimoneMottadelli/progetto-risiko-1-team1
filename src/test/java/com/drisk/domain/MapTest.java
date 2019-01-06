package com.drisk.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MapTest {
	
	@Before
	public void initMapTest() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class); 
			MapManager.destroy();
			MapManager.getInstance().createMap(obj);
		} catch (FileNotFoundException | SyntaxException e) {}
	}

	@Test
	public void createContinentTest() {
		assertEquals(3, MapManager.getInstance().getMapContinents().size());
	}
	
	@Test
	public void createTerritoriesTest() {
		assertEquals(25, MapManager.getInstance().getMapTerritories().size());
		Continent c = MapManager.getInstance().findContinentByName("europe");
		assertEquals(7, c.getTerritories().size());
		Territory t = MapManager.getInstance().findTerritoryByName("congo");
		assertEquals(3, t.getNeighbours().size());
		t = MapManager.getInstance().findTerritoryByName("egypt");
		assertTrue(t.getNeighbours().contains(new Territory("north africa")));
	}
	
	@Test
	public void toJsonTest() {
		JsonObject obj = MapManager.getInstance().toJson();
		assertEquals("easy", obj.get("difficulty").toString().replace("\"", ""));
		assertEquals(3, obj.getAsJsonArray("continents").size());
		assertEquals(25, obj.getAsJsonArray("neighbourhood").size());
	}
	
	@Test
	public void syntaxErrorNeighbourhoodKeywordCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " +
						"'continents' : ['africa', 'europe'], " + 
						"'territories' : ['italy','france', 'egypt', 'north africa']," +
						"'membership' : [{'name' : 'europe', " + 
										 "'territories' : ['italy', 'france']}, " +
										"{'name' : 'africa', " +
										 "'territories' : ['egypt', 'north africa']}],"
					+ " 'neighbourhoo' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, " +
										  "{'name' : 'north africa', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class); 
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {}
	}
	
	@Test
	public void syntaxErrorNameKeywordCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " +
						"'continents' : ['africa', 'europe'], " + 
						"'territories' : ['italy','france', 'egypt', 'north africa']," +
						"'membership' : [{'name' : 'europe', " + 
										 "'territories' : ['italy', 'france']}, " +
										"{'name' : 'africa', " +
										 "'territories' : ['egypt', 'north africa']}],"
					+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, " +
										  "{'nome' : 'north africa', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class); 
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {}
	}
	
	@Test
	public void syntaxErrorNotExistingTerritoryFoundInNeighbourhoodCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " +
						"'continents' : ['africa', 'europe'], " + 
						"'territories' : ['italy','france', 'egypt', 'north africa']," +
						"'membership' : [{'name' : 'europe', " + 
										 "'territories' : ['italy', 'france']}, " +
										"{'name' : 'africa', " +
										 "'territories' : ['egypt', 'north africa']}],"
					+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, " +
										  "{'name' : 'adsfakjòak', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class); 
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {}
	}
	
	@Test
	public void syntaxErrorNotExistingTerritoryFoundInMembershipCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " +
						"'continents' : ['africa', 'europe'], " + 
						"'territories' : ['italy','france', 'egypt', 'north africa']," +
						"'membership' : [{'name' : 'djsfs', " + 
										 "'territories' : ['italy', 'france']}, " +
										"{'name' : 'africa', " +
										 "'territories' : ['egypt', 'north africa']}],"
					+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, " +
										  "{'name' : 'north africa', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class); 
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {}
	}
	
	@Test
	public void syntaxErrorTerritoriesKeywordCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " +
						"'continents' : ['africa', 'europe'], " + 
						"'territories' : ['italy','france', 'egypt', 'north africa']," +
						"'membership' : [{'name' : 'europe', " + 
										 "'territores' : ['italy', 'france']}, " +
										"{'name' : 'africa', " +
										 "'territories' : ['egypt', 'north africa']}],"
					+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, " +
										  "{'name' : 'north africa', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class); 
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {}
	}
	
	@After
	public void destroyMapTest() {
		MapManager.destroy();
	}
	
}
