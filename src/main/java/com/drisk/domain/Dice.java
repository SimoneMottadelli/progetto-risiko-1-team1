package com.drisk.domain;

import java.util.Random;

public class Dice {
	
	// rollDice() returns a number between 1 and 6 randomly
	public int rollDice() {
		return new Random().nextInt(6);
	}
	
}
