package br.com.classwar.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.classwar.units.Archer;
import br.com.classwar.units.Guardian;
import br.com.classwar.units.Knight;
import br.com.classwar.units.Unit;
import br.com.classwar.units.Warrior;

public class Settings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7286365857877927925L;
	private int totalPlayers;
	private int actionsRound;
	private int gold;
	private Warrior warrior;
	private Archer archer;
	private Knight knight;
	private Guardian guardian;
	private List<Unit> listUnits;
	
	public Settings() {
		this.totalPlayers = 1;
		this.actionsRound = 4;
		this.gold = 6000;
		this.warrior = new Warrior(null);
		this.archer = new Archer(null);
		this.knight = new Knight(null);
		this.guardian = new Guardian(null);
		this.listUnits = new ArrayList<Unit>();
		this.listUnits.add(warrior);
		this.listUnits.add(archer);
		this.listUnits.add(knight);
		this.listUnits.add(guardian);
	}
	
	public int getTotalPlayers() {
		return totalPlayers;
	}
	
	public void setTotalPlayers(int totalPlayers) {
		this.totalPlayers = totalPlayers;
	}
	
	public int getActionsRound() {
		return actionsRound;
	}
	
	public void setActionsRound(int actionsRound) {
		this.actionsRound = actionsRound;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public Warrior getWarrior() {
		return warrior;
	}

	public void setWarrior(Warrior warrior) {
		this.warrior = warrior;
	}

	public Archer getArcher() {
		return archer;
	}

	public void setArcher(Archer archer) {
		this.archer = archer;
	}

	public Knight getKnight() {
		return knight;
	}

	public void setKnight(Knight knight) {
		this.knight = knight;
	}

	public Guardian getGuardian() {
		return guardian;
	}

	public void setGuardian(Guardian guardian) {
		this.guardian = guardian;
	}

	public List<Unit> getListUnits() {
		return listUnits;
	}

	public void setListUnits(List<Unit> listUnits) {
		this.listUnits = listUnits;
	}

}
