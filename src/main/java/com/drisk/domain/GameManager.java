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

	public void startGame(List<String> playersJson, String mapJson) {
		initPlayers(playersJson);
		initMap();
		initPlayersTerritories();
	}
	
	public void initMap() {
		Map.getInstance().createMap("easy");
	}
	
	public void initPlayers(List<String> playersNickname) { 
		Color[] colors = Color.values();
		for (int i = 0; i < playersNickname.size(); ++i) {
			Player player = new Player(playersNickname.get(i), colors[i]);
			players.add(player);
		}
	}
	
	
	public void initPlayersTerritories() {
		List<Territory> territories = Map.getInstance().getTerritories();
		for (int i = 0; i < territories.size(); ++i) {
			players.get(i % players.size()).addTerritoryOwned(territories.get(i));
		}
	}
	
	
	public boolean checkWin(Player currentPlayer) {
		List<Territory> territories = Map.getInstance().getTerritories();
		int totalNumberOfTerritories = territories.size();
		
		int currentPlayerNumberOfTerritories = currentPlayer.getNumberOfTerritoriesOwned();
		double playerTerritoriesRate = (double) currentPlayerNumberOfTerritories / totalNumberOfTerritories;
		return playerTerritoriesRate >= (double) 2 / 3;	
	}


	public List<Player> getPlayers() {
		return players;
	}
	
}