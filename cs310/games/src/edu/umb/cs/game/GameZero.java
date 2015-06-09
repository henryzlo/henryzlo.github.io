/**
 * cs680/hw6/games7/game/GameZero.java
 *
 * This is game GameZero implementation. Modification based
 * on Easy.java by Professor Bolker.
 *
 * Authors:
 *   Chuanquan Liu  ( cqliu@cs.umb.edu )
 *   Ziping Zhu     ( ziping@cs.umb.edu )
 *   Shih-ying yang ( syyang@cs.umb.edu )
 *
 * March, 2003
 */

package edu.umb.cs.game;

import java.util.*;

import edu.umb.cs.io.Terminal;

public class GameZero extends Game {
	private final static int QUIT = 0;
	private final static int PLAY_AGAIN = 5;
	private final static int NUMBER_OF_MOVES = 7;

	// Create the immutable list of moves once.
	private static final List<GameZeroMove> allMoves = new ArrayList<GameZeroMove>();
	static {
		for (int i = 0; i <= NUMBER_OF_MOVES; i++) {
			allMoves.add(new GameZeroMove(i));
		}
	}

	// These fields represent the actual position at any time.
	private boolean isGameNew;
	private Set<GameZeroMove> movesLeft = new TreeSet<GameZeroMove>();
	private Set<GameZeroMove> moves1 = new TreeSet<GameZeroMove>();
	private Set<GameZeroMove> moves2 = new TreeSet<GameZeroMove>();
	private PlayerNumber nextPlayer;
	private boolean someoneQuit = false;
	private PlayerNumber onePlayerLeft; // he didn't resign

	/**
	 * Construct an instance of the GameZero Game.
	 * <p>
	 * The real work happens in init().
	 */
	public GameZero() {
	}

	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return "GameZero";
	}

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author.
	 */
	public String getAuthor() {
		return "Authors:\n- Shih-ying Yang\n- Ziping Zhu\n- Chuanquan Liu\n";
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
		;
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
		return (movesLeft() == 0)
				|| someoneQuit // @@@ quit function use
				|| (containsInt(moves1, 2) && containsInt(moves1, 5))
				|| (containsInt(moves2, 2) && containsInt(moves2, 5));
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
		// @@@ quit function use
		if (someoneQuit) {
			return onePlayerLeft;
		}
		if (containsInt(moves1, 2) && containsInt(moves1, 5) && (movesLeft() != 0)) {
			return FIRST_PLAYER;
		}
		if (containsInt(moves2, 2) && containsInt(moves2, 5) && (movesLeft() != 0)) {
			return SECOND_PLAYER;
		}
		if (movesLeft() > 0) {
			return GAME_NOT_OVER;
		}
		return DRAW;
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
		GameZeroMove em = (GameZeroMove) m;
		if (!isLegal(em)) {
			throw new IllegalMoveException(em);
		}
		// @@@ quit function use
		if (em.isQuit()) {
			resignation(nextPlayer);
			return;
		}
		if (nextPlayer == FIRST_PLAYER) {
			moves1.add(em);
			movesLeft.remove(em);
		} else {
			moves2.add(em);
			movesLeft.remove(em);
		}
		if (!em.isPlayAgainMove()) {
			nextPlayer = (nextPlayer == FIRST_PLAYER ? SECOND_PLAYER
					: FIRST_PLAYER);
		}
	}

	/**
	 * Create a copy of the current position of this Game.
	 * <p>
	 * Does not call super.clone(), in order not to copy Observers.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		GameZero g = new GameZero();
		g.isGameNew = isGameNew;
		g.movesLeft = new TreeSet<GameZeroMove>(movesLeft);
		g.moves1 = new TreeSet<GameZeroMove>(moves1);
		g.moves2 = new TreeSet<GameZeroMove>(moves2);
		g.nextPlayer = nextPlayer;
		g.someoneQuit = someoneQuit;
		g.onePlayerLeft = onePlayerLeft;
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
		// Game is not over: see Easy for comments on this method
		int hash = 4;
		for (int i = 1; i < allMoves.size(); i++) {
			hash *= 3;
			if (containsInt(moves1, i)) {
				hash++;
			} else if (containsInt(moves2, i)) {
				hash += 2;
			}
		}
		return hash + 4;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.
	 * 
	 * @return true when hashCodes are == (OK because of perfect hash).
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GameZero)) {
			return false;
		}
		GameZero g = (GameZero) obj;
		return (g.hashCode() == this.hashCode());
	}

	public String toString() {
		StringBuffer s = new StringBuffer("\n");
		s.append("Player 1 owns: " + moves1);
		s.append('\n');
		s.append("Player 2 owns: " + moves2);
		s.append('\n');
		s.append(isGameOver() ? "Game is over.\n" : "Player " + nextPlayer
				+ " moves next.\n");
		return s.toString();
	}

	private boolean containsInt(Collection<GameZeroMove> c, int i) {
		return c.contains(allMoves.get(i));
	}

	private int movesLeft() {
		return movesLeft.size() - 1; // don't count quit
	}

	private void resignation(PlayerNumber resigner) {
		someoneQuit = true;
		onePlayerLeft = (resigner == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	public boolean isLegal(GameZeroMove m) {
		return (!(moves1.contains(m) || moves2.contains(m)));
	}
	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return an Iterator for the legal moves.
	 */
	public Iterator<Move> getMoves() {
		return new MoveSetIterator<GameZeroMove>(movesLeft);
	}

	
	/**
	 * Subclass the public class IntegerMove with this private class
	 * so that clients can't create GameZeroMoves: they have to call
	 * game.getMoves() to get access to Move objects for this game.
	 */
	private static class GameZeroMove extends IntegerMove {
		public GameZeroMove(int spot) {
			super(spot);
		}	
		public Boolean isQuit() {
			return equals(new GameZeroMove(GameZero.QUIT));
		}
		public Boolean isPlayAgainMove() {
			return equals(new GameZeroMove(GameZero.PLAY_AGAIN));
		}

	}
	
	/**
	 * View this Game using String i/o.
	 * 
	 * @return a GameStrings.
	 * 
	 */
	public GameStrings getGameStrings() {
		return new GameZeroGameStrings();
	}

	// ------------------------------------------------
	// inner class GameZeroStringApi
	// Implementation of string view of game GameZero
	//
	private class GameZeroGameStrings extends GameStrings {

		public GameZeroGameStrings() {
		}

		public String help() {
			StringBuffer s = new StringBuffer(
					"Guess the rules of this 2-person game by playing it.\n");
			s.append("Moves are integers in the range 0..7.\n");
			s.append("help to know what is legal move.\n");
			s.append("0 to quit the game.\n");
			return s.toString();
		}

		public String position() {
			StringBuffer s = new StringBuffer("\n");
			s.append("Player 1 owns: " + moves1);
			s.append('\n');
			s.append("Player 2 owns: " + moves2);
			s.append('\n');
			if (isGameOver()) {
				s.append("GameZero is over.\n");
			} else {
				s.append("Player " + nextPlayer + " moves next.\n");
				s.append("Moves left: " + movesLeft);
			}
			return s.toString();
		}

		public Move parseMove(String s) throws GameException {
			try {
				s = s.split("\\s+")[0];  // first token
				int i = Integer.parseInt(s);
				GameZero.GameZeroMove m = GameZero.allMoves.get(i);
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

	/**
	 * unit test
	 */

	public static void main(String[] args) {

		Game g = new GameZero();
		GameStrings gameStrings = g.getGameStrings();
		Terminal t = new Terminal();
		t.print("Unit Test ");

		// initialize game and test new
		g.init();
		t.println("=====  test of playing game:  =====");
		t.println(gameStrings.position());
		t.println("winner is" + g.winner());
		t.println("Is game new? " + g.isGameNew());

		// play game
		t.println("\nplay with hard coded moves 2 4 6 7 5");
		try {
			g.make(gameStrings.parseMove("2"));
			g.make(gameStrings.parseMove("4"));
			g.make(gameStrings.parseMove("6"));
			g.makeObservable(gameStrings.parseMove("7"));
			g.make(gameStrings.parseMove("5"));
			t.println("Is game new? " + g.isGameNew());
			t.println("\nWinner should be palyer1.");
			t.println("winner is" + g.winner());
		} catch (GameException e) {
			t.println(e);
		}

		// hash & copy
		t.println("\n=====  test hash and copy  =====");
		t.println("hash of finished GameZero: " + g.hashCode());
		t.println("view position before and after init():");
		t.println(gameStrings.position());
		g.init();
		t.println(gameStrings.position());

		t.println("hash of new GameZero: " + g.hashCode());
		Game copyOfG = g.copy();
		t.println("hash of copy: " + copyOfG.hashCode());
		t.println("g equals copyOfG? " + g.equals(copyOfG));
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
		t.println("hash of new GameZero after moves 1 and 3: "
				+ g.hashCode());
		t.println("hash of copy: " + copyOfG.hashCode());
		t.println("g equals copyOfG? " + g.equals(copyOfG));

		t.println("\nAssign copyOfG the current game");
		copyOfG = g.copy();
		t.println("hash of current game copy: " + copyOfG.hashCode());
		t.println("g equals copyOfG? " + g.equals(copyOfG));

		// error handling
		t.println("\n=====  test parseMove error handling   =====");

		t.println("Repeat input 1");
		try {
			gameStrings.parseMove("1");
		} catch (GameException e) {
			t.println("   " + e);
		}

		t.println("String input");
		try {
			gameStrings.parseMove("foo");
		} catch (GameException e) {
			t.println("  " + e);
		}

		t.println("Out of range input");
		try {
			gameStrings.parseMove("10");
		} catch (GameException e) {
			t.println("  " + e);
		}

		t.println("Null input");
		try {
			gameStrings.parseMove("");
		} catch (GameException e) {
			t.println("  " + e);
		}

		// test quit
		t.println("\n=====   test quit   =====");
		t.println("Player 1 quit and winner is 2");
		g.init();
		try {
			g.make(gameStrings.parseMove("0"));
			t.println(gameStrings.position());
		} catch (GameException e) {
			t.println(e);
		}
		t.println("winner is" + g.winner());

		// test iterator
		t.println("\n======   test Move Iterator   =====");
		g.init();
		t.print("0 1 2 3 4 5 6 7 || ");
		Iterator<Move> i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();

		try {
			g.makeObservable(gameStrings.parseMove("2"));
		} catch (GameException e) {
		}

		t.print("Moves left after move 2: ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.makeObservable(gameStrings.parseMove("4"));
		} catch (GameException e) {
		}
		t.print("Moves left after move 4:  ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		t
				.println("\nMove beyond end of the list. Exception should rise.");
		try {
			i.next();
		} catch (java.util.NoSuchElementException e) {
			t.println(e);
		}
		t.println();
		t.println("=====    Test End   ======");
	}

}
