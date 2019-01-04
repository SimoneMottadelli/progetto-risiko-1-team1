package com.drisk.domain;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TankManagerTest {

	@Before
	public void initialize() {
		String s = "{'difficulty' : 'custom', 'continents' : ['africa', 'europe', 'asia'], 'territories' : ['italy', 'france', 'egypt', 'north africa', 'kamchatka', 'china', 'japan', 'india', 'middle east'],"
				+ " 'membership' : [{'name' : 'europe', 'territories' : ['italy', 'france']}, {'name' : 'africa', 'territories' : ['egypt', 'north africa']}, {'name' : 'asia', 'territories' : ['china', 'kamchatka', 'japan', 'india', 'middle east']}],"
				+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, {'name' : 'north africa', 'territories' : ['egypt']}, {'name' : 'china', 'territories' : ['india', 'japan']}, {'name' : 'middle east', 'territories' : ['india']}]}";
		Gson json = new Gson();
		JsonObject obj = json.fromJson(s, JsonObject.class); 
		try {
			MatchManager.getInstance().setGameConfig(obj);
		} catch (SyntaxException | FileNotFoundException e) {}
		for(int i = 1; i <= 6; i++)
			MatchManager.getInstance().joinGame("Player" + i);
		MapManager.destroy();
		MatchManager.getInstance().startGame();
	}
	
	@Test
	public void initTanksTest() {
		
		List<Player> players = new LinkedList<>();
		players.add(new Player(null, "Zucca"));
		players.add(new Player(null, "Teo"));
		players.add(new Player(null, "Claudio"));
		players.add(new Player(null, "Simo"));
		
		TankManager.getInstance().initTanks(players);
		assertEquals(6, players.get(0).getAvailableTanks());
		
		for (Player p: players) {
			p.removeAvailableTanks();
		}
		
		players.add(new Player(null, "Ale"));
		TankManager.getInstance().initTanks(players);
		assertEquals(5, players.get(0).getAvailableTanks());
		
		for (Player p: players) {
			p.removeAvailableTanks();
		}
		
		players.add(new Player(null, "Burt"));
		TankManager.getInstance().initTanks(players);
		assertEquals(4, players.get(0).getAvailableTanks());
		
		for (Player p: players) {
			p.removeAvailableTanks();
		}
		
		players.add(new Player(null, "Andrea"));
		TankManager.getInstance().initTanks(players);
		assertEquals(0, players.get(0).getAvailableTanks());
	}
	
	@Test
	public void placeTanksTest() {
		List<Territory> territories = new LinkedList<>();
		territories.add(new Territory("great britain"));
		Territory t = territories.get(0);
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
		t.addNumberOfTanks(42);
		
		TankManager.getInstance().removeTanks(t, 13);
		assertEquals(29, t.getNumberOfTanks());
		
		//removing more tanks than those present
		TankManager.getInstance().removeTanks(t, 42);
		assertEquals(0, t.getNumberOfTanks());
	}
	
	@After
	public void destroyMap() {
		MapManager.destroy();
	}
}