package com.drisk.domain;

import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public static void destroy() {
		instance = null;
	}
	
	public void initTerritoryCards() {
		TerritoryCardSymbol[] symbols = {TerritoryCardSymbol.ARTILLERY, TerritoryCardSymbol.CAVALRY, TerritoryCardSymbol.INFANTRY};
		int i = 0;
		for(Territory t : MapManager.getInstance().getTerritories()) {
			TerritoryCard tc = new TerritoryCard(t, symbols[i++]);
			addTerritoryCard(tc);
			if(i >= 3)
				i = 0;
		}
	}
	
	private void addTerritoryCard(TerritoryCard tc) {
		if(!territoryCards.contains(tc))
			territoryCards.add(tc);
	}
	
	public void initMissionCards(Difficulty dif) {
		switch(dif) {
		case EASY:
			MissionCard missionEasy = new ConquestTerritoryMissionCard(14);
			missionCards.add(missionEasy);
			break;
		case MEDIUM:
			MissionCard missionMedium = new ConquestTerritoryMissionCard(19);
			missionCards.add(missionMedium);
			break;
		case HARD:
			MissionCard missionHard = new ConquestTerritoryMissionCard(24);
			missionCards.add(missionHard);
			break;
		case CUSTOM:
			MissionCard missionCustom = new ConquestTerritoryMissionCard(10);
			missionCards.add(missionCustom);
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
