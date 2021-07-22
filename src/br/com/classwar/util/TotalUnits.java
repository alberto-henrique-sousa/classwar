package br.com.classwar.util;

import java.io.Serializable;

public class TotalUnits implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7931099945168855603L;
	private int warrior;
	private int archer;
	private int knight;
	private int guardian;
	
	public void update(String name, int value) {
		if (name.equalsIgnoreCase("warrior"))
			this.warrior += value;
		else if (name.equalsIgnoreCase("archer"))
			this.archer += value;
		else if (name.equalsIgnoreCase("knight"))
			this.knight += value;
		else if (name.equalsIgnoreCase("guardian"))
			this.guardian += value;
	}

	public int getWarrior() {
		return warrior;
	}
	
	public void setWarrior(int warrior) {
		this.warrior = warrior;
	}
	
	public int getArcher() {
		return archer;
	}
	
	public void setArcher(int archer) {
		this.archer = archer;
	}
	
	public int getKnight() {
		return knight;
	}
	
	public void setKnight(int knight) {
		this.knight = knight;
	}
	
	public int getGuardian() {
		return guardian;
	}
	
	public void setGuardian(int guardian) {
		this.guardian = guardian;
	}

}
