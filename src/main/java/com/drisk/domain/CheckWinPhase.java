package com.drisk.domain;

import com.google.gson.JsonObject;

public class CheckWinPhase extends Phase {

	public CheckWinPhase(int id) {
		super(id);
	}

	@Override
	public void nextPhase() {
		
	}

	@Override
	public void playPhase(Player currentPlayer, JsonObject obj) {
		//probabilmente, al posto di una fase, è meglio creare un metodo a parte per la checkWin() TODO
	}
	
	

}
