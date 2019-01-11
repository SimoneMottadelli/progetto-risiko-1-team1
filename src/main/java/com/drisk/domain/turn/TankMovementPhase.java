package com.drisk.domain.turn;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.game.Player;
import com.drisk.domain.game.TankManager;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.map.Territory;
import com.google.gson.JsonObject;

public class TankMovementPhase extends Phase {
	
	private Territory from;
	private Territory to; 
	private int numOfTanks;
	private boolean moveDone;

	public TankMovementPhase() {
		super(PhaseEnum.TANKSMOVIMENT.getValue());
		moveDone = false;
	}

	@Override
	public void playPhase(Player currentPlayer, JsonObject obj) throws RequestNotValidException {
		if(moveDone)
			throw new RequestNotValidException("You have already moved your tanks");
		// player = currentPlayer; TODO riga da rimuovere?
		fromJson(obj);
		checkCondition();
		moveTanks();
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new TankAssignmentPhase());
	}

	private void moveTanks() {
		TankManager.getInstance().moveTanks(from, to, numOfTanks);
		moveDone = true;
	}
	
	@Override
	protected void checkCondition() throws RequestNotValidException {
		if (!from.getNeighbours().contains(to) || !from.getOwner().equals(to.getOwner())) 
			throw new RequestNotValidException("You can't move from " + from.getName() + " to " + to.getName());
		if(numOfTanks >= from.getNumberOfTanks())
			throw new RequestNotValidException("You can't move " + numOfTanks + " because you don't have enough tanks!" );
	}

	@Override
	public void fromJson(JsonObject obj) throws RequestNotValidException {
		Territory tFrom = MapManager.getInstance().findTerritoryByName(obj.get("from").getAsString().toLowerCase().replace("\"", ""));
		Territory tTo = MapManager.getInstance().findTerritoryByName(obj.get("to").getAsString().toLowerCase().replace("\"", ""));
		if(tFrom == null || tTo == null)
			throw new RequestNotValidException("Territories don't exist");
		from = tFrom;
		to = tTo;
		numOfTanks = obj.get("howMany").getAsInt();
	}
}
