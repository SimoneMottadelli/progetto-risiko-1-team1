package com.drisk.domain.card;

import com.drisk.domain.game.Player;
import com.drisk.domain.map.MapManager;

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
