package com.drisk.domain.map;


import java.util.List;
import java.util.LinkedList;

import com.drisk.domain.game.Player;
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
	
	private Player getOwner() {
		Player p = territories.get(0).getOwner();
		for(Territory t : territories)
			if(!t.getOwner().equals(p))
				return null;
		return p;
	}
	
	public JsonObject toJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", name);
		Player owner = getOwner();
		if(owner == null)
			obj.addProperty("owner", "onOne");
		else
			obj.addProperty("owner", owner.getColor().toString());
		return obj;
	}
	
}
