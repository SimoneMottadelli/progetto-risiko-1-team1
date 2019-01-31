package com.drisk.domain.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.Card;
import com.drisk.domain.CardManager;
import com.drisk.domain.GameManager;
import com.drisk.domain.LobbyManager;
import com.drisk.domain.MapManager;
import com.drisk.domain.Player;
import com.drisk.domain.TerritoryCard;
import com.drisk.domain.exceptions.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CardManagerTest {
	
	@Before
	public void init() {
		// players joining
		for(int i = 1; i <= 6; i++)
			LobbyManager.getInstance().joinGame("Player" + i);
		
		// creating map
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("default_map_easy.json"));
			Gson json = new Gson();
			JsonObject obj = json.fromJson(bufferedReader, JsonObject.class); 
			MapManager.getInstance().createMap(obj);
		} catch (FileNotFoundException | SyntaxException e) {
			e.printStackTrace();
		}
		
		// initializing the game
		LobbyManager.getInstance().initGame();
	}

	@Test
	public void initTerritoryCardsTest() {
		assertEquals(25, CardManager.getInstance().getTerritoryCards().size());
	}
	
	@Test
	public void shuffleDeckTest() {
		List<Card> territory1 = CardManager.getInstance().getTerritoryCards();
		List<Card> territory2 = new LinkedList<Card>();
		
		territory2.addAll(territory1);
		
		assertTrue(territory1.equals(territory2));

		CardManager.getInstance().shuffleDeck(territory2);

		assertFalse(territory1.equals(territory2));
		
	}
	
	@Test
	public void refillDeckTest() {
		List<Card> start = CardManager.getInstance().getTerritoryCards();
		List<Card> discarded = CardManager.getInstance().getDiscardedCards();
		
		while(!start.isEmpty())
			discarded.add(start.remove(0));
		
		assertEquals(0, start.size());
		assertTrue(start.isEmpty());

		CardManager.getInstance().refillDeck();
		
		assertEquals(0, discarded.size());
		assertFalse(start.isEmpty());
	}
	
	@Test
	public void drawCardTest() {
		List<Card> territory = CardManager.getInstance().getTerritoryCards();
		TerritoryCard tc = (TerritoryCard) territory.get(0);
		CardManager.getInstance().giveTerritoryCard(new Player(null, null));

		assertFalse(territory.contains(tc));
	}	
	
	@After
	public void destroySingletons() {
		GameManager.destroy();
		CardManager.destroy();
		LobbyManager.destroy();
	}
}
