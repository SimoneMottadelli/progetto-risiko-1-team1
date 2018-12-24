package com.drisk.domain;

import java.util.LinkedList;
import java.util.List;

public class Test {

	public static void main(String[] args) {

		List<String> players = new LinkedList<>();
		players.add("Zucca");
		players.add("Matteo");
		players.add("Claudio");
		players.add("Simo");
		players.add("Fra");
		players.add("Tizio a caso");
		
		GameManager.getInstance().initPlayers(players);
	}

}
