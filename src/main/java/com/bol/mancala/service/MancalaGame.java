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
		player2.setDummy(true);
		game.setPlayer1(player1);
		game.setPlayer2(player2);
		// create pits
		LinkedList<Pit> pits = new LinkedList<Pit>();
		for (int i = 0; i < 14; i++) {
			pits.add(new Pit(PILE_COUNT));
		}
		pits.get(6).toggleMancala(); // make as First player Mancala
		pits.get(6).setNumStones(0);
		pits.get(13).toggleMancala(); // make as Second player Mancala
		pits.get(13).setNumStones(0);
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
		game.setStatus(GameStatus.IN_PROGRESS);
		LinkedList<Pit> gamePits = (LinkedList<Pit>) game.getPits();
		if (!gamePits.get(move).isEmpty() && ((game.getTurn().equals(PlayerTurn.P2_Turn) && move > 6)
				|| (game.getTurn().equals(PlayerTurn.P1_Turn) && move < 6))) {
			isLandedMancala = false;
			int nextPit = move;
			nextPit++; // increment to get to next pile
			int numOfStones = gamePits.get(move).getStonesCount();
			for (int i = 1; i <= numOfStones; i++) {
				gamePits.get(move).removeStone();
				gamePits.get(nextPit).addStone();
				if ((game.getTurn().equals(PlayerTurn.P1_Turn) && nextPit == 13)
						|| (game.getTurn().equals(PlayerTurn.P2_Turn) && nextPit == 6)) {
					gamePits.get(move).addStone();
					gamePits.get(nextPit).removeStone();
					i--;
				}
				if (((game.getTurn().equals(PlayerTurn.P2_Turn) && (nextPit == 13))
						|| (game.getTurn().equals(PlayerTurn.P1_Turn) && (nextPit == 6))) && (i == numOfStones)) {
					isLandedMancala = true;
				}

				if (gamePits.get(move).isEmpty() == true && gamePits.get(nextPit).isLastStone()
						&& isAcrossFull(nextPit, gamePits) && isOnYourSide(nextPit, game.getTurn())) {
					// remove added stone from next pit to add it to mancala
					gamePits.get(nextPit).removeStone();
					// remove all stones and add them to Player Mancala
					int opStones = gamePits.get(12 - nextPit).getStonesCount();
					gamePits.get(12 - nextPit).clearPit();
					if (game.getTurn().equals(PlayerTurn.P1_Turn))
						gamePits.get(6).setNumStones(gamePits.get(6).getStonesCount() + opStones + 1);
					else
						gamePits.get(13).setNumStones(gamePits.get(13).getStonesCount() + opStones + 1);
				}
				nextPit++;
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
		game.setPits(gamePits);
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
			gameBoard.setWinner(gameBoard.getPlayer1().getName());
		} else if (gameBoard.getPits().get(6).getStonesCount() == gameBoard.getPits().get(13).getStonesCount()) {
			gameBoard.setWinner(null);
		} else {
			gameBoard.setWinner(gameBoard.getPlayer2().getName());
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

	public Game connectToGame(String playerName, String gameId) throws Exception {
		if (!MancalaStorage.getInstance().getGames().containsKey(gameId)) {
			throw new Exception("Game Doesnt Exist");
		}
		Game game = MancalaStorage.getInstance().getGames().get(gameId);
		if (!game.getPlayer2().isDummy()) {
			throw new Exception("Game is already Occupied");
		}
		Player player = new Player(playerName);
		game.setPlayer2(player);
		game.setStatus(GameStatus.IN_PROGRESS);
		MancalaStorage.getInstance().setGame(game);
		return game;
	}
}