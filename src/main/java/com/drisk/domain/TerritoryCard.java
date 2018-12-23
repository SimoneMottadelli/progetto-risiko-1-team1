package com.drisk.domain;

public class TerritoryCard extends Card {
	
	private Territory territory;
	private Type simbol;
	
	
	public TerritoryCard() {
		
	}
	
	public TerritoryCard(Territory territory, Type simbol) {
		setTerritory(territory);
		setSimbol(simbol);
	}


	public Territory getTerritory() {
		return territory;
	}


	public Type getSimbol() {
		return simbol;
	}


	public void setTerritory(Territory territory) {
		this.territory = territory;
	}


	public void setSimbol(Type simbol) {
		this.simbol = simbol;
	}
	
}
