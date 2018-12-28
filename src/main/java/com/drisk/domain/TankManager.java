package com.drisk.domain;

public class TankManager {
	
	private static TankManager instance;
	
	private TankManager() {
		
	}
	
	public static TankManager getInstance() {
		if (instance == null)
			instance = new TankManager();
		return instance;
	}
	
	public void initTanks() {
		//da implementare TODO
	}
	
	public void placeTanks(Territory whereTerritory, int numTanks) {
		//da implementare TODO
	}
	
	public void removeTanks(Territory whereTerritory, int numTanks) {
		//da implementare TODO
	}
	
	public void assignTanks(Player player) {
		//da implementare TODO
	}
	
}
