package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

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
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	public List<Territory> getTerritories() {
		return territories;
	}

	
	public void setTerritories(List<Territory> territories) {
		this.territories = territories;
	}
	
	
	
}
