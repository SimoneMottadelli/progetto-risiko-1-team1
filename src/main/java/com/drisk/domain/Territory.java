package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

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
		else if (!name.equals(other.name)) {
			return false;		
		}
		return true;
	}
	
	
	public void addNeighbour(Territory territory) {
		if(!neighbours.contains(territory))
			neighbours.add(territory);
	}


	public int getNumberOfTanks() {
		return numberOfTanks;
	}


	public void setNumberOfTanks(int numberOfTanks) {
		this.numberOfTanks = numberOfTanks;
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
	
}
