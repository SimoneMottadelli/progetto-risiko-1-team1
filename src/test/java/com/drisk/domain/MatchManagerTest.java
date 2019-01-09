package com.drisk.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

public class MatchManagerTest {
	
	@Before
	public void init() {
		for(int i = 1; i <= 6; i++)
			MatchManager.getInstance().joinGame("player" + i);
	}
	
	@Test
	public void joinGameTest() {
		assertTrue(MatchManager.getInstance().isMatchFull());
	}	

	@Test 
	public void colorPlayersTest() {
		Color[] colors = Color.values();
		boolean exist;
		List<Player> list = MatchManager.getInstance().getPlayers();
		for(Color c : colors) {
			exist = false;
			for(Player p : list)
				if(c.equals(p.getColor()))
					exist = true;
			if(!exist)
				fail();
		}	
	}
	
	@Test
	public void areThereAtLeastTwoPlayersTest() {
		assertTrue(MatchManager.getInstance().areThereAtLeastTwoPlayers());
		Color[] colors = Color.values();
		for (int i = 0; i < colors.length; ++i)
			MatchManager.getInstance().exitGame(colors[i]);
		assertFalse(MatchManager.getInstance().areThereAtLeastTwoPlayers());
	}
	
	@Test
	public void isEveryoneReadyTest() {
		assertFalse(MatchManager.getInstance().isEveryoneReady());
		for (Color c : Color.values())
			MatchManager.getInstance().setPlayerReady(c, true);
		assertTrue(MatchManager.getInstance().isEveryoneReady());
	}
	
	@Test
	public void isMatchFullTest() {
		assertTrue(MatchManager.getInstance().isMatchFull());
		MatchManager.getInstance().exitGame(Color.BLUE);
		assertFalse(MatchManager.getInstance().isMatchFull());
	}
	
	@Test
	public void exitGameTest() {
		assertEquals(6, MatchManager.getInstance().getPlayers().size());
		Color[] colors = Color.values();
		for (int i = 0; i < colors.length; ++i)
			MatchManager.getInstance().exitGame(colors[i]);
		assertEquals(0, MatchManager.getInstance().getPlayers().size());
	}
	
	@Test
	public void isMatchStartedTest() {
		assertFalse(MatchManager.getInstance().isMatchStarted());
	}
	
	@Test
	public void toJsonTest() {
		JsonObject obj = MatchManager.getInstance().toJson();
		assertEquals(6, obj.getAsJsonArray("players").size());
	}	
	
	@After
	public void destroySingletons() {
		MatchManager.destroy();
		GameManager.destroy();
	}
}
