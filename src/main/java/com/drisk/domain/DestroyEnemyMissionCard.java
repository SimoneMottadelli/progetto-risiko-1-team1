package com.drisk.domain;

public class DestroyEnemyMissionCard extends MissionCard {
	
	private Player enemy;
	
	public DestroyEnemyMissionCard(Player enemy) {
		super("Destroy the " + enemy.getColor() + " army");
		this.enemy = enemy;
	}

	public Player getEnemy() {
		return enemy;
	}

	@Override
	public boolean isAchievementReached(Player player) {
		return MapManager.getInstance().getMapTerritories(enemy).isEmpty();
	}

}
