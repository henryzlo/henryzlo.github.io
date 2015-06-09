// Game.java
//
// Ethan Bolker, February 2003

package edu.umb.cs.game;

import java.util.Observable;
import java.util.Iterator;

/**
 * Abstract class Game models an abstract two person game with perfect
 * information - each player knows everything the other does.
 * 
 * In principle, every such game is necessarily a win for the first player (if
 * she plays perfectly) or for the second (if she does) or a draw. But deciding
 * which may be nontrivial for any particular game, like Chess or Go.
 * 
 * Implementations of this interface capture the semantics of particular games
 * 
 * Written by Ethan Bolker, March 1984, for Math 301, to provide a framework for
 * exercising backtracking algorithms.
 * 
 * Revised October 1986, October 1987 for CS310 1990 translated to C Jan 93 by
 * Ethan Bolker to pass pointers to moves Fall '02 Ethan Bolker translated to
 * Java. Spring '09, Betty O'Neil, converted to Java generics: now using
 * Iterator<Move> instead of older MoveIterator. Also PlayerNumber is now an enum.
 * 
 * @author Ethan Bolker
 * 
 * @see PlayerNumber
 * @see Move
 */

public abstract class Game extends Observable {
	public final static PlayerNumber FIRST_PLAYER = PlayerNumber.ONE;
	public final static PlayerNumber SECOND_PLAYER = PlayerNumber.TWO;
	public final static PlayerNumber GAME_OVER = PlayerNumber.GAME_OVER;
	public final static PlayerNumber GAME_NOT_OVER = PlayerNumber.GAME_NOT_OVER;
	public final static PlayerNumber DRAW = PlayerNumber.DRAW;

	/**
	 * What is the name of the Game? May be called before init/initObservable.
	 * 
	 * @return the name of the Game.
	 */
	public abstract String getName();

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author. May be called before init/initObservable.
	 */
	public abstract String getAuthor();

	/**
	 * Does this Game need information to set itself up before the first move is
	 * made?  May be called before init/initObservable.
	 * 
	 * @return true if so.
	 */
	public abstract boolean needSetup();
	
	/**
	 * Configure the game before it starts
	 * Only needed if needSetup returns true
	 * Must be called before init/initObservable
	 * (or followed by call to init/initObservable)
	 * 
	 * @param params
	 * @throws RuntimeException, GameException
	 */
	public void setup(int[] params) throws GameException {
		throw new RuntimeException("bug: need setup for this game");
	}
	
	/* init and initObservable: only the above methods, gameStrings.help(), and
	* gameStrings.getSetupParamDefinitions() may be called before init or
	* initObservable is called, i.e., before game's init is called. 
	*/
	
	/**
	 * Set up the position ready to play.
	 * <p>
	 * This should <i>not<\i> be done in the constructor.
	 */
	public abstract void init();

	/**
	 * Set up the position ready to play.
	 * <p>
	 * This should <i>not<\i> be done in the constructor.
	 */

	/**
	 * Set up the position ready to play and notify Observers.
	 * Note that the first notification signals that the game
	 * has just been init'd.
	 */
	public final void initObservable() {
		init();
		setChanged();
		notifyObservers();
	}

	/**
	 * Inform this Game that it is over.
	 */
	public abstract void gameOver();

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public abstract Game copy();

	/**
	 * Is this game new?
	 * 
	 * @return true if yes.
	 */
	public abstract boolean isGameNew();

	/**
	 * Is this game over?
	 * 
	 * @return true if yes.
	 */
	public abstract boolean isGameOver();

	/**
	 * Since Players need not alternate making moves, you must be able to query
	 * the current position to find out whose turn it is.
	 * 
	 * If the game is over, return Game.GAME_OVER.
	 * 
	 * @return the PlayerNumber who moves next.
	 */
	public abstract PlayerNumber whoseTurn();

	/**
	 * Who has won the game? Returns one of Game.FIRST_PLAYER,
	 * Game.SECOND_PLAYER, Game.DRAW, Game.GAME_NOT_OVER.
	 * 
	 * @return the winner.
	 */
	public abstract PlayerNumber winner();

	/**
	 * Make a Move and notify this Game's Observers.
	 * 
	 * @param m
	 *            the Move to be made.
	 * @throws IllegalMoveException
	 *             if m is illegal.
	 */
	public final void makeObservable(Move m) throws IllegalMoveException {
		make(m);
		setChanged();
		notifyObservers(m);
	}

	/**
	 * The rules of the game are encapsulated here: Change this position by
	 * making Move m.
	 * 
	 * @param m
	 *            the Move to be made.
	 * @throws IllegalMoveException
	 *             if m is illegal.
	 */
	public abstract void make(Move m) throws IllegalMoveException;

	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return a Iterator for the legal moves.
	 */
	public abstract Iterator<Move> getMoves();
	
	/**
	 * The Game implementation must override hashCode from class Object. As
	 * usual, it must be consistent with equals: two objects for which equals
	 * returns true must have the same hashCode.
	 */
	public abstract int hashCode();

	/**
	 * The Game implementation must override equals from class Object. Two Game
	 * objects are equal if all the Game methods would return the same result,
	 * including whoseTurn(), winner(), and the move list from getMoves. 
	 * The intention is that two not-over game states are equal if the
	 * board position is the same and the player to play next is the same.
	 * Concluded game states may lump various board positions together,
	 * but must differ based on winner() values.
	 */
	public abstract boolean equals(Object obj);

	/**
	 * View this Game using Strings. Required support for game.
	 * 
	 */
	public abstract GameStrings getGameStrings();
	
	/**
	 * For debugging and simple test programs, provide a String describing 
	 * the current Game state. Some games override this implementation.
	 */
	public String toString() 
	{
		return getGameStrings().position();
	}
}
