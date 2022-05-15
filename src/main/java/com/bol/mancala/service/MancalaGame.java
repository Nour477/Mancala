package com.bol.mancala.service;

import java.util.LinkedList;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.bol.mancala.dto.Game;
import com.bol.mancala.dto.GameStatus;
import com.bol.mancala.dto.Pit;
import com.bol.mancala.dto.Player;
import com.bol.mancala.dto.PlayerTurn;
import com.bol.mancala.storage.MancalaStorage;

/**
 *
 * @author Nour
 */
@Service
public class MancalaGame implements IBoardGame {
	private static final Integer PILE_COUNT = 6;
	private boolean isLandedMancala;
	/**
	 * Creates new game from player Name
	 * 
	 * @return Game
	 */
	public Game createGame(String playerName) {
		Game game = new Game();
		// create players
		Player player1 = new Player(playerName);
		Player player2 = new Player("New Player");
		game.setPlayer1(player1);
		game.setPlayer2(player2);
		// create pits
		LinkedList<Pit> pits = new LinkedList<Pit>();
		for (int i = 0; i < 14; i++) {
			pits.add(new Pit(PILE_COUNT));
		}
		pits.get(6).toggleMancala(); // make as First player Mancala
		pits.get(13).toggleMancala(); // make as Second player Mancala
		game.setPits(pits);
		game.setGameId(UUID.randomUUID().toString());
		game.setStatus(GameStatus.NEW);
		game.setTurn(PlayerTurn.P1_Turn);
		MancalaStorage.getInstance().setGame(game);
		return game;
	}

	/**
	 * make a move to an Existing game
	 */
	public void gamePlay(Integer move, String gameId) throws Exception {
		Game game = getCurrentGameBoard(gameId);
		LinkedList<Pit> gamePits = (LinkedList<Pit>) game.getPits();
		if (!gamePits.get(move).isEmpty() && ((game.getTurn().equals(PlayerTurn.P2_Turn) && move > 6)
				|| (game.getTurn().equals(PlayerTurn.P1_Turn) && move < 6))) {
			isLandedMancala = false;
			int nextPit = move;
			nextPit++; // increment to get to next pile
			while (!gamePits.get(move).isEmpty()) {
				if (game.getTurn().equals(PlayerTurn.P1_Turn) && (nextPit == 6)) {
					gamePits.get(6).addStone(); // adds pebble to player 1's
												// mancala
					if (gamePits.get(move).isLastStone() == true) {
						isLandedMancala = true;
					}
					gamePits.get(move).removeStone();
					nextPit++;
				} else if (game.getTurn().equals(PlayerTurn.P2_Turn) && (nextPit == 13)) {
					gamePits.get(13).addStone(); // adds pebble to player 2's
													// mancala
					if (gamePits.get(move).isLastStone() == true) {
						isLandedMancala = true;
					}
					gamePits.get(move).removeStone();
					// if the pebble is dropped into an empty pile,
					nextPit++;
				} else if ((game.getTurn().equals(PlayerTurn.P1_Turn) && nextPit == 13)
						|| (game.getTurn().equals(PlayerTurn.P2_Turn) && nextPit == 6)) {
					nextPit++;
				} else if (gamePits.get(move).isLastStone() == true && gamePits.get(nextPit).isEmpty()
						&& isAcrossFull(nextPit, gamePits) && isOnYourSide(nextPit, game.getTurn())) {
					gamePits.get(move).removeStone();
					if (game.getTurn().equals(PlayerTurn.P1_Turn) == true) {
						gamePits.get(6).addStone();
					} else {
						gamePits.get(13).addStone();
					}

					// remove all stones and add them to Player Mancala
					int opStones = gamePits.get(12 - nextPit).getStonesCount();
					gamePits.get(12 - nextPit).clearPit();
					if (game.getTurn().equals(PlayerTurn.P1_Turn))
						gamePits.get(6).setNumStones(gamePits.get(6).getStonesCount() + opStones);
					else
						gamePits.get(13).setNumStones(gamePits.get(13).getStonesCount() + opStones);

					// do not need to update i
				} else {
					gamePits.get(move).removeStone();
					if (!gamePits.get(nextPit).isEmpty()) {
						gamePits.get(nextPit).addStone();
					}
					nextPit++;
				}
				if (nextPit == 14) {
					nextPit = 0;
				}
			}
			if (isLandedMancala == false) {
				if (game.getTurn().equals(PlayerTurn.P1_Turn)) {
					game.setTurn(PlayerTurn.P2_Turn);
				} else {
					game.setTurn(PlayerTurn.P1_Turn);
				}
			}
		}
		isGameOver(game);
		MancalaStorage.getInstance().setGame(game);
	}

	/**
	 * takes a Game as input and set the Winner to the game
	 * 
	 * @param Game
	 */
	private void determineWinner(Game gameBoard) {
		if (gameBoard.getPits().get(6).getStonesCount() > gameBoard.getPits().get(13).getStonesCount()) {
			gameBoard.setWinner(gameBoard.getPlayer1());
		} else if (gameBoard.getPits().get(6).getStonesCount() > gameBoard.getPits().get(13).getStonesCount()) {
			gameBoard.setWinner(null);
		} else {
			gameBoard.setWinner(gameBoard.getPlayer1());
		}
	}

	private void isGameOver(Game game) {
		if (isAnySideEmpty((LinkedList<Pit>) game.getPits())) {
			game.setStatus(GameStatus.FINISHED);
			determineWinner(game);
		}
	}

	/**
	 * @param gameId takes gameId as a String Parameter
	 * @return Game
	 */
	@Override
	public Game getCurrentGameBoard(String gameId) throws Exception {
		if (!MancalaStorage.getInstance().getGames().containsKey(gameId)) {
			throw new Exception("Game not found");
		}
		Game game = MancalaStorage.getInstance().getGames().get(gameId);
		if (game.getStatus().equals(GameStatus.FINISHED)) {
			throw new Exception("Game is already finished");
		}
		return game;
	}

	private boolean isAnySideEmpty(LinkedList<Pit> pits) {

		int firstPlayerCounter = 0;
		int secondPlayerCounter = 0;
		for (int i = 0; i < 6; i++) {
			firstPlayerCounter += pits.get(i).getStonesCount();
			secondPlayerCounter += pits.get(i + 7).getStonesCount();
		}
		if (firstPlayerCounter == 0) {
			pits.get(6).setNumStones(pits.get(6).getStonesCount() + secondPlayerCounter);
			return true;
		}
		if (secondPlayerCounter == 0) {
			pits.get(13).setNumStones(pits.get(13).getStonesCount() + firstPlayerCounter);
			return true;
		}
		return false;
	}

	public boolean isOnYourSide(Integer nationWide, PlayerTurn playerTurn) {
		if (playerTurn.equals(PlayerTurn.P1_Turn) && nationWide <= 5) {
			return true;
		} else if (playerTurn.equals(PlayerTurn.P2_Turn) && nationWide > 5) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAcrossFull(int pitIndex, LinkedList<Pit> pits) {
		Integer pileAcross = 12 - pitIndex;
		if (pits.get(pileAcross).isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public Game connectToGame(Player player2, String gameId) throws Exception {
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
}