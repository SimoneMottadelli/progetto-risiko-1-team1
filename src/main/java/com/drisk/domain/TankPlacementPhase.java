package com.drisk.domain;

import com.google.gson.JsonObject;

public class TankPlacementPhase extends Phase {

	public TankPlacementPhase() {
		super(3);
	}

	@Override
	public void playPhase(Player currentPlayer, JsonObject obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AttackPhase());
	}

	@Override
	public Object fromJson(JsonObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
