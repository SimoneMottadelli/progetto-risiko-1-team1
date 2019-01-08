package com.drisk.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GameManagerTest {	
	
	@Before 
	public void init() {
		for(int i = 1; i <= 6; i++)
			MatchManager.getInstance().joinGame("Player" + i);
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class); 
			MapManager.getInstance().createMap(obj);
		} catch (FileNotFoundException | SyntaxException e) {
			e.printStackTrace();
		}
		MatchManager.getInstance().initGame();
	}
	
	@Test
	public void startGameTest() {
		assertTrue(GameManager.getInstance().getPlayers().size() == 6);
		assertTrue(MapManager.getInstance().getMapContinents().size() == 3);
		assertTrue(MapManager.getInstance().getMapTerritories().size() == 25);
	}
	
	@Test
	public void findPlayerByColorTest() {
		Color[] colors = Color.values();
		int i = 0;
		for (Player p : GameManager.getInstance().getPlayers())
			assertEquals(p.getNickname(), GameManager.getInstance().findPlayerByColor(colors[i++]).getNickname());
	}
	
	
	@Test
	public void initPlayersTerritoriesTest() {
		List<Player> players = GameManager.getInstance().getPlayers();
		for(Player p : players)
			if(MapManager.getInstance().getMapTerritories(p).size() < 4)
				fail();
	}

	/* questo test deve essere sistemato poiche è cambiata la logica
	@Test
	public void checkWinTest() {
		
		Player p1 = new Player(Color.RED, "Simone");
		List<Territory> territories = Map.getInstance().getTerritories();
		
		//Simone has now 17 territories. He has completed the objective:
		// "conquer at least 2/3 of the world".
		//Note: there are 25 territories in the database right now.
		for (int i = 0; i < 17; ++i)
			p1.addTerritoryOwned(territories.get(i));
		assertTrue(GameManager.getInstance().checkWin(p1));
		
		
		//Claudio has less than 2/3 of the territories, so he hasn't won.
		Player p2 = new Player(Color.BLACK, "Claudio");
		assertFalse(GameManager.getInstance().checkWin(p2));
	}
	*/
}
