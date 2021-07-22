package br.com.classwar.util;

import java.io.Serializable;

public class Console implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private Player player;
	private String time;
	private String action;
	private String msg;
	
	public Console() {
		
	}
	
	public Console(long id, Player player, String time, String action, String msg) {
		super();
		this.id = id;
		this.player = player;
		this.time = time;
		this.action = action;
		this.msg = msg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
