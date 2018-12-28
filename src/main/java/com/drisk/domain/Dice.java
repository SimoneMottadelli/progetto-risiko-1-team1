package com.drisk.domain;

import java.security.SecureRandom;

public class Dice {
	
	// generateNumber() returns a number between 1 and 6 randomly
	private int generateNumber() {
		return new SecureRandom().nextInt(6) + 1;
	}
	
	public int rollDice() {
		return -1; //da implementare TODO
	}
	
}
