package com.bol.mancala.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class Game.
 * 
 * @author Nour
 */
public class Game {

	public Game() {
		super();
	}

	public List<Pit> pits;
	private GameStatus status;
	private String winner;
	private String gameId;
	private Player player1;
	private Player player2;
	private PlayerTurn turn;

	public PlayerTurn getTurn() {
		return turn;
	}

	public void setTurn(PlayerTurn turn) {
		this.turn = turn;
	}

	public List<Pit> getPits() {
		return pits;
	}

	public void setPits(LinkedList<Pit> pits) {
		this.pits = pits;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus gameStatus) {
		this.status = gameStatus;
	}

}
