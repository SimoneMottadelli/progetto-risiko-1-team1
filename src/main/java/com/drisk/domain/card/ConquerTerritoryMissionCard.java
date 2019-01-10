package com.drisk.domain.card;

import com.drisk.domain.game.Player;
import com.drisk.domain.map.MapManager;

public class ConquerTerritoryMissionCard extends MissionCard {
	
	private int toConquer;

	public ConquerTerritoryMissionCard(int toConquer) {
		super("Occupy " + toConquer + " territories");
		this.toConquer = toConquer;
	}

	public int getToConquer() {
		return toConquer;
	}

	@Override
	public boolean isAchievementReached(Player player) {
		return MapManager.getInstance().getMapTerritories(player).size() >= toConquer;
	}
	
}
