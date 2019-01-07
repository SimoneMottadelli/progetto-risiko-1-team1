package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

import com.drisk.technicalservice.JsonHelper;
import com.google.gson.JsonObject;

public class Map {
	
	private Difficulty difficulty;
	private List<Continent> continents;
	private boolean ready;
	
	public Map() {
		setReady(false);
		continents = new LinkedList<>();
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = Difficulty.valueOf(difficulty);
	}

	public List<Continent> getContinents() {
		return continents;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public void addContinent(Continent continent) {
		if(!continents.contains(continent))
			continents.add(continent);
	}
	
	public List<Territory> getTerritories() {	
		List<Territory> territories = new LinkedList<>();
		for(Continent c : continents)
			territories.addAll(c.getTerritories());
		return territories;
	}
	
	public JsonObject toJson() {
		return new JsonHelper().mapToJson(difficulty.toString(), continents, getTerritories());
	}
	
	public int getNumberOfTerritories() {
		return getTerritories().size();
	}

}
