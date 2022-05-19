package com.bol.mancala.model;

import java.util.UUID;

public class Player {
	public Player(String name) {
		this.name = name;
		this.isDummy=false; 
		this.id=UUID.randomUUID().toString(); 
	}
	private String name; 
	private boolean isDummy; 
	private String id; 
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

	public String getId() {
		return this.id;
	}
}
