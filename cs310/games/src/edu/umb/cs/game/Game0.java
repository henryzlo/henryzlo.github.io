// Title: Game0.java
// Author:  Saritha Janaswamy, Chitra Karki, Sumana Adma
// Spring 2003

package edu.umb.cs.game;

import java.util.*;

import edu.umb.cs.io.Terminal;

public class Game0 extends Game {
	private final static int NUMBER_OF_MOVES = 7; // Moves are 1..7
	private static List<Game0Move> allGameMoves = new ArrayList<Game0Move>();
	static {
		for (int i = 1; i <= NUMBER_OF_MOVES; i++) {
			allGameMoves.add(new Game0Move(i));
		}
	}
	private static Move BONUS_TURN_MOVE = new Game0Move(5);
	private boolean isGameNew;
	private Set<Game0Move> movesLeft = new TreeSet<Game0Move>();
	private Set<Game0Move> movesOfPlayer1 = new TreeSet<Game0Move>();
	private Set<Game0Move> movesOfPlayer2 = new TreeSet<Game0Move>();
	private PlayerNumber nextPlayer;
	private PlayerNumber checkGameOver;

	/**
	 * Constructs an instance of Game0 game
	 */
	public Game0() {
	}

	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return "Game0";
	}

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author.
	 */
	public String getAuthor() {
		return "\nSaritha Janaswamy \n" + "Sumana Adma \n" + "Chitra Karki";
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
	 * Set up the position ready to play.
	 * 
	 * This should not be done in the constructor.
	 */
	public void init() {
		movesLeft.addAll(allGameMoves);
		isGameNew = true;
		movesOfPlayer1.clear();
		movesOfPlayer2.clear();
		nextPlayer = Game.FIRST_PLAYER;
		checkGameOver = Game.GAME_NOT_OVER;
	}

	/**
	 * Inform this Game that it is over.
	 */
	public void gameOver() {
	}

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		Game0 g = new Game0();
		g.init();
		g.isGameNew = isGameNew;
		g.movesLeft = new TreeSet<Game0Move>(movesLeft);
		g.movesOfPlayer1 = new TreeSet<Game0Move>(movesOfPlayer1);
		g.movesOfPlayer2 = new TreeSet<Game0Move>(movesOfPlayer2);
		g.nextPlayer = nextPlayer;
		g.checkGameOver = checkGameOver;
		return g;
	}

	/**
	 * Is this game over?
	 * 
	 * @return true if yes.
	 */
	public boolean isGameOver() {
		if (movesLeft.isEmpty()
				&& !(checkGameOver == Game.FIRST_PLAYER || checkGameOver == Game.SECOND_PLAYER))
			checkGameOver = Game.DRAW;
		return (checkGameOver != Game.GAME_NOT_OVER);
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
		if (movesLeft.isEmpty()
				&& !(checkGameOver == Game.FIRST_PLAYER || checkGameOver == Game.SECOND_PLAYER))
			checkGameOver = Game.DRAW;
		if (checkGameOver == Game.GAME_NOT_OVER)
			return Game.GAME_NOT_OVER;
		if (checkGameOver == Game.FIRST_PLAYER)
			return Game.FIRST_PLAYER;
		if (checkGameOver == Game.SECOND_PLAYER)
			return Game.SECOND_PLAYER;
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
		Game0Move im = (Game0Move) m;
		if (!isLegal(im))
			throw new IllegalMoveException(im);
		if (nextPlayer == Game.FIRST_PLAYER) {
			movesOfPlayer1.add(im);
			movesLeft.remove(im);
			if (containsInt(movesOfPlayer1, 2) && containsInt(movesOfPlayer1, 5)) {
				checkGameOver = Game.FIRST_PLAYER;
			}
		} else if (nextPlayer == Game.SECOND_PLAYER) {
			movesOfPlayer2.add(im);
			movesLeft.remove(im);
			if (containsInt(movesOfPlayer2, 2) && containsInt(movesOfPlayer2, 5)) {
				checkGameOver = Game.SECOND_PLAYER;
			}
		} else if (movesLeft.isEmpty())
			// if a player plays 5, he gets an extra turn.
			checkGameOver = Game.DRAW;
		if (im.compareTo((Game0Move)BONUS_TURN_MOVE) != 0) {
			nextPlayer = ((nextPlayer == Game.FIRST_PLAYER) ? Game.SECOND_PLAYER
					: Game.FIRST_PLAYER);
		}
	}

	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return an Iterator for the legal moves.
	 */
	public Iterator<Move> getMoves() {
		return new MoveSetIterator<Game0Move>(this.movesLeft);
	}

	/**
	 * The Game implementation must override hashCode from class Object.
	 * This is a perfect hash.
	 * @return integer
	 */
	public int hashCode() {
		int hash = 0;
		if (isGameOver())
			return (1 + (winner() == Game.FIRST_PLAYER ? 1 : 2));
		if (hash <= 0)
			hash += 5;
		for (int i = 7; i > 0; i--) {
			hash *= 10;
			if (containsInt(movesOfPlayer1, i))
				hash += 1;
			else if (containsInt(movesOfPlayer2, i))
				hash += 2;
		}
		return hash + 4;
	}

	/**
	 * The Game implementation must override equals from class Object.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Game0)) {
			return false;
		}
		Game0 g = (Game0) obj;
		return (g.hashCode() == this.hashCode());  // OK because a perfect hash
	}

	/**
	 * check to see if a move is legal i.e 1...7 or already made.
	 * 
	 * @param im IntegerMove
	 * @return boolean value
	 */
	public boolean isLegal(IntegerMove im) {
		return !(movesOfPlayer1.contains(im) || movesOfPlayer2.contains(im));
	}

	/*
	 * To check if the element exists
	 * 
	 * @param Collection c and an integer i
	 * 
	 * @return boolean value
	 */
	private boolean containsInt(Collection<Game0Move> c, int i) {
		return c.contains(allGameMoves.get(i - 1));
	}

	/**
	 * View this game using String i/o.
	 * 
	 * @return a GameStrings.
	 * 
	 */
	public GameStrings getGameStrings() {
		return new Game0GameStrings();
	}
	/**
	 * Wrap the public class IntegerMove with this private class
	 * so that clients can't create Game0Moves: they have to do getMoves()
	 * to get access to Move objects for this game.
	 */
	private static class Game0Move extends IntegerMove {
		public Game0Move(int spot) {
			super(spot);
		}
	}
	
	private class Game0GameStrings extends GameStrings {

		/**
		 * Construct a String view of Game0.
		 * 
		 */
		public Game0GameStrings() {
		}

		/**
		 * What are the rules of the game? How are moves entered interactively?
		 * 
		 * @return a String with this information.
		 */
		public String help() {
			return "Guess the rules of this two person"
					+ " game by playing it.\n"
					+ "Moves are integers in the range 1..7.";
		}

		/**
		 * Describe the state of the game
		 * 
		 */
		public String position() {
			StringBuffer s = new StringBuffer("\n");
			s.append("Player 1 owns: " + movesOfPlayer1);
			s.append('\n');
			s.append("Player 2 owns: " + movesOfPlayer2);
			s.append('\n');
			s.append(isGameOver() ? "Game is over.\n" : "Player "
					+ whoseTurn() + " moves next.\n");
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
				Game0Move im;
				im = Game0.allGameMoves.get(i - 1);
				if (isLegal(im)) {
					return im;
				}
				throw new IllegalMoveException(im);
			} catch (NumberFormatException e) {
				throw new NoSuchMoveException(s);
			} catch (IndexOutOfBoundsException e) {
				throw new NoSuchMoveException(s);
			}
		}
	}

	// unit test

	public static void main(String[] args) {
		Terminal t = new Terminal();
		t.println("\n\n   Unit Test for Game0 ");
		t.println("   -------------------- \n");
		Game g = new Game0();
		GameStrings gameStrings = g.getGameStrings();
		
		
		t.println("Instance of Game: " + g.getName() + " created\n");
		t.println("Name of the game: " + g.getName());
		t.println("\nAuthor of the game: " + g.getAuthor());

		t.println("\nHelp for the game: ");
		t.println(gameStrings.help());
		g.init();
		t.println("\nStart of the game");
		t.println(gameStrings.position());
		t.println("winner is" + g.winner());
		t.println("\nIs game new?    " + g.isGameNew());
		t.println("\n\nPlay with hard coded moves to test the game.");
		t.println("===========================================");
		t.println("\n\nTesting : Part 1  ");
		t.println("----------------- ");
		try {
			t.println("\nMaking moves from 1 to 7");
			g.make(gameStrings.parseMove("1"));
			g.make(gameStrings.parseMove("2"));
			g.make(gameStrings.parseMove("3"));
			g.make(gameStrings.parseMove("4"));
			g.make(gameStrings.parseMove("5"));
			g.make(gameStrings.parseMove("6"));
			g.make(gameStrings.parseMove("7"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("\ng.winner(): " + g.winner());
		t.println("\ngame over: " + g.isGameOver());
		t
				.println("\nHash code of the finished Game0: " + g.hashCode());
		t.println("\n\nTesting : Part 2 ");
		t.println("----------------- ");
		t.println("Starting new game ....");
		g = new Game0();
		g.init();

		t.println("\nHash of new instance of Game0: " + g.hashCode());
		Game copyOfG = g.copy();
		t.println("\nHash of copy of Game0: " + copyOfG.hashCode());
		t.println("\ng.equals(copyOfG)? " + g.equals(copyOfG));
		t.println("\nTesting Game0's Move.");
		try {
			// get moves 3 and 6 from iterator
			Iterator<Move> i = g.getMoves();
			i.next(); i.next(); i.next();
			Move move3 = i.next();
			i.next(); i.next(); 
			Move move6 = i.next();
			g.make(move3);
			g.make(move6);
		} catch (GameException e) {
			t.println(e);
		}
		t.println("\nHash of new Game0 after moves 3 and 6: "
				+ g.hashCode());
		copyOfG = g.copy();
		t.println("\nHash of copy after moves 3 and 6: "
				+ copyOfG.hashCode());
		t.println("\nTest parseMove error handling");
		try {
			t.println("\ntesting parseMove for move 1");
			gameStrings.parseMove("1");
		} catch (GameException e) {
			t.println(e);
		}
		try {
			t.println("\ntesting parseMove for move \"foo\"");
			gameStrings.parseMove("foo");
		} catch (GameException e) {
			t.println(e);
		}
		t.println("\ntesting quit(no quit option in Game0)");
		try {
			g.make(gameStrings.parseMove("0"));
		} catch (GameException e) {
			t.println(e);
		}
		try {
			g.make(gameStrings.parseMove("q"));
		} catch (GameException e) {
			t.println(e);
		}
		try {
			g.make(gameStrings.parseMove("Q"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("\nTest the help displaying functionalities: ");
		t.println("\n\"h\" as a move ");
		try {
			g.make(gameStrings.parseMove("h"));
			t.println(gameStrings.position());
		} catch (GameException e) {
			t.println(e);
		}

		try {
			g.make(gameStrings.parseMove("7"));
			g.make(gameStrings.parseMove("2"));
		} catch (GameException e) {
			t.println(e);
		}

		t.println("\n\"H\" as a move ");
		try {
			g.make(gameStrings.parseMove("H"));
			t.println(gameStrings.position());
		} catch (GameException e) {
			t.println(e);
		}
		try {
			g.make(gameStrings.parseMove("1"));
			g.make(gameStrings.parseMove("5"));
			g.make(gameStrings.parseMove("4"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("\n\"?\" as a move ");
		try {
			g.make(gameStrings.parseMove("?"));
			t.println(gameStrings.position());
		} catch (GameException e) {
			t.println(e);
		}
		t.println("winner is" + g.winner());
		t.println("\n\nTesting : Part 3 ");
		t.println("----------------- ");
		t.println("Starting new game ....");
		g = new Game0();
		g.init();

		t.println("Whose turn to play before move 5. Player "
				+ g.whoseTurn());
		try {
			g.make(gameStrings.parseMove("5"));
			t.println("Move 5 made");
		} catch (GameException e) {
			t.println(e);
		}
		t.println("Whose turn to play after move 5. Player "
				+ g.whoseTurn());
		try {
			g.make(gameStrings.parseMove("2"));
			t.println("Move 2 made");
		} catch (GameException e) {
			t.println(e);
		}
		t.println("winner is" + g.winner());
		t.println("\n\nTesting : Part 4 ");
		t.println("----------------- ");
		t.println("Starting new game ....");
		g = new Game0();
		g.init();
	
		t.println("\nMaking move 5 as last move\n");
		try {
			t.println("Player " + g.whoseTurn() + "'s turn");
			g.make(gameStrings.parseMove("2"));
			t.println("Move 2 made");

			t.println("Player " + g.whoseTurn() + "'s turn");
			g.make(gameStrings.parseMove("3"));
			t.println("Move 3 made");

			t.println("Player " + g.whoseTurn() + "'s turn");
			g.make(gameStrings.parseMove("4"));
			t.println("Move 4 made");

			t.println("Player " + g.whoseTurn() + "'s turn");
			g.make(gameStrings.parseMove("7"));
			t.println("Move 7 made");

			t.println("Player " + g.whoseTurn() + "'s turn");
			g.make(gameStrings.parseMove("1"));
			t.println("Move 1 made");

			t.println("Player " + g.whoseTurn() + "'s turn");
			g.make(gameStrings.parseMove("6"));
			t.println("Move 6 made");

			t.println("Player " + g.whoseTurn() + "'s turn");
			g.make(gameStrings.parseMove("5"));
			t.println("Move 5 made");

			t.println("\n" + g.whoseTurn() + "\n");

		} catch (GameException e) {
			t.println(e);
		}
		t.println("winner is" + g.winner());
		t.println("\n\nTesting : Part 5 ");
		t.println("----------------- ");
		t.println("Starting new game ....");
		g = new Game0();
		g.init();
	
		t.println("\nTesting Iterator");
		t.print("1 2 3 4 5 6 7 ?    ");
		Iterator<Move> i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("5"));
			t.println("\nMove 5 made");
		} catch (GameException e) {
		}
		t.print("1 2 3 4 6 7     ?    ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.makeObservable(gameStrings.parseMove("4"));
			t.println("Move 4 made\n");
		} catch (GameException e) {
		}
		t.print("1 2 3 6 7       ?    ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("2"));
			t.println("\nMove 2 made");
			g.makeObservable(gameStrings.parseMove("7"));
			t.println("\nMove 7 made");
		} catch (GameException e) {
		}
		t.print("1 3 6           ?    ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		t.println("\nmove beyond end of the list");
		try {
			i.next();
		} catch (java.util.NoSuchElementException e) {
			t.println(e);
		}
	}
}
