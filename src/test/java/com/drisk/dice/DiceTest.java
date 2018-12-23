package com.drisk.dice;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.drisk.domain.Dice;

public class DiceTest {

	@Test
	public void rollDiceTest() {
		Dice dice = new Dice();
		int n;
		for(int i = 0; i < 100; i++) {
			n = dice.rollDice();
			assertTrue(n >= 1 && n <= 6);
		}
	}
	
}
