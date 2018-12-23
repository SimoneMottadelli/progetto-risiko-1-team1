package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class Territory {

	private String name;
	private List<Territory> adjacentTerritories;
	
	
	public Territory() {
		this.adjacentTerritories = new LinkedList<>();
	}
	
	
	public Territory(String name) {
		this();
		setName(name);
	}
	

	public Territory(String name, List<Territory> adjacentTerritories) {
		this(name);
		setAdjacentTerritories(adjacentTerritories);
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
