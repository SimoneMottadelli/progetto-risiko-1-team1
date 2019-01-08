package com.drisk.domain;

import com.google.gson.JsonObject;

public abstract class Phase {
	
	private int phaseId;
	
	public Phase(int id) {
		phaseId = id;
	}
	
	public final int getPhaseId() {
		return phaseId;
	}
	
	public abstract void nextPhase();
	public abstract void playPhase(Player currentPlayer, JsonObject obj);
	public abstract Object fromJson(JsonObject obj);

}
