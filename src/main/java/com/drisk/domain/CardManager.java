package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class CardManager {

	private List<MissionCard> missionCards;
	private List<TerritoryCard> territoryCards;
	private CardManager instance;
	
	
	private CardManager() {
		missionCards = new LinkedList<>();
		territoryCards = new LinkedList<>();
	}
	
	public CardManager getInstance() {
		if (instance == null)
			instance = new CardManager();
		return instance;
	}
	
	public List<MissionCard> getMissionCards() {
		return missionCards;
	}

	public List<TerritoryCard> getTerritoryCards() {
		return territoryCards;
	}

	public void setTerritoryCards(List<TerritoryCard> territoryCards) {
		this.territoryCards = territoryCards;
	}

	public void setMissionCards(List<MissionCard> missionCards) {
		this.missionCards = missionCards;
	}
	
	
}
