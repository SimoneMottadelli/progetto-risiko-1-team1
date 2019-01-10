package com.drisk.domain;

import com.drisk.domain.exceptions.RequestNotValidException;
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
		fromJson(obj);
		moveTanks();
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new TankAssignmentPhase());
	}

	private void moveTanks() throws RequestNotValidException {
		checkCondition();
		TankManager.getInstance().moveTanks(from, to, numOfTanks);
		moveDone = true;
	}
	
	private void checkCondition() throws RequestNotValidException {
		if (!from.getNeighbours().contains(to) || !from.getOwner().equals(to.getOwner())) 
			throw new RequestNotValidException("You can't move from " + from.getName() + " to " + to.getName());
		if(numOfTanks >= from.getNumberOfTanks())
			throw new RequestNotValidException("You can't move " + numOfTanks + " because you don't have enough tanks!" );
	}

	@Override
	public void fromJson(JsonObject obj) throws RequestNotValidException {
		Territory from = MapManager.getInstance().findTerritoryByName(obj.get("from").getAsString().toLowerCase().replace("\"", ""));
		Territory to = MapManager.getInstance().findTerritoryByName(obj.get("to").getAsString().toLowerCase().replace("\"", ""));
		if(from == null || to == null)
			throw new RequestNotValidException("Territories don't exist");
		this.from = from;
		this.to = to;
		numOfTanks = obj.get("howMany").getAsInt();
	}
}
