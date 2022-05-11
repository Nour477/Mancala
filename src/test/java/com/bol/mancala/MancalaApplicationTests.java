package com.bol.mancala;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bol.mancala.dto.Game;
import com.bol.mancala.service.IBoardGame;

@SpringBootTest
@RunWith(SpringRunner.class)
class MancalaApplicationTests {
	
	/*


	
	@Autowired
	private IBoardGame mancalaGame;

	@Test
	public void testStartNewGame() {
		
		mancalaGame.startNewGame();
		
		GameBoard gameBoard = mancalaGame.getCurrentGameBoard();
				
		List<Integer> piles = gameBoard.getPiles();
		
		Assert.assertEquals(14, piles.size());
		
		Assert.assertEquals("Error in piles size!", 14, piles.size());
		
		for(int i=0; i<6; i++)
			Assert.assertEquals("Error in the first player pebbles size!", Integer.valueOf(6), piles.get(i));
		
		for(int i=7; i<13; i++)
			Assert.assertEquals("Error in the second player pebbles size!", Integer.valueOf(6), piles.get(i));
		
		Assert.assertEquals("Error in the first player mancala!", Integer.valueOf(0), piles.get(6));
		
		Assert.assertEquals("Error in the second player mancala!", Integer.valueOf(0), piles.get(13));
	}
	
	@Test
	public void testMakeSingleMove() {
		
		mancalaGame.startNewGame();
		mancalaGame.makeSingleMove(0);
		GameBoard gameBoard = mancalaGame.getCurrentGameBoard();
		List<Integer> piles = gameBoard.getPiles();
		
		for(int i=1; i<6; i++)
			Assert.assertEquals("Error in the first player pebbles size!", Integer.valueOf(7), piles.get(i));
		
		Assert.assertEquals("Error in the first player mancala!", Integer.valueOf(1), piles.get(6));
		
	}
	
	@Test
	public void testDetermineWinner() {
		
		mancalaGame.startNewGame();
		
		//Tie case
		mancalaGame.determineWinner();
		GameBoard gameBoard = mancalaGame.getCurrentGameBoard();
		Assert.assertEquals("Error in determineWinner!", "It is a tie!", gameBoard.getWinner());
		
		//Player 1 won
		mancalaGame.makeSingleMove(0);
		mancalaGame.determineWinner();
		GameBoard gameBoard2 = mancalaGame.getCurrentGameBoard();
		Assert.assertEquals("Error in determineWinner!", "Player 1 won!", gameBoard2.getWinner());
	}
	*/
}
