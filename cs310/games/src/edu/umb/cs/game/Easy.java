// Easy.java
//
// Ethan Bolker

package edu.umb.cs.game;

import java.util.*;

import edu.umb.cs.io.Terminal;

/**
 * Primitives for a simple game.
 * 
 * Written by Ethan Bolker, March 1984, for Math 301, to provide an
 * implementation of the GAME ADT so that students can test their backtracking
 * algorithms.
 * 
 * Revised October 1986, October 1987 for CS310 translated to C by Joe Prosser
 * s'90 revised Jan 93 by Ethan Bolker so that all functions are passed pointers
 * to moves. revised Jan 95 by Ethan Bolker to implement quit. revised Oct 95 by
 * Daniel Keefe for greater data abstraction translated to Java by Ethan Bolker
 * Fall '02. design improved by Yan Sun, Bipin Vaddi, Pradeep Kanneganti Feb '03
 * Betty O'Neil S10: changed NUMBER_OF_MOVES from 4 to 3 to simplify game for hw
 */

public class Easy extends Game {
	private final static int QUIT = 0;
	private final static int NUMBER_OF_MOVES = 3;

	// Create the immutable list of moves once.
	private static final List<EasyMove> allMoves = new ArrayList<EasyMove>();
	static {
		for (int i = 0; i <= NUMBER_OF_MOVES; i++) {
			allMoves.add(new EasyMove(i));
		}
	}

	// These fields represent the actual position at any time.
	private boolean isGameNew;
	private Set<EasyMove> movesLeft = new TreeSet<EasyMove>();
	private Set<EasyMove> moves1 = new TreeSet<EasyMove>();
	private Set<EasyMove> moves2 = new TreeSet<EasyMove>();
	private PlayerNumber nextPlayer;
	private boolean someoneQuit = false;
	private PlayerNumber onePlayerLeft; // he didn't resign

	/**
	 * Construct an instance of the Easy Game.
	 * <p>
	 * The real work happens in init().
	 */
	public Easy() {
	}

	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return "Easy";
	}

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author.
	 */
	public String getAuthor() {
		return "Ethan Bolker";
	}

	/**
	 * Does this Game need information to set itself up before the first move is
	 * made?
	 * 
	 * @return true if so.
	 */
	public boolean needSetup() {
		return false;
	}

	/**
	 * Set up the position ready to play.
	 * <p>
	 * This should <i>not<\i> be done in the constructor.
	 */
	public void init() {
		isGameNew = true;
		nextPlayer = Game.FIRST_PLAYER;
		movesLeft.addAll(allMoves);
		moves1.clear();
		moves2.clear();
		someoneQuit = false;
		onePlayerLeft = null;
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
	 */
	public boolean isGameOver() {
		return (realMovesLeft() == 0) || someoneQuit;
	}

	/**
	 * Inform this Game that it is over.
	 */
	public void gameOver() {
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
	 * Who has won the game? Returns one of Game.FIRST_PLAYER,
	 * Game.SECOND_PLAYER, Game.DRAW, Game.GAME_NOT_OVER.
	 * 
	 * @return the winner.
	 */
	public PlayerNumber winner() {
		if (someoneQuit) {
			return onePlayerLeft;
		}
		if (realMovesLeft() > 0) {
			return Game.GAME_NOT_OVER;
		}
		if (containsInt(moves1, 1) && containsInt(moves1, 2)) {
			return Game.FIRST_PLAYER;
		}
		if (containsInt(moves2, 1) && containsInt(moves2, 2)) {
			return Game.SECOND_PLAYER;
		}
		return Game.DRAW;
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
		EasyMove em = (EasyMove) m;
		if (!isLegal(em)) {
			throw new IllegalMoveException(em);
		}
		isGameNew = false;
		if (em.spot == QUIT) {
			resignation(nextPlayer);
			return;
		}
		if (nextPlayer == Game.FIRST_PLAYER) {
			moves1.add(em);
			movesLeft.remove(em);
		} else {
			moves2.add(em);
			movesLeft.remove(em);
		}
		nextPlayer = (nextPlayer == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return an Iterator for the legal moves.
	 */
	public Iterator<Move> getMoves() {
		return new EasyMoveIterator();
	}

	/**
	 * Create a copy of the current position of this Game.
	 * <p>
	 * Does not call super.clone(), in order not to copy Observers.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		Easy g = new Easy();
		g.isGameNew = isGameNew;
		g.movesLeft = new TreeSet<EasyMove>(movesLeft);
		g.moves1 = new TreeSet<EasyMove>(moves1);
		g.moves2 = new TreeSet<EasyMove>(moves2);
		g.nextPlayer = nextPlayer;
		g.someoneQuit = someoneQuit;
		g.onePlayerLeft = onePlayerLeft;
		return g;
	}

	/**
	 * Provide a string representing game state, for debugging
	 * 
	 * @return the string.
	 */
	public String toString() {
		String s = moves1 + " " + moves2 + "(";
		s += isGameOver() ? "done)" : "" + nextPlayer + " next)";
		return s;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode. In other words, this is a perfect hash (no collisions.)
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
		// To trace how this works, use numbers base 3 for the hash value
		int hash = 4;
		for (int i = 1; i < allMoves.size(); i++) {
			hash *= 3;  // makes a rightmost 3-digit of 0, other digits go left
			if (containsInt(moves1, i)) {
				hash++; // makes rightmost 3-digit be 1
			} else if (containsInt(moves2, i)) {
				hash += 2; // makes rightmost 3-digit be 2
			}
		}
		return hash;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.
	 * 
	 * @return true when hashCodes are == (OK since it's a perfect hash)
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Easy)) {
			return false;
		}
		Easy g = (Easy) obj;
		return (g.hashCode() == this.hashCode());
	}

	/**
	 * View this Game using Strings.
	 * 
	 * @return a GameStrings.
	 * 
	 */
	public GameStrings getGameStrings() {
		return new EasyGameStrings();
	}
	
	// determine contains by spot number: for hashCode, etc.
	private boolean containsInt(Collection<EasyMove> c, int i) {
		return c.contains(allMoves.get(i));
	}

	private int realMovesLeft() {
		return movesLeft.size() - 1; // don't count quit
	}

	private void resignation(PlayerNumber resigner) {
		someoneQuit = true;
		onePlayerLeft = (resigner == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	public boolean isLegal(EasyMove m) {
		return !(moves1.contains(m) || moves2.contains(m));
	}

	// -----------------------------------------------
	// EasyMove
	//
	// A Move in the Easy game is a wrapper for an int.
	//
	// Note that we could subclass IntegerMove instead of
	// using a whole new class like this, but this gives
	// a self-contained implementation of a Game.
	//
	// This class is private so that clients cannot create
	// EasyMove objects: they have to use ask the game 
	// for them via getMoves();
	// 

	private static class EasyMove extends Move implements Comparable<EasyMove> {
		public final int spot;

		private EasyMove(int spot) {
			this.spot = spot;
		}
		
		public boolean equals(Object x) {
			if (x == null || x.getClass() != getClass())
				return false;
			return spot == ((EasyMove) x).spot;
		}
		/**
		 * This is a perfect hash, and consistent with equals
		 * @return integer
		 */
		public int hashCode() { return spot; }
		/**
		 * This is consistent with equals
		 * @return integer
		 */
		public int compareTo(EasyMove o) {
			return spot - ((EasyMove) o).spot;
		}

		public String toString() {
			return "" + spot;
		}

		// For this game, it's safe to return
		// a reference since EasyMoves are immutable.
		//
		public Move copy() {
			return this;
		}
	}

	// -----------------------------------------------
	// EasyMoveIterator
	//
	private class EasyMoveIterator implements Iterator<Move> {
		Iterator<EasyMove> i;

		private EasyMoveIterator() {
			i = movesLeft.iterator();
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public Move next() {
			return (i.next());
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	// -----------------------------------------------
	// EasyStringApi

	private class EasyGameStrings extends GameStrings {

		/**
		 * Construct a String view of Easy.
		 * 
		 */
		public EasyGameStrings() {
		}

		/**
		 * What are the rules of the game? How are moves entered interactively?
		 */
		public String help() {
			StringBuffer s = new StringBuffer("Easy backtracking test game.\n");
			s.append("Moves are integers in the range 0.." + NUMBER_OF_MOVES + ".\n");
			s.append("Player 1 wants #1, player 2 wants #2.\n");
			s.append("If both or neither succeed, game is a draw. 0 quits.");
			return s.toString();
		}

		/**
		 * Describe the current position.
		 */
		public String position() {
			StringBuffer s = new StringBuffer("\n");
			s.append("Player 1 owns: " + moves1);
			s.append('\n');
			s.append("Player 2 owns: " + moves2);
			s.append('\n');
			if (isGameOver()) {
				s.append("Game is over.\n");
			} else {
				s.append("Player " + nextPlayer + " moves next.\n");
				s.append("Moves left: " + movesLeft);
			}
			return s.toString();
		}

		/**
		 * When Players enter moves interactively the Game must be able to
		 * interpret what they say.
		 * 
		 * @param s
		 *            the String containing the Move.
		 * 
		 * @return the corresponding Move.
		 * @throws NoSuchMoveException
		 *             or IllegalMoveException
		 */
		public Move parseMove(String s) throws GameException {
			try {
				s = s.split("\\s+")[0];  // first token
				int i = Integer.parseInt(s);
				Easy.EasyMove m = Easy.allMoves.get(i);
				if (isLegal(m)) {
					return m;
				}
				throw new IllegalMoveException(m);
			} catch (NumberFormatException e) {
				throw new NoSuchMoveException(s);
			} catch (IndexOutOfBoundsException e) {
				throw new NoSuchMoveException(s);
			}
		}

	}

	// unit test
	public static void main(String[] args) {
		Game g = new Easy();
		GameStrings gameStrings = g.getGameStrings();
		Terminal t = new Terminal();
	
		g.init();
		t.println("Start of game:");
		t.println(gameStrings.position());
		t.println("winner is" + g.winner());
		t.println("Is game new? " + g.isGameNew());
		t.println("\nplay with hard coded moves 1 3 2(observable) 4");
		try {
			g.make(gameStrings.parseMove("1"));
			g.make(gameStrings.parseMove("3"));
			g.makeObservable(gameStrings.parseMove("2"));
			t.println("Is game new? " + g.isGameNew());
			g.make(gameStrings.parseMove("4"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("final position:");
		t.println("winner is" + g.winner());

		t.println("\ntest hash and copy");
		t.println("hash of finished Easy: " + g.hashCode());
		t.println("view position before and after init():");
		t.println(gameStrings.position());
		g.init();
		t.println(gameStrings.position());

		t.println("hash of new Easy: " + g.hashCode());
		Game copyOfG = g.copy();
		t.println("hash of copy: " + copyOfG.hashCode());
		t.println("g.equals(copyOfG)? " + g.equals(copyOfG));
		try {
			// get moves 1 and 3 from iterator
			Iterator<Move> i = g.getMoves();
			i.next();
			Move move1 = i.next();
			i.next();
			Move move3 = i.next();
			g.make(move1);
			g.makeObservable(move3);
		} catch (GameException e) {
			t.println(e);
		}
		t.println("hash of new Easy after moves 1 and 3: "
				+ g.hashCode());
		t.println("hash of copy: " + copyOfG.hashCode());

		t.println("\ntest parseMove error handling");
		try {
			gameStrings.parseMove("1");
		} catch (GameException e) {
			t.println(e);
		}
		try {
			gameStrings.parseMove("foo");
		} catch (GameException e) {
			t.println(e);
		}

		t.println(gameStrings.position());
		t.println("\ntest quit");
		try {
			g.makeObservable(gameStrings.parseMove("0"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("winner is" + g.winner());

		t.println("\ntest Move Iterator");
		g.init();
		t.print("0 1 2 3 4 ? ");
		Iterator<Move> i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.makeObservable(gameStrings.parseMove("2"));
		} catch (GameException e) {
		}

		t.print("0 1 3 4 ? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("4"));
		} catch (GameException e) {
		}
		t.print("0 1 3 ? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		t.println("move beyond end of the list");
		try {
			i.next();
		} catch (java.util.NoSuchElementException e) {
			t.println(e);
		}
	}
}
