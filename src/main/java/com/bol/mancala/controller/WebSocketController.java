package com.bol.mancala.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.bol.mancala.model.Game;
import com.bol.mancala.service.MancalaGameService;

/**
 *
 * @author Nour
 */
@Controller
public class WebSocketController {

	@Autowired
	private MancalaGameService mancalaGame;

	/**
	 * Start.
	 * @param name:PlayerName
	 * @return Game
	 * @throws Exception 
	 */
	@MessageMapping("/start")
	@SendTo("/topic/game")
	public Game start(String name) throws Exception {
		Game game = mancalaGame.createGame(name);
		return game;
	}

	/**
	 * connectToGame.
	 * @param input: String[PlayerName, GameId]
	 * @return Game
	 * @throws Exception 
	 */
	
	@MessageMapping("/connectToGame")
	@SendTo("/topic/game")
	public Game connectToGame(String input) throws Exception {
		String[] param = new String[2];
		param = input.split(",");
		String playerName = param[0];
		String gameId = param[1];
		Game game = mancalaGame.connectToGame(playerName, gameId);
		return game;
	}
	/**
	 * Move.
	 *
	 * @param input: String[current pit,GameId]
	 * @return Game
	 * @throws Exception 
	 */
	@MessageMapping("/move")
	@SendTo("/topic/game")
	public Game move(String input) throws Exception {
		String[] param = new String[3];
		param = input.split(",");
		int currentPit = Integer.parseInt(param[0]);
		String gameId = param[1];
		String playerId = param[2];
		Game game = mancalaGame.getCurrentGameBoard(gameId);
		mancalaGame.gamePlay(currentPit, gameId, playerId);
		return game;
	}
}