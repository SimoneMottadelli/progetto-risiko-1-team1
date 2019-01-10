package com.drisk.domain.turn;

import java.security.SecureRandom;

public class Dice {
	
	public int extractNumber() {
		return new SecureRandom().nextInt(6) + 1;
	}
	
}
