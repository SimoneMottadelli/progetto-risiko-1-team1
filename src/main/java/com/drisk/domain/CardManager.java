package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class CardManager {

	private List<MissionCard> missionCards;
	private List<TerritoryCard> territoryCards;
	private List<TerritoryCard> discardedCards;
	private static CardManager instance;
	
	
	private CardManager() {
		missionCards = new LinkedList<>();
		territoryCards = new LinkedList<>();
		discardedCards = new LinkedList<>();
	}
	
	public static CardManager getInstance() {
		if (instance == null)
			instance = new CardManager();
		return instance;
	}
	
	public void initMissionCards() {
		//da implementare TODO
	}
	
	public void initTerritoryCards() {
		//da implementare TODO
	}
	
	public void shuffleDeck(List<Card> cards) {
		//da implementare TODO
	}
	
	public Card drawCard(List<Card> cards) {
		return null; //da implementare TODO
	}
	
	public void refillDeck() {
		//da implementare TODO 
	}
	
	public boolean checkTris(Player player) {
		return false;//da implementare TODO
	}
	
	public int useTris(Player player, TerritoryCard[] tris) {
		return 0;//da implementare TODO	
	}
	
	public void removeCard(Player player, TerritoryCard card) {
		//da implementare TODO
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
