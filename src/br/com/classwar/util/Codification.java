package br.com.classwar.util;

import java.io.Serializable;

public class Codification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5120736990527356155L;
	private long id;
	private Player player;
	private String time;
	private String code;
	private String error;
	
	public Codification() {
		
	}
	
	public Codification(long id, Player player, String time, String code, String error) {
		super();
		this.id = id;
		this.player = player;
		this.time = time;
		this.code = code;
		this.error = error;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
