package com.drisk.domain;

public class TankPlacementPhase extends Phase {

	public TankPlacementPhase() {
		super(3);
	}

	@Override
	public void startPhase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AttackPhase());
	}

}
