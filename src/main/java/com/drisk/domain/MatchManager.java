package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class MatchManager {

	private static MatchManager instance;
	private boolean matchStarted;
	private boolean matchFull;
	private List<Color> colorAvailableList;
	private List<Player> players;
	
	private MatchManager() {
		matchStarted = false;
		matchFull = false;
		colorAvailableList = createColorAvailableList();
		players = new LinkedList<>();
	}
	
	public boolean isMatchStarted() {
		return matchStarted;
	}
	
	public boolean isMatchFull() {
		return matchFull;
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public void joinGame() {
		if(!isMatchFull()) {
			Color color = findFreeColor();
			Player player = new Player(color);
			addPlayer(player);
		}
	}
	
	public void startGame() {
		matchStarted = true;
		GameManager.getInstance().startGame();
	}
	
	public void addPlayer(Player player) {
		if(!players.contains(player))
			players.add(player);
		if(players.size() >= 6)
			matchFull = true;
	}

	private Color findFreeColor() {
		Color color = colorAvailableList.remove(0);
		return color;
	}
	
	private List<Color> createColorAvailableList() {
		Color[] colors = Color.values();
		List<Color> colorsList = new LinkedList<>();
		for(int i = 0; i < colors.length; ++i)
			colorsList.add(colors[i]);
		return colorsList;
	}
	
	public static MatchManager getInstance() {
		if(instance == null)
			instance = new MatchManager();
		return instance;
	}
	
}
