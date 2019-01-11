package com.drisk.domain.lobby;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.game.ColorEnum;
import com.drisk.domain.game.GameManager;
import com.drisk.domain.game.Player;
import com.drisk.domain.lobby.LobbyManager;

public class MatchManagerTest {
	
	@Before
	public void init() {
		for(int i = 1; i <= 6; i++)
			LobbyManager.getInstance().joinGame("player" + i);
	}
	
	@Test
	public void joinGameTest() {
		assertTrue(LobbyManager.getInstance().isMatchFull());
	}	

	@Test 
	public void colorPlayersTest() {
		ColorEnum[] colors = ColorEnum.values();
		boolean exist;
		List<Player> list = LobbyManager.getInstance().getPlayers();
		for(ColorEnum c : colors) {
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
		assertTrue(LobbyManager.getInstance().areThereAtLeastTwoPlayers());
		ColorEnum[] colors = ColorEnum.values();
		for (int i = 0; i < colors.length; ++i)
			LobbyManager.getInstance().exitGame(colors[i]);
		assertFalse(LobbyManager.getInstance().areThereAtLeastTwoPlayers());
	}
	
	@Test
	public void isEveryoneReadyTest() {
		assertFalse(LobbyManager.getInstance().isEveryoneReady());
		for (ColorEnum c : ColorEnum.values())
			LobbyManager.getInstance().setPlayerReady(c, true);
		assertTrue(LobbyManager.getInstance().isEveryoneReady());
	}
	
	@Test
	public void isMatchFullTest() {
		assertTrue(LobbyManager.getInstance().isMatchFull());
		LobbyManager.getInstance().exitGame(ColorEnum.BLUE);
		assertFalse(LobbyManager.getInstance().isMatchFull());
	}
	
	@Test
	public void exitGameTest() {
		assertEquals(6, LobbyManager.getInstance().getPlayers().size());
		ColorEnum[] colors = ColorEnum.values();
		for (int i = 0; i < colors.length; ++i)
			LobbyManager.getInstance().exitGame(colors[i]);
		assertEquals(0, LobbyManager.getInstance().getPlayers().size());
	}
	
	@Test
	public void isMatchStartedTest() {
		assertFalse(LobbyManager.getInstance().isMatchStarted());
	}
	
	@After
	public void destroySingletons() {
		LobbyManager.destroy();
		GameManager.destroy();
	}
}
