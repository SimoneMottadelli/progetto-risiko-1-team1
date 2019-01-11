package com.drisk.domain.turn;

import com.drisk.domain.exceptions.RequestNotValidException;
import com.drisk.domain.game.Player;
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

	@Override
	public void fromJson(JsonObject obj) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void checkCondition() throws RequestNotValidException {
		// TODO Auto-generated method stub
		
	}
}
