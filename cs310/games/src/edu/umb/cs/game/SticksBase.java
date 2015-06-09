// SticksBase.java, March 2003 
//
// Created by Qian Huang and Omofolakunmi Ogunlesi
// based on sticks.c and Easy.java

package edu.umb.cs.game;

import java.util.*;

/**
 * @author Qian Huang, Omofolakunmi Ogunlesi
 * 
 *         Class SticksBase is a super class for any concrete game sticks,
 *         stick5 and stick7. It has all the common methods that will be used by
 *         all sticks games.
 * 
 *         It can handle any game needed setup or not and control the procedure
 *         of setting up a game since the procedure for setup is the same for
 *         any game only the specific setup is different.
 * 
 *         It extends Observable to notify the Observer (GameView) for all the
 *         changes made after making a move. It knows nothing about the
 *         GameView, but only notify it.
 * 
 * @see Sticks
 * @see Sticks5
 * @see Sticks7
 */

public abstract class SticksBase extends Game {
	protected int sticksLeft;

	// variables needed to play the game
	protected int startSticks;
	protected boolean isGameNew;
	protected PlayerNumber nextPlayer; 
	
	private PlayerNumber onePlayerLeft;
	private boolean ifNeedSetup, someoneQuit;
	private String gameName, author;
	
	// help information used for every sticks game
	private String help = "Each player removes either one or two sticks during a turn.\n"
			+ "Each move should be an integer number. \n"
			+ "0 is for quit, 1 or 2 is for removing 1 or 2 sticks at a time.\n"
			+ "The player removing the last stick wins.\n";

	// constants for playing this game
	protected final static int QUIT = 0;
	protected final static int MAXMOVE = 2;
	protected final static int MINMOVE = 1;
	protected final static int MIN_STICKS = 1;
	protected final static int MAX_STICKS = 12;
	protected final static int DEFAULT_STICKS = 5;

	protected final static SticksMove[] moveList = {
			new SticksMove(QUIT), new SticksMove(MINMOVE),
			new SticksMove(MAXMOVE) };

	/**
	 * Construct the game with needed information.
	 * Note that this is only called from subclass constructors, so it
	 * may take parameters (concrete Game classes need to have no-args
	 * constructors to allow dynamic creation from class name).
	 * 
	 * @param gameName
	 *            name of the game
	 * @param author
	 *            author of the game
	 * @param ifNeedSetup
	 *            if need setup
	 * @param startSticks
	 *            subclass's startSticks (only effective if ifNeedSetup is false)
	 */
	protected SticksBase(String gameName, String author, boolean ifNeedSetup,
			int startSticks) {
		this.gameName = gameName;
		this.author = author;
		this.ifNeedSetup = ifNeedSetup;
		this.startSticks = startSticks;
		sticksLeft = startSticks;
	}

	/**
	 * Does this Game need information to set itself up before the first move is
	 * made? 
	 * 
	 * @return true if so.
	 */
	public boolean needSetup() {
		return ifNeedSetup;
	}

	/**
	 * Added method for game needs setup. Set the starting number of Sticks for
	 * this game.
	 */
	protected void setStartSticks(int sticks) {
		startSticks = sticks;
		ifNeedSetup = false;  // this is the only thing needed
	}
	

	/**
	 * Added method for sticks games.
	 * 
	 * @return startSticks nmber of sticks to start game with
	 */
	protected int getStartSticks() {
		return startSticks;
	}

	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return gameName;
	}

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * What are the rules of the game? How are moves entered interactively?
	 * 
	 * @return a String with this information.
	 */
	protected String getHelp() {
		String moreHelp = "\n\nHelp For " + getName() + " .....\n"
		+ "At first, there are " + getStartSticks()
		+ " sticks in a pile.\n" + help;
		return moreHelp;
	}

	/**
	 * Added method for sticks games. What is the number of sticks left in the
	 * game? A getter for the number of sticks.
	 */
	public int getSticksLeft() {
		return sticksLeft;
	}

	/**
	 * Is this game new?
	 * 
	 * @return true if yes.
	 */
	public boolean isGameNew() {
		return isGameNew;
	}

	/**
	 * Is this game over?
	 * 
	 * @return true if yes.
	 */
	public boolean isGameOver() {
		return (sticksLeft == 0) || someoneQuit;
	}

	/**
	 * What does it do when the game is over?
	 */
	public void gameOver() {
	}

	/**
	 * Initialize this game
	 */
	public void init() {
		isGameNew = true;
		nextPlayer = Game.FIRST_PLAYER;
		sticksLeft = getStartSticks();
		someoneQuit = false;
		onePlayerLeft = null;
	}

	/**
	 * Since Players need not alternate making moves, you must be able to query
	 * the current position to find out whose turn it is.
	 * 
	 * If the game is over, return Game.GAME_OVER.
	 * 
	 * @return the PlayerNumber who moves next.
	 */
	public PlayerNumber whoseTurn() {
		if (isGameOver()) {
			return Game.GAME_OVER;
		}
		return nextPlayer;
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
	public void make(Move m) throws IllegalMoveException {
		SticksMove em = (SticksMove) m;
		if (!isLegal(em)) {
			throw new IllegalMoveException(em);
		}
		isGameNew = false;
		if (em.sticks == QUIT) {
			resignation(nextPlayer);
			return;
		}
		nextPlayer = (nextPlayer == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
		sticksLeft -= em.sticks;
	}

	/**
	 * Who has won the game? Returns one of Game.FIRST_PLAYER,
	 * Game.SECOND_PLAYER, Game.DRAW, Game.GAME_NOT_OVER.
	 * 
	 * @return the winner.
	 */
	public PlayerNumber winner() {
		if (someoneQuit) {
			return onePlayerLeft;
		}
		if (sticksLeft > 0) {
			return Game.GAME_NOT_OVER;
		}
		if (nextPlayer == Game.SECOND_PLAYER && sticksLeft == 0) {
			return Game.FIRST_PLAYER;
		}
		if (nextPlayer == Game.FIRST_PLAYER && sticksLeft == 0) {
			return Game.SECOND_PLAYER;
		}

		return Game.DRAW;
	}

	/**
	 * What about one player quit? Set the someoneQuit flag and get the
	 * resigner's PlayerNumber.
	 * 
	 * @param resigner
	 *            a PlayerNumber
	 * @return onePlayerLeft the left Player
	 */
	private void resignation(PlayerNumber resigner) {
		someoneQuit = true;
		onePlayerLeft = (resigner == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode. A perfect hash.
	 * 
	 * @return the hashCode.
	 */
	public int hashCode() {
		if (winner() == Game.FIRST_PLAYER) {
			return 1;
		}
		if (winner() == Game.SECOND_PLAYER) {
			return 2;
		}
		if (winner() == Game.DRAW) {
			return 3;
		}
		// Game is not over
		int hash = 4;
		if (nextPlayer == Game.FIRST_PLAYER)
			hash += sticksLeft;
		else if (nextPlayer == Game.SECOND_PLAYER)
			hash += MAX_STICKS + sticksLeft;
		return hash;
	}

	/**
	 * Is one move legal? Check if a move is legal.
	 * 
	 * @param m
	 *            a SticksMove
	 * @return true if legal
	 */
	protected boolean isLegal(SticksMove m) {
		return (m.sticks <= 2 && m.sticks >= 0 && m.sticks <= sticksLeft);
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.
	 * 
	 * @return true when hashCodes are ==. OK because of perfect hash.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SticksBase)) {
			return false;
		}
		SticksBase g = (SticksBase) obj;
		return (g.hashCode() == this.hashCode());
	}

	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return an Iterator for the legal moves.
	 */
	public Iterator<Move> getMoves() {
		return new SticksMoveIterator();
	}

	// ---------------------------------------------------------
	// A Move in the Sticks game is a wrapper for an int.
	//

	/**
	 * Class SticksMove is a move for any sticks games.
	 */
	protected static class SticksMove extends Move {
		private final int sticks;

		/**
		 * Moves for Sticks games.
		 */
		private SticksMove(int sticks) {
			this.sticks = sticks;
		}
		/**
		 * Equals based on int's equality
		 * @return boolean
		 */
		public boolean equals(Object x) {
			if (x == null || x.getClass() != getClass())
				return false;
			return sticks == ((SticksMove) x).sticks;
		}
		
		/**
		 * This is a perfect hash, and consistent with equals
		 * @return integer
		 */
		public int hashCode() { return sticks; }
		

		/**
		 * Moves for Sticks games printed as a string.
		 */
		public String toString() {
			return "" + sticks;
		}

		/**
		 * For this game, it's safe to return a reference since SticksMoves
		 * are immutable.
		 */
		public Move copy() {
			return this;
		}
	}

	// -------------------------------------------------------

	/**
	 * Class SticksMoveIterator is a move iterator. It works for any Sticks
	 * game with rule to pick any number of sticks at a time without changing
	 * any implementation in this class by changing the MAXMOVE in SticksBase.
	 */
	private class SticksMoveIterator implements Iterator<Move> {

		private int legalSticks = 0; // legal moves

		/**
		 * Construct a move iterator for sticks games
		 */
		private SticksMoveIterator() {
		}

		/**
		 * If current is more than or equals to 2, legalSticks<3 and legal moves
		 * loop from 0, 1 to 2.
		 * 
		 * If current is less than 2, legalSticks<2 and legal moves loop from 0
		 * to 1.
		 */
		public boolean hasNext() {
			if (getSticksLeft() >= SticksBase.MAXMOVE)
				return (legalSticks < SticksBase.MAXMOVE + 1);
			else
				return (legalSticks < SticksBase.MAXMOVE);
		}

		/**
		 * All possible legal moves for next step.
		 */
		public Move next() {
			SticksMove m;
			m = SticksBase.moveList[legalSticks];
			legalSticks++;
			return m;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
