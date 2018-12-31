package com.drisk.domain;

public class MissionCard implements Card {
	
	private int id;
	private String text;

	
	public MissionCard(int id, String text) {
		this.id = id;
		this.text = text;
	}
	
	
	public int getMission() {
		return id;
	}
	
	public String getMissionText() {
		return text;
	}
}
