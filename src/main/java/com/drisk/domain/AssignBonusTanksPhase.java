package com.drisk.domain;

public class AssignBonusTanksPhase implements Phase{

	@Override
	public void startPhase() {
		//da implementare TODO		
	}

	@Override
	public void nextPhase() {
		Turn.getInstance().setCurrentPhase(new AssignTanksPhase());
	}

}
