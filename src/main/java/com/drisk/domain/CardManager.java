package com.drisk.domain;

import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.drisk.technicalservice.CardDataMapper;

public class CardManager {

	private List<Card> missionCards;
	private List<Card> territoryCards;
	private List<Card> discardedCards;
	private static CardManager instance;
	private Map<List<TerritoryCardSymbol>, Integer> trisMap;
	
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
			Territory territory = com.drisk.domain.Map.getInstance().findTerritoryByName(tc[0]);
			TerritoryCardSymbol symbol = TerritoryCardSymbol.valueOf(tc[1].toUpperCase().trim());
			TerritoryCard card = new TerritoryCard(territory, symbol);
			if(!territoryCards.contains(card))
				territoryCards.add(card);
		}
	}
	
	//DA MODIFICARE
	public void initMissionCards(String difficulty) {	
		List<String> missionCardString = CardDataMapper.getMissionCard(difficulty);
		int id = 0;
		for(String text : missionCardString) {
			MissionCard card = new MissionCard(id++, text);
			if(!missionCards.contains(card))
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
		if(territoryCards.isEmpty()) {
			setTerritoryCards(discardedCards);
			discardedCards.clear();
			shuffleDeck(territoryCards);
		}
	}
	
	public void removeCards(Player player, TerritoryCard[] tris) {
		for (int i = 0; i < tris.length; ++i)
			removeCard(player, tris[i]);
	}
	
	public void removeCard(Player player, TerritoryCard card) {
		if(player.getTerritoryCardsHand().contains(card)) {
			player.getTerritoryCardsHand().remove(card);
			discardedCards.add(card);
		}
	}
	
	public List<Card> getMissionCards() {
		return missionCards;
	}

	public List<Card> getTerritoryCards() {
		return territoryCards;
	}
	
	public List<Card> getDiscardedCards() {
		return discardedCards;
	}

	public void setDiscardedCards(List<Card> cards) {
		this.discardedCards.clear();
		this.discardedCards.addAll(cards);
	}

	public void setTerritoryCards(List<Card> cards) {
		this.territoryCards.clear();
		this.territoryCards.addAll(cards);
	}

	public void setMissionCards(List<Card> cards) {
		this.missionCards.clear();
		this.missionCards.addAll(cards);
	}
	
	public Map<List<TerritoryCardSymbol>, Integer> getTrisWithValue() {
		if (trisMap == null) {
			trisMap = new HashMap<>();
			
			//3 cannons = 4 tanks
			List<TerritoryCardSymbol> tris1 = new LinkedList<>();
			for (int i = 0; i < 3; ++i)
				tris1.add(TerritoryCardSymbol.ARTILLERY);
			trisMap.put(tris1, 4);
			
			//3 infantrymen = 6 tanks
			List<TerritoryCardSymbol> tris2 = new LinkedList<>();
			for (int i = 0; i < 3; ++i)
				tris2.add(TerritoryCardSymbol.INFANTRY);
			trisMap.put(tris2, 6);
			
			//3 knights = 8 tanks
			List<TerritoryCardSymbol> tris3 = new LinkedList<>();
			for (int i = 0; i < 3; ++i)
				tris3.add(TerritoryCardSymbol.CAVALRY);
			trisMap.put(tris3, 8);
			
			// 2 cannons (or infantrymen or knights) + jolly = 12 tanks
			List<TerritoryCardSymbol> tris4 = new LinkedList<>();
			for (int i = 0; i < 2; ++i)
				tris4.add(TerritoryCardSymbol.ARTILLERY);
			tris4.add(TerritoryCardSymbol.JOLLY);
			trisMap.put(tris4, 12);
			
			List<TerritoryCardSymbol> tris5 = new LinkedList<>();
			for (int i = 0; i < 2; ++i)
				tris5.add(TerritoryCardSymbol.INFANTRY);
			tris5.add(TerritoryCardSymbol.JOLLY);
			trisMap.put(tris5, 12);
			
			List<TerritoryCardSymbol> tris6 = new LinkedList<>();
			for (int i = 0; i < 2; ++i)
				tris6.add(TerritoryCardSymbol.CAVALRY);
			tris6.add(TerritoryCardSymbol.JOLLY);
			trisMap.put(tris6, 12);
			
			//artillery + infantry + cavalry = 10 tanks		
			List<TerritoryCardSymbol> tris7 = new LinkedList<>();
			tris7.add(TerritoryCardSymbol.INFANTRY);
			tris7.add(TerritoryCardSymbol.CAVALRY);
			tris7.add(TerritoryCardSymbol.ARTILLERY);
			trisMap.put(tris7, 10);
			
		}
		return trisMap;
	}	
	
}
