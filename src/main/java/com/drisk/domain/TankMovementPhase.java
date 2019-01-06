package com.drisk.domain;

public class TankMovementPhase extends Phase {

	public TankMovementPhase() {
		super(5);
	}

	@Override
	public void playPhase() {
		// TODO Auto-generated method stub
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AssignBonusTanksPhase());
	}

}
