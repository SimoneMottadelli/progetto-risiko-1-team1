package com.drisk.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TanksAssignmentPhase extends Phase {

	private static Map<List<TerritoryCardSymbolEnum>, Integer> trisMap;
	private TerritoryCard[] playerTris;
	private Player player;
	
	public TanksAssignmentPhase(Player player) {
		super(PhaseEnum.TANKASSIGNMENT.getValue());
		this.player = player;
		initTris();
		assignTanks();
	}

	@Override
	public void playPhase(JsonObject phaseConfig) throws RequestNotValidException {
		fromJson(phaseConfig);
		checkCondition();
		if (playerTris != null)
			useTris();
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new TanksPlacementPhase(player));
	}

	// each player has at least a tank at the beginning of his turn even if he owns
	// less then three territories
	private void assignTanks() {
		int numberTerritoriesOwned = MapManager.getInstance().getPlayerTerritories(player).size();
		int tanks;
		if (numberTerritoriesOwned / 3 < 1)
			tanks = 1;
		else
			tanks = numberTerritoriesOwned / 3;
		tanks += getTanksPerContinent();
		TankManager.getInstance().addTanksToPlayer(tanks, player);
		addMessage("Player " + player.getColor() + " has recived " + tanks + " tanks");
	}

	private int getTanksPerContinent() {
		int tanks = 0;
		List<Territory> territoriesOwned = MapManager.getInstance().getPlayerTerritories(player);
		List<Continent> continents = MapManager.getInstance().getMap().getContinents();
		for (Continent c : continents)
			if (territoriesOwned.containsAll(c.getTerritories()))
				switch (c.getName()) {
				case "africa":
					tanks += 3;
					break;
				case "asia":
					tanks += 7;
					break;
				case "australia":
				case "south_america":
					tanks += 2;
					break;
				case "europe":
				case "north_america":
					tanks += 5;
					break;
				default:
					tanks += 0;
				}
		return tanks;
	}

	private void useTris() throws RequestNotValidException {
		Arrays.sort(playerTris);
		List<TerritoryCardSymbolEnum> trisSymbols = new LinkedList<>();
		for (int i = 0; i < 3; ++i)
			trisSymbols.add(playerTris[i].getSymbol());

		Integer bonusTanks = trisMap.get(trisSymbols);
		if (bonusTanks == null) {
			int i = 0;
			String response = playerTris[i++].getSymbol().toString() + ", " + playerTris[i++].getSymbol().toString()
					+ ", " + playerTris[i].getSymbol().toString() + " is not a tris";
			throw new RequestNotValidException(response);
		}

		// +2 tanks if the player has a territory printed on a specific territory card
		// used
		for (TerritoryCard t : playerTris)
			if (t.getTerritory().getOwner().equals(player))
				bonusTanks += 2;

		TankManager.getInstance().addTanksToPlayer(bonusTanks, player);
		addMessage("Player " + player.getColor() + " has received " + bonusTanks + " tanks by using a tris");
		CardManager.getInstance().removeCards(player, playerTris);
		playerTris = null;
	}

	// This method is static. In this way, trisMap will be initialized only once.
	private static void initTris() {
		if (trisMap != null)
			return;

		trisMap = new HashMap<>();

		// 3 cannons = 4 tanks
		List<TerritoryCardSymbolEnum> tris1 = new LinkedList<>();
		for (int i = 0; i < 3; ++i)
			tris1.add(TerritoryCardSymbolEnum.ARTILLERY);
		trisMap.put(tris1, 4);

		// 3 infantrymen = 6 tanks
		List<TerritoryCardSymbolEnum> tris2 = new LinkedList<>();
		for (int i = 0; i < 3; ++i)
			tris2.add(TerritoryCardSymbolEnum.INFANTRY);
		trisMap.put(tris2, 6);

		// 3 knights = 8 tanks
		List<TerritoryCardSymbolEnum> tris3 = new LinkedList<>();
		for (int i = 0; i < 3; ++i)
			tris3.add(TerritoryCardSymbolEnum.CAVALRY);
		trisMap.put(tris3, 8);

		// 2 cannons (or infantrymen or knights) + jolly = 12 tanks
		List<TerritoryCardSymbolEnum> tris4 = new LinkedList<>();
		for (int i = 0; i < 2; ++i)
			tris4.add(TerritoryCardSymbolEnum.ARTILLERY);
		tris4.add(TerritoryCardSymbolEnum.JOLLY);
		trisMap.put(tris4, 12);

		List<TerritoryCardSymbolEnum> tris5 = new LinkedList<>();
		for (int i = 0; i < 2; ++i)
			tris5.add(TerritoryCardSymbolEnum.INFANTRY);
		tris5.add(TerritoryCardSymbolEnum.JOLLY);
		trisMap.put(tris5, 12);

		List<TerritoryCardSymbolEnum> tris6 = new LinkedList<>();
		for (int i = 0; i < 2; ++i)
			tris6.add(TerritoryCardSymbolEnum.CAVALRY);
		tris6.add(TerritoryCardSymbolEnum.JOLLY);
		trisMap.put(tris6, 12);

		// artillery + infantry + cavalry = 10 tanks
		List<TerritoryCardSymbolEnum> tris7 = new LinkedList<>();
		tris7.add(TerritoryCardSymbolEnum.INFANTRY);
		tris7.add(TerritoryCardSymbolEnum.CAVALRY);
		tris7.add(TerritoryCardSymbolEnum.ARTILLERY);
		trisMap.put(tris7, 10);
	}

	@Override
	public void fromJson(JsonObject obj) throws RequestNotValidException {
		TerritoryCard[] tris = new TerritoryCard[3];
		JsonArray cards = obj.getAsJsonArray("cards");
		if (cards != null) {
			int i = 0;
			if (cards.size() != 3)
				throw new RequestNotValidException("You must select exactly three cards to compose a tris");
			for (JsonElement e : cards) 
				tris[i++] = CardManager.getInstance().findTerritoryCardByTerritoryName(e.getAsJsonObject().get("territory").getAsString().toLowerCase().replace("\"", ""));
			
			playerTris = tris;
		}
	}

	@Override
	public void checkCondition() throws RequestNotValidException {
		for (TerritoryCard t : playerTris)
			if (!player.getTerritoryCardsHand().contains(t))
				throw new RequestNotValidException("You don't have " + t.getTerritory().getName() + " card in your hand");
	}

}