package com.drisk.domain.turn;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.game.Player;
import com.google.gson.JsonObject;

public abstract class Phase {
	
	private int phaseId;
	private Player currentPlayer;
	
	public Phase(int id) {
		phaseId = id;
	}
	
	public final int getPhaseId() {
		return phaseId;
	}
	
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public abstract void nextPhase();
	public abstract void playPhase(Player currentPlayer, JsonObject obj) throws RequestNotValidException;
	public abstract void fromJson(JsonObject obj) throws RequestNotValidException;
	protected abstract void checkCondition() throws RequestNotValidException;

}
