package com.bol.mancala.service;

import com.bol.mancala.model.Game;

/**
 * The Interface IBoardGame.
 *
 * @author Nour
 */
public interface MancalaGameService {

	public Game connectToGame(String playerName, String gameId) throws Exception;

	public Game getCurrentGameBoard(String gameId) throws Exception;

	public Game createGame(String string);

	public void gamePlay(int currentPile, String gameId, String playerName) throws Exception;

}
