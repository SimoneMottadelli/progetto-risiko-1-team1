package com.drisk.domain;

import java.util.List;

public class TankManager {
	
	private static TankManager instance;
	
	private TankManager() {
		
	}
	
	public static TankManager getInstance() {
		if (instance == null)
			instance = new TankManager();
		return instance;
	}
	
	public void initTanks(String difficulty, List<Player> players) {
		//il numero di truppe varierà anche in base alla difficoltà scelta (?)
		for (Player p: players) {
			switch (players.size()) {
			case 2:
				p.addAvailableTanks(40);
				break;
			case 3:
				p.addAvailableTanks(35);
				break;
			case 4:
				p.addAvailableTanks(30);
				break;
			case 5:
				p.addAvailableTanks(25);
				break;
			default:
				p.addAvailableTanks(20);
			}
		}
	}
	
	public void placeTanks(Territory whereTerritory, int numTanks) {
		whereTerritory.addNumberOfTanks(numTanks);
	}
	
	public void removeTanks(Territory whereTerritory, int numTanks) {
		whereTerritory.removeNumberOfTanks(numTanks);
	}
	
}

