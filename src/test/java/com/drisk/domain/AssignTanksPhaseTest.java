package com.drisk.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AssignTanksPhaseTest {
	
	@Before
	public void initialize() {
		String s = "{'difficulty' : 'easy', 'continents' : ['africa', 'europe'], 'territories' : ['italy', 'france', 'egypt', 'north africa'],"
				+ " 'membership' : [{'name' : 'europe', 'territories' : ['italy', 'france']}, {'name' : 'africa', 'territories' : ['egypt', 'north africa']}],"
				+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, {'name' : 'north africa', 'territories' : ['egypt']}]}";
		Gson json = new Gson();
		JsonObject obj = json.fromJson(s, JsonObject.class); 
		try {
			Map.getInstance().createMap(obj);
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void assignTanksTest() {
		
		Player player = new Player(null, "Zucca");
		new AssignTanksPhase().assignTanks(player);
		assertEquals(1, player.getAvailableTanks());
		player.removeAvailableTanks();

		player.addTerritoryOwned(new Territory("south africa"));
		player.addTerritoryOwned(new Territory("north africa"));
		player.addTerritoryOwned(new Territory("egypt"));
		player.addTerritoryOwned(new Territory("madagascar"));
		player.addTerritoryOwned(new Territory("congo"));
		player.addTerritoryOwned(new Territory("great britain"));
		new AssignTanksPhase().assignTanks(player);
		assertEquals(2, player.getAvailableTanks());
		player.removeAvailableTanks();
		
		player.addTerritoryOwned(new Territory("east africa"));
		new AssignTanksPhase().assignTanks(player);
		assertEquals(5, player.getAvailableTanks());
		player.removeAvailableTanks();
		
	}
	
	@Test
	public void getTanksPerContinentTest() {
			
		Player player = new Player(null, "Zucca");
		//all African Territories
		int numberOfTanks = new AssignTanksPhase().getTanksPerContinent(player);
		assertEquals(0, numberOfTanks);
		
		player.addTerritoryOwned(new Territory("south africa"));
		player.addTerritoryOwned(new Territory("north africa"));
		player.addTerritoryOwned(new Territory("egypt"));
		player.addTerritoryOwned(new Territory("madagascar"));
		player.addTerritoryOwned(new Territory("congo"));
		player.addTerritoryOwned(new Territory("east africa"));
		
		numberOfTanks = new AssignTanksPhase().getTanksPerContinent(player);
		assertEquals(3, numberOfTanks);
		
		player.addTerritoryOwned(new Territory("great britain"));
		player.addTerritoryOwned(new Territory("ukraine"));		
		player.addTerritoryOwned(new Territory("iceland"));
		player.addTerritoryOwned(new Territory("north europe"));
		player.addTerritoryOwned(new Territory("south europe"));
		player.addTerritoryOwned(new Territory("scandinavia"));
		player.addTerritoryOwned(new Territory("west europe"));
		numberOfTanks = new AssignTanksPhase().getTanksPerContinent(player);
		assertEquals(8, numberOfTanks);	
		
		player.removeTerritoryOwned(new Territory("egypt"));
		numberOfTanks = new AssignTanksPhase().getTanksPerContinent(player);
		assertEquals(5, numberOfTanks);	
		
	}
	
}