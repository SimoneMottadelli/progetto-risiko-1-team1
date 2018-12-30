package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

public class AssignBonusTanksPhase implements Phase{

	@Override
	public void startPhase() {
		//da implementare TODO		
	}

	@Override
	public void nextPhase() {
		Turn.getInstance().setCurrentPhase(new AssignTanksPhase());
	}
	
	public void useTris(Player player, TerritoryCard[] tris) {
		if (tris.length == 3) {
			Arrays.sort(tris);
			
			List<TerritoryCardSymbol> trisSymbols = new LinkedList<>();
			for (int i = 0; i < 3; ++i)
				trisSymbols.add(tris[i].getSymbol());
			
			Integer bonusTanks = getTrisWithValue().get(trisSymbols);
			
			if (bonusTanks != null) { // ...then the tris exists...
				
				// +2 tanks if the player has a territory printed on a specific territory card used
				for (TerritoryCard t : tris)
					if (player.getTerritoriesOwned().contains(t.getTerritory()))
						bonusTanks += 2;
				player.addAvailableTanks(bonusTanks);
				CardManager.getInstance().removeCards(player, tris);
			}
		}
	}
	
	public Map<List<TerritoryCardSymbol>, Integer> getTrisWithValue() {
		Map<List<TerritoryCardSymbol>, Integer> trisWithValue = new HashMap<>();
		
		//3 cannons = 4 tanks
		List<TerritoryCardSymbol> tris1 = new LinkedList<>();
		for (int i = 0; i < 3; ++i)
			tris1.add(TerritoryCardSymbol.ARTILLERY);
		trisWithValue.put(tris1, 4);
		
		//3 infantrymen = 6 tanks
		List<TerritoryCardSymbol> tris2 = new LinkedList<>();
		for (int i = 0; i < 3; ++i)
			tris2.add(TerritoryCardSymbol.INFANTRY);
		trisWithValue.put(tris2, 6);
		
		//3 knights = 6 tanks
		List<TerritoryCardSymbol> tris3 = new LinkedList<>();
		for (int i = 0; i < 3; ++i)
			tris3.add(TerritoryCardSymbol.CAVALRY);
		trisWithValue.put(tris3, 8);
		
		// 2 cannons (or infantrymen or knights) + jolly = 12 tanks
		List<TerritoryCardSymbol> tris4 = new LinkedList<>();
		for (int i = 0; i < 2; ++i)
			tris4.add(TerritoryCardSymbol.ARTILLERY);
		tris4.add(TerritoryCardSymbol.JOLLY);
		trisWithValue.put(tris4, 12);
		
		List<TerritoryCardSymbol> tris5 = new LinkedList<>();
		for (int i = 0; i < 2; ++i)
			tris5.add(TerritoryCardSymbol.INFANTRY);
		tris5.add(TerritoryCardSymbol.JOLLY);
		trisWithValue.put(tris5, 12);
		
		List<TerritoryCardSymbol> tris6 = new LinkedList<>();
		for (int i = 0; i < 2; ++i)
			tris6.add(TerritoryCardSymbol.CAVALRY);
		tris6.add(TerritoryCardSymbol.JOLLY);
		trisWithValue.put(tris6, 12);
		
		//artillery + infantry + cavalry = 10 tanks		
		List<TerritoryCardSymbol> tris7 = new LinkedList<>();
		tris7.add(TerritoryCardSymbol.INFANTRY);
		tris7.add(TerritoryCardSymbol.CAVALRY);
		tris7.add(TerritoryCardSymbol.ARTILLERY);
		trisWithValue.put(tris7, 10);
		
		return trisWithValue;
	}	
	
}
