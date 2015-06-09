// GameException.java
//
// Ethan Bolker, February 2003

package edu.umb.cs.game;

/**
 * The generic Exception for reporting errors from the game package.
 */
public class GameException extends Exception {
	private static final long serialVersionUID = -5770733022588839643L;

	public GameException() {
		super();
	}

	public GameException(String s) {
		super(s);
	}
}
