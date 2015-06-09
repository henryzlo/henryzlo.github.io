// Sticks5.java, March 2003 
//
// Created by Qian Huang and Omofolakunmi Ogunlesi
// based on Sticks.java

//Packages and Imports
package edu.umb.cs.game;

/**
 * @author Qian Huang, Omofolakunmi Ogunlesi
 * 
 *         Class Sticks5 is a concrete game extending from SticksGame and
 *         implementing Interface Game.
 */
public class Sticks5 extends SticksBase {
	/**
	 * Construct an instance of the Sticks Game with the position set for the
	 * start of the game.
	 */
	public Sticks5() {
		super("Sticks5", "Qian Huang and Omofolakunmi Ogunlesi", false, 5);
	}

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		Sticks5 g = new Sticks5();
		g.isGameNew = isGameNew;
		g.sticksLeft = this.getSticksLeft();
		g.nextPlayer = this.nextPlayer;
		return g;
	}

	/**
	 * View this Game using String i/o.
	 * 
	 * @return a GameStrings.
	 * 
	 */
	public GameStrings getGameStrings() {
		return new Sticks5GameStrings();
	}

	// -----------------------------------------------
	// Sticks5GameStrings March 2003
	//

	/**
	 * Sticks5GameStrings is a view for game Sticks5 in String format. It extends
	 * SticksGameGameStrings and uses all methods defined in that class. It
	 * implements currentPosition.
	 * 
	 * @see SticksBaseGameStrings
	 */
	private class Sticks5GameStrings extends SticksBaseGameStrings {
		private Sticks5GameStrings() {
			super(Sticks5.this);
		}
	}
}
