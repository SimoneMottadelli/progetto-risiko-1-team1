package com.drisk.domain;

public class TankMovementPhase implements Phase {

	@Override
	public void startPhase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AssignBonusTanksPhase());
	}

}
