package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class Player {
	
	private String nickname;
	private Color color;
	private MissionCard missionCard;
	private List<TerritoryCard> territoryCards; 
	private List<Territory> territoriesOwned;
	private int availableTanks;
	
	public Player(String nickname, Color color) {
		this.nickname = nickname;
		this.color = color;
		this.territoryCards = new LinkedList<>();
		territoriesOwned = new LinkedList<>();
	}
	
	public List<Territory> getTerritoriesOwned() {
		return territoriesOwned;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return color == other.color;
	}

	public String getNickname() {
		return nickname;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getNumberOfTerritoriesOwned() {
		return territoriesOwned.size();
	}
	
	
	public MissionCard getMissionCard() {
		return missionCard;
	}	
	
	public List<TerritoryCard> getTerritoryCards() {
		return territoryCards;
	}
	
	public void setMissionCard(MissionCard missionCard) {
		this.missionCard = missionCard;
	}
	
	public void addTerritoryCards(TerritoryCard territoryCard) {
		if(!territoryCards.contains(territoryCard))
			territoryCards.add(territoryCard);
	}	
	
	public void addTerritoryOwned(Territory territory) {
		if(!territoriesOwned.contains(territory))
			territoriesOwned.add(territory);
	}

	public int getAvailableTanks() {
		return availableTanks;
	}

	public void setAvailableTanks(int availableTanks) {
		this.availableTanks = availableTanks;
	}
}
