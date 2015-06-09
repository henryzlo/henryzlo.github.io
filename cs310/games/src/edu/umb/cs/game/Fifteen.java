// Fifteen.java
//
// Feng Dong
// March 2003

package edu.umb.cs.game;

import java.util.*;

import edu.umb.cs.io.Terminal;

/**
 * Primitives for a 15 game.
 * 
 * Written by Ethan Bolker, March 1984, for Math 301, to provide an
 * implementation of the GAME ADT so that students can test their backtracking
 * algorithms.
 * 
 * Revised October 1986, October 1987 for CS310 translated to C by Joe Prosser
 * s'90 revised Jan 93 by Ethan Bolker so that all functions are passed pointers
 * to moves. revised Jan 95 by Ethan Bolker to implement quit. revised Oct 95 by
 * Daniel Keefe for greater data abstraction translated to Java by Feng Dong
 * Spring '03.
 */

public class Fifteen extends Game {
	private final static int QUIT = 0;
	private final static int NUMBER_OF_MOVES = 9;

	// Create the immutable list of moves once.
	private static List<FifteenMove> allMoves = new ArrayList<FifteenMove>();
	static {
		for (int i = 0; i <= NUMBER_OF_MOVES; i++)
			allMoves.add(new FifteenMove(i));
	}

	// Create the possible winning combination of moves once
	private static final List<Set<FifteenMove>> winSet = new ArrayList<Set<FifteenMove>>();
	static {
		for (int i = 0; i <= 7; i++)
			winSet.add(i, new TreeSet<FifteenMove>());
		addWinSet(0, 8, 6, 1);
		addWinSet(1, 7, 5, 3);
		addWinSet(2, 9, 4, 2);
		addWinSet(3, 8, 4, 3);
		addWinSet(4, 9, 5, 1);
		addWinSet(5, 7, 6, 2);
		addWinSet(6, 8, 5, 2);
		addWinSet(7, 6, 5, 4);
	}

	/**
	 * Internal function for initializing winning set
	 * 
	 *@param i
	 *            array index
	 *@param a
	 *            one of the three numbers which makes up to 15
	 *@param b
	 *            one of the three numbers which makes up to 15
	 *@param c
	 *            one of the three numbers which makes up to 15
	 */
	private static void addWinSet(int i, int a, int b, int c) {
		winSet.get(i).add(new FifteenMove(a));
		winSet.get(i).add(new FifteenMove(b));
		winSet.get(i).add(new FifteenMove(c));
	}

	// These fields represent the actual position at any time.
	private Set<FifteenMove> moveSet = new TreeSet<FifteenMove>();
	private Set<FifteenMove> playerOneSet = new TreeSet<FifteenMove>(); // moves
																		// of
																		// player
																		// one
	private Set<FifteenMove> playerTwoSet = new TreeSet<FifteenMove>(); // moves
																		// of
																		// player
																		// two
	private PlayerNumber nextPlayer;
	private PlayerNumber gameStatus;
	private boolean isGameNew;

	/**
	 * Construct an instance of the Fifteen Game with the position set for the
	 * start of the game.
	 */
	public Fifteen() {
	}

	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return "Fifteen";
	}

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author.
	 */
	public String getAuthor() {
		return "Feng Dong";
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
	 */
	public void init() {
		isGameNew = true;
		moveSet.addAll(allMoves);
		playerOneSet.clear();
		playerTwoSet.clear();
		nextPlayer = Game.FIRST_PLAYER;
		gameStatus = Game.GAME_NOT_OVER;
	}

	/**
	 * Is this game over?
	 * 
	 * @return true if yes.
	 */
	public boolean isGameOver() {
		return (gameStatus != Game.GAME_NOT_OVER) || (gameStatus == Game.DRAW);
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
	 * Who has won the game? Returns one of Game.FIRST_PAYER,
	 * Game.SECOND_PLAYER, Game.DRAW, Game.GAME_NOT_OVER.
	 * 
	 * @return the winner.
	 */
	public PlayerNumber winner() {
		return gameStatus;
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
		FifteenMove fifteenMove = (FifteenMove) m;
		isGameNew = false;
		if (!isLegal(fifteenMove)) {
			throw new IllegalMoveException(fifteenMove);
		}
		if (fifteenMove.equals(new FifteenMove(QUIT))) {
			resignation(nextPlayer);
			return;
		}
		if (nextPlayer == Game.FIRST_PLAYER) {
			playerOneSet.add(fifteenMove);
			if (playerOneSet.size() >= 3)
				checkGameStatus(playerOneSet);
		} else {
			playerTwoSet.add(fifteenMove);
			if (playerOneSet.size() >= 3)
				checkGameStatus(playerTwoSet);
		}

		moveSet.remove(fifteenMove);
		if (moveSet.size() == 1) // only 0 is left behind
			gameStatus = Game.DRAW;

		nextPlayer = (nextPlayer == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	/*
	 * Check whether any player has won the game by taking this move.
	 * 
	 * @param set the set of move the player taken so far
	 */
	private void checkGameStatus(Set<FifteenMove> set) {
		for (int i = 0; i <= 7; i++)
			if (set.containsAll(winSet.get(i)))
				gameStatus = (nextPlayer == Game.FIRST_PLAYER ? Game.FIRST_PLAYER
						: Game.SECOND_PLAYER);
	}

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		Fifteen g = new Fifteen();
		g.playerOneSet = new TreeSet<FifteenMove>(playerOneSet);
		g.playerTwoSet = new TreeSet<FifteenMove>(playerTwoSet);
		g.moveSet = new TreeSet<FifteenMove>(moveSet);
		g.nextPlayer = this.nextPlayer;
		g.gameStatus = this.gameStatus;
		g.isGameNew = this.isGameNew;
		return g;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.
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
		int hash = 4;
		for (int i = 1; i <= NUMBER_OF_MOVES; i++) {
			hash *= 3;
			if (playerOneSet.contains(new FifteenMove(i))) {
				hash++;
			} else if (playerTwoSet.contains(new FifteenMove(i))) {
				hash += 2;
			}
		}
		return hash;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.
	 * 
	 * @param obj
	 *            the object to test.
	 * @return true when obj is the same as this.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Fifteen)) {
			return false;
		}
		Fifteen g = (Fifteen) obj;
		return (g.hashCode() == this.hashCode());
	}

	private void resignation(PlayerNumber resigner) {
		gameStatus = (resigner == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	private boolean isLegal(FifteenMove m) {
		return moveSet.contains(m);
	}
	
	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return an Iterator for the legal moves.
	 */
	public Iterator<Move> getMoves() {
		return new MoveSetIterator<FifteenMove>(this.moveSet);
	}

	
	/**
	 * Subclass the public class IntegerMove with this private class
	 * so that clients can't create FifteenMoves: they have to call
	 * game.getMoves() to get access to Move objects for this game.
	 */
	private static class FifteenMove extends IntegerMove {
		public FifteenMove(int spot) {
			super(spot);
		}	
	}
	
	/**
	 * View this Game using Strings
	 * 
	 * @return a GameStrings.
	 */
	public GameStrings getGameStrings() {
		return new FifteenGameStrings();
	}

	// -----------------------------------------------

	private class FifteenGameStrings extends GameStrings {

		/**
		 * Construct a String view of Fifteen.
		 * 
		 */
		public FifteenGameStrings() {
		}

		/**
		 * What are the rules of the game? How are moves entered interactively?
		 */
		public String help() {
			StringBuffer s = new StringBuffer("Fifteen game.\n");
			s.append("Moves are integers in the range 0..9.\n");
			s.append("Each player tries to find three that sum to 15.\n");
			s.append("If both or neither succeed, game is a draw. 0 quits.");
			return s.toString();
		}

		/**
		 * Describe the current position.
		 */
		public String position() {
			StringBuffer s = new StringBuffer("\n");
			s.append("Player 1 owns: " + playerOneSet);
			s.append('\n');
			s.append("Player 2 owns: " + playerTwoSet);
			s.append('\n');
			if (isGameOver()) {
				s.append("Game is over.\n");
			} else {
				s.append("Player " + nextPlayer + " moves next.\n");
				s.append("Moves left: " + moveSet);
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
				FifteenMove m = new FifteenMove(i);
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
		Game g = new Fifteen();
		GameStrings gameStrings = g.getGameStrings();
		Terminal t = new Terminal();
		g.init();
		t.println("Start of game:");
		t.println(gameStrings.position());
		t.println("winner is" + g.winner());
		t.println("Is game new? " + g.isGameNew());
		t.println("\nplay with hard coded moves 9 7 4 5 2");
		try {
			g.make(gameStrings.parseMove("9"));
			g.make(gameStrings.parseMove("7"));
			g.make(gameStrings.parseMove("4"));
			t.println("Is game new? " + g.isGameNew());
			g.make(gameStrings.parseMove("5"));
			g.make(gameStrings.parseMove("2"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("final position:");
		t.println(gameStrings.position());
		t.println("winner is" + g.winner());

		t.println("\ntest hash and copy");
		t.println("hash of finished Fifteen: " + g.hashCode());
		t.println("view position before and after init():");
		t.println(gameStrings.position());
		g.init();
		t.println(gameStrings.position());

		t.println("hash of new Fifteen: " + g.hashCode());
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
		} catch (IllegalMoveException e) {
			t.println(e);
		}
		t.println("hash of new Fifteen after moves 1 and 3: "
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
		t.print("0 1 2 3 4 5 6 7 8 9 ? ");
		Iterator<Move> i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.makeObservable(gameStrings.parseMove("4"));
		} catch (GameException e) {
		}

		t.print("0 1 2 3 5 6 7 8 9  ? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("9"));
		} catch (GameException e) {
		}
		t.print("0 1 3 5 6 7 8 ? ");
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
		Set<Move> myMoveSet = new TreeSet<Move>();
		Iterator<Move> moves = g.getMoves();
		while (moves.hasNext()) 
			myMoveSet.add(moves.next());
		t.println(myMoveSet);

	}
}
