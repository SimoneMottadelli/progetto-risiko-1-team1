package com.drisk.domain.map;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Map {
	
	private static final String TERRITORIES = "territories";
	
	private DifficultyEnum difficulty;
	private List<Continent> continents;
	private boolean ready;
	
	public Map() {
		setReady(false);
		continents = new LinkedList<>();
	}

	public DifficultyEnum getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = DifficultyEnum.valueOf(difficulty.toUpperCase());
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
	
	/**
	 * Used to get json representation of the map, with continents, territories and other
	 * @return JsonObject with json representation of the map
	 */
	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		result.addProperty("difficulty", difficulty.toString());
		result.add("continents", continentsToJson());
		result.add(TERRITORIES, territoriesToJson());
		result.add("membership", membershipToJson());
		result.add("neighbourhood", neighbourhoodToJson());
		return result;
	}
	
	/**
	 * Used to get json representation of continents
	 * @return JsonArray with json representation of the continent
	 */
	private JsonArray continentsToJson() {
		JsonArray continentsArray = new JsonArray();
		for(Continent c : continents)
			continentsArray.add(c.toJson());
		return continentsArray;
	}
	
	/**
	 * Used to get json representation of membership, that is the relation between territories and continents
	 * @return JsonArray with representation of the membership
	 */
	private JsonArray membershipToJson() {
		JsonArray membershipArray = new JsonArray();
		for(Continent c : continents) {
			JsonObject obj = new JsonObject();
			obj.addProperty("name", c.getName());
			JsonArray terrArray = new JsonArray();
			for(Territory t : c.getTerritories())
				terrArray.add(t.getName());
			obj.add(TERRITORIES, terrArray);
			membershipArray.add(obj);
		}
		return membershipArray;
	}
	
	/**
	 * Used to get json representation of territories
	 * @return JsonArray with representation of the territories
	 */
	private JsonArray territoriesToJson() {
		JsonArray territoriesArray = new JsonArray();
		for(Territory t : getTerritories())
			territoriesArray.add(t.toJson());
		return territoriesArray;
	}
	
	/**
	 * Used to get json representation of neighbourhood, that is the relation between territories neighbour
	 * @return JsonArray with representation of the neighbourhood of territories
	 */
	private JsonArray neighbourhoodToJson() {
		JsonArray neighbourhoodArray = new JsonArray();
		for(Territory t : getTerritories()) {
			JsonObject obj = new JsonObject();
			obj.addProperty("mame", t.getName());
			JsonArray neighboursArray = new JsonArray();
			for(Territory neighbour : t.getNeighbours())
				neighboursArray.add(neighbour.getName());
			obj.add(TERRITORIES, neighboursArray);
			neighbourhoodArray.add(obj);
		}
		return neighbourhoodArray;
	}
}
