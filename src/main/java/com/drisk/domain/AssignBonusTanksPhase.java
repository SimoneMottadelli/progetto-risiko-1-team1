package com.drisk.domain;

import java.util.LinkedList;
import java.util.Arrays;

public class AssignBonusTanksPhase implements Phase{

	@Override
	public void startPhase() {
		//da implementare TODO		
	}

	@Override
	public void nextPhase() {
		Turn.getInstance().setCurrentPhase(new AssignTanksPhase());
	}

	public void assignBonusTanks(Player player, TerritoryCard[] tris) {
		
		//checkTris()?
		boolean trisAvailable = true;
		TerritoryCardType[] simbols = CardManager.getInstance().useTris(player, tris);
		//ordino i simboli in base a come sono disposti nell'enum TerritoryCardType (?)
		Arrays.sort(simbols);
		
		int tanks = assignTanksPerTris(simbols);
		if (tanks == 0) {
			trisAvailable = false;
		}
		
		LinkedList<Territory> territoriesOwned = player.getTerritoriesOwned();
		for(int i = 0; i < 3; ++i) {
			if (territoriesOwned.contains(tris[i].getTerritory()) && trisAvailable) {
				tanks += 2;
			}
			if (!trisAvailable) {
				//se il tris non è valido, ridai le carte indietro al giocatore
				//(tolte nel metodo useTris in CardManager)
				player.addTerritoryCards(tris[i]);
			}
		}
		
		player.addAvailableTanks(tanks);
		
	}
	
	// Metodo che restituisce, in base al tris che ho, il numero di truppe associato
	public int assignTanksPerTris(TerritoryCardType[] simbols) {
		
		TerritoryCardType[] types = TerritoryCardType.values();
		int tanks = 0;
		
		/*Possible cases:
		 *case1 -> 3 artilleries
		 *case2 -> 3 infantries
		 *case3 -> 3 cavalries
		 *case4 -> 1 infantry, 1 cavalry, 1 artillery
		 *case5, case6, case7 -> 2 same types + wildcard
		 */
		
		TerritoryCardType[] case1 = {types[2], types[2], types[2]};
		TerritoryCardType[] case2 = {types[0], types[0], types[0]};
		TerritoryCardType[] case3 = {types[1], types[1], types[1]};
		TerritoryCardType[] case4 = {types[0], types[1], types[2]};
		TerritoryCardType[] case5 = {types[0], types[0], types[3]};
		TerritoryCardType[] case6 = {types[1], types[1], types[3]};
		TerritoryCardType[] case7 = {types[2], types[2], types[3]};
		
		if (simbols.equals(case1)) {
			tanks = 4;
		} else {
			if (simbols.equals(case2)) {
				tanks = 6;
			} else {
				if (simbols.equals(case3)) {
					tanks = 8;
				} else {
					if (simbols.equals(case4)) {
						tanks = 10;
					} else {
						if (simbols.equals(case5) || simbols.equals(case6) ||
								simbols.equals(case7)) {
							tanks = 12;
						} else {
							System.out.println("Not a valid tris!");
							tanks = 0;
						}
					}
				}
			}
		}
		
		return tanks;
	}
	
}
