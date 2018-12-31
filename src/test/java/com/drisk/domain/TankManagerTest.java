package com.drisk.domain;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TankManagerTest {

	@Test
	public void initTanksTest() {
		
		List<Player> players = new LinkedList<>();
		players.add(new Player(null, "Zucca"));
		players.add(new Player(null, "Teo"));
		players.add(new Player(null, "Claudio"));
		players.add(new Player(null, "Simo"));
		
		TankManager.getInstance().initTanks("easy", players);
		assertEquals(30, players.get(0).getAvailableTanks());
		
		for (Player p: players) {
			p.removeAvailableTanks();
		}
		
		players.add(new Player(null, "Ale"));
		TankManager.getInstance().initTanks("easy", players);
		assertEquals(25, players.get(0).getAvailableTanks());
		
		for (Player p: players) {
			p.removeAvailableTanks();
		}
		
		players.add(new Player(null, "Burt"));
		TankManager.getInstance().initTanks("easy", players);
		assertEquals(20, players.get(0).getAvailableTanks());
		
		for (Player p: players) {
			p.removeAvailableTanks();
		}
		
		players.add(new Player(null, "Andrea"));
		TankManager.getInstance().initTanks("easy", players);
		assertEquals(0, players.get(0).getAvailableTanks());
	}
}
