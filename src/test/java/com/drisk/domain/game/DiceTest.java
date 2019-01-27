package com.drisk.domain.game;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.drisk.domain.Dice;

public class DiceTest {

	@Test
	public void ExtractNumberTest() {
		Dice dice = new Dice();
		int n;
		for(int i = 0; i < 100; i++) {
			n = dice.extractNumber();
			if (n < 1 || n > 6)
				fail("The result of the dice is " + n);
		}
	}
	
}
