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

	public void initGame(List<Player> players) {
		initPlayers(players);
		initCards();
		initPlayersMission();
		initPlayersTerritories();
		initTanks();
	}
	
	private void startGame() {
		TurnManager.getInstance().initTurn();
	}

	private void initPlayers(List<Player> players) {
		this.players = players;
	}
	
	public void initCards() {
		CardManager.getInstance().initCards();
	}
	
	private void initPlayersMission() {
		// questo true dovra essere sostituito dalle impostazioni di gioco che verranno ricevute dal matchManager
		CardManager.getInstance().initPlayersMission(players, true);
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