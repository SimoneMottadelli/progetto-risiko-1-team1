package com.drisk.domain.turn;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.game.Player;
import com.drisk.domain.game.TankManager;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.map.Territory;
import com.google.gson.JsonObject;

public class TankPlacementPhase extends Phase {
	
	private Territory where;
	private int howManyTanks;
	private Player player;

	public TankPlacementPhase() {
		super(PhaseEnum.TANKPLACEMENT.getValue());
	}

	@Override
	public void playPhase(Player currentPlayer, JsonObject obj) throws RequestNotValidException {
		player = currentPlayer;
		fromJson(obj);
		placeTanks();
		
	}
	
	private void placeTanks() throws RequestNotValidException{
		if(player.getAvailableTanks() < howManyTanks)
			throw new RequestNotValidException("You don't have " + howManyTanks + " tanks to place");
		TankManager.getInstance().placeTanks(where, howManyTanks);
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AttackPhase());
	}

	@Override
	public void fromJson(JsonObject obj) throws RequestNotValidException {
		Territory t = MapManager.getInstance().findTerritoryByName(obj.get("territory").toString().toLowerCase().replace("\"", ""));
		if(t == null)
			throw new RequestNotValidException("Territory name doesn't exist");
		where = t;
		howManyTanks = obj.get("numOfTanks").getAsInt();
	}

	@Override
	protected void checkCondition() throws RequestNotValidException {
		// TODO Auto-generated method stub
		
	}

}
