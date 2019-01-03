package com.drisk.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.drisk.technicalservice.SyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CardManagerTest {
	
	@Before
	public void init() {
		String s = "{'difficulty' : 'easy', 'continents' : ['africa', 'europe'], 'territories' : ['italy', 'france', 'egypt', 'north africa'],"
				+ " 'membership' : [{'name' : 'europe', 'territories' : ['italy', 'france']}, {'name' : 'africa', 'territories' : ['egypt', 'north africa']}],"
				+ " 'neighbourhood' : [{'name' : 'italy', 'territories' : ['france', 'egypt']}, {'name' : 'north africa', 'territories' : ['egypt']}]}";
		Gson json = new Gson();
		JsonObject obj = json.fromJson(s, JsonObject.class); 
		try {
			MatchManager.getInstance().createMap(obj);
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
		CardManager.getInstance().initTerritoryCards("easy");
		CardManager.getInstance().initMissionCards("easy");
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
		
		Card drawn = CardManager.getInstance().drawCard(territory);

		assertFalse(territory.contains(drawn));
	}
	
}
