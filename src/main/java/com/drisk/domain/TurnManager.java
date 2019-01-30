package com.drisk.domain;

import java.util.List;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.google.gson.JsonObject;

public class TurnManager {
	
	private static TurnManager instance;
	private Player currentPlayer;
	private Phase currentPhase;
	private List<Player> players;
	
	private TurnManager() {}
	
	public void initTurn(List<Player> players) {
		this.players = players;
		currentPlayer = nextPlayer();
		currentPhase = new TanksAssignmentPhase(currentPlayer);
		
	}
	
	public static TurnManager getInstance() {
		if (instance == null)
			instance = new TurnManager();
		return instance;
	}
	
	public Player nextPlayer() {
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
	
	public void exit(Player player) {
		if(currentPlayer != null && currentPlayer.equals(player))
			currentPhase = new TanksAssignmentPhase(nextPlayer());
		if(players != null)
			players.remove(player);
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
		if (currentPhase != null) {
			phaseId = currentPhase.getPhaseId();
			result.add("history", currentPhase.getHistoryToJson());			
		}
		result.addProperty("currentPhaseId", phaseId);
		return result;
	}
	
	public static void destroy() {
		Phase.destroyHistory();
		instance = null;
	}

}

