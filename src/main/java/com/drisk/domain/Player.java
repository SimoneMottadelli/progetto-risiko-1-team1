package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Player {
	
	private ColorEnum color;
	private String nickname;
	private boolean ready;
	private MissionCard missionCard;
	private List<TerritoryCard> territoryCardsHand; 
	private int availableTanks;
	
	public Player(ColorEnum color, String nickname) {
		this.nickname = nickname;
		this.ready = false;
		this.color = color;
		this.territoryCardsHand = new LinkedList<>();
	}
	
	public String getNickname() {
		return nickname;
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

	public ColorEnum getColor() {
		return color;
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

	public int getAvailableTanks() {
		return availableTanks;
	}

	public void addAvailableTanks(int tanks) {
		availableTanks += tanks;
	}
	
	public void removeAvailableTanks(int tanks) {
		availableTanks -= tanks;
	}
	
	public JsonObject toJson() {
		JsonObject jsonPlayer = new JsonObject();
		jsonPlayer.addProperty("nickname", nickname);
		jsonPlayer.addProperty("availableTanks", availableTanks);
		jsonPlayer.addProperty("color", color.toString().toUpperCase());
		if (missionCard != null)
			jsonPlayer.add("missionCard", missionCard.toJson());
		JsonArray cards = new JsonArray();
		for(TerritoryCard t : territoryCardsHand)
			cards.add(t.toJson());
		jsonPlayer.add("cards", cards);
		jsonPlayer.addProperty("ready", ready);
		return jsonPlayer;
	}
}
