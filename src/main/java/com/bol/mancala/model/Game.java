package com.bol.mancala.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.bol.mancala.utils.MancalaConstants;

/**
 * The Class Game.
 * 
 * @author Nour
 */
public class Game {

	public Game() {
		this.setGameId(UUID.randomUUID().toString());
		this.setStatus(GameStatus.NEW);
		this.setTurn(PlayerTurn.P1_Turn);
	}

	public LinkedList<Pit> pits;
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

	public void setPits(LinkedList<Pit> pits2) {
		this.pits = pits2;
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

	public void createPits()
	{
		 pits = new LinkedList<Pit>();
		for (int i = 0; i < 14; i++) {
			pits.add(new Pit(MancalaConstants.INIT_STONES_COUNT));
		}		
		pits.get(MancalaConstants.PLAYER1_MANCALA).toggleMancala(); // make as First player Mancala
		pits.get(MancalaConstants.PLAYER1_MANCALA).setNumStones(MancalaConstants.MANCALA_STONES);
		pits.get(MancalaConstants.PLAYER2_MANCALA).toggleMancala(); // make as Second player Mancala
		pits.get(MancalaConstants.PLAYER2_MANCALA).setNumStones(MancalaConstants.MANCALA_STONES);
		this.setPits(pits);
	}
	public void createPlayer1 (String playerName)
	{
		Player player1 = new Player(playerName);
		Player player2 = new Player("New Player");
		player2.setDummy(true);
		this.setPlayer1(player1);
		this.setPlayer2(player2);
	}
	
	public void createPlayer2 (String playerName)
	{
		Player player2 = new Player(playerName);
		player2.setDummy(false);
		this.setPlayer2(player2);
	}
	
	public void changeTurn()
	{
		if (this.getTurn().equals(PlayerTurn.P1_Turn)) {
			this.setTurn(PlayerTurn.P2_Turn);
		} else {
			this.setTurn(PlayerTurn.P1_Turn);
		}
		
	}
}
