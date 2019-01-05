package com.drisk.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AttackPhase implements Phase {

	@Override
	public void startPhase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new TankMovementPhase());
	}

	public void attackEnemyTerritory(Territory territoryAttacker, Territory territoryDefender, int attackerTanks) {

		Player attacker = TurnManager.getInstance().getCurrentPlayer();
		Player defender = territoryDefender.findPlayer();

			int defenderTanks = territoryDefender.getNumberOfTanks();
			if (defenderTanks > 3) {
				defenderTanks = 3;
			}
			
			Integer[] dicesResult = new Integer[2];
			territoryAttacker.removeNumberOfTanks(dicesResult[0]);
			territoryDefender.removeNumberOfTanks(dicesResult[1]);

			if (territoryDefender.getNumberOfTanks() == 0) {
				TankManager tm = TankManager.getInstance();
				defender.removeTerritoryOwned(territoryDefender);
				attacker.addTerritoryOwned(territoryDefender);
				tm.placeTanks(territoryDefender, 1);
				tm.removeTanks(territoryAttacker, 1);
			}
		
	}
	
	public int[] rollDices(int attackerTanks, int defenderTanks) {

		Integer[] attackerDicesResults = new Integer[attackerTanks];
		Integer[] defenderDicesResults = new Integer[defenderTanks];
		Dice dice = new Dice();
		
		for(int i = 0; i < attackerDicesResults.length; ++i) {
			attackerDicesResults[i] = dice.extractNumber();
		}
		
		for(int i = 0; i < defenderDicesResults.length; ++i) {
			defenderDicesResults[i] = dice.extractNumber();
		}

		Arrays.sort(attackerDicesResults, Collections.reverseOrder());
		Arrays.sort(defenderDicesResults, Collections.reverseOrder());
		
		List<Integer[]> results = new LinkedList<>();
		results.add(attackerDicesResults);
		results.add(defenderDicesResults);
		return compareDices(results);
	
	}
	
	public int[] compareDices(List<Integer[]> results) {
		
		Integer[] attackerDicesResults = results.get(0);
		Integer[] defenderDicesResults = results.get(1);
		int attackerTanksLost = 0;
		int defenderTanksLost = 0;
		
		int numIterations = Math.min(attackerDicesResults.length, defenderDicesResults.length);
		
		for(int i = 0; i < numIterations; ++i) {
			if (attackerDicesResults[i] > defenderDicesResults[i]) {
				defenderTanksLost++;
			} else {
				attackerTanksLost++;
			}
		}
		
		int[] tanksLost = new int[2];
		tanksLost[0] = attackerTanksLost;
		tanksLost[1] = defenderTanksLost;
		
		return tanksLost;
	}
	
}
