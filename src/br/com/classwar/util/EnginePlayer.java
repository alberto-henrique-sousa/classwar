package br.com.classwar.util;

import java.util.Map;

import br.com.classwar.bean.EngineBean;
import br.com.classwar.units.Archer;
import br.com.classwar.units.Guardian;
import br.com.classwar.units.Knight;
import br.com.classwar.units.Unit;
import br.com.classwar.units.Warrior;

public abstract class EnginePlayer {

	private Player player;
	private int team;
	public static String ARCHER = "arche";
	public static String GUARDIAN = "guardian";
	public static String KNIGHT = "knight";
	public static String WARRIOR = "warrior";
	public static String TITLE = "title";
	public static String PLAYER_NAME = "playername";
	public static String PLAYER_GOLD = "playergold";
	public static String PLAYER_TOTAL_ACTIONS = "playertotalactions";
	public static String TEAM = "team";	
	public static String TEAM_GOLD = "teamgold";
	public static String LIFE = "life";
	public static String FORCE = "force";
	public static String AGILITY = "agility";
	public static String ATTACK = "attack";
	public static String DEFENSE = "defense";
	public static String COST = "cost";
	public static String COST_CHAMPION = "costChampion";
	public static String POWER_CHAMPION = "powerChampion";
	public static String BONUS = "bonus";
	public static String GAME_ACTIONS_ROUND = "gameactions";
	public static String GAME_GOLD_PLAYER = "gamegold";
	public static String GAME_PLAYER_TEAM = "gameplayerteam";

	public abstract void run();

	public EnginePlayer(Player player, int team) {
		super();
		this.player = player;
		this.team = team;
	};

	private Archer archer() {
		return new Archer(this.player);
	}

	private Guardian guardian() {
		return new Guardian(this.player);
	}

	private Knight knight() {
		return new Knight(this.player);
	}

	private Warrior warrior() {
		return new Warrior(this.player);
	}

	public boolean addUnit(String name, String cell) {
		boolean ok = false;
		Unit unit = null;
		int index = -1;
		if (name.trim().equalsIgnoreCase(WARRIOR)) {
			unit = warrior();
			index = 0;
		} else if (name.trim().equalsIgnoreCase(ARCHER)) {
			unit = archer();
			index = 1;
		} else if (name.trim().equalsIgnoreCase(KNIGHT)) {
			unit = knight();
			index = 2;
		} else if (name.trim().equalsIgnoreCase(GUARDIAN)) {
			unit = guardian();
			index = 3;
		}
		if (unit != null) {
			Unit unitSettings = this.player.getEngineBean().getSettings().getListUnits().get(index);
			unit.setAgility(unitSettings.getAgility());
			unit.setAttack(unitSettings.getAttack());
			unit.setBonus(unitSettings.getBonus());
			unit.setCost(unitSettings.getCost());
			unit.setCostChampion(unitSettings.getCostChampion());
			unit.setDefense(unitSettings.getDefense());
			unit.setForce(unitSettings.getForce());
			unit.setPowerChampion(unitSettings.getPowerChampion());
			this.player.addAction();
			ok = this.player.addUnit(unit, cell);
		}
		if (!ok) {
			Animation animation = new Animation(null);
			animation.setFailed(true);
			animation.setSound(Animation.SOUND_FAIL);
			animation.setCellEffect("team_"+this.player.getTeam()+"_d");
			animation.setType("add");
			animation.setTeam(this.player.getTeam());
			animation.setNamePlayer(this.player.getName());
			this.player.getEngineBean().getListAnimation().add(animation);			
		}
		return ok;
	}

	private void deleteUnit(Player[] players, int row, int col) {
		try {
			boolean found = false;
			for (int i = 0; !found && i < players.length; i++) {
				for (int j = 0; !found && j < players[i].getUnits().size(); j++) {
					Unit unit = players[i].getUnits().get(j); 
					if (unit != null && unit.getRow() == row && unit.getCol() == col) {
						Double div = Double.valueOf(unit.getBonus())/100;
						this.player.setGold(this.player.getGold()+(Double.valueOf(Math.round(unit.getCost() * div)).intValue()));						
						players[i].team(players[i].getTeam()).getTotalUnits().update(unit.getTitle(), -1);
						players[i].getUnits().remove(j);
						found = true;
					}
				}
			}			
			if (found) {
				this.player.getEngineBean().getBoardGame()[row][col] = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				String msg[] = e.getMessage().split("\n");
				this.player.getEngineBean().addMsgConsole(this.player, "error", msg);
			}	
		}		
	}

	private Unit selectUnit(int row, int col) {		
		Unit unit = null;
		try {
			boolean found = false;
			for (int i = 0; i < this.player.getUnits().size() && !found; i++) {
				if (this.player.getUnits().get(i) != null && row == player.getUnits().get(i).getRow() && 
						col == player.getUnits().get(i).getCol()) {
					unit = this.player.getUnits().get(i);
					found = true;
				}
			}
			if (!found) {
				String fromCell = this.player.getEngineBean().convertIndexLetter(row+1) + (col+1);
				String msg[] = {fromCell+" - failed..."};
				this.player.getEngineBean().addMsgConsole(this.player, "select", msg);																		
				Animation animation = new Animation(unit);
				animation.setFailed(true);
				animation.setSound(Animation.SOUND_FAIL);
				animation.setCellEffect("team_"+this.player.getTeam()+"_d");
				animation.setType("select");
				animation.setNamePlayer(this.player.getName());
				animation.setTeam(this.player.getTeam());
				this.player.getEngineBean().getListAnimation().add(animation);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				String msg[] = e.getMessage().split("\n");
				this.player.getEngineBean().addMsgConsole(this.player, "error", msg);
			}	
		}
		return unit;
	}
	
	public Object game(String type) {
		Object ret = null;
		if (type.equals(GAME_ACTIONS_ROUND))
			ret = this.player.getEngineBean().getSettings().getActionsRound();
		else if (type.equals(GAME_GOLD_PLAYER))
			ret = this.player.getEngineBean().getSettings().getGold();
		else if (type.equals(GAME_PLAYER_TEAM))
			ret = this.player.getTeam();
		else if (type.equals(PLAYER_GOLD))
			ret = this.player.getGold();
		else if (type.equals(PLAYER_NAME))
			ret = this.player.getName();
		else if (type.equals(PLAYER_TOTAL_ACTIONS)) 
			ret = this.player.getCountActions();
		return ret;
	}
	
	public Object unit(String cell, String type) {
		Object ret = null;
		try {
			int row = EngineBean.convertRowCell(cell);
			int	col = EngineBean.convertColCell(cell);			
			if (row >= 0 && row < 4 && col >= 0 && col < 12) {
				Unit unit = this.player.getEngineBean().getBoardGame()[row][col];
				if (unit != null) {
					if (type.equals(TITLE))
						ret = unit.getTitle();
					else if (type.equals(PLAYER_NAME))
						ret = unit.getPlayer().getName();
					else if (type.equals(TEAM))
						ret = unit.getPlayer().getTeam();
					else if (type.equals(TEAM_GOLD))
						ret = unit.getPlayer().getGold();
					else if (type.equals(LIFE))
						ret = unit.getLife();
					else if (type.equals(FORCE))
						ret = unit.getForce();
					else if (type.equals(AGILITY))
						ret = unit.getAgility();
					else if (type.equals(ATTACK))
						ret = unit.getAttack();
					else if (type.equals(DEFENSE))
						ret = unit.getDefense();
					else if (type.equals(COST))
						ret = unit.getCost();
					else if (type.equals(COST_CHAMPION))
						ret = unit.getCostChampion();
					else if (type.equals(POWER_CHAMPION))
						ret = unit.getPowerChampion();
					else if (type.equals(BONUS))
						ret = unit.getBonus();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				String msg[] = e.getMessage().split("\n");
				this.player.getEngineBean().addMsgConsole(this.player, "error", msg);
			}	
		}
		return ret;
	}

	public String cellLastAttack() {
		return this.player.getEngineBean().getCellLastAttack();
	}

	public String cellLastMove() {
		return this.player.getEngineBean().getCellLastMove();
	}

	public boolean move(String cellOrig, String cellDest) {
		boolean ok = false;
		this.player.addAction();
		if (!(this.player.getCountActions() > this.player.getEngineBean().getSettings().getActionsRound())) {
			try {
				int row = EngineBean.convertRowCell(cellOrig);
				int	col = EngineBean.convertColCell(cellOrig);
				Unit unit = selectUnit(row, col);
				if (unit != null) {
					int rowDest = EngineBean.convertRowCell(cellDest);
					int	colDest = EngineBean.convertColCell(cellDest);
					if (rowDest >= 0 && rowDest < 4 && colDest >= 0 && colDest < 12) {
						int square = 0;
						if (rowDest > row)
							square = rowDest - row;
						else if (rowDest < row)
							square = row - rowDest;
						if (colDest > col)
							square += colDest - col;
						else if (colDest < col)
							square += col - colDest;
						String fromCell = this.player.getEngineBean().convertIndexLetter(unit.getRow()+1) + (unit.getCol()+1);
						String toCell = this.player.getEngineBean().convertIndexLetter(rowDest+1) + (colDest+1);
						if (square > 0 && unit.getAgility() >= square) {
							if (this.player.getEngineBean().getBoardGame()[rowDest][colDest] == null) {
								this.player.getEngineBean().getBoardGame()[row][col] = null;
								this.player.getEngineBean().getBoardGame()[rowDest][colDest] = unit;
								unit.setRow(rowDest);
								unit.setCol(colDest);
								this.player.getEngineBean().setCellLastMove(cellDest);
								Animation animation = new Animation(unit);
								animation.setCell(cellDest);
								animation.setSpriter(Animation.ANIMATION_RUN);
								animation.setSound(Animation.SOUND_RUN);
								animation.setCellEffect(fromCell);
								animation.setType("move");
								this.player.getEngineBean().getListAnimation().add(animation);
								String msg[] = {"from "+fromCell+" to "+toCell};
								this.player.getEngineBean().addMsgConsole(this.player, "move", msg);
								ok = true;
							}
						}
						if (!ok) {
							String msg[] = {"from "+fromCell+" to "+toCell + " - failed..."};
							this.player.getEngineBean().addMsgConsole(this.player, "move", msg);
							Animation animation = new Animation(unit);
							animation.setCell(cellDest);
							animation.setFailed(true);
							animation.setSound(Animation.SOUND_FAIL);
							animation.setCellEffect("team_"+this.player.getTeam()+"_d");
							animation.setType("move");
							animation.setNamePlayer(this.player.getName());
							animation.setTeam(this.player.getTeam());
							this.player.getEngineBean().getListAnimation().add(animation);
						}
					}
				}			
			} catch (Exception e) {
				e.printStackTrace();
				if (e.getMessage() != null) {
					String msg[] = e.getMessage().split("\n");
					this.player.getEngineBean().addMsgConsole(this.player, "error", msg);
				}	
			}					
		} else {
			Animation animation = new Animation(null);
			animation.setFailed(true);
			animation.setSound(Animation.SOUND_FAIL);
			animation.setCellEffect("team_"+this.player.getTeam()+"_d");
			animation.setType("move");
			animation.setTeam(this.player.getTeam());
			animation.setNamePlayer(this.player.getName());
			this.player.getEngineBean().getListAnimation().add(animation);			
		}
		return ok;
	}

	public boolean attack(String cellOrig, String cellDest) {
		boolean ok = false;
		this.player.addAction();
		if (!(this.player.getCountActions() > this.player.getEngineBean().getSettings().getActionsRound())) {
			try {
				int row = EngineBean.convertRowCell(cellOrig);
				int	col = EngineBean.convertColCell(cellOrig);
				Unit unit = selectUnit(row, col);
				if (unit != null) {
					int rowDest = EngineBean.convertRowCell(cellDest);
					int	colDest = EngineBean.convertColCell(cellDest);
					if (rowDest >= 0 && rowDest < 4 && colDest >= 0 && colDest < 12) {
						int square = 0;
						if (rowDest > row)
							square = rowDest - row;
						else if (rowDest < row)
							square = row - rowDest;
						if (colDest > col)
							square += colDest - col;
						else if (colDest < col)
							square += col - colDest;
						String fromCell = this.player.getEngineBean().convertIndexLetter(unit.getRow()+1) + (unit.getCol()+1);
						if (square > 0 && unit.getAttack() >= square) {
							Unit unitDest = this.player.getEngineBean().getBoardGame()[rowDest][colDest];
							if (unitDest != null) {
								String toCell = this.player.getEngineBean().convertIndexLetter(unitDest.getRow()+1) + (unitDest.getCol()+1);
								String msg[] = {"from "+fromCell+" to "+toCell};
								int bonus = 0;
								boolean rotate = false;
								if (this.team == 1 && unit.getPlayer().getTeam() == 1 && unit.getCol() > unitDest.getCol()) {
									rotate = true;									
								} else 	if (this.team == 1 && unit.getPlayer().getTeam() == 2 && unit.getCol() < unitDest.getCol()) {
									rotate = true;									
								} else if (this.team == 2 && unit.getPlayer().getTeam() == 2 && unit.getCol() < unitDest.getCol()) {
									rotate = true;									
								} else 	if (this.team == 2 && unit.getPlayer().getTeam() == 1 && unit.getCol() > unitDest.getCol()) {
									rotate = true;									
								}
								if (rotate) {	
									Double div = Double.valueOf(unit.getBonus())/100;
									bonus = Double.valueOf(Math.round(unitDest.getDefense() * div)).intValue();
								}
								int attack = unit.getForce() - (unitDest.getDefense() - bonus);
								if (attack > 0) {
									unitDest.setLife(unitDest.getLife() - attack);
									this.player.getEngineBean().setCellLastAttack(cellDest);
									Animation animation1 = new Animation(unit);
									animation1.setCell(cellOrig);
									animation1.setSpriter(Animation.ANIMATION_ATTACK);
									animation1.setSound(Animation.SOUND_ATTACK);
									animation1.setRotate(rotate);
									animation1.setType("attack");
									this.player.getEngineBean().getListAnimation().add(animation1);
									Animation animation2 = new Animation(unitDest);
									animation2.setCell(cellDest);
									animation2.setSpriter(Animation.ANIMATION_DEFEND);
									animation2.setSound(Animation.SOUND_DEFEND);
									animation2.setType("defend");
									this.player.getEngineBean().getListAnimation().add(animation2);									
									this.player.getEngineBean().addMsgConsole(this.player, "attack", msg);
									ok = true;
								}	
								if (unitDest.getLife() <= 0) {
									Animation animation = new Animation(unitDest);
									animation.setCell(cellDest);
									animation.setSpriter(Animation.ANIMATION_DEAD);
									animation.setSound(Animation.SOUND_DEAD);
									animation.setType("dead");
									this.player.getEngineBean().getListAnimation().add(animation);
									unitDest = null;
									this.player.getEngineBean().getBoardGame()[rowDest][colDest] = unitDest;
									if (this.player.getTeam() == 1)
										this.deleteUnit(this.player.getEngineBean().getTeam2().getPlayers(), rowDest, colDest);
									else
										this.deleteUnit(this.player.getEngineBean().getTeam1().getPlayers(), rowDest, colDest);
									this.player.getEngineBean().addMsgConsole(this.player, "dead", msg);
								}									
							}
						}
						if (!ok) {
							String msg[] = {"from "+fromCell+" - failed..."};
							this.player.getEngineBean().addMsgConsole(this.player, "attack", msg);														
							Animation animation = new Animation(unit);
							animation.setFailed(true);
							animation.setSound(Animation.SOUND_FAIL);
							animation.setCellEffect("team_"+this.player.getTeam()+"_d");
							animation.setType("attack");
							animation.setNamePlayer(this.player.getName());
							animation.setTeam(this.player.getTeam());
							this.player.getEngineBean().getListAnimation().add(animation);
						}
					}
				}			
			} catch (Exception e) {
				e.printStackTrace();
				if (e.getMessage() != null) {
					String msg[] = e.getMessage().split("\n");
					this.player.getEngineBean().addMsgConsole(this.player, "error", msg);
				}	
			}
		} else {
			Animation animation = new Animation(null);
			animation.setFailed(true);
			animation.setSound(Animation.SOUND_FAIL);
			animation.setCellEffect("team_"+this.player.getTeam()+"_d");
			animation.setType("attack");
			animation.setTeam(this.player.getTeam());
			animation.setNamePlayer(this.player.getName());
			this.player.getEngineBean().getListAnimation().add(animation);			
		}
		return ok;
	}

	public void addUnits(String[] types, int total, String row) {
		if (types == null)
			types = new String[] {WARRIOR, KNIGHT, ARCHER, GUARDIAN};
		int count = 0;
		int ini = 1;
		if (row.equalsIgnoreCase("a")) ini = 1;
		if (row.equalsIgnoreCase("b")) ini = 2;
		if (row.equalsIgnoreCase("c")) ini = 3;
		if (row.equalsIgnoreCase("d")) ini = 4;
		for (int i = ini; i < 5 && count < total; i++) {				
			for (int j = 1; j < 5 && count < total; j++) {
				if (this.player.getEngineBean().convertIndexLetter(i).length() > 0) {
					if (addUnit(types[j-1], this.player.getEngineBean().convertIndexLetter(i) + (this.player.getTeam() == 1 ? j : (13 - j))))
						count++;
				}	
			}
		}
	}

	public boolean championUnit(String cell) {
		boolean ok = false;
		this.player.addAction();
		if (!(this.player.getCountActions() > this.player.getEngineBean().getSettings().getActionsRound())) {
			try {
				int row = EngineBean.convertRowCell(cell);
				int	col = EngineBean.convertColCell(cell);
				Unit unit = selectUnit(row, col);
				if (unit != null && unit.getType() == Unit.BASIC) {
					int balance = this.player.getGold() - unit.getCostChampion();
					if (balance >= 0) {
						this.player.setGold(balance);
						unit.setType(Unit.CHAMPION);
						unit.setDefense(unit.getDefense() + Double.valueOf(unit.getDefense() * (unit.getPowerChampion()/100)).intValue());
						unit.setForce(unit.getForce() + Double.valueOf(unit.getForce() * (unit.getPowerChampion()/100)).intValue());
						ok = true;
						Animation animation = new Animation(unit);
						animation.setCell(cell);
						animation.setSpriter(Animation.ANIMATION_DEFEND);
						animation.setSound(Animation.SOUND_CHAMPION);
						animation.setType("defend");
						this.player.getEngineBean().getListAnimation().add(animation);									
					}
				}
				if (!ok) {
					Animation animation = new Animation(unit);
					animation.setCell(cell);
					animation.setFailed(true);
					animation.setSound(Animation.SOUND_FAIL);
					animation.setCellEffect("team_"+this.player.getTeam()+"_d");
					animation.setType("champion");
					animation.setNamePlayer(this.player.getName());
					animation.setTeam(this.player.getTeam());
					this.player.getEngineBean().getListAnimation().add(animation);									
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (e.getMessage() != null) {
					String msg[] = e.getMessage().split("\n");
					this.player.getEngineBean().addMsgConsole(this.player, "error", msg);
				}	
			}
		} else {
			Animation animation = new Animation(null);
			animation.setFailed(true);
			animation.setSound(Animation.SOUND_FAIL);
			animation.setCellEffect("team_"+this.player.getTeam()+"_d");
			animation.setType("champion");
			animation.setTeam(this.player.getTeam());
			animation.setNamePlayer(this.player.getName());
			this.player.getEngineBean().getListAnimation().add(animation);			
		}
		return ok;
	}
		
	public void console(Exception e) {
		String msg[] = null;
		if (e != null && e.getMessage() != null) {
			msg = e.getMessage().split("\n");
		} else if (e != null && e.getCause() != null && e.getCause().getMessage() != null) {
			msg = e.getCause().getMessage().split("\n");
		} else if (e.fillInStackTrace() != null && e.fillInStackTrace().getMessage() != null) {
			msg = e.fillInStackTrace().getMessage().split("\n");
		} else {
			String trace = player.getEngineBean().stackTrace(e);
			if (trace != null)
				msg = trace.split("\n");
		}
		Animation animation = new Animation(null);
		animation.setFailed(true);
		animation.setSound(Animation.SOUND_FAIL);
		animation.setType("code");
		animation.setNamePlayer(player.getName());
		animation.setTeam(player.getTeam());
		animation.setCellEffect("team_"+player.getTeam()+"_d");
		player.getEngineBean().getListAnimation().add(animation);
		this.player.getEngineBean().addMsgConsole(this.player, "error", msg);
	}	
	
	public void console(String str) {
		if (str != null) {
			String msg[] = str.split("\n");
			this.player.getEngineBean().addMsgConsole(this.player, "msg", msg);
		}
	}

	public void console(Object obj) {
		String msg[] = new String[]{"null"};
		if (obj != null) {
			msg = new String[]{obj.toString()};
		}
		this.player.getEngineBean().addMsgConsole(this.player, "msg", msg);
	}

	public Map<String,String> memory() {
		return this.player.getMemory();
	}
}
