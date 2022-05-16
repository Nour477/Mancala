package com.bol.mancala.dto;

/**
 * The Class Pit.
 *
 * @author Nour
 */
public class Pit {

	private int stonesCount;
	public boolean isMancala;

	public Pit(Integer numStones) {
		stonesCount = numStones;
		isMancala = false;
	}

	public void toggleMancala() {
		isMancala = true;
	}

	public int getStonesCount() {
		return stonesCount;
	}

	public void clearPit() {
		stonesCount = 0;
	}

	public void addStone() {
		stonesCount++;
	}

	public void removeStone() {
		stonesCount--;
	}

	public boolean isLastStone() {
		return (stonesCount == 1) ? true : false;
	}

	public void setNumStones(int numPebbles) {
		stonesCount = numPebbles;
	}

	public boolean isEmpty() {
		return (stonesCount == 0) ? true : false;
	}

}
