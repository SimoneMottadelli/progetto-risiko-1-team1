package com.drisk.domain;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.google.gson.JsonObject;

public class TankPlacementPhase extends Phase {
	
	private Territory where;
	private int howManyTanks;

	public TankPlacementPhase() {
		super(PhaseEnum.TANKPLACEMENT.getValue());
	}

	@Override
	public void playPhase(Player currentPlayer, JsonObject obj) throws RequestNotValidException {
		fromJson(obj);
		placeTanks(currentPlayer);
		
	}
	
	private void placeTanks(Player currentPlayer) throws RequestNotValidException{
		if(currentPlayer.getAvailableTanks() < howManyTanks)
			throw new RequestNotValidException("You don't have " + howManyTanks + " tanks to place");
		TankManager.getInstance().placeTanks(where, howManyTanks);
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AttackPhase());
	}

	@Override
	public void fromJson(JsonObject obj) throws RequestNotValidException {
		Territory t = MapManager.getInstance().findTerritoryByName(obj.get("territory").toString().toLowerCase());
		if(t == null)
			throw new RequestNotValidException("Territory name doesn't exist");
		where = t;
		howManyTanks = obj.get("numOfTanks").getAsInt();
	}

}
