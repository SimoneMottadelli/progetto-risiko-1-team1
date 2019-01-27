package com.drisk.domain;

public class ConquerContinentMissionCard extends MissionCard {
	
	private Continent[] toConquer;
	
	public ConquerContinentMissionCard(Continent c1, Continent c2) {
		super("Conquer " + c1.getName() + " and " + c2.getName());
		toConquer = new Continent[] {c1, c2};
	}

	public Continent[] getContinentsToConquer() {
		return toConquer;
	}

	@Override
	public boolean isAchievementReached(Player player) {
		for(Continent c: toConquer)
			if(!MapManager.getInstance().getPlayerTerritories(player).containsAll(c.getTerritories()))
				return false;
		return true;
	}

	
}
