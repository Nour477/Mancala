package com.bol.mancala.dto;

public class MancalaMove {
    private PlayerTurn turn;
    private String gameId;
    private Integer move;
	public PlayerTurn getTurn() {
		return turn;
	}
	public void setTurn(PlayerTurn turn) {
		this.turn = turn;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public Integer getMove() {
		return move;
	}
	public void setMove(Integer move) {
		this.move = move;
	} 

}
