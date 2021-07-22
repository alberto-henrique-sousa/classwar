package br.com.classwar.units;

import java.io.Serializable;

import br.com.classwar.util.Player;

public class Knight extends Unit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4460527523214489313L;

	public Knight(Player player) {
		super(player);
		this.setTitle("knight");
		this.setAgility(3);
		this.setAttack(1);
		this.setDefense(10);
		this.setForce(50);
		this.setCost(400);
		this.setCostChampion(120);
		this.setPowerChampion(25);
		this.setBonus(25);
		this.setType(BASIC);
	}
	
}
