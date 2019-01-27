package com.drisk.domain;

import java.util.Arrays;
import java.util.Collections;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.google.gson.JsonObject;

public class AttackPhase extends Phase {

	private Territory territoryAttacker;
	private Player attacker;
	private Territory territoryDefender;
	private int attackerTanks;
	private boolean canDrawTerritoryCard;
	private static final String TANKS = " tanks";
	private static final String PLAYER = "Player ";

	public AttackPhase(Player player) {
		super(PhaseEnum.ATTACK.getValue());
		canDrawTerritoryCard = false;
		attacker = player;
	}

	@Override
	public void playPhase(JsonObject obj) throws RequestNotValidException {
		fromJson(obj);
		checkCondition();
		attackEnemyTerritory();
		if (!checkWin())
			if (checkLoss())
				addMessage(PLAYER + territoryDefender.getOwner().getColor() + " has lost");
			else
				addMessage(PLAYER + attacker.getColor() + " has won the game");
	}

	private boolean checkWin() {
		return GameManager.getInstance().checkWin(attacker);
	}

	private boolean checkLoss() {
		return GameManager.getInstance().checkLoss(territoryDefender.getOwner());
	}

	@Override
	public void nextPhase() {
		if (canDrawTerritoryCard)
			drawTerritoryCard();
		TurnManager.getInstance().setCurrentPhase(new TanksMovementPhase());
	}

	private void drawTerritoryCard() {
		CardManager.getInstance().giveTerritoryCard(attacker);
	}

	@Override
	public void checkCondition() throws RequestNotValidException {
		if (attackerTanks > territoryAttacker.getNumberOfTanks())
			throw new RequestNotValidException("You don't have " + attackerTanks + TANKS);
		if (attackerTanks == territoryAttacker.getNumberOfTanks())
			throw new RequestNotValidException(
					"You can't attack with " + attackerTanks + " because a tank must remain in your territory!");
		if (attacker.equals(territoryDefender.getOwner()))
			throw new RequestNotValidException("Come on! You can't attack yourself!");
		if (!territoryAttacker.getNeighbours().contains(territoryDefender))
			throw new RequestNotValidException("You can't attack from " + territoryAttacker.getName().toUpperCase()
					+ " to " + territoryDefender.getName().toUpperCase());
		if (attackerTanks <= 0)
			throw new RequestNotValidException("You must attack at least with one tanks");
		if (territoryAttacker.getNumberOfTanks() <= 1)
			throw new RequestNotValidException("You can't attack from this territory because there is only a tank");
	}

	private void attackEnemyTerritory() {

		int defenderTanks = territoryDefender.getNumberOfTanks();
		if (defenderTanks > 3)
			defenderTanks = 3;
		int maxNumberOfAttackerTanks = 3;
		if (attackerTanks < 3)
			maxNumberOfAttackerTanks = attackerTanks;

		int[] tanksToRemove = rollDices(maxNumberOfAttackerTanks, defenderTanks);

		TankManager tm = TankManager.getInstance();
		tm.removeTanks(territoryAttacker, tanksToRemove[0]);
		tm.removeTanks(territoryDefender, tanksToRemove[1]);
		addMessage(PLAYER + attacker.getColor() + " has attacked " + territoryDefender.getName().toUpperCase()
				+ " from " + territoryAttacker.getName().toUpperCase());
		addMessage(PLAYER + attacker.getColor() + " has lost " + tanksToRemove[0] + TANKS);
		addMessage(PLAYER + territoryDefender.getOwner().getColor() + " has lost " + tanksToRemove[1] + TANKS);

		if (territoryDefender.getNumberOfTanks() == 0) {
			territoryDefender.setOwner(attacker);
			tm.moveTanks(territoryAttacker, territoryDefender, attackerTanks - tanksToRemove[0]);
			canDrawTerritoryCard = true;
			addMessage(PLAYER + attacker.getColor() + " has conquered " + territoryDefender.getName().toUpperCase());
		}

	}

	private int[] rollDices(int attackerTanks, int defenderTanks) {

		Integer[] attackerDicesResults = new Integer[attackerTanks];
		Integer[] defenderDicesResults = new Integer[defenderTanks];
		Dice dice = new Dice();

		for (int i = 0; i < attackerDicesResults.length; ++i) {
			attackerDicesResults[i] = dice.extractNumber();
		}

		for (int i = 0; i < defenderDicesResults.length; ++i) {
			defenderDicesResults[i] = dice.extractNumber();
		}

		Arrays.sort(attackerDicesResults, Collections.reverseOrder());
		Arrays.sort(defenderDicesResults, Collections.reverseOrder());

		return compareDices(attackerDicesResults, defenderDicesResults);

	}

	private int[] compareDices(Integer[] attackerDicesResults, Integer[] defenderDicesResults) {

		int attackerTanksLost = 0;
		int defenderTanksLost = 0;

		int numIterations = Math.min(attackerDicesResults.length, defenderDicesResults.length);

		for (int i = 0; i < numIterations; ++i)
			if (attackerDicesResults[i] > defenderDicesResults[i])
				++defenderTanksLost;
			else
				++attackerTanksLost;

		int[] tanksLost = new int[2];
		tanksLost[0] = attackerTanksLost;
		tanksLost[1] = defenderTanksLost;

		return tanksLost;
	}

	@Override
	public void fromJson(JsonObject obj) throws RequestNotValidException {
		Territory from = MapManager.getInstance().getMap()
				.findTerritoryByName(obj.get("from").getAsString().toLowerCase().replace("\"", ""));
		Territory to = MapManager.getInstance().getMap()
				.findTerritoryByName(obj.get("to").getAsString().toLowerCase().replace("\"", ""));
		if (from == null || to == null)
			throw new RequestNotValidException("Territories don't exist");
		territoryAttacker = from;
		territoryDefender = to;
		attackerTanks = obj.get("howMany").getAsInt();
	}

}
