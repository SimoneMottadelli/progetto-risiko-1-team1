package com.drisk.domain;

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
		return MapManager.getInstance().getPlayerTerritories(player).size() >= toConquer;
	}
	
}
