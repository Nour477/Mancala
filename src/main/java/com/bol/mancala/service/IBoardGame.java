package com.bol.mancala.service;

import com.bol.mancala.dto.Game;
import com.bol.mancala.dto.Player;

/**
 * The Interface IBoardGame.
 *
 * @author ashraf
 */
public interface IBoardGame {

	public void determineWinner(Game gameBoard);

	public void startNewGame();

	public void isGameOver(Game game);

	public void makeSingleMove(Integer currentPile);

	public Game connectToGame(Player player, String gameId) throws Exception;

	Game getCurrentGameBoard(String gameId);

	public Game createGame(String string);

}
