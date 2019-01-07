package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonObject;

public class GameManager {
	
	private List<Player> players;
	private static GameManager instance;
	
	private GameManager() {
		players = new LinkedList<>();
	}
	
	public static GameManager getInstance() {
		if (instance == null)
			instance = new GameManager();
		return instance;
	}

	public void initGame(List<Player> players) {
		initPlayers(players);
		initCards();
		initPlayersMission();
		initPlayersTerritories();
		initTanks();
	}
	
	private void startGame() {
		TurnManager.getInstance().initTurn();
	}

	private void initPlayers(List<Player> players) {
		this.players = players;
	}
	
	public void initCards() {
		CardManager.getInstance().initCards();
	}
	
	public void initPlayersMission() {
		// questo true dovra essere sostituito dalle impostazioni di gioco che verranno ricevute dal matchManager
		CardManager.getInstance().initPlayersMission(players, true);
	}
	
	public void initPlayersTerritories() {
		MapManager.getInstance().initPlayersTerritories(players);
	}

	public void initTanks() {
		TankManager.getInstance().initTanks(getPlayers());
	}
	
	// TODO probabilmente da spostare
	public boolean checkWin(Player currentPlayer) {
		return currentPlayer.getMissionCard().checkWin();
	}
	
	//da implementare TODO
	public boolean checkLoss() {
		return false;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	// TODO we arrived here!
	// this method allow to check, in the initial phase, if all player have placed all own tanks
	private boolean areAllTanksPlaced() {
		for(Player p : players)
			if(p.getAvailableTanks() != 0)
				return false;
		return true;
	}
	
	public Player findPlayerByColor(Color color) {
		for(Player p : players)
			if(p.getColor().equals(color))
				return p;
		return null;
	}
	
	public void tryToStartGame() {
		if(areAllTanksPlaced())
			startGame();
	}
	
	private String getStringColorOfCurrentPlayer() {
		Player currentPlayerInTurn = TurnManager.getInstance().getCurrentPlayer();
		if(currentPlayerInTurn != null)
			return currentPlayerInTurn.getColor().toString();
		else
			return null;
	}
	
	private Integer getCurrentPhaseId() {
		Phase phase = TurnManager.getInstance().getCurrentPhase();
		if(phase != null)
			return phase.getPhaseId();
		else
			return null;
	}
	
	public JsonObject toJson() {
		return JsonHelper.gameManagerToJson(getStringColorOfCurrentPlayer(), getCurrentPhaseId(), players);
	}
	
}