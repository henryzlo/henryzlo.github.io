// ComputerPlayer.java
//
// Ethan Bolker, February 2003

package edu.umb.cs.game;

/**
 * A ComputerPlayer is not interactive.
 */
public abstract class ComputerPlayer extends Player {
	/**
	 * Create a ComputerPlayer
	 * 
	 * @param name
	 *            the Player's name (probably the algorithm).
	 */
	public ComputerPlayer(String name) {
		super(name);
	}
}
