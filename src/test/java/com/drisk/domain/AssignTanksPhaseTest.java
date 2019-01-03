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
		String s = "{'difficulty' : 'easy', 'continents' : ['africa', 'europe', 'asia'], 'territories' : ['italy', 'france', 'egypt', 'north africa', 'kamchatka', 'china', 'japan', 'india', 'middle east'],"
				+ " 'membership' : [{'name' : 'europe', 'territories' : ['italy', 'france']}, {'name' : 'africa', 'territories' : ['egypt', 'north africa']}, {'name' : 'asia', 'territories' : ['china', 'kamchatka', 'japan', 'india', 'middle east']}],"
				+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, {'name' : 'north africa', 'territories' : ['egypt']}, {'name' : 'china', 'territories' : ['india', 'japan']}, {'name' : 'middle east', 'territories' : ['india']}]}";
		Gson json = new Gson();
		JsonObject obj = json.fromJson(s, JsonObject.class); 
		try {
			//waiting for implement of createMap
			MatchManager.getInstance().setGameConfig(obj);
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
		for(int i = 1; i <= 6; i++)
			MatchManager.getInstance().joinGame("Player" + i);
		MatchManager.getInstance().startGame();
	}
	
	@Test
	public void assignTanksTest() {
		
		Player player = new Player(null, "Zucca");
		new AssignTanksPhase().assignTanks(player);
		assertEquals(1, player.getAvailableTanks());
		player.removeAvailableTanks();

		player.addTerritoryOwned(new Territory("italy"));
		player.addTerritoryOwned(new Territory("egypt"));
		player.addTerritoryOwned(new Territory("japan"));
		player.addTerritoryOwned(new Territory("kamchatka"));
		player.addTerritoryOwned(new Territory("middle east"));
		new AssignTanksPhase().assignTanks(player);
		assertEquals(1, player.getAvailableTanks());
		player.removeAvailableTanks();
		
		player.addTerritoryOwned(new Territory("india"));
		new AssignTanksPhase().assignTanks(player);
		assertEquals(2, player.getAvailableTanks());
		player.removeAvailableTanks();
		
	}
	
	@Test
	public void getTanksPerContinentTest() {
			
		Player player = new Player(null, "Zucca");
		//all African Territories
		int numberOfTanks = new AssignTanksPhase().getTanksPerContinent(player);
		assertEquals(0, numberOfTanks);
		
		player.addTerritoryOwned(new Territory("north africa"));
		player.addTerritoryOwned(new Territory("egypt"));
		
		numberOfTanks = new AssignTanksPhase().getTanksPerContinent(player);
		assertEquals(3, numberOfTanks);
		
		player.addTerritoryOwned(new Territory("italy"));		
		player.addTerritoryOwned(new Territory("france"));
		numberOfTanks = new AssignTanksPhase().getTanksPerContinent(player);
		assertEquals(8, numberOfTanks);	
		
		player.removeTerritoryOwned(new Territory("egypt"));
		numberOfTanks = new AssignTanksPhase().getTanksPerContinent(player);
		assertEquals(5, numberOfTanks);	
		
	}
	
}