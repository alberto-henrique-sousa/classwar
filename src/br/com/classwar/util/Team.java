package br.com.classwar.util;

import java.io.Serializable;

import br.com.classwar.bean.EngineBean;

public class Team implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6529911439417816346L;
	private int id;
	private String name;
	private int numberPlayers;
	private EngineBean engineBean;
	private Player[] players;
	private int countUnit;
	private TotalUnits totalUnits;
	
	public Team() {
		
	}
	
	public Team(int id, String name, int numberPlayers, EngineBean engineBean) {
		this.id = id;
		this.name = name;
		this.numberPlayers = numberPlayers;
		this.engineBean = engineBean;
		this.players = new Player[numberPlayers];
		this.countUnit = 0;
		this.totalUnits = new TotalUnits();
	}
	
	public void reset() {
		this.players = null;
		this.numberPlayers = 0;		
		this.name = null;
	}
	
	public void addPlayer() {
		boolean found = false;
		for (int i = 0; i < players.length && !found; i++) {
			if (players[i] == null) {
				players[i] = new Player(this.id, i, "", false, this.engineBean, false, codeDefault());
				found = true;
			}
		}
	}
	
	public boolean allReady() {
		boolean ret = true;
		for (int i = 0; i < players.length && ret; i++) {
			ret = players[i].isReady();
		}
		return ret;
	}

	public void allReady(boolean ready) {
		for (int i = 0; i < players.length; i++) {
			players[i].setReady(ready);
		}
	}
	
	public void allWait(boolean wait) {
		for (int i = 0; i < players.length; i++) {
			players[i].setWait(wait);
		}
	}
	
	public int totalUnits() {
		int total = 0;
		for (int i = 0; i < players.length; i++) {
			total += players[i].getTotalUnit();
		}		
		return total;
	}

	public String codeDefault() {
		String java =  
				"@Override\n"
				+ "public void run() {\n"
				+ "	try {\n"
				+ "		// perform your actions...\n"
				+ "		\n"
				+ "	} catch (Exception e) {\n"
				+ "		console(e);\n"
				+ "	}"
				+ "	\n"				
				+ "}\n";
		return java;		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumberPlayers() {
		return numberPlayers;
	}

	public void setNumberPlayers(int numberPlayers) {
		this.numberPlayers = numberPlayers;
	}

	public EngineBean getEngineBean() {
		return engineBean;
	}

	public void setEngineBean(EngineBean engineBean) {
		this.engineBean = engineBean;
	}

	public int getCountUnit() {
		return countUnit;
	}

	public void setCountUnit(int countUnit) {
		this.countUnit = countUnit;
	}

	public TotalUnits getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(TotalUnits totalUnits) {
		this.totalUnits = totalUnits;
	}

	public void addUnit() {
		this.countUnit += 1;
	}
	
	public int gold() {
		int gold = 0;
		for (Player player : players) {
			if (player != null)
				gold += player.getGold();
		}
		return gold;
	}
}
