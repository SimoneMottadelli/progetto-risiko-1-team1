package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class Player {
	
	private Color color;
	private MissionCard missionCard;
	private List<TerritoryCard> territoryCards;
	
	
	public Player() {
		this.territoryCards = new LinkedList<>();
	}
	
	
	public Player(Color color) {
		this();
		setColor(color);
	}
	
	
	public Player(Color color, MissionCard missionCard) {
		this(color);
		setMissionCard(missionCard);
	}
	
	
	public Player(Color color, MissionCard missionCard, List<TerritoryCard> territoryCards) {
		this(color, missionCard);
		setTerritoryCards(territoryCards);
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
