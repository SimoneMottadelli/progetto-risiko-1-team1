package com.drisk.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class GameManagerTest {	
	
	private GameManager gm;
	private Map map;
	private List<String> playersName;
	
	@Before
	public void init() {
		gm = GameManager.getInstance();
		map = Map.getInstance();
		
		playersName = new LinkedList<>();
		playersName.add("Zucca");
		playersName.add("Matteo");
		playersName.add("Claudio");
		playersName.add("Simo");
	}
	
	/*@Test
	public void initPlayersTest() {
		
		gm.initPlayers(playersName);	
		
		assertEquals("Zucca", gm.getPlayers().get(0).getNickname());
		assertEquals("Matteo", gm.getPlayers().get(1).getNickname());
		assertEquals("Claudio", gm.getPlayers().get(2).getNickname());
		assertEquals("Simo", gm.getPlayers().get(3).getNickname());
		
		assertEquals(Color.RED, gm.getPlayers().get(0).getColor());
		assertEquals(Color.YELLOW, gm.getPlayers().get(1).getColor());
		assertEquals(Color.GREEN, gm.getPlayers().get(2).getColor());
		assertEquals(Color.BLACK, gm.getPlayers().get(3).getColor());
	}
	
	
	@Test
	public void initPlayersTerritoriesTest() {
		
		gm.initPlayers(playersName);
		gm.initMap();
		gm.initPlayersTerritories();
		
		List<Player> players = gm.getPlayers();
		
		for(Player p : players)
			if(p.getNumberOfTerritoriesOwned() < 6)
				fail();
	}
	
	
	@Test
	public void checkWinTest() {
		
		Player p1 = new Player("Simone", Color.RED);
		List<Territory> territories = map.getTerritories();
		
		//Simone has now 17 territories. He has completed the objective:
		// "conquer at least 2/3 of the world".
		//Note: there are 25 territories in the database right now.
		for (int i = 0; i < 17; ++i)
			p1.addTerritoryOwned(territories.get(i));
		assertTrue(gm.checkWin(p1));
		
		
		//Claudio has less than 2/3 of the territories, so he hasn't won.
		Player p2 = new Player("Claudio", Color.BLACK);
		assertFalse(gm.checkWin(p2));
		
	}*/
}
