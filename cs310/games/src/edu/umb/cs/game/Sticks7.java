// Sticks7.java, March 2003 
//
// Created by Qian Huang and Omofolakunmi Ogunlesi 
// based on Sticks.java

//Packages and Imports
package edu.umb.cs.game;

/**
 * @author Qian Huang, Omofolakunmi Ogunlesi
 * 
 *         Class Sticks7 is a concrete game extending from SticksGame and
 *         implementing Interface Game.
 */
public class Sticks7 extends SticksBase {
	/**
	 * Construct an instance of the Sticks Game with the position set for the
	 * start of the game.
	 */
	public Sticks7() {
		super("Sticks7", "Qian Huang and Omofolakunmi Ogunlesi", false, 7);
	}

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		Sticks7 g = new Sticks7();
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
		return new Sticks7GameStrings();
	}

	// -----------------------------------------------
	// Sticks7GameStrings, March 2003
	//

	/**
	 * Sticks7GameStrings is a view for game Sticks7 in String format. It extends
	 * SticksGameStrings (abstract) and uses all methods defined in that class. 
	 * 
	 * @see SticksBaseGameStrings
	 */
	private class Sticks7GameStrings extends SticksBaseGameStrings {
		private Sticks7GameStrings() {
			super(Sticks7.this);
		}
	}
}
