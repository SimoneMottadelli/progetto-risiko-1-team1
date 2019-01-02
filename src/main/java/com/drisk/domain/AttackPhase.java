package com.drisk.domain;

import java.util.Scanner;

public class AttackPhase implements Phase {

	@Override
	public void startPhase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextPhase() {
		Turn.getInstance().setCurrentPhase(new TankMovementPhase());
	}

	public void attackEnemyTerritory(Player attacker) {
		
		Scanner reader = new Scanner(System.in);
		//da controllare se il giocatore vuole effettivamente attaccare
		boolean wantToAttack = true;
		
		if(wantToAttack) {
			System.out.println("Choose from which territory you want to attack");
			String t1 = reader.next();
			System.out.println("Choose the territory you want to attack");
			String t2 = reader.next();
			
			Territory territoryAttacker = Map.getInstance().findTerritoryByName(t1);
			Territory territoryDefender = Map.getInstance().findTerritoryByName(t2);

			boolean attackAvailability = checkAttackAvailability(territoryAttacker, territoryDefender);
			
			if (attackAvailability) {
				//attacca
			}
		}

		
	}
	
	public boolean checkAttackAvailability(Territory territoryAttacker, Territory territoryDefender) {
		
		boolean condition1 = territoryAttacker.getNumberOfTanks() > 1;
		boolean condition2 = territoryAttacker.getNeighbours().contains(territoryDefender);
		return condition1 && condition2;
	}
}
