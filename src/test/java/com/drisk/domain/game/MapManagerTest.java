package com.drisk.domain.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.ColorEnum;
import com.drisk.domain.Continent;
import com.drisk.domain.MapManager;
import com.drisk.domain.Player;
import com.drisk.domain.Territory;
import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MapManagerTest {

	@Before
	public void initMapManagerTest() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class);
			MapManager.getInstance().createMap(obj);
		} catch (FileNotFoundException | SyntaxException e) {
		}
	}

	@Test
	public void initMapEasyTest() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class);
			MapManager.getInstance().createMap(obj);

			assertEquals(3, MapManager.getInstance().getMap().getContinents().size());
			assertEquals(25, MapManager.getInstance().getMap().getTerritories().size());
		} catch (FileNotFoundException | SyntaxException e) {
		}
	}

	@Test
	public void initMapHardTest() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_hard.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class);
			MapManager.getInstance().createMap(obj);
			assertEquals(6, MapManager.getInstance().getMap().getContinents().size());
			assertEquals(42, MapManager.getInstance().getMap().getTerritories().size());
		} catch (FileNotFoundException | SyntaxException e) {
		}
	}

	@Test
	public void createContinentTest() {
		assertEquals(3, MapManager.getInstance().getMap().getContinents().size());
	}

	@Test
	public void createTerritoriesTest() {
		assertEquals(25, MapManager.getInstance().getMap().getTerritories().size());
		Continent c = MapManager.getInstance().getMap().findContinentByName("europe");
		assertEquals(7, c.getTerritories().size());
		Territory t = MapManager.getInstance().getMap().findTerritoryByName("congo");
		assertEquals(3, t.getNeighbours().size());
		t = MapManager.getInstance().getMap().findTerritoryByName("egypt");
		assertTrue(t.getNeighbours().contains(new Territory("north_africa")));
	}

	@Test
	public void toJsonTest() {
		Player p = new Player(ColorEnum.RED, "Simone");
		for (Territory t : MapManager.getInstance().getMap().getTerritories())
			t.setOwner(p);
		JsonObject obj = MapManager.getInstance().getMap().toJson();
		assertEquals("EASY", obj.get("difficulty").toString().replace("\"", ""));
		assertEquals(3, obj.getAsJsonArray("continents").size());
		assertEquals(25, obj.getAsJsonArray("neighbourhood").size());
		assertEquals(25, obj.getAsJsonArray("territories").size());
		assertEquals(3, obj.getAsJsonArray("membership").size());
		System.out.println(obj);
	}

	@Test
	public void syntaxErrorNeighbourhoodKeywordCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " + "'continents' : ['africa', 'europe'], "
					+ "'territories' : ['italy','france', 'egypt', 'north_africa'],"
					+ "'membership' : [{'name' : 'europe', " + "'territories' : ['italy', 'france']}, "
					+ "{'name' : 'africa', " + "'territories' : ['egypt', 'north_africa']}],"
					+ " 'neighbourhoo' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, "
					+ "{'name' : 'north_africa', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class);
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {
		}
	}

	@Test
	public void syntaxErrorNameKeywordCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " + "'continents' : ['africa', 'europe'], "
					+ "'territories' : ['italy','france', 'egypt', 'north_africa'],"
					+ "'membership' : [{'name' : 'europe', " + "'territories' : ['italy', 'france']}, "
					+ "{'name' : 'africa', " + "'territories' : ['egypt', 'north_africa']}],"
					+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, "
					+ "{'nome' : 'north_africa', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class);
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {
		}
	}

	@Test
	public void syntaxErrorNotExistingTerritoryFoundInNeighbourhoodCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " + "'continents' : ['africa', 'europe'], "
					+ "'territories' : ['italy','france', 'egypt', 'north_africa'],"
					+ "'membership' : [{'name' : 'europe', " + "'territories' : ['italy', 'france']}, "
					+ "{'name' : 'africa', " + "'territories' : ['egypt', 'north_africa']}],"
					+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, "
					+ "{'name' : 'adsfakjòak', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class);
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {
		}
	}

	@Test
	public void syntaxErrorNotExistingTerritoryFoundInMembershipCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " + "'continents' : ['africa', 'europe'], "
					+ "'territories' : ['italy','france', 'egypt', 'north_africa'],"
					+ "'membership' : [{'name' : 'djsfs', " + "'territories' : ['italy', 'france']}, "
					+ "{'name' : 'africa', " + "'territories' : ['egypt', 'north_africa']}],"
					+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, "
					+ "{'name' : 'north_africa', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class);
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {
		}
	}

	@Test
	public void syntaxErrorTerritoriesKeywordCreateMapTest() {
		try {
			String s = "{'difficulty' : 'custom', " + "'continents' : ['africa', 'europe'], "
					+ "'territories' : ['italy','france', 'egypt', 'north_africa'],"
					+ "'membership' : [{'name' : 'europe', " + "'territores' : ['italy', 'france']}, "
					+ "{'name' : 'africa', " + "'territories' : ['egypt', 'north_africa']}],"
					+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, "
					+ "{'name' : 'north_africa', 'territories' : ['egypt']}]}";

			Gson json = new Gson();
			JsonObject obj = json.fromJson(s, JsonObject.class);
			MapManager.getInstance().createMap(obj);
			fail();
		} catch (SyntaxException | FileNotFoundException e) {
		}
	}
}
