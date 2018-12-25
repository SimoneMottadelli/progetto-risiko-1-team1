package com.drisk.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MapTest {
	
	@Before
	public void createMap() {
		Map map = Map.getInstance();
		map.createMap("easy");
	}

	@Test
	public void createContinentsTest() {
		assertEquals(3, Map.getInstance().getContinents().size());
	}
	
	@Test
	public void createTerritoriesTest() {
		assertEquals(25, Map.getInstance().getTerritories().size());
	}
	
	@Test
	public void createNeighboursTest() {
		Territory t = Map.getInstance().findTerritoryByName("china");
		assertEquals(6, t.getAdjacentTerritories().size());
		Territory t1 = Map.getInstance().findTerritoryByName("mongolia");
		assertEquals(5, t1.getAdjacentTerritories().size());
	}
	
}
