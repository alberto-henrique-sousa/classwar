package br.com.classwar.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.classwar.bean.EngineBean;
import br.com.classwar.units.Unit;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4136450328216942059L;	
	private int team;
	private int id;
	private String name;
	private List<Unit> units;
	private boolean inUse;
	private boolean wait;
	private boolean bad;
	private boolean ready;
	private String code;
	private EngineBean engineBean;
	private int totalUnit;
	private String color;
	private int gold;
	private int countActions;
	private Map<String,String> memory;
    
	public Player(int team, int id, String name, boolean inUse, EngineBean engineBean, boolean wait, String code) {
		super();
		this.units = new ArrayList<Unit>();
		this.team = team;
		this.id = id;
		this.name = name;
		this.inUse = inUse;
		this.engineBean = engineBean;
		this.wait = wait;
		this.code = code;
		this.gold = engineBean.getSettings().getGold();
		this.memory = new LinkedHashMap<String,String>();
	}

	public int getTeam() {
		return team;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
			
	public boolean addUnit(Unit unit, String cell) {
		boolean ok = false;
		try {
			int balance = getGold() - unit.getCost();
			if (balance >= 0) {
				int row = EngineBean.convertRowCell(cell);
				int	col = EngineBean.convertColCell(cell);
				boolean found = false;
				if (row >=0 && row < 4 && col >=0 && col < 12) {
					if (this.team == 1) {
						if (col >= 0 && col < 4)
							found = this.getEngineBean().getBoardGame()[row][col] != null;
						else
							found = true;
					} else if (this.team == 2) {
						if (col >= 8 && col < 12)
							found = this.getEngineBean().getBoardGame()[row][col] != null;
						else
							found = true;
					}
				} else {
					found = true;
				}
				if (!found) {
					teamInstance().addUnit();
					unit.setRow(row);
					unit.setCol(col);
					this.units.add(unit);
					this.engineBean.getBoardGame()[row][col] = unit;
					Animation animation = new Animation(unit);
					animation.setCell(cell);
					animation.setSpriter(Animation.ANIMATION_RUN);
					animation.setSound(Animation.SOUND_RUN);
					animation.setCellEffect("team_"+this.team+"_b");
					animation.setType("add");
					this.getEngineBean().getListAnimation().add(animation);
					String msg[] = {cell + " - " +unit.getTitle()};
					this.engineBean.addMsgConsole(this, "addUnit", msg);
					setGold(getGold()-unit.getCost());
					this.teamInstance().getTotalUnits().update(unit.getTitle(), 1);
					ok = true;
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				String msg[] = e.getMessage().split("\n");
				this.engineBean.addMsgConsole(this, "error", msg);
			}	
		}
		if (!ok) {
			String msg[] = {unit.getTitle() + " - failed..."};
			this.engineBean.addMsgConsole(this, "addUnit", msg);
		}
		return ok;
	}

	public boolean isWait() {
		return wait || this.getEngineBean().isGameOver();
	}

	public void setWait(boolean wait) {
		this.wait = wait;
	}

	public EngineBean getEngineBean() {
		return engineBean;
	}

	public boolean isBad() {
		return bad;
	}

	public void setBad(boolean bad) {
		this.bad = bad;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public String getColor() {
		this.color = (this.team == 1 ? "#357F20" : "#872021");
		return color;
	}

	public int getTotalUnit() {
		this.totalUnit = 0;
		for (Unit unit : units) {
			if (unit != null)
				this.totalUnit++;
		}
		return totalUnit;
	}
	
	public Team teamInstance() {
		Team ret = null;
		if (this.team == 1)
			ret = this.engineBean.getTeam1();
		else if (this.team == 2)
			ret = this.engineBean.getTeam2();
		return ret;
	}
	
	public Team team(int team) {
		Team ret = null;
		if (team == 1)
			ret = this.engineBean.getTeam1();
		else if (team == 2)
			ret = this.engineBean.getTeam2();
		return ret;
	}
	
	public List<Unit> getUnits() {
		return units;
	}

	public int getCountActions() {
		return countActions;
	}

	public void setCountActions(int countActions) {
		this.countActions = countActions;
	}
	
	public void addAction() {
		this.countActions += 1;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public Map<String, String> getMemory() {
		return memory;
	}

	public void setMemory(Map<String, String> memory) {
		this.memory = memory;
	}

}
