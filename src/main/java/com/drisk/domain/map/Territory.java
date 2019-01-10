package com.drisk.domain.map;

import java.util.LinkedList;
import java.util.List;

import com.drisk.domain.game.Player;
import com.google.gson.JsonObject;

public class Territory {

	private String name;
	private int numberOfTanks;
	private List<Territory> neighbours;
	private Player owner;
	
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

	public void addNeighbour(Territory territory) {
		if(!neighbours.contains(territory))
			neighbours.add(territory);
	}
	
	public int getNumberOfTanks() {
		return numberOfTanks;
	}
	
	public void addTanks(int numberOfTanks) {
		this.numberOfTanks += numberOfTanks;
	}
	
	public void removeTanks(int numberOfTanks) {
		if (this.numberOfTanks >= numberOfTanks)
			this.numberOfTanks -= numberOfTanks;
		else
			this.numberOfTanks = 0;
	}

	public String getName() {
		return name;
	}
	
	public List<Territory> getNeighbours() {
		return neighbours;
	}
	
	public JsonObject toJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", name);
		obj.addProperty("owner", owner.getColor().toString());
		obj.addProperty("numberOfTanks", numberOfTanks);
		return obj;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
}
