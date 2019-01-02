package com.drisk.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MapTest {
	
	@Before
	public void createMapTest() {
		Map.getInstance();
		String s = "{'difficulty' : 'easy', 'continents' : ['africa', 'europe'], 'territories' : ['italy', 'france', 'egypt', 'north africa'],"
				+ " 'membership' : [{'name' : 'europe', 'territories' : ['italy', 'france']}, {'name' : 'africa', 'territories' : ['egypt', 'north africa']}],"
				+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, {'name' : 'north africa', 'territories' : ['egypt']}]}";
		Gson json = new Gson();
		JsonObject obj = json.fromJson(s, JsonObject.class); 
		Map.getInstance().createMap(obj);
	}
	
	@Test
	public void createContinentTest() {
		assertEquals(2, Map.getInstance().getContinents().size());
	}
	
	@Test
	public void createTerritoriesTest() {
		assertEquals(4, Map.getInstance().getTerritories().size());
		Continent c = Map.getInstance().findContinentByName("europe");
		assertEquals(2, c.getTerritories().size());
		Territory t = Map.getInstance().findTerritoryByName("italy");
		assertEquals(2, t.getNeighbours().size());
		t = Map.getInstance().findTerritoryByName("egypt");
		assertTrue(t.getNeighbours().contains(new Territory("north africa")));
	}
	
	@Test
	public void toJsonTest() {
		JsonObject obj = Map.getInstance().toJson();
		assertEquals("easy", obj.get("difficulty").toString().replace("\"", ""));
		assertEquals(2, obj.getAsJsonArray("continents").size());
		assertEquals(4, obj.getAsJsonArray("neighbourhood").size());
	}
	
}
