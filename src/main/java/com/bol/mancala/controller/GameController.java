package com.bol.mancala.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.bol.mancala.dto.Game;
import com.bol.mancala.service.IBoardGame;


/**
 * The Class GameController.
 *
 * @author Nour
 */
@Controller
public class GameController {

	 @Autowired
	 private IBoardGame mancalaGame; 

	/**
	 * Start.
	 *
	 * @return the game board
	 * @throws Exception the exception
	 */
	@MessageMapping("/start")
	@SendTo("/topic/game")
	public Game start() throws Exception {
		Game game= mancalaGame.createGame("Nour");
		return game;
	}
	
//	@MessageMapping("/connectToGame")
//	@SendTo("/topic/game")
//	public Game connectToGame() throws Exception {
//		Game gameBoard= mancalaGame.createGame("Nour");
//		//Game gameBoard = mancalaGame.getCurrentGameBoard();
//		return gameBoard;
//	}
	
	
	
	/**
	 * Move.
	 *
	 * @param input the current pit
	 * @return Game
	 * @throws Exception the exception
	 */
	@MessageMapping("/move")
	@SendTo("/topic/game")
	public Game move(String input) throws Exception {
		String [] param =new String [2]; 
		param = input.split(","); 
		Integer currentPile= Integer. parseInt(param[0]);
		String gameId = param[1]; 
		Game game = mancalaGame.getCurrentGameBoard(gameId);
		mancalaGame.gamePlay(currentPile,gameId);
		return game;
	}
	

}