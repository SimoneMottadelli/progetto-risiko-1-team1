package com.drisk.domain;

import java.util.ArrayList;
import java.util.List;

public class Map {

	private List<Continent> continents;
	private Map map;
	
	private Map() {
		continents = new ArrayList<>();
	}
	
	public Map getInstance() {
		if (map == null)
			map = new Map();
		return map;
	}

	
	public List<Continent> getContinents() {
		return continents;
	}

	
	public void setContinents(List<Continent> continents) {
		this.continents = continents;
	}
	
}
