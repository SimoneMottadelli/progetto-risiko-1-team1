package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

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
	
	/* TODO probabilmente da spostare
	public boolean checkWin(Player currentPlayer) {
		return currentPlayer.getMissionCard().checkWin();
	}
	*/
	//da implementare TODO
	public boolean checkLoss() {
		return false;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public Player findPlayerByColor(Color color) {
		for(Player p : players)
			if(p.getColor().equals(color))
				return p;
		return null;
	}
	
	public void tryToStartGame() {
		if(TankManager.getInstance().areAllTanksPlaced(players))
			startGame();
	}	
}