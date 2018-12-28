package com.drisk.domain;

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

import com.drisk.technicalservice.CardDataMapper;

public class CardManager {

	private LinkedList<MissionCard> missionCards;
	private LinkedList<TerritoryCard> territoryCards;
	private LinkedList<TerritoryCard> discardedCards;
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
	
	public void initTerritoryCards(String difficulty) {
		List<String[]> territoryCardString = CardDataMapper.getTerritoryCard(difficulty);
		for(String[] tc : territoryCardString) {
			Territory territory = Map.getInstance().findTerritoryByName(tc[0]);
			TerritoryCardType symbol = TerritoryCardType.valueOf(tc[1]);
			TerritoryCard card = new TerritoryCard(territory, symbol);
			territoryCards.add(card);
		}
	}
	
	private void addCard(List<Card> deck, Card card) {
		if(!deck.contains(card))
			deck.add(card);
	}
	
	public void shuffleDeck(List<Card> cards) {
		Collections.shuffle(cards);
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

	public void setTerritoryCards(LinkedList<TerritoryCard> territoryCards) {
		this.territoryCards = territoryCards;
	}

	public void setMissionCards(LinkedList<MissionCard> missionCards) {
		this.missionCards = missionCards;
	}
	
	
}
