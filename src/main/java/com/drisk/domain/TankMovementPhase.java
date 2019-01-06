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

}
