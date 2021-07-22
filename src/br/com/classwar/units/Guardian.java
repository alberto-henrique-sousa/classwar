package br.com.classwar.units;

import java.io.Serializable;

import br.com.classwar.util.Player;

public class Guardian extends Unit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3392652628554605796L;

	public Guardian(Player player) {
		super(player);
		this.setTitle("guardian");
		this.setAgility(2);
		this.setAttack(2);
		this.setDefense(15);
		this.setForce(60);
		this.setCost(430);
		this.setCostChampion(129);
		this.setPowerChampion(25);
		this.setBonus(25);
		this.setType(BASIC);
	}
	
}
