package br.com.classwar.units;

import java.io.Serializable;

import br.com.classwar.util.Player;

public class Warrior extends Unit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6098953611682461077L;

	public Warrior(Player player) {
		super(player);
		this.setTitle("warrior");
		this.setAgility(2);
		this.setAttack(1);
		this.setDefense(7);
		this.setForce(35);
		this.setCost(300);
		this.setCostChampion(90);
		this.setType(BASIC);
		this.setPowerChampion(25);
		this.setBonus(25);
	}
	
}
