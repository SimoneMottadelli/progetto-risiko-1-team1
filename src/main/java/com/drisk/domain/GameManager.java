package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;
import com.google.gson.Gson;

public class GameManager {
	
	private List<Player> players;
	private Map map;
	private static GameManager instance;
	
	
	private GameManager() {
		players = new LinkedList<>();
	}
	
	
	public static GameManager getInstance() {
		if (instance == null)
			instance = new GameManager();
		return instance;
	}
	
	
	public void initGame(List<String> playersJson, String mapJson) {
		initPlayers(playersJson);
		initMap(mapJson);
	}
	
	
	public void initPlayers(List<String> players) { //Da occuparsi col json
		Color[] colors = Color.values();
		for (int i = 0; i < players.size(); ++i) {
			Player player = new Player(players.get(i), colors[i]);
			this.players.add(player);
		}
	}
	
	
	public void initMap(String mapJson) {
		//Crea la mappa a partire da mapJson
	}
	
	public void initTerritories() {
		
		List<Territory> cloneTerritories = map.getTerritories();
		for (int i = 0, j = 0; i < cloneTerritories.size(); ++i, ++j) {
			cloneTerritories.get(i).setPlayer(players.get(j % players.size()));
		}
		
	}
	
	
	public List<Player> getPlayers() {
		return players;
	}


	public Map getMap() {
		return map;
	}
	
}