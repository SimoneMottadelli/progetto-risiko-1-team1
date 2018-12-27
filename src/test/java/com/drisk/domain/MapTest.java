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
		int numberOfTerritories = 0;
		for(Continent c : Map.getInstance().getContinents())
			numberOfTerritories = numberOfTerritories + c.getTerritories().size();
		assertEquals(25, numberOfTerritories);
	}
	
	@Test
	public void createNeighboursTest() {
		Territory t = Map.getInstance().findTerritoryByName("china");
		assertEquals(6, t.getNeighbours().size());
		Territory t1 = Map.getInstance().findTerritoryByName("mongolia");
		assertEquals(5, t1.getNeighbours().size());
	}
	
}
