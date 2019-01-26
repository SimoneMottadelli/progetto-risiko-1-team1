package com.drisk.domain.game;

import java.util.LinkedList;
import java.util.List;

import com.drisk.domain.card.CardManager;
import com.drisk.domain.lobby.LobbyManager;
import com.drisk.domain.map.MapManager;
import com.drisk.domain.turn.TurnManager;

public class GameManager {
	
	private List<Player> players;
	private static GameManager instance;
	private Player winner;
	private boolean gameStarted;
	
	private GameManager() {
		players = new LinkedList<>();
		gameStarted = false;
	}
	
	public static GameManager getInstance() {
		if (instance == null)
			instance = new GameManager();
		return instance;
	}
	
	public boolean isGameStarted() {
		return gameStarted;
	}

	public void initGame(List<Player> players, ObjectiveTypeEnum objective) {
		gameStarted = true;
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
	
	public ColorEnum getColorOfWinner() {
		if(winner != null)
			return winner.getColor();
		else
			return null;
	}

	public void resetGame() {
		CardManager.destroy();
		MapManager.destroy();
		TurnManager.destroy();
		TankManager.destroy();
		LobbyManager.destroy();
		destroy();
	}
	
}