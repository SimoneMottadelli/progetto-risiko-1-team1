package com.drisk.domain;

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

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
	
	public void initPlayersMission(List<Player> players, boolean singleMission) {
		if(singleMission) {
			MissionCard mission = (MissionCard) missionCards.get(0);
			for(Player p : players) 
				p.setMission(mission);
		} else {
			// missione non singola
		}
	}
	
	public void initCards() {
		initTerritoryCards();
		initMissionCards(MapManager.getInstance().getMapDifficulty());
		shuffleDeck(territoryCards);
		shuffleDeck(missionCards);
	}
	
	public void initTerritoryCards() {
		TerritoryCardSymbol[] symbols = TerritoryCardSymbol.values();
		int i = 0;
		for(Territory t : MapManager.getInstance().getMapTerritories()) {
			TerritoryCard tc = new TerritoryCard(t, symbols[i % 3]);
			addTerritoryCard(tc);
			++i;
		}
	}
	
	private void addTerritoryCard(TerritoryCard tc) {
		if(!territoryCards.contains(tc))
			territoryCards.add(tc);
	}
	
	public void initMissionCards(Difficulty dif) {
		switch(dif) {
		case EASY:
			MissionCard missionEasy = new ConquerTerritoryMissionCard(14);
			missionCards.add(missionEasy);
			break;
		case MEDIUM:
			MissionCard missionMedium = new ConquerTerritoryMissionCard(19);
			missionCards.add(missionMedium);
			break;
		case HARD:
			MissionCard missionHard = new ConquerTerritoryMissionCard(24);
			missionCards.add(missionHard);
			break;
		case CUSTOM:
			MissionCard missionCustom = new ConquerTerritoryMissionCard(10);
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
}
