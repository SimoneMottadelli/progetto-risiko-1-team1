package com.drisk.domain;

import com.google.gson.JsonObject;

public class TankMovementPhase extends Phase {

	public TankMovementPhase() {
		super(4);
	}

	@Override
	public void playPhase(Player currentPlayer, JsonObject obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AssignTanksPhase());
	}

	public void moveTanks(Territory oldTerritory, Territory newTerritory, int numTanks) {
		
		if (oldTerritory.getNeighbours().contains(newTerritory)) {
			int numOldTerritoryTanks = oldTerritory.getNumberOfTanks();
			if (numOldTerritoryTanks > 1 && numTanks < numOldTerritoryTanks) {
				oldTerritory.removeTanks(numTanks);
				newTerritory.addTanks(numTanks);
			} else {
				oldTerritory.removeTanks(numOldTerritoryTanks - 1);
				newTerritory.addTanks(numOldTerritoryTanks - 1);
			}
		}
		
	}

	@Override
	public void fromJson(JsonObject obj) {
		// TODO Auto-generated method stub
	}
}
