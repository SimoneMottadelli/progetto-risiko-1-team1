package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class GameManager {
	
	
	private List<Player> players;
	private Map map;
	
	
	public GameManager() {
		players = new LinkedList<>();
	}
	
	public GameManager(List<Player> players, Map map) {
		this();
		setPlayers(players);
		setMap(map);
	}
	
	
	public GameManager(List<Player> players) {
		this(players, null);
	}
	
	
	public GameManager(Map map) {
		this(null, map);
	}
	
	
	public List<Player> getPlayers() {
		return players;
	}


	public void setPlayers(List<Player> players) {
		this.players = players;
	}


	public Map getMap() {
		return map;
	}

	
	public void setMap(Map map) {
		this.map = map;
	}
	
	
	public void addPlayer(Player player) {
		players.add(player);
	}
}