package com.drisk.domain.turn;

public enum PhaseEnum {
	TANKASSIGNMENT(1),
	TANKPLACEMENT(2),
	ATTACK(3),
	TANKSMOVEMENT(4);
	
	private final int value;

    private PhaseEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
