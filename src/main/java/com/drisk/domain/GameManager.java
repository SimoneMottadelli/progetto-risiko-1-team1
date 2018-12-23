package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;


public class GameManager {
	
	private List<Player> players;
	private Map map;
	private GameManager instance;
	
	
	private GameManager() {
		players = new LinkedList<>();
	}
	
	
	public GameManager getInstance() {
		if (instance == null)
			instance = new GameManager();
		return instance;
	}
	
	
	public void initGame(String playersJson, String mapJson) {
		initPlayers(playersJson);
		initMap(mapJson);
	}
	
	
	private void initPlayers(String playersJson) {
		//Crea i giocatori a partire da playersJson
	}
	
	
	private void initMap(String mapJson) {
		//Crea la mappa a partire da mapJson
	}
	
	
	public List<Player> getPlayers() {
		return players;
	}


	public Map getMap() {
		return map;
	}
	
	
	public void addPlayer(Player player) {
		players.add(player);
	}
}