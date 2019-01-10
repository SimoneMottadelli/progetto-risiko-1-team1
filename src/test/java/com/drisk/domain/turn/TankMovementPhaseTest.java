package com.drisk.domain.turn;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.exceptions.SyntaxException;
import com.drisk.domain.game.GameManager;
import com.drisk.domain.game.Player;
import com.drisk.domain.lobby.LobbyManager;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.map.Territory;
import com.drisk.domain.turn.TankMovementPhase;
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
			LobbyManager.getInstance().joinGame("Player" + i);
		LobbyManager.getInstance().initGame();
		System.out.print(MapManager.getInstance().toJson().toString());
	}
	
	@Test
	public void moveTanksTest() {
		
		// Both territories have an initial tank
		Territory from = MapManager.getInstance().findTerritoryByName("china");
		Territory to = MapManager.getInstance().findTerritoryByName("japan");
		from.addTanks(4);
		to.addTanks(2);
		
		Player p = from.getOwner();
		JsonObject obj = new JsonObject();
		obj.addProperty("from", from.getName());
		obj.addProperty("to", to.getName());
		obj.addProperty("howMany", 3);
		System.out.print(obj.get("from").toString());
		try {
			new TankMovementPhase().playPhase(p, obj);
			fail();
		} catch (RequestNotValidException e) {
			e.printStackTrace();
		}
		
	}
	
	public void destroySingletons() {
		GameManager.destroy();
		LobbyManager.destroy();
	}
}
