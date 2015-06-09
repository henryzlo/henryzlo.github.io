// Nim.java (ported from nim.c)
// CS680, hw3, Alex Glushchenko and Ronojoy Bhaumik
// bug in Game.copy() fixed by Szymon Jaroszewicz
//      (not all fields were copied)

package edu.umb.cs.game;

import java.util.*;

import edu.umb.cs.io.Terminal;

/**
 * Primitives for a Nim game translated from C. Adapted from Easy.java by Ethan
 * Bolker.
 */

public class Nim extends Game {
		
	final static int NUM_ROWS = 3;
	private final static int SZ_ROW0 = 5;
	private final static int SZ_ROW1 = 3;
	private final static int SZ_ROW2 = 1;
	private final static String QUIT = "Q";

	// set of current moves
	private Set<NimMove> nimMoves = new TreeSet<NimMove>();

	// These fields represent the actual position at any time.
	private int[] heap = new int[NUM_ROWS];
	private int whichMove;
	private boolean isGameNew;
	private boolean someoneQuit = false;
	private PlayerNumber onePlayerLeft; // he didn't resign
	PlayerNumber nextPlayer;

	/**
	 * Construct an instance of the Nim Game
	 */
	public Nim() {
	}

	/**
	 * Set up the position ready to play.
	 * <p>
	 * This should <i>not<\i> be done in the constructor.
	 */
	public void init() {
		heap[0] = SZ_ROW0;
		heap[1] = SZ_ROW1;
		heap[2] = SZ_ROW2;
		isGameNew = true;
		nextPlayer = Game.FIRST_PLAYER;
		someoneQuit = false;
		onePlayerLeft = null;
		fillMoves();
	}
	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return "Nim";
	}

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author.
	 */
	public String getAuthor() {
		return "Alex Glushchenko and Ronojoy Bhaumik";
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
	 * Is this game new?
	 * 
	 * @return true if yes.
	 */
	public boolean isGameNew() {
		return isGameNew;
	}

	/**
	 * Compute the total number of stars left.
	 */
	private int getStarsLeft() {
		return (heap[0] + heap[1] + heap[2]);
	}

	/**
	 * Store all Nim moves in a set.
	 */
	private void fillMoves() {
		nimMoves.add(new NimMove(QUIT));
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = heap[i]; j > 0; j--) {
				nimMoves.add(new NimMove(i,j));
			}
		}
	}

	/**
	 * Is this game over?
	 * 
	 * @return true if yes.
	 */
	public boolean isGameOver() {
		return ((getStarsLeft() == 0) || someoneQuit);
	}

	/**
	 * See if the game over?
	 * 
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
	 * Who has won the game? Returns one of Game.FIRST_PAYER,
	 * Game.SECOND_PLAYER, Game.DRAW, Game.GAME_NOT_OVER.
	 * 
	 * @return the winner.
	 */
	public PlayerNumber winner() {
		if (someoneQuit) {
			return onePlayerLeft;
		}
		if (getStarsLeft() > 0) {
			return Game.GAME_NOT_OVER;
		}
		if (nextPlayer == Game.SECOND_PLAYER && getStarsLeft() == 0) {
			return Game.FIRST_PLAYER;
		}
		if (nextPlayer == Game.FIRST_PLAYER && getStarsLeft() == 0) {
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
		NimMove em = (NimMove) m;
		isGameNew = false;
		if (!isLegal(em)) {
			throw new IllegalMoveException(em);
		} else if (em.equals(new NimMove(QUIT))) {
			resignation(nextPlayer);
		} else {
			nextPlayer = (nextPlayer == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
					: Game.FIRST_PLAYER);

			int rowNumber = em.getRowNumber();
			int oldStars = heap[rowNumber];
			heap[rowNumber] = heap[rowNumber] - em.getStars();
			int newStars = heap[rowNumber];
			// drop moves that no longer are possible, like A3 if only 2 stars
			// are left in row 0
			for (int j = newStars + 1; j <= oldStars; j++) {
				nimMoves.remove(new NimMove(rowNumber, j));
			}
		}
	}

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		Nim g = new Nim();
		g.isGameNew = isGameNew;
		g.nextPlayer = nextPlayer;
		g.someoneQuit = someoneQuit;
		g.onePlayerLeft = onePlayerLeft;
		g.whichMove = whichMove;
		g.heap = new int[NUM_ROWS];
		g.heap[0] = heap[0];
		g.heap[1] = heap[1];
		g.heap[2] = heap[2];
		g.nimMoves = new TreeSet<NimMove>();
		g.fillMoves();
		return g;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.
	 * 
	 * @return the hashCode.
	 */
	public int hashCode() {
		if (isGameOver()) {
			return (1 + (winner() == Game.FIRST_PLAYER ? 1 : 2));
		}
		int total = (whoseTurn() == Game.FIRST_PLAYER ? 1 : 2);

		// Ends up with 2421 for player 2 to move, heap[m] = {4, 2,1}
		for (int m = 0; m < NUM_ROWS; m++) {
			total = heap[m] + 10 * total;
		}
		return total;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.
	 * 
	 * @return true when hashCodes are == (OK because hashCode is perfect hash)
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Nim)) {
			return false;
		}
		Nim g = (Nim) obj;
		return (g.hashCode() == this.hashCode());
	}

	/**
	 * View this Game using Strings.
	 * 
	 * @return a GameStrings.
	 * 
	 */
	public GameStrings getGameStrings() {
		return new NimGameStrings();
	}

	private void resignation(PlayerNumber resigner) {
		someoneQuit = true;
		onePlayerLeft = (resigner == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	private boolean isLegal(NimMove m) {
		return nimMoves.contains(m);

	}

	// -----------------------------------------------
	// NimMove
	//
	// A Move in the Nim game is a wrapper for a String.
	//
	// This private class is static so we can create
	// the list of immutable NimMove objects once for
	// all instances of Nim.
	// 

	private static class NimMove extends Move implements Comparable<NimMove> {
		final int rowNumber;
		final int stars;
		final boolean isQuit;

		public NimMove(String moveString) {
			isQuit = moveString.equalsIgnoreCase("Q");
			if (!isQuit) {
				rowNumber = moveString.toUpperCase().charAt(0) - 'A';
				stars = Integer.parseInt(moveString.substring(1));
			} else {
				rowNumber = 0;
				stars = 0;
			}
		}

		public NimMove(int rowNumber, int stars) {
			this.isQuit = false;
			this.rowNumber = rowNumber;
			this.stars = stars;
		}

		public boolean equals(Object x) {
			if (x == null || x.getClass() != getClass())
				return false;
			NimMove other = (NimMove) x;
			return isQuit == other.isQuit 
				&& rowNumber == other.rowNumber 
				&& stars == other.stars;
		}
		
		// a perfect hash, in expected order
		public int hashCode() {
			return (isQuit?0:1) + 10*rowNumber + stars; 
		}
		// since hashCode is perfect, and in order, can use it
		// here to compare
		public int compareTo(NimMove o) {
			return hashCode() - o.hashCode();
		}
		
		public String toString() {
			return "" + (char)('A' + rowNumber) + stars;
		}

		// For this game, it's safe to return
		// a reference since NimMoves are immutable.
		//
		public Move copy() {
			return this;
		}

		// specific to NimMove--
		public int getRowNumber() {
			return rowNumber;
		}

		public int getStars() {
			if (isQuit)
				return 0;
			else
				return stars;
		}
	}

	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return an Iterator for the legal moves.
	 */
	public Iterator<Move> getMoves() {
		return new MoveSetIterator<NimMove>(nimMoves);
	}

	private class NimGameStrings extends GameStrings {

		/**
		 * Constructor
		 */
		public NimGameStrings() {
		}

		/**
		 * This method displays current position
		 */
		public String position() {
			StringBuffer board = new StringBuffer("\n");

			for (int i = 0; i < NUM_ROWS; i++) {
				char c = (char) ((int) 'A' + i);
				board.append(c + ": ");
				for (int j = heap[i]; j > 0; j--) {
					board.append("* ");
				}
				board.append('\n');
			}

			board.append(isGameOver() ? "Game is over.\n" : "Player "
					+ nextPlayer + " moves next.\n");

			return board.toString();
		}
	
		/**
		 * What are the rules of the game? How are moves entered interactively?
		 * 
		 * @return a String with this information.
		 */
		public String help() {
			StringBuffer s = new StringBuffer(
					"\nNim is the name of the game.\n");
			s.append("The board contains three ");
			s.append("rows of stars.\nA move removes stars (at least one) ");
			s.append("from a single row.\nThe player who takes the last star wins.\n");
			s.append("Type Xn (or xn) at the terminal to remove n stars from row X.\n");
			s.append("? displays the current position, q quits.\n");
			return s.toString();
		}

		/**
		 * When Players enter moves interactively the Game must be able to
		 * interpret what they say.
		 * 
		 * @param s
		 *            the String containing the Move.
		 * @return the corresponding Move.
		 * @throws NoSuchMoveException
		 *             or IllegalMoveException
		 */
		public Move parseMove(String s) throws GameException {
			NimMove m;
			try {
				s = s.split("\\s+")[0];  // first token
				m = new NimMove(s);
			} catch (Exception e) {
				throw new NoSuchMoveException(s);
			}
			if (isLegal(m)) {
				return m;
			}
			throw new IllegalMoveException(m);
		}

	}

	// unit test
	public static void main(String[] args) {

		Game g = new Nim();
		GameStrings gameStrings = g.getGameStrings();
		Terminal t = new Terminal();

		g.init();
		t.println("Start of game:");
		t.println(gameStrings.position());
		t.println("winner is" + g.winner());
		t.println("Is game new? " + g.isGameNew());

		t.println("play with hard coded moves");
		t.println("Start of game:\n" + g);
		t.println("g.winner(): " + g.winner());
		try {
			g.make(gameStrings.parseMove("A4"));
			g.make(gameStrings.parseMove("B2"));
			g.make(gameStrings.parseMove("C1"));
			g.make(gameStrings.parseMove("B1"));
			g.make(gameStrings.parseMove("A1"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("game over:\n" + g);
		t.println("g.winner(): " + g.winner());

		t.println("\ntest hash and copy");
		t.println("hash of finished Nim: " + g.hashCode());
		g = new Nim();
		g.init();
		t.println("hash of new Nim: " + g.hashCode());
		Game copyOfG = g.copy();
		t.println("hash of copy: " + copyOfG.hashCode());
		t.println("g.equals(copyOfG)? " + g.equals(copyOfG));
		try {
			g.make(gameStrings.parseMove("A4"));
			g.make(gameStrings.parseMove("C1"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("hash of new Nim after moves A4 and C1: "
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
		t.println("\ntest quit");
		try {
			g.make(gameStrings.parseMove("0"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println(g);
		t.println("g.winner(): " + g.winner());

		t.println("\ntest Move Iterator");
		g.init();
		t.print("A1 A2 A3 A4..");
		Iterator<Move> i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("2"));
		} catch (GameException e) {
		}
		t.print("A1 A2 A3 A4 .. ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("A4"));
		} catch (GameException e) {
		}
		t.print("A1 A2 A3 ... ");
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
