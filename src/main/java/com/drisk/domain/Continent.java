package com.drisk.domain;


import java.util.List;
import java.util.LinkedList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Continent {
	
	private String name;
	private List<Territory> territories;
	
	
	public Continent(String name) {
		this.name = name;
		this.territories = new LinkedList<>();
	}
	
	public String getName() {
		return name;
	}


	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Continent other = (Continent) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} 
		else if (!name.equals(other.name))
			return false;	
		
		return true;
	}


	public List<Territory> getTerritories() {
		return territories;
	}
	
	public void addTerritory(Territory territory) {
		if(!territories.contains(territory))
			territories.add(territory);
	}
	
	public JsonObject toJson() {
		JsonObject jsonContinent = new JsonObject();
		jsonContinent.addProperty("name", name);
		JsonArray arrayTerritories = new JsonArray();
		for(Territory t : territories)
			arrayTerritories.add(t.toJson());
		jsonContinent.add("territories", arrayTerritories);
		return jsonContinent;
	}

	
}
