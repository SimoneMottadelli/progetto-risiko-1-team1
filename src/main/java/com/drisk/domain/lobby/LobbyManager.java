package com.drisk.domain.lobby;

import java.util.LinkedList;
import java.util.List;

import com.drisk.domain.game.ColorEnum;
import com.drisk.domain.game.GameManager;
import com.drisk.domain.game.ObjectiveTypeEnum;
import com.drisk.domain.game.Player;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LobbyManager {

	private static LobbyManager instance;
	private boolean matchStarted;
	private boolean matchFull;
	private ObjectiveTypeEnum objective;
	private List<ColorEnum> colorsAvailablesList;
	private List<Player> players;

	private LobbyManager() {
		matchStarted = false;
		matchFull = false;
		objective = ObjectiveTypeEnum.COMMON;
		colorsAvailablesList = createColorAvailableList();
		players = new LinkedList<>();
	}

	public boolean isMatchStarted() {
		return matchStarted;
	}
	
	public void setObjective(ObjectiveTypeEnum objective) {
		this.objective = objective;
	}
	
	public boolean isMatchFull() {
		return matchFull;
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public void joinGame(String nickName) {
		if(!isMatchFull()) {
			ColorEnum color = findFreeColor();
			Player player = new Player(color, nickName);
			addPlayer(player);
		}
	}
	
	public ColorEnum findLastPlayerColor() {
		return players.get(players.size() - 1).getColor();
	}
	
	
	public void exitGame(ColorEnum color) {
		//for-each modificato in for-normale per evitare "ConcurrentModificationException"
		for (int i = 0; i < players.size(); ++i)
			if (players.get(i).getColor().equals(color)) {
				colorsAvailablesList.add(color);
				removePlayer(players.get(i));
			}
	}
	
	public void setPlayerReady(ColorEnum color, boolean ready) {
		for (Player p : players)
			if (p.getColor().equals(color))
				p.setReady(ready);
	}
	
	public boolean isEveryoneReady() {
		boolean everyoneReady = false;
		if (!players.isEmpty()) {
			everyoneReady = true;
			for (Player p : players)
				if (!p.isReady())
					everyoneReady = false;
		}
		return everyoneReady;
	}
	
	public boolean areThereAtLeastTwoPlayers() {
		return players.size() >= 2;
	}
	
	public void initGame(){
		matchStarted = true;
		GameManager.getInstance().initGame(players, objective);
	}
	
	private void addPlayer(Player player) {
		if(!players.contains(player))
			players.add(player);
		if(players.size() >= 6)
			matchFull = true;
	}
	
	private void removePlayer(Player player) {
		if(players.contains(player))
			players.remove(player);
		if(players.size() < 6)
			matchFull = false;
	}

	private ColorEnum findFreeColor() {
		return colorsAvailablesList.remove(0);
	}
	
	private List<ColorEnum> createColorAvailableList() {
		ColorEnum[] colors = ColorEnum.values();
		List<ColorEnum> colorsList = new LinkedList<>();
		for(ColorEnum c : colors)
			colorsList.add(c);
		return colorsList;
	}
	
	public static LobbyManager getInstance() {
		if(instance == null)
			instance = new LobbyManager();
		return instance;
	}
	
	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		JsonArray jsonArrayPlayers = new JsonArray();
		for(Player p : players)
			jsonArrayPlayers.add(p.toJson());
		result.add("players", jsonArrayPlayers);
		return result;
	}

	public static void destroy() {
		instance = null;
	}
	
}
