package com.drisk.domain;

public class Player {
	
	private Color color;
	
	
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
