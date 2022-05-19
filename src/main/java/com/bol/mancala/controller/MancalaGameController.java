package com.bol.mancala.controller;

import java.util.LinkedList;

import org.springframework.stereotype.Service;

import com.bol.mancala.model.Game;
import com.bol.mancala.model.GameStatus;
import com.bol.mancala.model.Pit;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.service.MancalaGameService;
import com.bol.mancala.storage.MancalaStorage;
import com.bol.mancala.utils.MancalaConstants;

/**
 *
 * @author Nour
 */
@Service
public class MancalaGameController implements MancalaGameService {

	private boolean isLandedMancala;

	/**
	 * Creates new game from player Name
	 * 
	 * @return Game
	 */
	public Game createGame(String playerName) {
		Game game = new Game();
		// create players
		game.createPlayer1(playerName);
		game.createPits();
		MancalaStorage.getInstance().setGame(game);
		return game;
	}

	/**
	 * Connects to Existing Game
	 * 
	 * @param String PlayerName: Second Player Name
	 * @param String gameId: Id of the game to connect to
	 * @return Game
	 */
	public Game connectToGame(String playerName, String gameId) throws Exception {
		if (!MancalaStorage.getInstance().getGames().containsKey(gameId)) {
			throw new Exception("Game Doesnt Exist");
		}
		Game game = MancalaStorage.getInstance().getGames().get(gameId);
		if (!game.getPlayer2().isDummy()) {
			throw new Exception("Game is already Occupied");
		}
		game.createPlayer2(playerName);
		game.setStatus(GameStatus.IN_PROGRESS);
		MancalaStorage.getInstance().setGame(game);
		return game;
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

	/**
	 * make a move to an Existing game
	 * 
	 * @param Integer move: Pit selected to move stones
	 * @param String  gameId: game Id to apply move to
	 */
	public void gamePlay(int move, String gameId, String playerId) throws Exception {
		Game game = getCurrentGameBoard(gameId);
		game.setStatus(GameStatus.IN_PROGRESS);
		LinkedList<Pit> gamePits = (LinkedList<Pit>) game.getPits();
		PlayerTurn gameTurn = game.getTurn();
		if (canMove(game, move, playerId)) {
			isLandedMancala = false;
			int nextPit = move;
			nextPit++; // increment to get to next pile
			int numOfStones = gamePits.get(move).getStonesCount();
			for (int i = 1; i <= numOfStones; i++) {
				gamePits.get(move).removeStone();
				gamePits.get(nextPit).addStone();
				if (isBeforeOppMancala(gameTurn, nextPit)) {
					gamePits.get(move).addStone();
					gamePits.get(nextPit).removeStone();
					i--;
				}
				if ((isBeforeMyMancala(gameTurn, nextPit)) && (i == numOfStones)) {
					isLandedMancala = true;
				}

				if (gamePits.get(move).isEmpty() && gamePits.get(nextPit).isLastStone()
						&& isAcrossFull(nextPit, gamePits) && isOnYourSide(nextPit, gameTurn)) {
					// remove all stones and add them to Player Mancala
					takeOppositStones(game, nextPit);
				}
				nextPit++;
				if (nextPit == 14) {
					nextPit = 0;
				}
			}
			if (!isLandedMancala) {
				game.changeTurn();
			}
		}
		game.setPits(gamePits);
		isGameOver(game);
		MancalaStorage.getInstance().setGame(game);
	}

	private void isGameOver(Game game) {
		if (isAnySideEmpty((LinkedList<Pit>) game.getPits())) {
			game.setStatus(GameStatus.FINISHED);
			determineWinner(game);
		}
	}

	private void determineWinner(Game game) {
		if (game.getPits().get(MancalaConstants.PLAYER1_MANCALA).getStonesCount() > game.getPits().get(MancalaConstants.PLAYER2_MANCALA).getStonesCount()) {
			game.setWinner(game.getPlayer1().getName());
		} else if (game.getPits().get(MancalaConstants.PLAYER1_MANCALA).getStonesCount() == game.getPits().get(MancalaConstants.PLAYER2_MANCALA).getStonesCount()) {
			game.setWinner(null);
		} else {
			game.setWinner(game.getPlayer2().getName());
		}
	}

	// returns True is Any of the sides are Empty
	private boolean isAnySideEmpty(LinkedList<Pit> pits) {

		int firstPlayerCounter = 0;
		int secondPlayerCounter = 0;
		for (int i = 0; i < 6; i++) {
			firstPlayerCounter += pits.get(i).getStonesCount();
			secondPlayerCounter += pits.get(i + 7).getStonesCount();
		}
		if (firstPlayerCounter == 0) {
			pits.get(MancalaConstants.PLAYER1_MANCALA)
					.setNumStones(pits.get(MancalaConstants.PLAYER1_MANCALA).getStonesCount() + secondPlayerCounter);
			return true;
		} else if (secondPlayerCounter == 0) {
			pits.get(MancalaConstants.PLAYER2_MANCALA)
					.setNumStones(pits.get(MancalaConstants.PLAYER2_MANCALA).getStonesCount() + firstPlayerCounter);
			return true;
		} else {
			return false;
		}
	}

	// returns True if Pit is on current Player Side
	public boolean isOnYourSide(int pitIndex, PlayerTurn playerTurn) {
		if (playerTurn.equals(PlayerTurn.P1_Turn) && pitIndex <= 5) {
			return true;
		} else if (playerTurn.equals(PlayerTurn.P2_Turn) && pitIndex > 5) {
			return true;
		} else {
			return false;
		}
	}

	// returns true if the opposit Pit is not empty
	public boolean isAcrossFull(int pitIndex, LinkedList<Pit> pits) {
		Integer pileAcross = 12 - pitIndex;
		if (pits.get(pileAcross).isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean canMove(Game game, int pitIndex, String playerId) {
		if (!game.getPits().get(pitIndex).isEmpty() && isOnYourSide(pitIndex, game.getTurn())
				&& isPlayerTurn(game, playerId)) {
			return true;
		} else {
			return false;
		}

	}

	private boolean isPlayerTurn(Game game, String playerId) {
		if ((game.getTurn().equals(PlayerTurn.P1_Turn) && playerId.equals(game.getPlayer1().getId()))
				|| (game.getTurn().equals(PlayerTurn.P2_Turn) && playerId.equals(game.getPlayer2().getId()))) {
			return true;

		} else {
			return false;
		}

	}

	private boolean isBeforeOppMancala(PlayerTurn playerTurn, int nextPitIndex) {
		if ((playerTurn.equals(PlayerTurn.P1_Turn) && nextPitIndex == MancalaConstants.PLAYER2_MANCALA)
				|| (playerTurn.equals(PlayerTurn.P2_Turn) && nextPitIndex == MancalaConstants.PLAYER1_MANCALA)) {
			return true;
		} else {
			return false;
		}

	}

	private boolean isBeforeMyMancala(PlayerTurn playerTurn, int nextPitIndex) {
		if ((playerTurn.equals(PlayerTurn.P1_Turn) && nextPitIndex == MancalaConstants.PLAYER1_MANCALA)
				|| (playerTurn.equals(PlayerTurn.P2_Turn) && nextPitIndex == MancalaConstants.PLAYER2_MANCALA)) {
			return true;
		} else {
			return false;
		}

	}

	private void takeOppositStones(Game game, int nextPitIndex) {
		int opStones = game.getPits().get(12 - nextPitIndex).getStonesCount();

		game.getPits().get(nextPitIndex).removeStone();
		// remove all stones and add them to Player Mancala
		game.getPits().get(12 - nextPitIndex).clearPit();
		if (game.getTurn().equals(PlayerTurn.P1_Turn)) {
			game.getPits().get(MancalaConstants.PLAYER1_MANCALA)
					.setNumStones(game.getPits().get(MancalaConstants.PLAYER1_MANCALA).getStonesCount() + opStones + 1);
		} else {
			game.getPits().get(MancalaConstants.PLAYER2_MANCALA)
					.setNumStones(game.getPits().get(MancalaConstants.PLAYER2_MANCALA).getStonesCount() + opStones + 1);
		}

	}
}