package com.bol.mancala.controller.dto;

import com.bol.mancala.dto.Player;

public class ConnectRequest {
	
	public ConnectRequest(Player player, String gameId) {
		super();
		this.player = player;
		this.gameId = gameId;
	}
	private Player player ; 
	private String gameId;
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	} 

}
