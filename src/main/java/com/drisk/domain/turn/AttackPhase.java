package com.drisk.domain.turn;

import java.util.Arrays;
import java.util.Collections;

import com.drisk.domain.card.CardManager;
import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.game.GameManager;
import com.drisk.domain.game.Player;
import com.drisk.domain.game.TankManager;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.map.Territory;
import com.google.gson.JsonObject;

public class AttackPhase extends Phase {

	private Territory territoryAttacker;
	private Player attacker;
	private Territory territoryDefender;
	private int attackerTanks;
	private boolean canDrawTerritoryCard;

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
			checkLoss();
	}

	private boolean checkWin() {
		return GameManager.getInstance().checkWin(attacker);
	}

	private void checkLoss() {
		GameManager.getInstance().checkLoss(territoryDefender.getOwner());
	}

	@Override
	public void nextPhase() {
		if (canDrawTerritoryCard)
			drawTerritoryCard();
		TurnManager.getInstance().setCurrentPhase(new TankMovementPhase());
	}

	private void drawTerritoryCard() {
		CardManager.getInstance().giveTerritoryCard(attacker);
	}

	@Override
	protected void checkCondition() throws RequestNotValidException {
		if (attackerTanks >= territoryAttacker.getNumberOfTanks())
			throw new RequestNotValidException(
					"You can't attack with " + attackerTanks + "because you don't have enough tanks!");
		if (attacker.equals(territoryDefender.getOwner()))
			throw new RequestNotValidException("Come on! You can't attack yourself!");
		if (!territoryAttacker.getNeighbours().contains(territoryDefender))
			throw new RequestNotValidException(
					"You can't attack from " + territoryAttacker.getName() + " to " + territoryDefender.getName());
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
		addMessage("Player " + attacker.getColor() + " has attacked " + territoryDefender.getName() + " from "
				+ territoryAttacker.getName());
		addMessage("Player " + attacker.getColor() + " has lost " + tanksToRemove[0] + " tanks");
		addMessage("Player " + territoryDefender.getOwner().getColor() + " has lost " + tanksToRemove[1] + " tanks");
		if (territoryDefender.getNumberOfTanks() == 0) {
			territoryDefender.setOwner(attacker);
			tm.placeTanks(territoryDefender, 1);
			tm.removeTanks(territoryAttacker, 1);
			canDrawTerritoryCard = true;
			addMessage("Player " + attacker.getColor() + " has conquered " + territoryDefender.getName());
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
		Territory from = MapManager.getInstance()
				.findTerritoryByName(obj.get("from").getAsString().toLowerCase().replace("\"", ""));
		Territory to = MapManager.getInstance()
				.findTerritoryByName(obj.get("to").getAsString().toLowerCase().replace("\"", ""));
		if (from == null || to == null)
			throw new RequestNotValidException("Territories don't exist");
		territoryAttacker = from;
		territoryDefender = to;
		attackerTanks = obj.get("howMany").getAsInt();
	}

}
