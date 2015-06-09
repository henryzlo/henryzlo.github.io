// Player.java
//
// Ethan Bolker, February 2003

package edu.umb.cs.game;

import edu.umb.cs.gamesui.HumanPlayer;

/**
 * Anyone can play any Game.
 * 
 * @see HumanPlayer
 * @see ComputerPlayer
 */

public abstract class Player {
	private String name;

	/**
	 * Create a Player.
	 * 
	 * @param name
	 *            the Player's name.
	 */
	protected Player(String name) {
		this.name = name;
	}

	/**
	 * Will this Player play a particular Game?
	 * 
	 * @param g
	 *            the Game.
	 * @return true the default.
	 */
	public boolean canPlay(Game g) {
		return true;
	}

	/**
	 * Who is this Player?
	 * 
	 * @return the Player's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Represent this Player as a String.
	 * 
	 * @return name
	 */
	public String toString() {
		return "player " + name;
	}

	/**
	 * This Player decides what to do next ...
	 * 
	 * @param g
	 *            the current Game position.
	 * @return the best Move to make.
	 * @throws GameException.
	 */
	public abstract Move findbest(Game g) throws GameException;

	/**
	 * What this Player wants to do when the current Game is about to start.
	 * 
	 * Default does nothing.
	 * 
	 * @param g
	 *            the Game that's about to start.
	 */
	public void gameStarting(Game g) {
	}

	/**
	 * What this Player wants to do when the current Game is over.
	 * 
	 * Default does nothing.
	 * 
	 * @param g
	 *            the Game that just ended.
	 */
	public void gameOver(Game g) {
	}
}
