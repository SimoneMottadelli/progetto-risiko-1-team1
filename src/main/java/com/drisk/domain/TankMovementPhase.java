package com.drisk.domain;

import com.google.gson.JsonObject;

public class TankMovementPhase extends Phase {

	public TankMovementPhase() {
		super(5);
	}

	@Override
	public void playPhase(JsonObject obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AssignBonusTanksPhase());
	}

	public void moveTanks(Territory oldTerritory, Territory newTerritory, int numTanks) {
		
		if (oldTerritory.getNeighbours().contains(newTerritory)) {
			int numOldTerritoryTanks = oldTerritory.getNumberOfTanks();
			if (numOldTerritoryTanks > 1 && numTanks < numOldTerritoryTanks) {
				oldTerritory.removeNumberOfTanks(numTanks);
				newTerritory.addNumberOfTanks(numTanks);
			} else {
				oldTerritory.removeNumberOfTanks(numOldTerritoryTanks - 1);
				newTerritory.addNumberOfTanks(numOldTerritoryTanks - 1);
			}
		}
		
	}
}
