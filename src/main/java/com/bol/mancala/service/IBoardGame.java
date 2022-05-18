package com.bol.mancala.service;

import com.bol.mancala.dto.Game;

/**
 * The Interface IBoardGame.
 *
 * @author Nour
 */
public interface IBoardGame {

	public Game connectToGame(String playerName, String gameId) throws Exception;

	public Game getCurrentGameBoard(String gameId) throws Exception;

	public Game createGame(String string);

	public void gamePlay(Integer currentPile, String gameId, String playerName) throws Exception;

}
