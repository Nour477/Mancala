package com.bol.mancala.dto;

public class Player {
	public Player(String name) {
		this.name = name;
		this.isDummy=false; 
	}
	private String name; 
	private boolean isDummy; 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDummy() {
		return isDummy;
	}
	public void setDummy(boolean isDummy) {
		this.isDummy = isDummy;
	}
}
