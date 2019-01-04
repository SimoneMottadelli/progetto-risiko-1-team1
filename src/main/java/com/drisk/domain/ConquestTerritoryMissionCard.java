package com.drisk.domain;

public class ConquestTerritoryMissionCard implements MissionCard {
	
	private String text;
	private int toConquest;

	public ConquestTerritoryMissionCard(int toConquest) {
		super();
		this.text = "Occupy " + toConquest + " territories";
		this.toConquest = toConquest;
	}

	public String getText() {
		return text;
	}


	public int getToConquest() {
		return toConquest;
	}


	@Override
	public boolean checkWin() {
		return TurnManager.getInstance().getCurrentPlayer().getNumberOfTerritoriesOwned() >= toConquest;
	}

}
