package com.drisk.domain;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.google.gson.JsonObject;

public class TanksMovementPhase extends Phase {

	private Territory from;
	private Territory to;
	private int numOfTanks;
	private boolean moveDone;

	public TanksMovementPhase() {
		super(PhaseEnum.TANKSMOVEMENT.getValue());
		moveDone = false;
	}

	@Override
	public void playPhase(JsonObject obj) throws RequestNotValidException {
		if (moveDone)
			throw new RequestNotValidException("You have already moved your tanks");
		fromJson(obj);
		checkCondition();
		moveTanks();
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new TanksAssignmentPhase(TurnManager.getInstance().nextPlayer()));
	}

	private void moveTanks() {
		TankManager.getInstance().moveTanks(from, to, numOfTanks);
		addMessage("Player " + from.getOwner().getColor() + " has moved " + numOfTanks + " tanks from " + from.getName().toUpperCase()
				+ " to " + to.getName().toUpperCase());
		moveDone = true;
	}

	@Override
	public void checkCondition() throws RequestNotValidException {
		if (!from.getNeighbours().contains(to) || !from.getOwner().equals(to.getOwner()))
			throw new RequestNotValidException("You can't move from " + from.getName().toUpperCase() + " to " + to.getName().toUpperCase());
		if (numOfTanks >= from.getNumberOfTanks())
			throw new RequestNotValidException(
					"You can't move " + numOfTanks + " because you don't have enough tanks!");
	}

	@Override
	public void fromJson(JsonObject obj) throws RequestNotValidException {
		Territory tFrom = MapManager.getInstance()
				.getMap().findTerritoryByName(obj.get("from").getAsString().toLowerCase().replace("\"", ""));
		Territory tTo = MapManager.getInstance()
				.getMap().findTerritoryByName(obj.get("to").getAsString().toLowerCase().replace("\"", ""));
		if (tFrom == null || tTo == null)
			throw new RequestNotValidException("Territories don't exist");
		from = tFrom;
		to = tTo;
		numOfTanks = obj.get("howMany").getAsInt();
	}
}
