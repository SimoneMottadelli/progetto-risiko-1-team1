package com.drisk.domain;

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

import com.drisk.technicalservice.CardDataMapper;

public class CardManager {

	private List<Card> missionCards;
	private List<Card> territoryCards;
	private List<Card> discardedCards;
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
	
	public void initTerritoryCards(String difficulty) {
		List<String[]> territoryCardString = CardDataMapper.getTerritoryCard(difficulty);
		for(String[] tc : territoryCardString) {
			Territory territory = Map.getInstance().findTerritoryByName(tc[0]);
			TerritoryCardSymbol symbol = TerritoryCardSymbol.valueOf(tc[1]);
			TerritoryCard card = new TerritoryCard(territory, symbol);
			territoryCards.add(card);
		}
	}
	
	//DA MODIFICARE
	public void initMissionCards(String difficulty) {	
		List<String> missionCardString = CardDataMapper.getMissionCard(difficulty);
		int id = 0;
		for(String text : missionCardString) {
			MissionCard card = new MissionCard(id++, text);
			missionCards.add(card);
		}
	}
	
	public void shuffleDeck(List<Card> cards) {
		Collections.shuffle(cards);
	}
	
	public Card drawCard(List<Card> cards) {
		return cards.remove(0);
	}
	
	public void refillDeck() {
		setTerritoryCards(discardedCards);
		shuffleDeck(territoryCards);
	}
	
	public void removeCards(Player player, TerritoryCard[] tris) {
		for (int i = 0; i < tris.length; ++i)
			removeCard(player, tris[i]);
	}
	
	public void removeCard(Player player, TerritoryCard card) {
		player.getTerritoryCards().remove(card);
	}
	
	public List<Card> getMissionCards() {
		return missionCards;
	}

	public List<Card> getTerritoryCards() {
		return territoryCards;
	}

	public void setTerritoryCards(List<Card> territoryCards) {
		this.territoryCards = territoryCards;
	}

	public void setMissionCards(List<Card> missionCards) {
		this.missionCards = missionCards;
	}
	
	
}
