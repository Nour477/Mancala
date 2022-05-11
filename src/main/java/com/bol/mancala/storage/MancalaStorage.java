package com.bol.mancala.storage;

import java.util.HashMap;
import java.util.Map;

import com.bol.mancala.dto.Game;

public class MancalaStorage {

	private static Map<String, Game> games;
	private static MancalaStorage instance;

	private MancalaStorage() {
		games = new HashMap<>();
	}

	public static synchronized MancalaStorage getInstance() {
		if (instance == null) {
			instance = new MancalaStorage();
		}
		return instance;
	}

	public Map<String, Game> getGames() {
		return games;
	}

	public void setGame(Game game) {
		games.put(game.getGameId(), game);
	}

}
