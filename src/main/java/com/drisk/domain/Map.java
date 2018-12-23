package com.drisk.domain;

public class Map {

	private Continent[] continents;
	
	
	public Map() {
		
	}

	
	public Map(int numberOfContinents) {
		continents = new Continent[numberOfContinents];
	}
	
	
	public Map(Continent[] continents) {
		this.continents = continents;
	}

	
	public Continent[] getContinents() {
		return continents;
	}

	
	public void setContinents(Continent[] continents) {
		this.continents = continents;
	}
	
}
