package br.com.classwar.units;

import java.io.Serializable;

import br.com.classwar.util.Player;

public class Archer extends Unit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4751631762507497095L;

	public Archer(Player player) {
		super(player);
		this.setTitle("archer");
		this.setAgility(2);
		this.setAttack(3);
		this.setDefense(5);
		this.setForce(40);
		this.setCost(350);
		this.setCostChampion(105);
		this.setPowerChampion(25);
		this.setBonus(25);
		this.setType(BASIC);
	}
	
}
