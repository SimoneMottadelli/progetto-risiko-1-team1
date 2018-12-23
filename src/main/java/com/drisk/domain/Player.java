package com.drisk.domain;

import java.util.List;

public class Player {
	
	private Color color;
	private List<TerritoryCard> hand;
	private MissionCard mission;
	
	public Player(Color color) {
		setColor(color);
	}
		
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
}
