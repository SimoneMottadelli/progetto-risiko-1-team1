package com.drisk.domain;

import java.security.SecureRandom;
import java.util.Arrays; 
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Dice {
	
	public int extractNumber() {
		return new SecureRandom().nextInt(6) + 1;
	}
	
	public int[] rollDices(int attackerTanks, int defenderTanks) {

		Integer[] attackerDicesResults = new Integer[attackerTanks];
		Integer[] defenderDicesResults = new Integer[defenderTanks];
		
		for(int i = 0; i < attackerDicesResults.length; ++i) {
			attackerDicesResults[i] = extractNumber();
		}
		
		for(int i = 0; i < defenderDicesResults.length; ++i) {
			defenderDicesResults[i] = extractNumber();
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
