package com.drisk.domain.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

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
	public void initPlayersTerritoriesTest() {
		
		List<Player> players = new LinkedList<>();
		Player player1 = new Player(ColorEnum.BLACK, "Player1");
		players.add(player1);
		Player player2 = new Player(ColorEnum.BLUE, "Player2");
		players.add(player2);
		
		MapManager mm = MapManager.getInstance();
		mm.initPlayersTerritories(players);
		
		List<Territory> tmp1 = mm.getPlayerTerritories(player1);
		List<Territory> tmp2 = mm.getPlayerTerritories(player2);
		List<Territory> territories = mm.getPlayerTerritories(player1);
		for (Territory t: territories)
			System.out.print(t.getName() + ", ");
		System.out.println();
		
		for (Territory t1: tmp1) 
			t1.setOwner(null);
		for (Territory t2: tmp2)
			t2.setOwner(null);
		
		mm.initPlayersTerritories(players);
		
		List<Territory> territoriesAfter = mm.getPlayerTerritories(player1);
		for (Territory t: territoriesAfter)
			System.out.print(t.getName() + ", ");
		
		assertTrue("Territories not shuffled!", !territoriesAfter.containsAll(territories));
		
	}
	
	@Test
	public void initMapEasyTest() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class);
			MapManager.getInstance().createMap(obj);

			assertEquals(6, MapManager.getInstance().getMap().getContinents().size());
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
		assertEquals(6, MapManager.getInstance().getMap().getContinents().size());
	}

	@Test
	public void createTerritoriesTest() {
		assertEquals(25, MapManager.getInstance().getMap().getTerritories().size());
		Continent c = MapManager.getInstance().getMap().findContinentByName("europe");
		assertEquals(4, c.getTerritories().size());
		Territory t = MapManager.getInstance().getMap().findTerritoryByName("venezuela");
		assertEquals(3, t.getNeighbours().size());
		t = MapManager.getInstance().getMap().findTerritoryByName("egypt");
		assertTrue(t.getNeighbours().contains(new Territory("north_west_africa")));
	}

	@Test
	public void toJsonTest() {
		Player p = new Player(ColorEnum.RED, "Simone");
		for (Territory t : MapManager.getInstance().getMap().getTerritories())
			t.setOwner(p);
		JsonObject obj = MapManager.getInstance().getMap().toJson();
		assertEquals("EASY", obj.get("difficulty").toString().replace("\"", ""));
		assertEquals(6, obj.getAsJsonArray("continents").size());
		assertEquals(25, obj.getAsJsonArray("neighbourhood").size());
		assertEquals(25, obj.getAsJsonArray("territories").size());
		assertEquals(6, obj.getAsJsonArray("membership").size());
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
