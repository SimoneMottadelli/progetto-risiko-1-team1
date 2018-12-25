package com.drisk.domain;

import static org.junit.Assert.assertEquals;
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
	
	@Test
	public void initPlayersTest() {
		
		gm.initPlayers(playersName);	
		
		assertEquals(gm.getPlayers().get(0).getNickname(), "Zucca");
		assertEquals(gm.getPlayers().get(1).getNickname(), "Matteo");
		assertEquals(gm.getPlayers().get(2).getNickname(), "Claudio");
		assertEquals(gm.getPlayers().get(3).getNickname(), "Simo");
		
		assertEquals(gm.getPlayers().get(0).getColor(), Color.RED);
		assertEquals(gm.getPlayers().get(1).getColor(), Color.YELLOW);
		assertEquals(gm.getPlayers().get(2).getColor(), Color.GREEN);
		assertEquals(gm.getPlayers().get(3).getColor(), Color.BLACK);
	}
	
	
	@Test
	public void initPlayersTerritoriesTest() {
		
		gm.initPlayers(playersName);
		gm.initMap();
		gm.initPlayersTerritories();
		
		List<Player> players = gm.getPlayers();
		
		int[] numberOfTerritoriesPerPlayer = new int[4];
		
		for (Territory t : map.getTerritories()) {
			int index = players.indexOf(t.getPlayer());
			++numberOfTerritoriesPerPlayer[index];
		}
		
		// In totale ci sono 25 territori del database; quindi all'inizio ogni giocatore 
		// deve avere almeno 10 territori a testa.
		for (int i = 0; i < numberOfTerritoriesPerPlayer.length; ++i)
			if (numberOfTerritoriesPerPlayer[i] < 6)
				fail();
	}
}
