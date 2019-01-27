package com.drisk.domain;

import com.google.gson.JsonObject;

public class TerritoryCard implements Card, Comparable<TerritoryCard> {
	
	private Territory territory;
	private TerritoryCardSymbolEnum symbol;
	
	
	public TerritoryCard(Territory territory, TerritoryCardSymbolEnum simbol) {
		setTerritory(territory);
		setSymbol(simbol);
	}

	public Territory getTerritory() {
		return territory;
	}


	public TerritoryCardSymbolEnum getSymbol() {
		return symbol;
	}


	public void setTerritory(Territory territory) {
		this.territory = territory;
	}


	public void setSymbol(TerritoryCardSymbolEnum simbol) {
		this.symbol = simbol;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerritoryCard other = (TerritoryCard) obj;
		if (symbol != other.symbol)
			return false;
		if (territory == null) {
			if (other.territory != null)
				return false;
		} 
		else if (!territory.equals(other.territory))
			return false;
		return true;
	}


	@Override
	public int compareTo(TerritoryCard o) {
		return this.getSymbol().compareTo(o.getSymbol());	
	}
	
	
	public JsonObject toJson() {
		JsonObject jsonCard = new JsonObject();
		jsonCard.addProperty("territory", getTerritory().getName());
		jsonCard.addProperty("symbol", getSymbol().toString());
		return jsonCard;
	}
	
}
