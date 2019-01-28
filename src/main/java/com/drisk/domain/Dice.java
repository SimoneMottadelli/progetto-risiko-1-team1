package com.drisk.domain;

import java.security.SecureRandom;

public class Dice {
	public int extractNumber() {
		return new SecureRandom().nextInt(6) + 1;
	}
}
