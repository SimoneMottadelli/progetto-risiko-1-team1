package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class Continent {
	
	private List<Territory> territories;
	
	
	public Continent() {
		this.territories = new LinkedList<>();
	}
	
	
	public Continent(List<Territory> territories) {
		this();
		setTerritories(territories);
	}

	
	public List<Territory> getTerritories() {
		return territories;
	}

	
	public void setTerritories(List<Territory> territories) {
		this.territories = territories;
	}
	
	
	
}
