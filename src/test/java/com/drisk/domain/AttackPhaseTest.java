package com.drisk.domain;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class AttackPhaseTest {

	@Test
	public void compareDices() {
		
		Integer[] resultAttacker_1 = {6, 4, 2};
		Integer[] resultDefender_1 = {5, 3, 1};
		List<Integer[]> results = new LinkedList<>();
		results.add(resultAttacker_1);
		results.add(resultDefender_1);
		int[] tanksLost = new AttackPhase().compareDices(results);
		assertEquals(0, tanksLost[0]);
		assertEquals(3, tanksLost[1]);
		
		results.remove(resultDefender_1);
		Integer[] resultDefender_2 = {5, 5, 2};
		results.add(resultDefender_2);
		tanksLost = new AttackPhase().compareDices(results);
		assertEquals(2, tanksLost[0]);
		assertEquals(1, tanksLost[1]);
		
		results.remove(resultDefender_2);
		Integer[] resultDefender_3 = {5, 4};
		results.add(resultDefender_3);
		tanksLost = new AttackPhase().compareDices(results);
		assertEquals(1, tanksLost[0]);
		assertEquals(1, tanksLost[1]);
		
		results.remove(resultAttacker_1);
		results.remove(resultDefender_3);
		Integer[] resultAttacker_2 = {3, 2};
		Integer[] resultDefender_4 = {5, 1, 1};
		results.add(resultAttacker_2);
		results.add(resultDefender_4);
		tanksLost = new AttackPhase().compareDices(results);
		assertEquals(1, tanksLost[0]);
		assertEquals(1, tanksLost[1]);
		
	}
	
}
