package com.drisk.domain.card;

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

import com.drisk.domain.game.ObjectiveTypeEnum;
import com.drisk.domain.game.Player;
import com.drisk.domain.map.Continent;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.map.Territory;

public class CardManager {

	private List<Card> missionCards;
	private List<Card> territoryCards;
	private List<Card> allCards;
	private List<Card> discardedCards;
	private static CardManager instance;
	
	private CardManager() {
		missionCards = new LinkedList<>();
		territoryCards = new LinkedList<>();
		discardedCards = new LinkedList<>();
		allCards = new LinkedList<>();
	}
	
	public static CardManager getInstance() {
		if (instance == null)
			instance = new CardManager();
		return instance;
	}	
	
	public void initPlayersMission(List<Player> players) {	
		for(int i = 0; i < players.size(); ++i) 
			players.get(i).setMission((MissionCard) missionCards.get(i % missionCards.size()));
	}
	
	public TerritoryCard findTerritoryCardByTerritoryName(String territoryName) {
		for (Card tc : allCards)
			if (((TerritoryCard)tc).getTerritory().getName().equals(territoryName))
				return (TerritoryCard) tc;
		return null;
	}
	
	public void initCards(List<Player> players, ObjectiveTypeEnum objective) {
		initTerritoryCards();
		initMissionCards(players, objective);
		shuffleDeck(territoryCards);
		shuffleDeck(missionCards);
	}
	
	public void initTerritoryCards() {
		TerritoryCardSymbolEnum[] symbols = TerritoryCardSymbolEnum.values();
		int i = 0;
		for(Territory t : MapManager.getInstance().getMapTerritories()) {
			TerritoryCard tc = new TerritoryCard(t, symbols[i % 3]);
			addTerritoryCard(tc);
			++i;
		}
	}
	
	private void addTerritoryCard(TerritoryCard tc) {
		if(!territoryCards.contains(tc)) {
			territoryCards.add(tc);
			allCards.add(tc);
		}
	}
	
	public void initMissionCards(List<Player> players, ObjectiveTypeEnum objective) {
		if(objective.equals(ObjectiveTypeEnum.MULTIPLE)) {
			missionCards.addAll(createConqerContinentMission());
			missionCards.addAll(createDestroyEnemyMission(players));
		}
		missionCards.add(new ConquerTerritoryMissionCard((MapManager.getInstance().getNumberOfTerritories()*4)/7));
	}

	private List<MissionCard> createConqerContinentMission() {
		
		Continent asia = MapManager.getInstance().findContinentByName("asia");
		Continent africa = MapManager.getInstance().findContinentByName("africa");
		Continent north_america = MapManager.getInstance().findContinentByName("north_america");
		Continent south_america = MapManager.getInstance().findContinentByName("south_america");
		Continent australia = MapManager.getInstance().findContinentByName("australia");
		
		List<MissionCard> missions = new LinkedList<>();
		
		missions.add(new ConquerContinentMissionCard(asia, africa));
		missions.add(new ConquerContinentMissionCard(asia, south_america));
		missions.add(new ConquerContinentMissionCard(north_america, africa));
		missions.add(new ConquerContinentMissionCard(north_america, australia));
		
		return missions;
	}

	private List<MissionCard> createDestroyEnemyMission(List<Player> players) {
		List<MissionCard> missions = new LinkedList<>();
		
		for(Player p : players)
			missions.add(new DestroyEnemyMissionCard(p));
			
		return missions;
	}
	
	public void shuffleDeck(List<Card> cards) {
		Collections.shuffle(cards);
	}
	
	public void giveTerritoryCard(Player player) {
		if(territoryCards.isEmpty())
			refillDeck();
		player.addTerritoryCards((TerritoryCard) drawCard(territoryCards));
	}
	
	private Card drawCard(List<Card> cards) {
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

	public void removeCards(Player player, List<TerritoryCard> territoryCards) {
		for(TerritoryCard tc : territoryCards)
			removeCard(player, tc);
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

	public static void destroy() {
		instance = null;
	}	
}
