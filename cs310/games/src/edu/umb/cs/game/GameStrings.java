// GameStrings.java
//
// Ethan Bolker, March 2003 
// Mar 09: renamed from StringView, to avoid confusion with true views

package edu.umb.cs.game;

/**
 * Support for a String "view" of a Game, no actual i/o
 * Useful for line-oriented UI, and GUIs too. Also debugging.
 * Returned by Game.getGameStrings()
 */
public abstract class GameStrings {

	/**
	 * Support various String methods for a Game.
	 * 
	 */
	public GameStrings() {
	}

	/**
	 * What are the rules of the game? How are moves entered interactively?
	 * May be called before init/initObservable
	 */
	public abstract String help();
	
	/**
	 * Get setup parameter definitions
	 * Only needed if game needs setup, i.e., needSetup returns true
	 * May be called before init/initObservable
	 * @return  String[] descriptions of needed setup parameters
	 * @throws GameException
	 */
	public String[] getSetupParamDefinitions() throws GameException {
		throw new GameException("need getSetupParamDefinitions for this game");
	}
	
	/**
	 * Describe the current position.
	 */
	public abstract String position();

	/**
	 * When Players enter moves interactively the Game must be able to interpret
	 * what they say.
	 * 
	 * @param s
	 *            the String containing the Move.
	 * 
	 * @return the corresponding Move.
	 * @throws NoSuchMoveException
	 *             or IllegalMoveException
	 */
	public abstract Move parseMove(String s) throws GameException;

}
