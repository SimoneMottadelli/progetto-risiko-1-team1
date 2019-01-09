package com.drisk.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

public class AttackPhaseTest {

	@Test
	public void compareDices() {
		
		// first attempt
		Integer[] resultAttacker_1 = {6, 4, 2};
		Integer[] resultDefender_1 = {5, 3, 1};
		int[] tanksLost = new AttackPhase().compareDices(resultAttacker_1, resultDefender_1);
		assertEquals(0, tanksLost[0]);
		assertEquals(3, tanksLost[1]);
		
		// second attempt
		Integer[] resultDefender_2 = {5, 5, 2};
		tanksLost = new AttackPhase().compareDices(resultAttacker_1, resultDefender_2);
		assertEquals(2, tanksLost[0]);
		assertEquals(1, tanksLost[1]);
		
		// third attempt
		Integer[] resultDefender_3 = {5, 4};
		tanksLost = new AttackPhase().compareDices(resultAttacker_1, resultDefender_3);
		assertEquals(1, tanksLost[0]);
		assertEquals(1, tanksLost[1]);
		
		Integer[] resultAttacker_2 = {3, 2};
		Integer[] resultDefender_4 = {5, 1, 1};
		tanksLost = new AttackPhase().compareDices(resultAttacker_2, resultDefender_4);
		assertEquals(1, tanksLost[0]);
		assertEquals(1, tanksLost[1]);
		
	}
	
	@After
	public void destroySingletons() {
		GameManager.destroy();
	}
}
