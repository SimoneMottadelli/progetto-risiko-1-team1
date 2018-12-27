package com.drisk.domain;

public class TerritoryCard extends Card {
	
	private Territory territory;
	private TerritoryCardType simbol;
	
	
	public TerritoryCard() {
		
	}
	
	public TerritoryCard(Territory territory, TerritoryCardType simbol) {
		setTerritory(territory);
		setSimbol(simbol);
	}


	public Territory getTerritory() {
		return territory;
	}


	public TerritoryCardType getSimbol() {
		return simbol;
	}


	public void setTerritory(Territory territory) {
		this.territory = territory;
	}


	public void setSimbol(TerritoryCardType simbol) {
		this.simbol = simbol;
	}
	
}
