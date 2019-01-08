package com.drisk.domain;

import java.util.List;
import com.google.gson.JsonObject;

public class AssignTanksPhase extends Phase {

	public AssignTanksPhase() {
		super(2);
	}

	@Override
	public void playPhase(Player currentPlayer, JsonObject obj) {
		assignTanks(currentPlayer);
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new TankPlacementPhase());
	}
	
	// each player has at least a tank at the beginning of his turn even if he owns less then three territories
	private void assignTanks(Player player) {
		int numberTerritoriesOwned = MapManager.getInstance().getMapTerritories(player).size();
		int tanks;
		if (numberTerritoriesOwned / 3 < 1)
			tanks = 1;
		else
			tanks = numberTerritoriesOwned / 3;
		tanks += getTanksPerContinent(player);
		TankManager.getInstance().addTanksToPlayer(tanks, player);
	}
	
	
	private int getTanksPerContinent (Player player) {
		int tanks = 0;
		List<Territory> territoriesOwned = MapManager.getInstance().getMapTerritories(player);
		List<Continent> continents = MapManager.getInstance().getMapContinents();
		for(Continent c: continents) 
			if (territoriesOwned.containsAll(c.getTerritories()))
				switch(c.getName()) {
					case "africa": 
						tanks += 3;
						break;
					case "asia":
						tanks += 7;
						break;
					case "australia":
					case "south america":
						tanks += 2;
						break;
					case "europe":
					case "north america":
						tanks += 5;
						break;
					default:
						tanks += 0;
				}	
		return tanks;
	}

	@Override
	public Object fromJson(JsonObject obj) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
