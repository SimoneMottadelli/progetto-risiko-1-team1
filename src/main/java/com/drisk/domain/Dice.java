package com.drisk.domain;

import java.security.SecureRandom;

public class Dice {
	
	// rollDice() returns a number between 1 and 6 randomly
	public int rollDice() {
		return new SecureRandom().nextInt(6) + 1;
	}
	
}
