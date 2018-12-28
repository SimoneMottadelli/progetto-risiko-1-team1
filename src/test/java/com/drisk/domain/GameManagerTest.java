package com.drisk.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class GameManagerTest {	
	
	@Before 
	public void init() {
		for(int i = 1; i <= 6; i++)
			MatchManager.getInstance().joinGame("Player" + i);
		MatchManager.getInstance().startGame();
	}
	
	@Test
	public void startGameTest() {
		assertTrue(GameManager.getInstance().getPlayers().size() == 6);
		assertTrue(Map.getInstance().getContinents().size() == 3);
		assertTrue(Map.getInstance().getTerritories().size() == 25);
	}
	
	/*
	@Test
	public void initPlayersTerritoriesTest() {
		List<Player> players = GameManager.getInstance().getPlayers();
		for(Player p : players)
			if(p.getNumberOfTerritoriesOwned() < 6)
				fail();
	}
*/
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
}
