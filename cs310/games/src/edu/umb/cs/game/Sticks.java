// Sticks.java, March 2003 
//
// Created by Qian Huang and Omofolakunmi Ogunlesi 
// based on sticks.c and Easy.java

package edu.umb.cs.game;

/**
 * @author Qian Huang, Omofolakunmi Ogunlesi
 * 
 *         Class Sticks is a concrete game extending from SticksGame and
 *         Game. It is setup with the number of sticks to play and 
 *         selects the player who takes the last one stick
 *         as the winner.
 */
public class Sticks extends SticksBase {

	/**
	 * Construct an instance of the Sticks Game with all the information needed
	 * to run this game.
	 */
	public Sticks() {
		super("Sticks", "Qian Huang and Omofolakunmi Ogunlesi", true,
				DEFAULT_STICKS);
	}
	
	// call this before init
	public void setup(int[] params) throws GameException {
		setStartSticks(params[0]);
	}

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		Sticks g = new Sticks();
		g.isGameNew = isGameNew;
		g.sticksLeft = this.getSticksLeft();
		g.nextPlayer = this.nextPlayer;
		return g;
	}

	/**
	 * View this Game using String i/o.
	 * 
	 * @return a GameStrings.
	 */
	public GameStrings getGameStrings() {
		return new SticksGameStrings();
	}

	// -------------------------------------------------------
	// SticksGameStrings.java, March 2003
	//

	/**
	 * SticksGameStrings is a view for game Sticks in String format. It extends
	 * SticksBaseGameStrings and uses all methods defined in that class. It
	 * implements currentPosition.
	 * 
	 * @see SticksBaseGameStrings
	 */
	private class SticksGameStrings extends SticksBaseGameStrings {
		SticksGameStrings() {
			super(Sticks.this);
		}

		public String[] getSetupParamDefinitions() {
			return new String[] { "numberOfSticks (1-12)" };
		}

	}

}
