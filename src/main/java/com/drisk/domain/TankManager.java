package com.drisk.domain;

import java.util.List;

import com.drisk.domain.exceptions.ExceededAvailableTanksException;

public class TankManager {
	
	private static TankManager instance;
	
	private TankManager() {
		
	}
	
	public static TankManager getInstance() {
		if (instance == null)
			instance = new TankManager();
		return instance;
	}
	
	public static void destroy() {
		instance = null;
	}
	
	public void initTanks(List<Player> players) {
		int numberTerritories = MapManager.getInstance().getNumberOfTerritories();
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
			for(Territory t : MapManager.getInstance().getMapTerritories())
				if(t.getOwner().equals(p))
					placeTanks(t, 1);
		}
		
	}
	
	// this method allow to check, in the initial phase, if all player have placed all own tanks
	public boolean areAllTanksPlaced(List<Player> players) {
		for(Player p : players)
			if(p.getAvailableTanks() != 0)
				return false;
		return true;
	}

	public void addTanksToPlayer(int tanks, Player p) {
		p.addAvailableTanks(tanks);
	}
	
	public void tryToPlaceTanks(Territory whereTerritory, int numTanks) throws ExceededAvailableTanksException {
		if(whereTerritory.getOwner().getAvailableTanks() < numTanks)
			throw new ExceededAvailableTanksException("Not enough available tanks");
		placeTanks(whereTerritory, numTanks);
	}
	
	public void placeTanks(Territory whereTerritory, int numTanks) {
		whereTerritory.addTanks(numTanks);
		whereTerritory.getOwner().removeAvailableTanks(numTanks);
	}
	
	public void removeTanks(Territory whereTerritory, int numTanks) {
		whereTerritory.removeTanks(numTanks);
	}
}

