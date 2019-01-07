package com.drisk.domain;

import java.util.List;

import com.google.gson.JsonObject;

public class TurnManager {
	
	private static TurnManager instance;
	private Player currentPlayer;
	private Phase currentPhase;
	
	private TurnManager() {}
	
	public void initTurn() {
		currentPhase = new AssignTanksPhase();
	}
	
	public static TurnManager getInstance() {
		if (instance == null)
			instance = new TurnManager();
		return instance;
	}
	
	public void newTurn(List<Player> players) {
		currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void playPhase(JsonObject obj) {
		currentPhase.playPhase(obj);
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public boolean isPlayerTurn(Color color) {
		return currentPlayer.getColor().equals(color);
	}

	public Phase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(Phase currentPhase) {
		this.currentPhase = currentPhase;
	}

}
