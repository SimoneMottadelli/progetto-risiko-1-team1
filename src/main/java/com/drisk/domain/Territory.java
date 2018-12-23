package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class Territory {

	private String name;
	private Player player;
	private int numberOfTanks;
	private List<Territory> adjacentTerritories;
	
	
	public Territory() {
		this.adjacentTerritories = new LinkedList<>();
	}
	
	
	public Territory(String name, Player player, int numberOfTanks, List<Territory> adjacentTerritories) {
		this();
		setName(name);
		setPlayer(player);
		setNumberOfTanks(numberOfTanks);
		setAdjacentTerritories(adjacentTerritories);
	}
	
	
	public Territory(String name) {
		this(name, null, 0, null);
	}
	
	
	public Territory(String name, Player player) {
		this(name, player, 0, null);
	}
	
	
	public Territory(String name, Player player, int numberOfTanks) {
		this(name, player, numberOfTanks, null);
	}
	
	
	public Territory(String name, List<Territory> adjacentTerritories) {
		this(name, null, 0, adjacentTerritories);
	}
	
	
	public Player getPlayer() {
		return player;
	}


	public void setPlayer(Player player) {
		this.player = player;
	}


	public int getNumberOfTanks() {
		return numberOfTanks;
	}


	public void setNumberOfTanks(int numberOfTanks) {
		this.numberOfTanks = numberOfTanks;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public void setAdjacentTerritories(List<Territory> adjacentTerritories) {
		this.adjacentTerritories = adjacentTerritories;
	}


	public String getName() {
		return name;
	}
	
	
	public List<Territory> getAdjacentTerritories() {
		return adjacentTerritories;
	}
	
	
	public void addTerritory(Territory territory) {
		adjacentTerritories.add(territory);
	}
	
}
