package com.drisk.domain;

import com.google.gson.JsonObject;

public abstract class Phase {
	
	int phaseId;
	
	public Phase(int id) {
		phaseId = id;
	}
	
	public final int getPhaseId() {
		return phaseId;
	}
	
	public abstract void nextPhase();
	public abstract void playPhase(JsonObject obj);

}
