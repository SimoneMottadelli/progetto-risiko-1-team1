package com.drisk.domain;

public abstract class MissionCard extends Card {
	
	private String text;
	
	public MissionCard(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public abstract boolean isAchievementReached(Player player);
	
}
