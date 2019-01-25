package com.drisk.domain.turn;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.card.CardManager;
import com.drisk.domain.card.TerritoryCard;
import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.exceptions.SyntaxException;
import com.drisk.domain.game.GameManager;
import com.drisk.domain.game.Player;
import com.drisk.domain.lobby.LobbyManager;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.turn.TanksAssignmentPhase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TankAssignmentPhaseTest {
	
	@Before
	public void initialize() {
		// initializing players
		for(int i = 1; i <= 6; i++)
			LobbyManager.getInstance().joinGame("Player" + i);
		
		// creating map
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class);
			MapManager.getInstance().createMap(obj);
		} catch (FileNotFoundException | SyntaxException e) {}
		
		// initializing game
		LobbyManager.getInstance().initGame();
	}
	
	@Test
	public void playPhaseTest() {
		
		// getting current player and building the tris he wants to use
		Player currentPlayer = LobbyManager.getInstance().getPlayers().get(0);
		
		TanksAssignmentPhase phase = new TanksAssignmentPhase(currentPlayer);
		TerritoryCard card_1 = CardManager.getInstance().findTerritoryCardByTerritoryName("egypt");
		TerritoryCard card_2 = CardManager.getInstance().findTerritoryCardByTerritoryName("north_east_asia");
		TerritoryCard card_3 = CardManager.getInstance().findTerritoryCardByTerritoryName("north_east_europe");
		
		// building the json request payload
		JsonObject phaseConfig = new JsonObject();
		JsonArray cards = new JsonArray();
		cards.add(card_1.toJson());
		cards.add(card_2.toJson());
		cards.add(card_3.toJson());
		phaseConfig.add("cards", cards);
		
		// it have to throws new RequestNotValidException because CAVALRY, INFANTRY, INFANTRY is not a tris
		try {
			System.out.print(currentPlayer.toJson());
			phase.playPhase(phaseConfig);
			fail();
		} catch (RequestNotValidException e) {}
	
	}
	
	@After
	public void destroySingletons() {
		LobbyManager.destroy();
		GameManager.destroy();
		CardManager.destroy();
	}
}