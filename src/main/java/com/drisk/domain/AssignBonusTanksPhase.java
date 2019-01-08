package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;

public class AssignBonusTanksPhase extends Phase{
	
	private static Map<List<TerritoryCardSymbol>, Integer> trisMap;
	
	public AssignBonusTanksPhase() {
		super(1);
		initTris();
	}

	@Override
	public void playPhase(Player currentPlayer, JsonObject phaseConfig) {
		useTris(currentPlayer, (TerritoryCard[]) fromJson(phaseConfig));
	}
	
	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AssignTanksPhase());
	}
	
	private void useTris(Player player, TerritoryCard[] tris) {
		if (tris.length == 3) {
			Arrays.sort(tris);
			List<TerritoryCardSymbol> trisSymbols = new LinkedList<>();
			for (int i = 0; i < 3; ++i)
				trisSymbols.add(tris[i].getSymbol());
			
			Integer bonusTanks = trisMap.get(trisSymbols);
			
			if (bonusTanks != null) { // ...then the tris exists...
				
				// +2 tanks if the player has a territory printed on a specific territory card used
				for (TerritoryCard t : tris)
					if (t.getTerritory().getOwner().equals(player))
						bonusTanks += 2;
				
				TankManager.getInstance().addTanksToPlayer(bonusTanks, player);
				CardManager.getInstance().removeCards(player, tris);
			}
		}
	}
	
	// This method is static. In this way, trisMap will be initialized only once.
	private static void initTris() {
		if (trisMap != null)
			return;

		trisMap = new HashMap<>();
		
		// 3 cannons = 4 tanks
		List<TerritoryCardSymbol> tris1 = new LinkedList<>();
		for (int i = 0; i < 3; ++i)
			tris1.add(TerritoryCardSymbol.ARTILLERY);
		trisMap.put(tris1, 4);
			
		// 3 infantrymen = 6 tanks
		List<TerritoryCardSymbol> tris2 = new LinkedList<>();
		for (int i = 0; i < 3; ++i)
			tris2.add(TerritoryCardSymbol.INFANTRY);
		trisMap.put(tris2, 6);
			
		// 3 knights = 8 tanks
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
			
		// artillery + infantry + cavalry = 10 tanks		
		List<TerritoryCardSymbol> tris7 = new LinkedList<>();
		tris7.add(TerritoryCardSymbol.INFANTRY);
		tris7.add(TerritoryCardSymbol.CAVALRY);
		tris7.add(TerritoryCardSymbol.ARTILLERY);
		trisMap.put(tris7, 10);
	}

	@Override
	public Object fromJson(JsonObject obj) {
		TerritoryCard[] tris = new TerritoryCard[3];
		JsonArray cards = obj.getAsJsonArray("cards");
		int i = 0;
		for(JsonElement cardObj : cards) {
			JsonObject card = cardObj.getAsJsonObject();
			Territory t = MapManager.getInstance().findTerritoryByName(card.get("name").toString().toLowerCase());
			tris[i++] = new TerritoryCard(t, TerritoryCardSymbol.valueOf(card.get("symbol").toString().toUpperCase()));
		}
		return tris;
	}
}
