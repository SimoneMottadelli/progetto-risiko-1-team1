package com.drisk.technicalservice;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.drisk.domain.Continent;

public class MapDataMapperTest {
	
	@Test
	public void getContinentsNamesTest() {
		assertTrue(MapDataMapper.getContinentsNames("noDifficulty").size() == 0);
		assertTrue(MapDataMapper.getContinentsNames("easy").size() == 3);
		Continent c = new Continent("europe");
		List<String> continentsName = MapDataMapper.getContinentsNames("easy");
		assertTrue(continentsName.contains(c.getName()));
	}
	
	@Test
	public void getTerritoriesAndContinentsNamesTest() {
		assertTrue(MapDataMapper.getTerritoriesAndContinentsNames("noDifficuly").size() == 0);
		assertTrue(MapDataMapper.getTerritoriesAndContinentsNames("easy").size() == 25);
		List<String[]> territoriesAndContinentsNames = MapDataMapper.getTerritoriesAndContinentsNames("easy");
		String[] tc = {"scandinavia", "europe"};
		boolean found = false;
		for(String[] s : territoriesAndContinentsNames)
			if(s[0].equals(tc[0]) && s[1].equals(tc[1]))
				found = true;
		if(!found)
			fail();
	}
	
	@Test
	public void getTerritoriesAndNeighboursNamesTest() {
		assertTrue(MapDataMapper.getTerritoriesAndNeighboursNames("noDifficulty").size() == 0);
		assertTrue(MapDataMapper.getTerritoriesAndNeighboursNames("easy").size() == 95);
	}

}
