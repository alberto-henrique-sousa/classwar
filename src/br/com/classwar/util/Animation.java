package br.com.classwar.util;

import java.io.Serializable;

import br.com.classwar.units.Unit;

public class Animation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1071354828436010114L;
	private Unit unit;
	private String cell;
	private String spriter;
	private String sound;
	private String cellEffect;
	private String type;
	private int team;
	private String namePlayer;
	private boolean failed;
	private boolean rotate;
	public static String ANIMATION_IDLE = "idle";
	public static String ANIMATION_RUN = "run";
	public static String ANIMATION_ATTACK = "attack";
	public static String ANIMATION_DEFEND = "defend";
	public static String ANIMATION_DEAD = "dead";
	public static String NO_SOUND = "";
	public static String SOUND_RUN = "run";
	public static String SOUND_ATTACK = "attack";
	public static String SOUND_DEFEND = "defend";
	public static String SOUND_DEAD = "dead";
	public static String SOUND_FAIL = "fail";
	public static String SOUND_GAMEOVER = "gameover";
	public static String SOUND_CHAMPION = "champion";

	public Animation() {
		reset();
	}
	
	public Animation(Unit unit) {
		this.unit = unit;		
		reset();
	}
	
	public void reset() {
		this.spriter = ANIMATION_IDLE;
		this.sound = NO_SOUND;
		this.cell = "";
		this.cellEffect = "";
		this.failed = false;
		this.namePlayer = "";
	}
		
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getSpriter() {
		return spriter;
	}

	public void setSpriter(String spriter) {
		this.spriter = spriter;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public String getCellEffect() {
		return cellEffect;
	}

	public void setCellEffect(String cellEffect) {
		this.cellEffect = cellEffect;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public String getNamePlayer() {
		return namePlayer;
	}

	public void setNamePlayer(String namePlayer) {
		this.namePlayer = namePlayer;
	}

	public boolean isRotate() {
		return rotate;
	}

	public void setRotate(boolean rotate) {
		this.rotate = rotate;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

}
