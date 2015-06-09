/**
 * < BigTictactoe.java >
 *
 * Ported from tictactoe.c by Haibo Liu and Wenjie Jin
 * Due Wed 19, Feb. 2003
 * Apr 22, Szymon Jaroszewicz: fixed a bug in hashCode which gave a
 * loss and a draw the same codes
 * Apr 22: Betty O'Neil, fixed hashCode typo
 * Apr 23: Betty O'Neil, fixed equals, thanks to Kimmy Lin's TestGame
 *
 * BigTictactoe game: Player can setup the size of game
 * board and the winning threshold number.  */

package edu.umb.cs.game;

import java.util.*;

// for unit test only
import edu.umb.cs.io.Terminal;

/**
 * In this BigTictactoe, we make it more generous so that players can set up the
 * board as they wish.
 */
public class BigTictactoe extends Game {
	private static final int QUIT = 0;
	private boolean needSetup = true;
	private boolean someoneQuit = false;
	private int cellsInRow = 3;
	private int cellsInColumn = 3;
	private int bingo = 3;
	private int numberOfMoves = 9;
	private PlayerNumber nextPlayer, onePlayerLeft;

	private boolean isGameNew;
	private List<TictacMove> moveList = new ArrayList<TictacMove>();
	private Set<TictacMove> movesLeft = new TreeSet<TictacMove>();
	private Set<TictacMove> movesOfPlayerOne = new TreeSet<TictacMove>();
	private Set<TictacMove> movesOfPlayerTwo = new TreeSet<TictacMove>();

	/**
	 * Construct an instance of the BigTictactoe Game with the position set for
	 * the start of the game.
	 */
	public BigTictactoe() {
	}

	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return "BigTictactoe";
	}

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author.
	 */
	public String getAuthor() {
		return "Wenjie and Haibo";
	}

	/**
	 * method to initialize all important variables. This method
	 * is also useful to flush the completed game away and begin a new game.
	 */
	public void init() {
		isGameNew = true;
		moveList.clear();
		for (int i = 0; i <= numberOfMoves; i++)
			moveList.add(new TictacMove(i));
		movesLeft.addAll(moveList);
		movesOfPlayerOne.clear();
		movesOfPlayerTwo.clear();
		nextPlayer = Game.FIRST_PLAYER;
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
	 * Get the next player of the game.
	 */
	public PlayerNumber getNextPlayer() {
		return nextPlayer;
	}

	/**
	 * Get the number of columns in tictactoe
	 */
	public int getNoOfColumns() {
		return cellsInColumn;
	}

	/**
	 * Get the number of rows in tictactoe
	 */
	public int getNoOfRows() {
		return cellsInRow;
	}

	/**
	 * Does this Game need information to set itself up before the first move is
	 * made?
	 * 
	 * @return true if so.
	 */
	public boolean needSetup() {
		return needSetup;
	}

	/**
	 * setup the game with given number of rows, columns and winning number
	 */
	public void setup(int[] params) {
		cellsInRow = params[0];
		cellsInColumn = params[1];
		bingo = params[2];	
		numberOfMoves = cellsInRow * cellsInColumn;
		needSetup = false;
		init();
	}

	/**
	 * Is this game over?
	 * 
	 * @return true if yes.
	 */
	public boolean isGameOver() {
		PlayerNumber winner = winner();
		return (getNoOfMovesLeft() == 0) || someoneQuit
				|| (winner == Game.FIRST_PLAYER) || // Modified to check winning
													// case
				(winner == Game.SECOND_PLAYER);
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
		if (someoneQuit) {
			return onePlayerLeft;
		}
		if (checkHorizontal(movesOfPlayerOne)
				|| checkVertical(movesOfPlayerOne)
				|| checkDiagonal(movesOfPlayerOne))
			return Game.FIRST_PLAYER;

		if (checkHorizontal(movesOfPlayerTwo)
				|| checkVertical(movesOfPlayerTwo)
				|| checkDiagonal(movesOfPlayerTwo))
			return Game.SECOND_PLAYER;

		if (getNoOfMovesLeft() > 0)
			return Game.GAME_NOT_OVER;

		return Game.DRAW;
	}
	
	// access to moves by row, col numbers (0-based)
	private Move getMoveAt(int i, int j) {
		return moveList.get(1 + i*cellsInRow + j);
	}

	/**
	 * Check each row if there is a winning line
	 */
	private boolean checkHorizontal(Set<TictacMove> ml) {
		for (int i = 0; i< cellsInColumn; i++) {
			int bingoCounter = 0;
			for (int j=0;j<cellsInRow; j++)
			if (ml.contains(getMoveAt(i,j)))
					bingoCounter++;
			if (bingoCounter >= bingo)
				return true;
		}
		return false;
	}

	/**
	 * Check each column if there is a winning line
	 */
	private boolean checkVertical(Set<TictacMove> ml) {
		// loop over columns
		for (int j=0;j<cellsInRow; j++) {
			int bingoCounter = 0;
		for (int i = 0; i< cellsInColumn; i++)
			if (ml.contains(getMoveAt(i,j)))
				bingoCounter++;
		if (bingoCounter >= bingo)
			return true;
		}
		return false;
	}

	/**
	 * Check each diagonal if there is a winning line
	 */
	private boolean checkDiagonal(Set<TictacMove> ml) {
		// first is ' \ ' direction of cross
		if (cellsInRow >= cellsInColumn) {
			for (int baseCol = 0; baseCol < cellsInRow - cellsInColumn + 1; baseCol++) {
				int bingoCounter = 0;
				for (int i = 0; i < cellsInColumn; i++)
					if (ml.contains(getMoveAt(i, baseCol + i)))
						bingoCounter++;
				if (bingoCounter >= bingo)
					return true;
			}
		} else {
			// more rows than columns, use cellsInRows rows starting from
			// baseRow
			for (int baseRow = 0; baseRow < cellsInColumn - cellsInRow + 1; baseRow++) {
				int bingoCounter = 0;
				for (int j = 0; j < cellsInRow; j++)
					if (ml.contains(getMoveAt(baseRow + j, j)))
						bingoCounter++;
				if (bingoCounter >= bingo)
					return true;
			}
		} 
			// Second is ' / ' direction of cross
			if (cellsInRow >= cellsInColumn) {
				for (int baseCol = 0; baseCol < cellsInRow - cellsInColumn + 1; baseCol++) {
					int bingoCounter = 0;
					for (int i = 0; i < cellsInColumn; i++)
						if (ml.contains(getMoveAt(i, baseCol + (cellsInColumn - 1 - i))))
							bingoCounter++;
					if (bingoCounter >= bingo)
						return true;
				}
			} else {
				// more rows than columns, use cellsInRows rows starting from
				// baseRow
				for (int baseRow = 0; baseRow < cellsInColumn - cellsInRow + 1; baseRow++) {
					int bingoCounter = 0;
					for (int j = 0; j < cellsInRow; j++)
						if (ml.contains(getMoveAt(baseRow + j, (cellsInRow - 1 - j))))
							bingoCounter++;
					if (bingoCounter >= bingo)
						return true;
				}	
		}
			return false;
	}

	

	/**
	 * The rules of the game are encapsulated here: Change this position by
	 * making Move m. After made a move, notify all the observer of the game to
	 * update their view.
	 * 
	 * @param m
	 *            the Move to be made.
	 * @throws IllegalMoveException
	 *             if m is illegal.
	 */
	public void make(Move m) throws IllegalMoveException {
		TictacMove a_move = (TictacMove) m;
		if (!isLegal(a_move)) {
			throw new IllegalMoveException(a_move);
		}
		isGameNew = false;
		if (a_move.isQuit()) {
			resignation(nextPlayer);
			return;
		}
		if (nextPlayer == Game.FIRST_PLAYER) {
			movesOfPlayerOne.add(a_move);
			movesLeft.remove(a_move);
		} else {
			movesOfPlayerTwo.add(a_move);
			movesLeft.remove(a_move);
		}

		nextPlayer = (nextPlayer == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		BigTictactoe g = new BigTictactoe();
		g.isGameNew = isGameNew;
		g.moveList = new ArrayList<TictacMove>(moveList);
		g.movesLeft = new TreeSet<TictacMove>(movesLeft);
		g.movesOfPlayerOne = new TreeSet<TictacMove>(movesOfPlayerOne);
		g.movesOfPlayerTwo = new TreeSet<TictacMove>(movesOfPlayerTwo);
		g.nextPlayer = nextPlayer;
		g.someoneQuit = someoneQuit;
		g.onePlayerLeft = onePlayerLeft;
		g.needSetup = needSetup;
		g.cellsInRow = cellsInRow;
		g.cellsInColumn = cellsInColumn;
		g.bingo = bingo;
		g.numberOfMoves = numberOfMoves;
		return g;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.  A perfect hash. See Easy.java for comments on this method.
	 * 
	 * @return the hashCode.
	 */
	public int hashCode() {
		int hash = 0;
		if (isGameOver()) {
			if (winner() == Game.FIRST_PLAYER)
				return 2;
			if (winner() == Game.SECOND_PLAYER)
				return 3;
			return 1;
		}
		hash = 4;
		for (int i = 1; i <= numberOfMoves; i++) {
			hash *= 3;
			if (movesOfPlayerOne.contains(moveList.get(i)))
				hash++;
			else if (movesOfPlayerTwo.contains(moveList.get(i)))
				hash += 2;
		}
		return hash;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode. OK because of the perfect hash.
	 * 
	 * @return the hashCode.
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof BigTictactoe))
			return false;

		BigTictactoe g = (BigTictactoe) obj;
		return (g.hashCode() == this.hashCode());
	}

	private int getNoOfMovesLeft() {
		return movesLeft.size() - 1; // don't count quit
	}

	private void resignation(PlayerNumber resigner) {
		someoneQuit = true;
		onePlayerLeft = (resigner == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	/**
	 * Check if the given move is legal
	 */
	public boolean isLegal(TictacMove m) {
		return !(movesOfPlayerOne.contains(m) || 
				movesOfPlayerTwo.contains(m));
	}

	public GameStrings getGameStrings() {
		return new BigTictactoeGameStrings();
	}
	/**
	 * Subclass the public class IntegerMove with this private class
	 * so that clients can't create TictacMoves: they have to do 
	 * game.getMoves() to get access to Move objects for this game.
	 */
	private static class TictacMove extends IntegerMove {
		public TictacMove(int spot) {
			super(spot);
		}
		public Boolean isQuit() {
			return equals(new TictacMove(BigTictactoe.QUIT));
		}
	}
	
	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return an Iterator for the legal moves.
	 */
	public Iterator<Move> getMoves() {
		return new MoveSetIterator<TictacMove>(this.movesLeft);
	}


	private class BigTictactoeGameStrings extends GameStrings {

		/**
		 * Construct a String view of Tictactoe.
		 */
		public BigTictactoeGameStrings() {
		}

		/**
		 * What are the rules of the game? How are moves entered interactively?
		 */
		public String help() {
			StringBuffer s = new StringBuffer("Tictactoe:\n");
			s.append("Player 1 puts X, player 2 puts O,\n");
			s.append("players move in turn, trying to put marks in a\n");
			s.append("straight line. If neither succeeds, game is a draw.\n");
			s.append("Type number of a square to make a move, 0 quits.\n");
			return s.toString();
		}

		/**
		 * Display the current position.
		 */
		public String position() {
			StringBuffer s = new StringBuffer("\n");
			s.append("Player 1 owns: ");
			for (Move m:movesOfPlayerOne) {
				s.append("   " + m);
			}
			s.append('\n');
			s.append("Player 2 owns: ");
			for (Move m:movesOfPlayerTwo) {
				s.append("   " + m);
			}
			s.append('\n');
			for (int i = 0; i < cellsInRow; i++)
				s.append("+---");
			s.append("+\n");
			for (int i = 0; i < cellsInColumn; i++) {
				s.append("|");
				for (int j = 0; j < cellsInRow; j++) {
					if (movesOfPlayerOne.contains(getMoveAt(i,j)))
						s.append(" X ");
					else if (movesOfPlayerTwo.contains(getMoveAt(i,j)))
						s.append(" O ");
					else {
						int spot = 1 + i * cellsInRow + j;
						if (spot > 99)
							s.append(String.valueOf(spot));
						else if (spot > 9)  // fits in 2 digigs
							s.append(" " + String.valueOf(spot));
						else  // fits in 1 digit
							s.append(" " + String.valueOf(spot) + " ");
					}
					s.append("|");
				}
				s.append("\n");
				for (int j = 0; j < cellsInRow; j++)
					s.append("+---");
				s.append("+\n");
			}
			s.append('\n');
			s.append(isGameOver() ? "Game is over.\n" : "Player " + nextPlayer
					+ " moves next.\n");
			return s.toString();
		}
		
		public String[] getSetupParamDefinitions() {
			return new String[] {"rowCount", "columnCount", "threshold"};
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
				if (i > numberOfMoves)
					throw new NoSuchMoveException(s);
				BigTictactoe.TictacMove m = moveList.get(i);
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
		Game g = new BigTictactoe();
		GameStrings gameStrings = g.getGameStrings();
		Terminal t = new Terminal();
		g.init();
		t.println("Start of game:");
		t.println(gameStrings.position());
		t.println("winner is" + g.winner());
		t.println("Is game new? " + g.isGameNew());
		t
				.println("\nplay with hard coded moves 1 4 2 7(observable) 3");
		try {
			g.make(gameStrings.parseMove("1"));
			g.make(gameStrings.parseMove("4"));
			g.make(gameStrings.parseMove("2"));
			g.make(gameStrings.parseMove("7"));
			g.makeObservable(gameStrings.parseMove("3"));
			t.println("Is game new? " + g.isGameNew());
		} catch (GameException e) {
			t.println(e);
		}
		t.println("final position:");
		t.println("winner is" + g.winner());

		t.println("\ntest hash and copy");
		t.println("hash of finished Tictactoe: " + g.hashCode());
		t.println("view position before and after init():");
		t.println("Before init():");
		t.println("-------------------------------------");
		t.println(gameStrings.position());
		g.init();
		t.println("After init():");
		t.println("-------------------------------------");
		t.println(gameStrings.position());

		t.println("hash of new Tictactoe: " + g.hashCode());
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
		t.println("hash of new Tictactoe after moves 1 and 3: "
				+ g.hashCode());
		copyOfG = g.copy();
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

		t.println("-------------------------------------");
		t.println("\ntest Move Iterator");
		g.init();
		t.print("0 1 2 3 4 5 6 7 8 9 ");
		Iterator<Move> i = g.getMoves();
		t.println();
		t.println("The following should be the same with the above");
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();

		t.println();
		t.println("make a move 2(Observable)");
		t.println("-------------------------------------");

		try {
			g.makeObservable(gameStrings.parseMove("2"));
		} catch (GameException e) {
		}

		t.print("0 1 3 4 5 6 7 8 9 ");
		i = g.getMoves();
		t.println();
		t.println("The following should be the same with the above");
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();

		t.println();
		t.println("make a move 8(not observable)");

		try {
			g.make(gameStrings.parseMove("8"));
		} catch (GameException e) {
		}

		t.println("-------------------------------------");
		t.print("0 1 3 4 5 6 7 9 ");
		i = g.getMoves();
		t.println();
		t.println("The following should be the same with the above");
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();

		t.println("-------------------------------------");
		t.println("move beyond end of the list");
		try {
			i.next();
		} catch (java.util.NoSuchElementException e) {
			t.println(e);
		}
	}
}
