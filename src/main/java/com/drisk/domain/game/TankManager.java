package com.drisk.domain.game;

import java.util.List;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.map.Territory;

public class TankManager {
	
	private static TankManager instance;
	
	private TankManager() {}
	
	public static TankManager getInstance() {
		if (instance == null)
			instance = new TankManager();
		return instance;
	}
	
	public void initTanks(List<Player> players) {
		int numberTerritories = MapManager.getInstance().getNumberOfTerritories();
		//the number of tanks is proportional to the number of territories 
		//of the original difficulty (hard --> 42 territories)
		int numberOfTanks;
		switch (players.size()) {
		case 2:
			numberOfTanks = ((int)(40 * numberTerritories  / 42));
			break;
		case 3:
			numberOfTanks = ((int)(35 * numberTerritories  / 42));
			break;
		case 4:
			numberOfTanks = ((int)(30 * numberTerritories  / 42));
			break;
		case 5:
			numberOfTanks = ((int)(25 * numberTerritories  / 42));
			break;
		case 6:
			numberOfTanks = ((int)(20 * numberTerritories  / 42));
			break;
		default:
			numberOfTanks = 0;
		}
		for (Player p: players) {
			p.addAvailableTanks(numberOfTanks);
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
	
	public void tryToPlaceTanks(Player player, Territory whereTerritory, int numTanks) throws RequestNotValidException {
		if(whereTerritory.getOwner().getAvailableTanks() < numTanks)
			throw new RequestNotValidException("Not enough available tanks");
		if(!whereTerritory.getOwner().equals(player))
			throw new RequestNotValidException(whereTerritory.getName() + " is not yours");
		placeTanks(whereTerritory, numTanks);
	}
	
	public void placeTanks(Territory whereTerritory, int numTanks) {
		whereTerritory.addTanks(numTanks);
		whereTerritory.getOwner().removeAvailableTanks(numTanks);
	}
	
	public void moveTanks(Territory from, Territory to, int numOfTanks) {
		to.addTanks(numOfTanks);
		removeTanks(from, numOfTanks);
	}
	
	public void removeTanks(Territory whereTerritory, int numTanks) {
		whereTerritory.removeTanks(numTanks);
	}

	public static void destroy() {
		instance = null;
	}

}

