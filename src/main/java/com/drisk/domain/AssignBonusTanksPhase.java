package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

public class AssignBonusTanksPhase extends Phase{
	
	public AssignBonusTanksPhase() {
		super(1);
	}

	@Override
	public void playPhase() {
		//da implementare TODO		
	}
	
	@Override
	public void nextPhase() {
		TurnManager.getInstance().setCurrentPhase(new AssignTanksPhase());
	}
	
	public void useTris(Player player, TerritoryCard[] tris) {
		if (tris.length == 3) {
			Arrays.sort(tris);
			
			List<TerritoryCardSymbol> trisSymbols = new LinkedList<>();
			for (int i = 0; i < 3; ++i)
				trisSymbols.add(tris[i].getSymbol());
			
			Integer bonusTanks = CardManager.getInstance().getTrisWithValue().get(trisSymbols);
			
			if (bonusTanks != null) { // ...then the tris exists...
				
				// +2 tanks if the player has a territory printed on a specific territory card used
				for (TerritoryCard t : tris)
					if (player.getTerritoriesOwned().contains(t.getTerritory()))
						bonusTanks += 2;
				TankManager.getInstance().addTanksToPlayer(bonusTanks, player);
				CardManager.getInstance().removeCards(player, tris);
			}
		}
	}
	
}
