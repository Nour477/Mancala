package com.bol.mancala.service;

import com.bol.mancala.dto.Game;
import com.bol.mancala.dto.Player;

/**
 * The Interface IBoardGame.
 *
 * @author Nour
 */
public interface IBoardGame {

	public Game connectToGame(Player player, String gameId) throws Exception;

	public Game getCurrentGameBoard(String gameId) throws Exception;

	public Game createGame(String string);

	public void gamePlay(Integer currentPile, String gameId) throws Exception;

}
