package com.bol.mancala.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bol.mancala.controller.MancalaGameController;
import com.bol.mancala.model.Game;
import com.bol.mancala.model.Pit;
import com.bol.mancala.utils.MancalaConstants;

public class MancalaGameTest {
	@Autowired
	private MancalaGameService mancalaGame =new MancalaGameController();

	@Test
	public void testCreateGame() {
		Game game=mancalaGame.createGame("Nour");	
		List<Pit> pits = game.getPits();
		Assert.assertEquals(14, pits.size());
		Assert.assertEquals(6, pits.get(0).getStonesCount());
		Assert.assertEquals(true, pits.get(6).isMancala);
		Assert.assertEquals(0, pits.get(13).getStonesCount());
	}

	@Test
	public void testGamePlay1() throws Exception {
		//case player 1 won 
		Game game=mancalaGame.createGame("Nour");
		game.getPits().get(MancalaConstants.PLAYER1_MANCALA).setNumStones(22);
		game.getPits().get(MancalaConstants.PLAYER2_MANCALA).setNumStones(20);
		game.getPits().get(0).setNumStones(0);
		game.getPits().get(1).setNumStones(0);
		game.getPits().get(2).setNumStones(0);
		game.getPits().get(3).setNumStones(0);
		game.getPits().get(4).setNumStones(0);

		mancalaGame.gamePlay(5,game.getGameId(),game.getPlayer1().getId());
		Assert.assertEquals("Nour", game.getWinner());
	}
	
	@Test
	public void testGamePlay2() throws Exception {
		//case player 2 won 
		//testing is across full as Pits [4] is moved and pits [5] is 0 and pits [7] is full 
		
		Game game=mancalaGame.createGame("Nour");
		game.getPits().get(MancalaConstants.PLAYER1_MANCALA).setNumStones(15);
		game.getPits().get(MancalaConstants.PLAYER2_MANCALA).setNumStones(20);
		game.getPits().get(0).setNumStones(0);
		game.getPits().get(1).setNumStones(0);
		game.getPits().get(2).setNumStones(0);
		game.getPits().get(3).setNumStones(0);
		game.getPits().get(4).setNumStones(1);
		game.getPits().get(5).setNumStones(0);
		game.getPits().get(7).setNumStones(3);
		game.getPits().get(8).setNumStones(0);
		game.getPits().get(9).setNumStones(0);
		game.getPits().get(10).setNumStones(0);
		game.getPits().get(11).setNumStones(0);
		game.getPits().get(12).setNumStones(0);

		mancalaGame.gamePlay(4,game.getGameId(),game.getPlayer1().getId());
		Assert.assertEquals("New Player", game.getWinner());
		Assert.assertEquals(game.getPits().get(MancalaConstants.PLAYER1_MANCALA).getStonesCount(),19); //all stones moved to mancala as across is full
		Assert.assertEquals(game.getPits().get(MancalaConstants.PLAYER2_MANCALA).getStonesCount(),20);
	}
	@Test
	public void testConnectToGame() throws Exception {
		Game game=mancalaGame.createGame("Nour");	
		mancalaGame.connectToGame("John",game.getGameId()); 
		Assert.assertEquals(game.getPlayer1().getName(),"Nour");
		Assert.assertEquals(game.getPlayer2().getName(),"John");
	}
}
