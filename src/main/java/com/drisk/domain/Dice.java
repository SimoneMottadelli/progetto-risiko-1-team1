package com.drisk.domain;

import java.security.SecureRandom;
import java.util.Arrays; 
import java.util.Collections;

public class Dice {
	
	public int extractNumber() {
		return new SecureRandom().nextInt(6) + 1;
	}
	
	public int rollDices(int attackerTanks, int defenderTanks) {
		
		int attackerTanksLost = 0;
		int defenderTanksLost = 0;
		int maxNumberOfRolls = 0;
		
		if (attackerTanks >= defenderTanks) {
			maxNumberOfRolls = defenderTanks;
		} else {
			maxNumberOfRolls = attackerTanks;
		}
		
		Integer[] attackerDicesResults = new Integer[maxNumberOfRolls];
		Integer[] defenderDicesResults = new Integer[maxNumberOfRolls];
		
		for(int i = 0; i < maxNumberOfRolls; ++i) {
			attackerDicesResults[i] = extractNumber();
			defenderDicesResults[i] = extractNumber();
		}
		
		if (maxNumberOfRolls > 1) {
			Arrays.sort(attackerDicesResults, Collections.reverseOrder());
		}

		for(int i = 0; i < maxNumberOfRolls; ++i) {
			if (attackerDicesResults[i] > defenderDicesResults[i]) {
				defenderTanksLost++;
			} else {
				attackerTanksLost++;
			}
		}
		
		if (defenderTanksLost != 0) {
			
		}
		return 0;
		//to be continued
	
	}
	
}
