package com.drisk.domain;

public class Turn {
	
	private static Turn instance;
	private Player currentPlayer;
	private Phase currentPhase;
	
	private Turn() {
		currentPhase = new AssignBonusTanksPhase();
	}
	
	public static Turn getInstance() {
		if (instance == null)
			instance = new Turn();
		return instance;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Phase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(Phase currentPhase) {
		this.currentPhase = currentPhase;
	}	
}
