package br.com.classwar.units;

import br.com.classwar.util.Player;

public abstract class Unit {

	private int row;
	private int col;
	private char type;
	private int life;
	private int force;
	private int agility;
	private int attack;
	private int defense;
	private int cost;
	private int costChampion;
	private int powerChampion;
	private int bonus;
	private String title;
	private Player player;
	public static char BASIC = 'b';
	public static char CHAMPION = 'c';
	public static char HERO = 'h';

	public Unit() {
		
	}	
	
	public Unit(Player player) {
		this.player = player;
		this.life = 100;
		this.force = 5; 
		this.agility = 3;
		this.attack = 3;
		this.defense = 5;
		this.cost = 300;
		this.costChampion = 90;
		this.powerChampion = 25;
		this.bonus = 25;
		this.type = BASIC;
	}

	public int getLife() {
		return life;
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
	public int getForce() {
		return force;
	}
	
	public void setForce(int force) {
		this.force = force;
	}
	
	public int getAgility() {
		return agility;
	}
	
	public void setAgility(int agility) {
		this.agility = agility;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public void setAttack(int attack) {
		this.attack = attack;
	}
	
	public int getDefense() {
		return defense;
	}
	
	public void setDefense(int defense) {
		this.defense = defense;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public int getCostChampion() {
		return costChampion;
	}

	public void setCostChampion(int costChampion) {
		this.costChampion = costChampion;
	}

	public int getPowerChampion() {
		return powerChampion;
	}

	public void setPowerChampion(int powerChampion) {
		this.powerChampion = powerChampion;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

}