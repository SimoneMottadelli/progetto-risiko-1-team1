package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Territory {

	private String name;
	private int numberOfTanks;
	private List<Territory> neighbours;
	
	
	public Territory(String name) {
		this.name = name;
		neighbours = new LinkedList<>();
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Territory other = (Territory) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} 
		else if (!name.equals(other.name))
			return false;		
		
		return true;
	}
	
	
	
	@Override
	public String toString() {
		return "[name= " + name + "]";
	}


	public void addNeighbour(Territory territory) {
		if(!neighbours.contains(territory))
			neighbours.add(territory);
	}


	public int getNumberOfTanks() {
		return numberOfTanks;
	}

	public Player findPlayer() {
		List<Player> players = MatchManager.getInstance().getPlayers();
		for (Player p: players) {
			if (p.getTerritoriesOwned().contains(this)) {
				return p;
			}
		}
		return null;
	}
	
	public void addNumberOfTanks(int numberOfTanks) {
		this.numberOfTanks += numberOfTanks;
	}
	
	public void removeNumberOfTanks(int numberOfTanks) {
		int difference = this.numberOfTanks - numberOfTanks;
		if (difference >= 0) {
			this.numberOfTanks = difference;
		} else {
			this.numberOfTanks = 0;
		}
	}

	public String getName() {
		return name;
	}
	
	
	public List<Territory> getNeighbours() {
		return neighbours;
	}
	
	
	public void addTerritory(Territory territory) {
		if(!neighbours.contains(territory))
			neighbours.add(territory);
	}
	
	public JsonObject toJson() {
		JsonObject jsonTerritory = new JsonObject();
		JsonArray array = new JsonArray();
		for(Territory t : getNeighbours())
			array.add(t.getName());
		jsonTerritory.addProperty("name", name);
		jsonTerritory.addProperty("numberOfTanks", numberOfTanks);
		jsonTerritory.add("neighbours", array);
		return jsonTerritory;
	}
	
}
