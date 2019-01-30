package com.drisk.domain;

import java.util.LinkedList;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

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
		int i = 0;
		while (i < players.size()) {
			MissionCard mc = (MissionCard) missionCards.get(new SecureRandom().nextInt(missionCards.size()));
			if (mc instanceof DestroyEnemyMissionCard) {
				if (!((DestroyEnemyMissionCard) mc).getEnemy().equals(players.get(i)))
					players.get(i).setMission(mc);
				else
					--i; // to avoid that this player remains without a missioncard
			} 
			else
				players.get(i).setMission(mc);
			++i;
		}
	}

	public TerritoryCard findTerritoryCardByTerritoryName(String territoryName) {
		for (Card tc : allCards)
			if (((TerritoryCard) tc).getTerritory().getName().equals(territoryName))
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
		for (Territory t : MapManager.getInstance().getMap().getTerritories()) {
			TerritoryCard tc = new TerritoryCard(t, symbols[i % 3]);
			addTerritoryCard(tc);
			++i;
		}
	}

	private void addTerritoryCard(TerritoryCard tc) {
		if (!territoryCards.contains(tc)) {
			territoryCards.add(tc);
			allCards.add(tc);
		}
	}

	public void initMissionCards(List<Player> players, ObjectiveTypeEnum objective) {
		if (objective.equals(ObjectiveTypeEnum.MULTIPLE)) {
			missionCards.addAll(createConqerContinentMission());
			missionCards.addAll(createDestroyEnemyMission(players));
		}
		missionCards.add(createConquerTerritoryMissionCard());
	}

	private MissionCard createConquerTerritoryMissionCard() {
		return new ConquerTerritoryMissionCard((MapManager.getInstance().getMap().getTerritories().size() * 4) / 7);
	}

	private List<MissionCard> createConqerContinentMission() {
		List<MissionCard> missions = new LinkedList<>();
		if (!MapManager.getInstance().getMapDifficulty().equals(DifficultyEnum.CUSTOM)) {
			Continent asia = MapManager.getInstance().getMap().findContinentByName("asia");
			Continent africa = MapManager.getInstance().getMap().findContinentByName("africa");
			Continent northAmerica = MapManager.getInstance().getMap().findContinentByName("north_america");
			Continent southAmerica = MapManager.getInstance().getMap().findContinentByName("south_america");
			Continent australia = MapManager.getInstance().getMap().findContinentByName("australia");

			missions.add(new ConquerContinentMissionCard(asia, africa));
			missions.add(new ConquerContinentMissionCard(asia, southAmerica));
			missions.add(new ConquerContinentMissionCard(northAmerica, africa));
			missions.add(new ConquerContinentMissionCard(northAmerica, australia));
		}

		return missions;
	}

	private List<MissionCard> createDestroyEnemyMission(List<Player> players) {
		List<MissionCard> missions = new LinkedList<>();

		for (Player p : players)
			missions.add(new DestroyEnemyMissionCard(p));

		return missions;
	}

	public void shuffleDeck(List<Card> cards) {
		Collections.shuffle(cards);
	}

	public void giveTerritoryCard(Player player) {
		if (territoryCards.isEmpty())
			refillDeck();
		player.addTerritoryCards((TerritoryCard) drawCard(territoryCards));
	}

	private Card drawCard(List<Card> cards) {
		return cards.remove(0);
	}

	public void refillDeck() {
		if (territoryCards.isEmpty()) {
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
		for (TerritoryCard tc : territoryCards)
			removeCard(player, tc);
	}

	public void removeCard(Player player, TerritoryCard card) {
		if (player.getTerritoryCardsHand().contains(card)) {
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

	public void changeMission(List<Player> players, Player loser) {
		for (Player p : players)
			if (p.getMissionCard() instanceof DestroyEnemyMissionCard) {
				DestroyEnemyMissionCard card = (DestroyEnemyMissionCard) p.getMissionCard();
				if (card.getEnemy().equals(loser))
					p.setMission(createConquerTerritoryMissionCard());
			}
	}
}
