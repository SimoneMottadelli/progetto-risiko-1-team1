package com.drisk.domain;

import java.util.List;

public class ConquestContinentMissionCard implements MissionCard {
	
	private String text;
	private Continent[] toConquest;
	
	public ConquestContinentMissionCard(Continent c1, Continent c2) {
		super();
		this.text = "Conquer " + c1.getName() + " and " + c2.getName();
		toConquest = new Continent[2];
		toConquest[0] = c1;
		toConquest[1] = c2;
	}

	
	public String getText() {
		return text;
	}

	public Continent[] getToBeConquered() {
		return toConquest;
	}

	@Override
	public boolean checkWin() {
		Player current = TurnManager.getInstance().getCurrentPlayer();
		List<Territory> playerTerritories = current.getTerritoriesOwned();
		
		for(Continent c: toConquest) {
			if(!playerTerritories.containsAll(c.getTerritories()))
				return false;
		}
		return true;
	}

}
