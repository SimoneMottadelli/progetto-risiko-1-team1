package com.drisk.domain.game;

import java.util.LinkedList;
import java.util.List;

import com.drisk.domain.card.CardManager;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.turn.TurnManager;

public class GameManager {
	
	private List<Player> players;
	private static GameManager instance;
	private Player winner;
	
	private GameManager() {
		players = new LinkedList<>();
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public static GameManager getInstance() {
		if (instance == null)
			instance = new GameManager();
		return instance;
	}

	public void initGame(List<Player> players, ObjectiveTypeEnum objective) {
		initPlayers(players);
		initPlayersTerritories();
		initCards(objective);
		initPlayersMission();
		initTanks();
	}
	
	private void startGame() {
		TurnManager.getInstance().initTurn(players);
	}

	private void initPlayers(List<Player> players) {
		this.players = players;
	}
	
	public void initCards(ObjectiveTypeEnum objective) {
		CardManager.getInstance().initCards(players, objective);
	}
	
	private void initPlayersMission() {
		CardManager.getInstance().initPlayersMission(players);
	}
	
	public void initPlayersTerritories() {
		MapManager.getInstance().initPlayersTerritories(players);
	}

	public void initTanks() {
		TankManager.getInstance().initTanks(players);
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public Player findPlayerByColor(ColorEnum color) {
		for(Player p : players)
			if(p.getColor().equals(color))
				return p;
		return null;
	}
	
	public boolean checkWin(Player player) {
		if(player.getMissionCard().isAchievementReached(player)) {
			winner = player;
			endGame();
			return true;
		}
		return false;
	}
	
	private void endGame() {
		// TODO qualsiasi cosa deve succedere quando il gioco deve terminare
		System.out.println("Il giocatore " + TurnManager.getInstance().getCurrentPlayer().getColor() + " ha vinto!");
	}
	
	public void tryToStartGame() {
		if(TankManager.getInstance().areAllTanksPlaced(players))
			startGame();
	}

	public static void destroy() {
		instance = null;
	}

	public void checkLoss(Player player) {
		if(MapManager.getInstance().getMapTerritories(player).isEmpty()) {
			CardManager.getInstance().removeCards(player, player.getTerritoryCardsHand());
			players.remove(player);
		}
	}	
}