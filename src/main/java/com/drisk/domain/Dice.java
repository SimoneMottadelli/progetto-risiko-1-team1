package com.drisk.domain;

public class Dice {
	
	// rollDice() returns a number between 1 and 6 randomly
	public int rollDice() {
		return (int) ((Math.random() * 6) + 1);
	}
	
}
