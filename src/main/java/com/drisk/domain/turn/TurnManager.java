package com.drisk.domain.turn;

import java.util.List;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.game.ColorEnum;
import com.drisk.domain.game.Player;
import com.google.gson.JsonObject;

public class TurnManager {
	
	private static TurnManager instance;
	private Player currentPlayer;
	private Phase currentPhase;
	private List<Player> players;
	
	private TurnManager() {}
	
	public void initTurn(List<Player> players) {
		this.players = players;
		currentPlayer = newTurn();
		currentPhase = new TankAssignmentPhase(currentPlayer);
	}
	
	public static TurnManager getInstance() {
		if (instance == null)
			instance = new TurnManager();
		return instance;
	}
	
	public Player newTurn() {
		currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
		return currentPlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void playPhase(JsonObject obj) throws RequestNotValidException {
		currentPhase.playPhase(obj);
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public boolean isPlayerTurn(ColorEnum color) {
		return currentPlayer.getColor().equals(color);
	}

	public Phase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(Phase currentPhase) {
		this.currentPhase = currentPhase;
	}
	
	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		String colorPlayer = null;
		if (currentPlayer != null) 
			colorPlayer = currentPlayer.getColor().toString();
		result.addProperty("currentPlayerColor", colorPlayer);
		Integer phaseId = null;
		if (currentPhase != null)
			phaseId = currentPhase.getPhaseId();
		result.addProperty("currentPhaseId", phaseId);
		return result;
	}

}
