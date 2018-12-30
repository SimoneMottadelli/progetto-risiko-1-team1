package com.drisk.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class AssignBonusTanksPhaseTest {
	
	@Test
	public void getTrisWithValueTest() {
		Map<List<TerritoryCardSymbol>, Integer> trisWithValue = new AssignBonusTanksPhase().getTrisWithValue();
		List<TerritoryCardSymbol> tris = new LinkedList<>();
		
		assertEquals(7, trisWithValue.size());
		
		tris.add(TerritoryCardSymbol.CAVALRY);
		tris.add(TerritoryCardSymbol.CAVALRY);
		tris.add(TerritoryCardSymbol.CAVALRY);
		assertEquals(8, (int)(trisWithValue.get(tris)));
		tris.clear();
		
		tris.add(TerritoryCardSymbol.INFANTRY);
		tris.add(TerritoryCardSymbol.INFANTRY);
		tris.add(TerritoryCardSymbol.JOLLY);
		assertEquals(12, (int)(trisWithValue.get(tris)));
		tris.clear();
		
		tris.add(TerritoryCardSymbol.INFANTRY);
		tris.add(TerritoryCardSymbol.CAVALRY);
		tris.add(TerritoryCardSymbol.ARTILLERY);
		assertEquals(10, (int)(trisWithValue.get(tris)));
		tris.clear();
		
		
		tris.add(TerritoryCardSymbol.ARTILLERY);
		tris.add(TerritoryCardSymbol.JOLLY);
		tris.add(TerritoryCardSymbol.JOLLY);
		assertNull(trisWithValue.get(tris));
	}
	
	
	@Test
	public void useTrisTest() {
		AssignBonusTanksPhase phase = new AssignBonusTanksPhase();
		Player player = new Player(Color.RED, "Simone");
		
		TerritoryCard[] tris = new TerritoryCard[3];
		
		tris[0] = new TerritoryCard(null, TerritoryCardSymbol.CAVALRY);
		tris[1] = new TerritoryCard(null, TerritoryCardSymbol.INFANTRY);
		tris[2] = new TerritoryCard(null, TerritoryCardSymbol.ARTILLERY);
		phase.useTris(player, tris);
		assertEquals(10, player.getAvailableTanks());
		player.removeAvailableTanks();
		
		tris[0] = new TerritoryCard(null, TerritoryCardSymbol.CAVALRY);
		tris[1] = new TerritoryCard(null, TerritoryCardSymbol.CAVALRY);
		tris[2] = new TerritoryCard(null, TerritoryCardSymbol.JOLLY);
		phase.useTris(player, tris);
		assertEquals(12, player.getAvailableTanks());
		player.removeAvailableTanks();
		
		TerritoryCard[] tris2 = new TerritoryCard[4];
		tris2[0] = new TerritoryCard(null, TerritoryCardSymbol.CAVALRY);
		tris2[1] = new TerritoryCard(null, TerritoryCardSymbol.INFANTRY);
		tris2[2] = new TerritoryCard(null, TerritoryCardSymbol.ARTILLERY);
		tris2[3] = new TerritoryCard(null, TerritoryCardSymbol.CAVALRY);
		phase.useTris(player, tris2);
		assertEquals(0, player.getAvailableTanks());
		
		Territory t = new Territory("NomeTerritorio");
		player.getTerritoriesOwned().add(t);
		tris[0] = new TerritoryCard(t, TerritoryCardSymbol.CAVALRY);
		tris[1] = new TerritoryCard(null, TerritoryCardSymbol.CAVALRY);
		tris[2] = new TerritoryCard(null, TerritoryCardSymbol.JOLLY);
		phase.useTris(player, tris);
		assertEquals(14, player.getAvailableTanks());
		player.removeAvailableTanks();
	}
	
}
