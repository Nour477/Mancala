package com.bol.mancala.service;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bol.mancala.dto.Game;
import com.bol.mancala.dto.Pit;
import com.bol.mancala.dto.PlayerTurn;

public class MancalaGameTest {
	@Autowired
	private IBoardGame mancalaGame =new MancalaGame();

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
	public void testGamePlay1() {
		//case player 1 won 
		Pit mancalaP1 =new Pit (15); 
		Pit mancalaP2 =new Pit (20); 
		Game game=mancalaGame.createGame("Nour");
		List<Pit> pits =new LinkedList <Pit> (); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(1)); 
		pits.add(mancalaP1); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(3)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(5)); 
		pits.add(new Pit(0)); 
		pits.add(mancalaP2); 
		game.setPits((LinkedList<Pit>) pits);
		game.setTurn(PlayerTurn.P1_Turn);
		try {
			mancalaGame.gamePlay(5,game.getGameId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals("Nour", game.getWinner());
	}
	
	@Test
	public void testGamePlay2() {
		//case player 2 won 
		//testing is across full as Pits [4] is moved and pits [5] is 0 and pits [7] is full 
		Pit mancalaP1 =new Pit (15); 
		Pit mancalaP2 =new Pit (20); 
		Game game=mancalaGame.createGame("Nour");
		List<Pit> pits =new LinkedList <Pit> (); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(1)); 
		pits.add(new Pit(0)); 
		pits.add(mancalaP1); 
		pits.add(new Pit(3)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(new Pit(0)); 
		pits.add(mancalaP2); 
		game.setPits((LinkedList<Pit>) pits);
		game.setTurn(PlayerTurn.P1_Turn);
		try {
			mancalaGame.gamePlay(4,game.getGameId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals("New Player", game.getWinner());
		Assert.assertEquals(pits.get(6).getStonesCount(),19); //all stones moved to mancala as across is full
		Assert.assertEquals(pits.get(13).getStonesCount(),20);
	}
	@Test
	public void testConnectToGame() {
		fail("Not yet implemented");
	}

}
