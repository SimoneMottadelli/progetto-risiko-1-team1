package com.drisk.domain;

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

import com.drisk.technicalservice.CardDataMapper;

public class CardManager {

	private LinkedList<Card> missionCards;
	private LinkedList<Card> territoryCards;
	private LinkedList<Card> discardedCards;
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
	
	//DA MODIFICARE
	public void initMissionCards(String difficulty) {	
		List<String> missionCardString = CardDataMapper.getMissionCard(difficulty);
		int id = 0;
		for(String text : missionCardString) {
			MissionCard card = new MissionCard(id++, text);
			missionCards.add(card);
		}
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
	

//	private void addCard(List<Card> deck, Card card) {
//		if(!deck.contains(card))
//			deck.add(card);
//	}
	
	
	public void shuffleDeck(LinkedList<Card> cards) {
		Collections.shuffle(cards);
	}
	
	public Card drawCard(LinkedList<Card> cards) {
		return cards.remove(0);
	}
	
	public void refillDeck() {
		setTerritoryCards(discardedCards);
		shuffleDeck(territoryCards);
	}
	
	public boolean checkTris(Player player) {
		return false;//da implementare TODO
	}
	
	public int useTris(Player player, TerritoryCard[] tris) {
		return 0;//da implementare TODO	
	}
	
	public void removeCard(Player player, TerritoryCard card) {
		player.getTerritoryCards().remove(card);
	}
	
	public LinkedList<Card> getMissionCards() {
		return missionCards;
	}

	public LinkedList<Card> getTerritoryCards() {
		return territoryCards;
	}

	public void setTerritoryCards(LinkedList<Card> territoryCards) {
		this.territoryCards = territoryCards;
	}

	public void setMissionCards(LinkedList<Card> missionCards) {
		this.missionCards = missionCards;
	}
	
	
}
