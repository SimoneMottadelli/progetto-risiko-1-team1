package com.drisk.domain.turn;

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
	
	public abstract void nextPhase() throws RequestNotValidException;
	public abstract void playPhase(JsonObject obj) throws RequestNotValidException;
	public abstract void fromJson(JsonObject obj) throws RequestNotValidException;
	protected abstract void checkCondition() throws RequestNotValidException;

}
