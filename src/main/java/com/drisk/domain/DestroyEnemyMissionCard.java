package com.drisk.domain;

public class DestroyEnemyMissionCard implements MissionCard {
	
	private String text;
	private Player enemy;
	
	public DestroyEnemyMissionCard(Player enemy) {
		super();
		this.text = "Destroy the " + enemy.getColor() + " army";
		this.enemy = enemy;
	}

	public String getText() {
		return text;
	}

	public Player getEnemy() {
		return enemy;
	}

	@Override
	public boolean checkWin() {
		return enemy.getNumberOfTerritoriesOwned() == 0;
	}

}
