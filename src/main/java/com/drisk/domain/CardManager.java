package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class CardManager {

	private List<Card> missionCards;
	
	
	public CardManager() {
		missionCards = new LinkedList<>();
	}
	
	
	public CardManager(List<Card> missionCards) {
		this();
		setMissionCards(missionCards);
	}


	public List<Card> getMissionCards() {
		return missionCards;
	}


	public void setMissionCards(List<Card> missionCards) {
		this.missionCards = missionCards;
	}
	
}
