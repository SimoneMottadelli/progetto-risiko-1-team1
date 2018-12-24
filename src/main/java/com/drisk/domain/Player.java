package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class Player {
	
	private String nickname;
	private Color color;
	private MissionCard missionCard;
	private List<TerritoryCard> territoryCards;
	
	
	public Player() {
		this.territoryCards = new LinkedList<>();
	}
	
	
	/*public Player(String nickname, Color color, MissionCard missionCard, List<TerritoryCard> territoryCards) {
		this();
		setNickname(nickname);
		setColor(color);
		setMissionCard(missionCard);
		setTerritoryCards(territoryCards);
	}*/
	
	public Player(String nickname, Color color) {
		this();
		setNickname(nickname);
		setColor(color);
	}
	
	
	/*public Player(String nickname, Color color, MissionCard missionCard) {
		this(nickname, color, missionCard, null);
	}
	
	
	public Player(String nickname, Color color, List<TerritoryCard> territoryCards) {
		this(nickname, color, null, territoryCards);
	}*/
	
	
	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public Color getColor() {
		return color;
	}
	

	public MissionCard getMissionCard() {
		return missionCard;
	}
	

	public List<TerritoryCard> getTerritoryCards() {
		return territoryCards;
	}


	public void setColor(Color color) {
		this.color = color;
	}


	public void setMissionCard(MissionCard missionCard) {
		this.missionCard = missionCard;
	}


	public void setTerritoryCards(List<TerritoryCard> territoryCards) {
		this.territoryCards = territoryCards;
	}	
	
}
