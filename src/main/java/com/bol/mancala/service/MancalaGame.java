package com.bol.mancala.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bol.mancal.utils.GameConstants;
import com.bol.mancala.dto.Game;
import com.bol.mancala.dto.GameStatus;
import com.bol.mancala.dto.MancalaMove;
import com.bol.mancala.dto.Pile;
import com.bol.mancala.dto.Player;
import com.bol.mancala.dto.PlayerTurn;
import com.bol.mancala.storage.MancalaStorage;

/**
 * The Class MancalaGame.
 *
 * @author ashraf
 */
@Service
public class MancalaGame implements IBoardGame {
	/**
	 * Description of instance variables player1: boolean that denotes whether
	 * player1 or player2 is allowed to click on a certain pile on the board
	 * turn1: boolean denoting whose turn it is
	 */
	private Player firstPlayer; 
	private Player secondPlayer;
	private boolean isLandedMancala;
	
    public Game createGame(String playerName) {
        Game game = new Game();       
        Player player1= new Player(playerName); 
        game.setPlayer1(player1);
        LinkedList<Pile> pits = new LinkedList<Pile>(); // creates a new list of piles
		// fills one side of piles LinkedList with 6 piles (4 pebbles each)
		for (int i = 0; i < 6; i++) {
			pits.add(new Pile(GameConstants.PILE_COUNT));
		}
		// creates mancala pile
		pits.add(new Pile(0));
		// fills remaining side of piles LinkedList with 6 piles (4 pebbles
		// each)
		for (int j = 0; j < 6; j++) {
			pits.add(new Pile(GameConstants.PILE_COUNT));
		}
		// creates remaining mancala pile
		pits.get(6).toggleMancala(); // this pile becomes a mancala
		pits.add(new Pile(0));
		pits.get(13).toggleMancala(); // this pile becomes a mancala
        game.setPits(pits);
        game.setGameId(UUID.randomUUID().toString());
        game.setStatus(GameStatus.NEW);
        MancalaStorage.getInstance().setGame(game);
        return game;
    }
	
	public Game gamePlay(MancalaMove mancalaMove) throws Exception {
		if (!MancalaStorage.getInstance().getGames().containsKey(mancalaMove.getGameId())) {
			throw new Exception("Game not found");
		}
		Game game = MancalaStorage.getInstance().getGames().get(mancalaMove.getGameId());
		if (game.getStatus().equals(GameStatus.FINISHED)) {
			throw new Exception("Game is already finished");
		}
		LinkedList<Pile> gamePits = (LinkedList<Pile>) game.getPits();

		if (!gamePits.get(mancalaMove.getMove()).isEmpty() && ((mancalaMove.getTurn().equals(PlayerTurn.P2_Turn)
				&& mancalaMove.getMove()> 6)
				|| (mancalaMove.getTurn().equals(PlayerTurn.P1_Turn)&& mancalaMove.getMove()<6))) {
			isLandedMancala = false;
			int nextPit = mancalaMove.getMove();
			nextPit++; // increment to get to next pile

			while (!gamePits.get(mancalaMove.getMove()).isEmpty()) {
				if (mancalaMove.getTurn().equals(PlayerTurn.P1_Turn) && (nextPit == 6)) {
					gamePits.get(6).addPebble(); // adds pebble to player 1's
													// mancala
					if (gamePits.get(mancalaMove.getMove()).checkIfLastPebble() == true) {
						isLandedMancala = true;
					}
					// we want player1 to go again
					gamePits.get(mancalaMove.getMove()).removePebble(); // need to take into
					// consideration if
					// it's
					// the last one that landed in it
					nextPit++;
				} else if (mancalaMove.getTurn().equals(PlayerTurn.P2_Turn) && (nextPit == 13)) {
					gamePits.get(13).addPebble(); // adds pebble to player 2's
													// mancala
					if (gamePits.get(mancalaMove.getMove()).checkIfLastPebble() == true) {
						isLandedMancala = true;
					}
					gamePits.get(mancalaMove.getMove()).removePebble();
					// if the pebble is dropped into an empty pile,
					nextPit++;
				} else if ((mancalaMove.getTurn().equals(PlayerTurn.P1_Turn) && nextPit == 13)
						|| (mancalaMove.getTurn().equals(PlayerTurn.P2_Turn) && nextPit == 6)) {
					// do nothing
					nextPit++;
				} else if (gamePits.get(mancalaMove.getMove()).checkIfLastPebble() == true
						&& gamePits.get(nextPit).isEmpty() 
						&& isAcrossFull(nextPit, mancalaMove, gamePits) 
						&& isOnYourSide(nextPit, mancalaMove)) {
					gamePits.get(mancalaMove.getMove()).removePebble();
					if (mancalaMove.getTurn().equals(PlayerTurn.P1_Turn) == true) {
						gamePits.get(6).addPebble();
					} else {
						gamePits.get(13).addPebble();
					}

					allotPebbles(nextPit);
					// do not need to update i
				} else {
					gamePits.get(mancalaMove.getMove()).removePebble();

					if (!gamePits.get(nextPit).isEmpty()) {
						gamePits.get(nextPit).addPebble();
					} else {
						gamePits.get(nextPit).addPebble();
						// piles.get(i).peek().toggleLast(true);
					}
					nextPit++;
				}
				if (nextPit == 14) {
					nextPit = 0;
				}
			}
			if (isLandedMancala == false) {
				if (mancalaMove.getTurn().equals(PlayerTurn.P1_Turn)) {
					mancalaMove.setTurn(PlayerTurn.P2_Turn);
				} else {
					mancalaMove.setTurn(PlayerTurn.P1_Turn);
				}
			}
			isGameOver(game);
			MancalaStorage.getInstance().setGame(game);
			return game; 
		}
		return game;
	}
		@Override
		public  void determineWinner(Game gameBoard) {
			if (gameBoard.getPits().get(6).getNumPebbles() > 
				gameBoard.getPits().get(13).getNumPebbles()) {
				gameBoard.setWinner(firstPlayer);
			} else if (gameBoard.getPits().get(6).getNumPebbles() > 
				gameBoard.getPits().get(13).getNumPebbles()) {
				gameBoard.setWinner(null);
			} else {
				gameBoard.setWinner(secondPlayer);
			}
		}
	/**
	 * Determines whether the game has reached its end, by using isSideEmpty to
	 * check the state of each side of the board
	 * @return 
	 * 
	 * @return boolean
	 */
	@Override
	public  void isGameOver(Game game) {
		if (isAnySideEmpty((LinkedList<Pile>) game.getPits())) {
			game.setStatus(GameStatus.FINISHED);				
			determineWinner(game);			
		}
	}
	
	@Override
	public Game getCurrentGameBoard(String gameId) {
		Game game = MancalaStorage.getInstance().getGames().get(gameId);
		return game;
	}

	/**
	 * Determines whether a side of the board is empty.
	 *
	 * @return true, if is side empty
	 */
	private boolean isAnySideEmpty(LinkedList<Pile> pits) {

		int firstPlayerCounter = 0;
		int secondPlayerCounter = 0;
		for (int i = 0; i < 6; i++) {
			firstPlayerCounter = pits.get(i).getNumPebbles();
		}
		for (int i = 7; i < 13; i++) {
			secondPlayerCounter = pits.get(i).getNumPebbles();
		}

		if (firstPlayerCounter == 0) {
			pits.get(6).setNumPebbles(pits.get(6).getNumPebbles() + secondPlayerCounter);
			return true;
		}
		if (secondPlayerCounter == 0) {
			pits.get(13).setNumPebbles(pits.get(13).getNumPebbles() + firstPlayerCounter);
			return true;
		}
		return false;
	}


	/**
	 * Checks whether the pile that has been clicked on is actually on the
	 * current players side.
	 *
	 * @param nationWide the nation wide
	 * @return true, if is on your side
	 */
	public boolean isOnYourSide(Integer nationWide, MancalaMove move) {
		if (move.getTurn().equals(PlayerTurn.P1_Turn) && nationWide <= 5) {
			return true;
		} else if (move.getTurn().equals(PlayerTurn.P2_Turn) && nationWide > 5) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * During a move, checks whether the last pebble to be distributed landed in
	 * an empty pile with pebbles across from it. Will call findAcross() method
	 * in order to find the Pile to check.
	 *
	 * @param thisOne the this one
	 * @return true, if is across full
	 */
	public boolean isAcrossFull(int pitIndex, MancalaMove move,LinkedList<Pile> pits )  {
		Integer pileAcross = 12 - pitIndex;
		if (pits.get(pileAcross).isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Redistributes pebbles from a given pile to the mancala of the current
	 * player.
	 *
	 * @param i the i
	 */
	public void allotPebbles(Integer i) {
//		while (!piles.get(12 - i).isEmpty()) {
//			if (player1 == true) {
//				piles.get(12 - i).removePebble();
//				piles.get(6).addPebble();
//			} else {
//				piles.get(12 - i).removePebble();
//				piles.get(13).addPebble();
//			}
//		}
	}

	
	
    public Game connectToGame(Player player2, String gameId) throws Exception  {
        if (!MancalaStorage.getInstance().getGames().containsKey(gameId)) {
           throw new Exception("Game Doesnt Exist"); 
        }
        Game game = MancalaStorage.getInstance().getGames().get(gameId);
        if (game.getPlayer2() != null) {
        	throw new Exception("Game is already Occupied"); 
        }
        game.setPlayer2(player2);
        game.setStatus(GameStatus.IN_PROGRESS);
        MancalaStorage.getInstance().setGame(game);
        return game;
    }

	@Override
	public void startNewGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeSingleMove(Integer currentPile) {
		// TODO Auto-generated method stub
		
	}
}