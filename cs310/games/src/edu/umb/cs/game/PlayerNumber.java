// PlayerNumber.java
//
// Ethan Bolker, February 2003
// converted to enum by Betty O'Neil Jan 2009

package edu.umb.cs.game;

/**
 * This class provides five constant values, so that PlayerNumber
 * values can be compared with ==.
 */

public enum PlayerNumber {
	ONE("1", true), TWO("2", true), GAME_OVER("game is over", false), 
		GAME_NOT_OVER("game not over", false), DRAW("draw", false);

	private String name;
	private boolean isRealPlayer;

	private PlayerNumber(String name, boolean isRealPlayer) {
		this.name = name;
		this.isRealPlayer = isRealPlayer;
	}

	/**
	 * Is this a Player in the Game, or some other identifier?
	 * 
	 * @return true if ONE or TWO.
	 */
	public boolean isRealPlayer() {
		return isRealPlayer;
	}
	// override default toString based on ONE, TWO, etc.
	public String toString() {
		return name;
	}
}
