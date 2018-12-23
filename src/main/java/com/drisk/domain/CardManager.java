package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class CardManager {

	private List<Card> missionCards;
	private CardManager instance;
	
	
	private CardManager() {
		missionCards = new LinkedList<>();
	}
	
	
	public CardManager getInstance() {
		if (instance == null)
			instance = new CardManager();
		return instance;
	}
	
	
	public List<Card> getMissionCards() {
		return missionCards;
	}
	
}
