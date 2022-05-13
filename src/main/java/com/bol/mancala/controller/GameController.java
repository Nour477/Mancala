package com.bol.mancala.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bol.mancala.controller.dto.ConnectRequest;
import com.bol.mancala.dto.Game;
import com.bol.mancala.service.IBoardGame;


/**
 * The Class GameController.
 *
 * @author ashraf
 */
@Controller
public class GameController {

	 @Autowired
//	private IBoardGame mancalaGame=new MancalaGame();
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
		Game gameBoard= mancalaGame.createGame("Nour");
		//Game gameBoard = mancalaGame.getCurrentGameBoard();
		return gameBoard;
	}
	
	/**
	 * Move.
	 *
	 * @param currentPile the current pile
	 * @return the game board
	 * @throws Exception the exception
	 */
	@MessageMapping("/move")
	@SendTo("/topic/game")
	public Game move(Integer currentPile, String gameId) throws Exception {
		mancalaGame.makeSingleMove(currentPile);
		Game gameBoard = mancalaGame.getCurrentGameBoard(gameId);
		return gameBoard;
	}
	
    @PostMapping("/connect")
    public ResponseEntity<Object> connect(@RequestBody ConnectRequest request)throws Exception  {
    //    log.info("connect request: {}", request);
        return ResponseEntity.ok(mancalaGame.connectToGame(request.getPlayer(), request.getGameId()));
    }

}