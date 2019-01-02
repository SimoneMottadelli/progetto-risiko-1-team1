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
	
	public void initTanks(List<Player> players) {
		int numberTerritories = Map.getInstance().getTerritories().size();
		//the number of tanks is proportional to the number of territories 
		//of the original difficulty (hard --> 42 territories)
		for (Player p: players) {
			switch (players.size()) {
			case 2:
				p.addAvailableTanks((int)(40 * numberTerritories  / 42));
				break;
			case 3:
				p.addAvailableTanks((int)(35 * numberTerritories  / 42));
				break;
			case 4:
				p.addAvailableTanks((int)(30 * numberTerritories  / 42));
				break;
			case 5:
				p.addAvailableTanks((int)(25 * numberTerritories  / 42));
				break;
			case 6:
				p.addAvailableTanks((int)(20 * numberTerritories  / 42));
				break;
			default:
				p.addAvailableTanks(0);
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

