package com.drisk.domain.game;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.GameManager;
import com.drisk.domain.LobbyManager;
import com.drisk.domain.MapManager;
import com.drisk.domain.Player;
import com.drisk.domain.TanksMovementPhase;
import com.drisk.domain.Territory;
import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TankMovementPhaseTest {
	
	@Before
	public void initialize() {
		// initializing players
		for(int i = 1; i <= 2; i++)
			LobbyManager.getInstance().joinGame("Player" + i);
		
		// creating map
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_hard.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class);
			MapManager.getInstance().createMap(obj);
		} catch (FileNotFoundException | SyntaxException e) {}
		
		// initializing game
		LobbyManager.getInstance().initGame();
	}
	
	@Test
	public void moveTanksTest() {
		
		// getting current player and building the tris he wants to use
		Player currentPlayer = LobbyManager.getInstance().getPlayers().get(0);
		
		Territory to = null;
		Territory from = null;
		int count = 0;
		
		// to be sure that "to" territory as at least a neighbour
		while(to == null) {
			from = MapManager.getInstance().getPlayerTerritories(currentPlayer).get(count);
			for (Territory neighbour: from.getNeighbours()) {
				if (neighbour.getOwner().equals(from.getOwner()))
					to = neighbour;
			}
			if (to == null)
				++count;
		}
		
		// adding tanks to territories, remember that both of territories have an initial tank
		from.addTanks(4);
		to.addTanks(2);
				
		TanksMovementPhase phase = new TanksMovementPhase();
		
		// building the json request payload
		JsonObject phaseConfig = new JsonObject();
		phaseConfig.addProperty("from", from.getName());
		phaseConfig.addProperty("to", to.getName());
		phaseConfig.addProperty("howMany", 3);
		System.out.print(phaseConfig.get("from").toString());
		System.out.print(phaseConfig.get("to").toString());
		
		try {
			phase.playPhase(phaseConfig);
			assertEquals(2, from.getNumberOfTanks());
			assertEquals(6, to.getNumberOfTanks());
		} catch (RequestNotValidException e) {
			e.printStackTrace();
		}
		
	}
	
	@After
	public void destroySingletons() {
		GameManager.destroy();
		LobbyManager.destroy();
	}
	
}
