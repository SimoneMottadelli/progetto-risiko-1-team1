package com.drisk.domain.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.GameManager;
import com.drisk.domain.LobbyManager;
import com.drisk.domain.MapManager;
import com.drisk.domain.Player;
import com.drisk.domain.TankManager;
import com.drisk.domain.Territory;
import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TankManagerTest {

	@Before
	public void initialize() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class);
			MapManager.getInstance().createMap(obj);
		} catch (FileNotFoundException | SyntaxException e) {}
		
		for (int i = 1; i <= 6; i++)
			LobbyManager.getInstance().joinGame("Player" + i);
		LobbyManager.getInstance().initGame();
	}

	@Test
	public void areAllTanksPlacedTest() {
		assertFalse(TankManager.getInstance().areAllTanksPlaced(GameManager.getInstance().getPlayers()));
		for (Player p : GameManager.getInstance().getPlayers()) 
			while (p.getAvailableTanks() > 0) {
				System.out.println(p.getAvailableTanks());
				Territory t = MapManager.getInstance().getPlayerTerritories(p).get(0);
					TankManager.getInstance().placeTanks(t, 1);
			}
		assertTrue(TankManager.getInstance().areAllTanksPlaced(GameManager.getInstance().getPlayers()));
	}

	@Test
	public void addTanksToPlayerTest() {
		Player p = new Player(null, "Simone");
		TankManager.getInstance().addTanksToPlayer(2, p);
		assertEquals(2, p.getAvailableTanks());
	}

	@Test
	public void tryToPlaceTanksWithoutExceptionTest() {
		Territory t = new Territory("England");
		Player p = new Player(null, "Simone");
		t.setOwner(p);
		TankManager.getInstance().addTanksToPlayer(3, p);
		try {
			TankManager.getInstance().tryToPlaceTanks(p, t, 2);
		} catch (RequestNotValidException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(1, p.getAvailableTanks());
		assertEquals(2, t.getNumberOfTanks());
	}

	@Test
	public void tryToPlaceTanksWithExceptionTest() {
		Territory t = new Territory("England");
		Player p = new Player(null, "Simone");
		t.setOwner(p);
		TankManager.getInstance().addTanksToPlayer(3, p);
		try {
			TankManager.getInstance().tryToPlaceTanks(p, t, 4);
			fail();
		} catch (RequestNotValidException e) {}
	}

	@Test
	public void initTanksTest() {

		List<Player> players = new LinkedList<>();
		players.add(new Player(null, "Zucca"));
		players.add(new Player(null, "Teo"));
		players.add(new Player(null, "Claudio"));
		players.add(new Player(null, "Simo"));

		for (Territory t : MapManager.getInstance().getMap().getTerritories())
			if (t.getNumberOfTanks() != 1)
				fail();

		TankManager.getInstance().initTanks(players);
		assertEquals(17, players.get(0).getAvailableTanks());

		for (Player p : players)
			p.removeAvailableTanks(17);

		players.add(new Player(null, "Ale"));
		TankManager.getInstance().initTanks(players);
		assertEquals(14, players.get(1).getAvailableTanks());

		for (Player p : players)
			p.removeAvailableTanks(14);

		players.add(new Player(null, "Burt"));
		TankManager.getInstance().initTanks(players);
		assertEquals(11, players.get(0).getAvailableTanks());

		for (Player p : players)
			p.removeAvailableTanks(11);

		players.add(new Player(null, "Andrea"));
		TankManager.getInstance().initTanks(players);
		assertEquals(0, players.get(0).getAvailableTanks());
	}

	@Test
	public void placeTanksTest() {
		List<Territory> territories = new LinkedList<>();
		territories.add(new Territory("great britain"));
		Territory t = territories.get(0);
		t.setOwner(new Player(null, "Simone"));
		assertEquals(0, t.getNumberOfTanks());

		TankManager.getInstance().placeTanks(t, 5);
		assertEquals(5, t.getNumberOfTanks());

		TankManager.getInstance().placeTanks(t, 37);
		assertEquals(42, t.getNumberOfTanks());
	}

	@Test
	public void removeTanksTest() {
		List<Territory> territories = new LinkedList<>();
		territories.add(new Territory("egypt"));
		Territory t = territories.get(0);
		t.addTanks(42);

		TankManager.getInstance().removeTanks(t, 13);
		assertEquals(29, t.getNumberOfTanks());

		// removing more tanks than those present
		TankManager.getInstance().removeTanks(t, 42);
		assertEquals(0, t.getNumberOfTanks());
	}

	@After
	public void destroySingletons() {
		GameManager.destroy();
		LobbyManager.destroy();
		TankManager.destroy();
	}
}