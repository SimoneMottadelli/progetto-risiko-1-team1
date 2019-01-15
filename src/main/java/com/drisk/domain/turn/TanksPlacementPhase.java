package com.drisk.domain.turn;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.game.Player;
import com.drisk.domain.game.TankManager;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.map.Territory;
import com.google.gson.JsonObject;

public class TanksPlacementPhase extends Phase {
	
	private Territory where;
	private int howManyTanks;
	private Player player;

	public TanksPlacementPhase(Player player) {
		super(PhaseEnum.TANKPLACEMENT.getValue());
		this.player = player;
	}

	@Override
	public void playPhase(JsonObject obj) throws RequestNotValidException {
		fromJson(obj);
		checkCondition();
		placeTanks();
	}
	
	private void placeTanks() {
		TankManager.getInstance().placeTanks(where, howManyTanks);
		if (howManyTanks > 0)
			addMessage("Player " + player.getColor() + " has placed " + howManyTanks + " tanks in " + where.getName().toUpperCase());
	}

	@Override
	public void nextPhase() throws RequestNotValidException{
		if(player.getAvailableTanks() > 0)
			throw new RequestNotValidException("You must place another " + player.getAvailableTanks() + " tanks");
		TurnManager.getInstance().setCurrentPhase(new AttackPhase(player));
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
		if (!where.getOwner().equals(player))
			throw new RequestNotValidException(where.getName().toUpperCase() + " is not yours");
		if(player.getAvailableTanks() < howManyTanks)
			throw new RequestNotValidException("You don't have " + howManyTanks + " tanks to place");
	}

}
