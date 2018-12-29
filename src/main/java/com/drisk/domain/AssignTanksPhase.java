package com.drisk.domain;

import java.util.LinkedList;

public class AssignTanksPhase implements Phase {

	@Override
	public void startPhase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextPhase() {
		Turn.getInstance().setCurrentPhase(new TankPlacementPhase());
	}
	
	public void assignTanks(Player player) {
		
		//Ogni giocatore avrà almeno un tank di base all'inizio del turno,
		//anche se dovesse avere meno di 3 territori.
		int numberTerritoriesOwned = player.getNumberOfTerritoriesOwned();
		int tanks;
		
		if (numberTerritoriesOwned / 3 < 1) {
			tanks = 1;
		} else {
			tanks = numberTerritoriesOwned / 3;
		}
		
		LinkedList<Territory> territoriesOwned = player.getTerritoriesOwned();
		LinkedList<Continent> continents = Map.getInstance().getContinents();
		for(Continent c: continents) {
			LinkedList<Territory> territories = c.getTerritories();
			//da analizzare se va bene oppure no la condizione dell'if
			if (territoriesOwned.containsAll(territories)) {
				switch(c.getName()) {
				case "africa": 
					tanks += 3;
					break;
				case "asia":
					tanks += 7;
					break;
				case "australia":
					tanks += 2;
					break;
				case "europe":
					tanks += 5;
					break;
				case "north america":
					tanks += 5;
					break;
				case "south america":
					tanks += 2;
					break;
				}
			}
		}
		
		player.addAvailableTanks(tanks);
	}
	
}
