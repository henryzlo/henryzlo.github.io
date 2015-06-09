// NoSuchMoveException.java
//
// Ethan Bolker, February 2003

package edu.umb.cs.game;

/**
 * Exception returned when a String that ought to specify a Move doesn't.
 */
public class NoSuchMoveException extends GameException {

	private static final long serialVersionUID = -5961374825386470428L;
	private String badMoveString;

	/**
	 * Create a NoSuchMoveException.
	 * 
	 * @param s
	 *            the String that should specify a Move.
	 */
	public NoSuchMoveException(String s) {
		badMoveString = s;
	}

	/**
	 * Get the faultuy String.
	 * 
	 * @return the String that should specify a Move.
	 */
	public String getBadMoveString() {
		return badMoveString;
	}

	public String toString() {
		return super.toString() + " " + badMoveString;
	}
}
