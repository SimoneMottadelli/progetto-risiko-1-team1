package com.drisk.domain;

import java.util.Arrays;
import java.util.Collections;
import com.google.gson.JsonObject;

public class AttackPhase extends Phase {

	private Territory territoryAttacker;
	private Territory territoryDefender; 
	private int attackerTanks;
	
	public AttackPhase() {
		super(PhaseEnum.ATTACK.getValue());
	}

	@Override
	public void playPhase(Player currentPlayer, JsonObject obj) {
		fromJson(obj);
		attackEnemyTerritory(currentPlayer);
	}

	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new TankMovementPhase());
	}

	public void attackEnemyTerritory(Player attacker) {
		if(attackerTanks < territoryAttacker.getNumberOfTanks() || attacker.equals(territoryDefender.getOwner())) {
			
			// TODO throws exception
			//lancia una eccezione!
			
			
			int defenderTanks = territoryDefender.getNumberOfTanks();
			if (defenderTanks > 3) 
				defenderTanks = 3;
			int maxNumberOfAttackerTanks = 3;
			if(attackerTanks < 3)
				maxNumberOfAttackerTanks = attackerTanks;
			int[] tanksToRemove = rollDices(maxNumberOfAttackerTanks, defenderTanks);
			TankManager tm = TankManager.getInstance();
			tm.removeTanks(territoryAttacker, tanksToRemove[0]);
			tm.removeTanks(territoryDefender, tanksToRemove[1]);
			
			if (territoryDefender.getNumberOfTanks() == 0) {
				territoryDefender.setOwner(attacker);
				tm.placeTanks(territoryDefender, 1);
				tm.removeTanks(territoryAttacker, 1);
			}	
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
		
		return compareDices(attackerDicesResults, defenderDicesResults);
	
	}
	
	public int[] compareDices(Integer[] attackerDicesResults, Integer[] defenderDicesResults) {
		
		int attackerTanksLost = 0;
		int defenderTanksLost = 0;
		
		int numIterations = Math.min(attackerDicesResults.length, defenderDicesResults.length);
		
		for(int i = 0; i < numIterations; ++i)
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
	public void fromJson(JsonObject obj) {
		// TODO Auto-generated method stub
	}
	
}
