package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class GameManager {
	
	private List<Player> players;
	private static GameManager instance;	
	private boolean matchStarted;
	
	private GameManager() {
		players = new LinkedList<>();
		matchStarted = false;
	}
	
	public static GameManager getInstance() {
		if (instance == null)
			instance = new GameManager();
		return instance;
	}
	
	
	public boolean isMatchStarted() {
		return matchStarted;
	}
	
	
	//"template" perchè posso inizializzare il gioco sia attraverso il database
	//con una mappa predefinita, sia inizializzando una mappa nuova passata come
	//json dal client. Quindi in realtà ci saranno due implementaizoni diverse.
	public void initGameTemplate(List<String> playersNames) {
		initPlayers(playersNames);
		initMap();
		initCards();
		initPlayersMission();
		initPlayersTerritories();
		initTanks();
	}
	
	public void initPlayers(List<String> playersNickname) { 
		Color[] colors = Color.values();
		for (int i = 0; i < playersNickname.size(); ++i) {
			Player player = new Player(playersNickname.get(i), colors[i]);
			players.add(player);
		}
	}
	
	public void initMap() {
		Map.getInstance().createMap("easy");
	}
	
	public void initCards() {
		//da implementare TODO
	}
	
	public void initPlayersMission() {
		//da implementare TODO
	}
	
	public void initPlayersTerritories() {
		List<Territory> territories = Map.getInstance().getTerritories();
		for (int i = 0; i < territories.size(); ++i) {
			players.get(i % players.size()).addTerritoryOwned(territories.get(i));
		}
	}
	
	public void initTanks() {
		//da implementare TODO
	}
	
	public void startGame() {
		//da implementare TODO
	}
	
	public void joinGame() {
		
	}
	
	public boolean checkWin(Player currentPlayer) {
		List<Territory> territories = Map.getInstance().getTerritories();
		int totalNumberOfTerritories = territories.size();
		
		int currentPlayerNumberOfTerritories = currentPlayer.getNumberOfTerritoriesOwned();
		double playerTerritoriesRate = (double) currentPlayerNumberOfTerritories / totalNumberOfTerritories;
		return playerTerritoriesRate >= (double) 2 / 3;	
	}
	
	public boolean checkLoss() {
		//da implementare TODO
		return false;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
}