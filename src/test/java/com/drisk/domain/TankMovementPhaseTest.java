package com.drisk.domain;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TankMovementPhaseTest {
	
	@Before
	public void initialize() {
		
		String mapStringJson = "{'difficulty' : 'custom', 'continents' : ['africa', 'europe', 'asia'], 'territories' : ['italy', 'france', 'egypt', 'north africa', 'kamchatka', 'china', 'japan', 'india', 'middle east'],"
				+ " 'membership' : [{'name' : 'europe', 'territories' : ['italy', 'france']}, {'name' : 'africa', 'territories' : ['egypt', 'north africa']}, {'name' : 'asia', 'territories' : ['china', 'kamchatka', 'japan', 'india', 'middle east']}],"
				+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, {'name' : 'north africa', 'territories' : ['egypt']}, {'name' : 'china', 'territories' : ['india', 'japan']}, {'name' : 'middle east', 'territories' : ['india']}]}";
		Gson json = new Gson();
		JsonObject mapConfig = json.fromJson(mapStringJson, JsonObject.class); 
		try {
			MapManager.getInstance().createMap(mapConfig);
		} catch (SyntaxException | FileNotFoundException e) {}
		for(int i = 1; i <= 6; i++)
			MatchManager.getInstance().joinGame("Player" + i);
		MatchManager.getInstance().initGame();
	}
	
	@Test
	public void moveTanksTest() {
		
		// Both territories have an initial tank
		Territory oldTerritory = MapManager.getInstance().findTerritoryByName("china");
		Territory newTerritory = MapManager.getInstance().findTerritoryByName("japan");
		
		oldTerritory.addTanks(4);
		newTerritory.addTanks(2);
		new TankMovementPhase().moveTanks(oldTerritory, newTerritory, 3);
		assertEquals(2, oldTerritory.getNumberOfTanks());
		assertEquals(6, newTerritory.getNumberOfTanks());
		
		new TankMovementPhase().moveTanks(oldTerritory, newTerritory, 3);
		assertEquals(1, oldTerritory.getNumberOfTanks());
		assertEquals(7, newTerritory.getNumberOfTanks());
		
	}
	
	public void destroySingletons() {
		GameManager.destroy();
		MatchManager.destroy();
	}
}
