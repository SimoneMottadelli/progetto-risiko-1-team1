package com.drisk.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MapTest {

	@Test
	public void mapTest() {
		Map map = Map.getInstance();
		map.createMap("easy");
		assertEquals(3, Map.getInstance().getContinents().size());
		assertEquals(25, Map.getInstance().getTerritories().size());
		Territory t = Map.getInstance().findTerritoryByName("china");
		assertEquals(6, t.getAdjacentTerritories().size());
		Territory t1 = Map.getInstance().findTerritoryByName("mongolia");
		assertEquals(5, t1.getAdjacentTerritories().size());
	}
	
}
