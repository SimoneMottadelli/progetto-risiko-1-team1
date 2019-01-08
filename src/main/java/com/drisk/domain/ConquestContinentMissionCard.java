package com.drisk.domain;

import java.util.List;

public class ConquestContinentMissionCard implements MissionCard {
	
	private String text;
	private Continent[] toConquer;
	
	public ConquestContinentMissionCard(Continent c1, Continent c2) {
		text = "Conquer " + c1.getName() + " and " + c2.getName();
		toConquer = new Continent[] {c1, c2};
	}

	public String getText() {
		return text;
	}

	public Continent[] getContinentsToConquer() {
		return toConquer;
	}

	/* TODO da spostare 
	@Override
	public boolean checkWin() {
		Player current = TurnManager.getInstance().getCurrentPlayer();
		List<Territory> playerTerritories = current.getTerritoriesOwned();
		
		for(Continent c: toConquer) {
			if(!playerTerritories.containsAll(c.getTerritories()))
				return false;
		}
		return true;
	}
	*/
}
