package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class Player {
	
	private String nickname;
	private Color color;
	private MissionCard missionCard;
	private List<TerritoryCard> territoryCards; //il nome confonde! queste carte non sono i territori che possiede il giocatore, ma solo le carte che ha pescato per fare le combinazioni
	private int territoriesOwned;
	
	
	public Player() {
		this.territoryCards = new LinkedList<>();
	}
	
	public Player(String nickname, Color color) {
		this();
		setNickname(nickname);
		setColor(color);
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
		if (color != other.color)
			return false;
		return true;
	}

	public String getNickname() {
		return nickname;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getTerritoriesOwned() {
		return territoriesOwned;
	}
	
	public MissionCard getMissionCard() {
		return missionCard;
	}	
	
	public List<TerritoryCard> getTerritoryCards() {
		return territoryCards;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setTerritoriesOwned(int territoriesOwned) {
		this.territoriesOwned = territoriesOwned;
	}
	
	public void setMissionCard(MissionCard missionCard) {
		this.missionCard = missionCard;
	}
	
	public void setTerritoryCards(List<TerritoryCard> territoryCards) {
		this.territoryCards = territoryCards;
	}	

}
