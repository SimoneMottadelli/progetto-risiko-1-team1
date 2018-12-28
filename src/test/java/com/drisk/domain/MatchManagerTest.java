package com.drisk.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MatchManagerTest {
	
	@Before
	public void init() {
		for(int i = 1; i <= 6; i++)
			MatchManager.getInstance().joinGame("player" + i);
	}
	
	@Test
	public void joinGameTest() {
		for(int i = 0; i < 3; i++) {
			assertTrue(MatchManager.getInstance().isMatchFull());
		}
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
	
}
