package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class GameManager {
	
	private List<Player> players;			//Lista giocatori in partita
	private Map map;						//Mappa di gioco
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
		map = Map.getInstance();
		map.createMap("easy");
	}
	
	//DA IMPLEMENTARE CON SINGOLA STRINGA JSON
	public void initPlayers(List<String> players) { 
		
		Color[] colors = Color.values();
		
		for (int i = 0; i < players.size(); ++i) {
			Player player = new Player(players.get(i), colors[i]);
			this.players.add(player);
		}
	}
	
	
	public void initPlayersTerritories() {
		List<Territory> territories = map.getTerritories();
		for (int i = 0, j = 0; i < territories.size(); ++i, ++j) {
			territories.get(i).setPlayer(players.get(j % players.size()));
		}
		
	}
	
	
	public boolean checkWin(Player currentPlayer) {
		List<Territory> territories = map.getTerritories();
		int totalNumberOfTerritories = territories.size();
		
		int currentPlayerNumberOfTerritories = 0;
		for (Territory t : territories) 
			if (t.getPlayer().equals(currentPlayer))
				++currentPlayerNumberOfTerritories;
		
		int playerTerritoriesRate = currentPlayerNumberOfTerritories / totalNumberOfTerritories;
		if (playerTerritoriesRate >= (2 / 3))
			return true;
		else
			return false;	
	}


	public List<Player> getPlayers() {
		return players;
	}
	
}