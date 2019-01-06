package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonObject;

public class Player {
	
	private Color color;
	private String nickname;
	private boolean ready;
	private MissionCard missionCard;
	private List<TerritoryCard> territoryCardsHand; 
	private List<Territory> territoriesOwned;
	private int availableTanks;
	
	public Player(Color color, String nickname) {
		this.nickname = nickname;
		this.ready = false;
		this.color = color;
		this.territoryCardsHand = new LinkedList<>();
		territoriesOwned = new LinkedList<>();
	}
	
	public int placeTanks(int tanks) {
		if(tanks > availableTanks)
			return availableTanks;
		else {
			availableTanks = availableTanks - tanks;
			return tanks;
		}
	}
	
	public List<Territory> getTerritoriesOwned() {
		return territoriesOwned;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public boolean isMyTerritory(Territory t) {
		return territoriesOwned.contains(t);
	}
	
	public boolean isMyContinent(Continent c) {
		for(Territory t : c.getTerritories())
			if(!territoriesOwned.contains(t))
				return false;
		return true;
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
	
	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
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
	
	public List<TerritoryCard> getTerritoryCardsHand() {
		return territoryCardsHand;
	}
	
	public void setMission(MissionCard missionCard) {
		this.missionCard = missionCard;
	}
	
	public void addTerritoryCards(TerritoryCard territoryCard) {
		if(!territoryCardsHand.contains(territoryCard))
			territoryCardsHand.add(territoryCard);
	}	
	
	public void addTerritoryOwned(Territory territory) {
		if(!territoriesOwned.contains(territory))
			territoriesOwned.add(territory);
	}
	
	public void removeTerritoryOwned(Territory territory) {
		if(territoriesOwned.contains(territory))
			territoriesOwned.remove(territory);
	}

	public int getAvailableTanks() {
		return availableTanks;
	}

	public void addAvailableTanks(int availableTanks) {
		this.availableTanks += availableTanks;
	}
	
	public void removeAvailableTanks() {
		this.availableTanks = 0;
	}
	
	public JsonObject toJson() {
		return JsonHelper.playerToJson(nickname, color.toString(), availableTanks, ready);
	}
}
