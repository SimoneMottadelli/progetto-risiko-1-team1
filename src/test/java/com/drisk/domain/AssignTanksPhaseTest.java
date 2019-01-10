package com.drisk.domain;


import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AssignTanksPhaseTest {
	
	@Before
	public void initialize() {
		// initializing players
		for(int i = 1; i <= 6; i++)
			MatchManager.getInstance().joinGame("Player" + i);
		
		// creating map
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class);
			MapManager.getInstance().createMap(obj);
		} catch (FileNotFoundException | SyntaxException e) {}
		
		// initializing game
		MatchManager.getInstance().initGame();
	}
	
	@Test
	public void playPhaseTest() {
		
		TankAssignmentPhase phase = new TankAssignmentPhase();
		
		// getting current player and building the tris he wants to use
		Player currentPlayer = MatchManager.getInstance().getPlayers().get(0);
		TerritoryCard card_1 = CardManager.getInstance().findTerritoryCardByTerritoryName("egypt");
		TerritoryCard card_2 = CardManager.getInstance().findTerritoryCardByTerritoryName("mongolia");
		TerritoryCard card_3 = CardManager.getInstance().findTerritoryCardByTerritoryName("scandinavia");
		
		// building the json request payload
		JsonObject phaseConfig = new JsonObject();
		JsonArray cards = new JsonArray();
		cards.add(card_1.toJson());
		cards.add(card_2.toJson());
		cards.add(card_3.toJson());
		phaseConfig.add("cards", cards);
		
		try {
			phase.playPhase(currentPlayer, phaseConfig);
		} catch (RequestNotValidException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	@Test
	public void assignTanksTest() {
		
		Player player = new Player(null, "Zucca");
		new AssignTanksPhase().playPhase(player, null);
		assertEquals(1, player.getAvailableTanks());
		player.removeAvailableTanks();

		player.addTerritoryOwned(new Territory("italy"));
		player.addTerritoryOwned(new Territory("egypt"));
		player.addTerritoryOwned(new Territory("japan"));
		player.addTerritoryOwned(new Territory("kamchatka"));
		player.addTerritoryOwned(new Territory("middle_east"));
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
		
		player.addTerritoryOwned(new Territory("north_africa"));
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
	
	@Test
	public void useTrisTest() {
		AssignTanksPhase phase = new AssignTanksPhase();
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
		tris[2] = new TerritoryCard(null, TerritoryCardSymbol.JOLLY);
		tris[1] = new TerritoryCard(null, TerritoryCardSymbol.CAVALRY);
		phase.useTris(player, tris);
		assertEquals(14, player.getAvailableTanks());
		player.removeAvailableTanks();
	}
	*/
	
	@After
	public void destroySingletons() {
		MatchManager.destroy();
		GameManager.destroy();
		CardManager.destroy();
	}
}