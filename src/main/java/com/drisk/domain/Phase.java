package com.drisk.domain;

import com.drisk.domain.exceptions.RequestNotValidException;
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
	public abstract void playPhase(Player currentPlayer, JsonObject obj) throws RequestNotValidException;
	public abstract void fromJson(JsonObject obj) throws RequestNotValidException;

}
