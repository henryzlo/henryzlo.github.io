/*   
 * Primitives for the c-m-n game
 *
 * Choose c numbers from 1..m that sum to n. 
 * (The 15 game is 3-9-15.)
 *
 * Prasoon Kejriwal,Vikas Dua & Shantanu Inamdar
 * February 2003
 *
 * Modified to accommodate the Observer-Observable phenomenon
 * March 2003, Shantanu Inamdar
 */

package edu.umb.cs.game;

import java.util.*;

import edu.umb.cs.io.Terminal;

public class CMN extends Game {
	private final static int QUIT = 0;
	private static int NUMBER_OF_MOVES = 10;
	private static int CC = 3;
	private static int MM = 9;
	private static int NN = 15;

	private static List<Set<CMNMove>> WINS;
	private final static List<CMNMove> moveList = new ArrayList<CMNMove>();
	private boolean needsSetup = true;

	// These fields represent the actual position at any time.
	private boolean isGameNew;
	private Set<CMNMove> moves1;
	private Set<CMNMove> moves2;
	private Set<CMNMove> movesLeft;

	private PlayerNumber nextPlayer;

	private boolean someoneQuit = false;
	private PlayerNumber onePlayerLeft; // he didn't resign
	private PlayerNumber winningPlayer = Game.GAME_NOT_OVER;

	/**
	 * Construct an instance of the c-m-n Game with the position set for the
	 * start of the game.
	 */
	public CMN() {
	}

	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return "CMN";
	}

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author.
	 */
	public String getAuthor() {
		return "Prasoon, Vikas, Shantanu";
	}

	/**
	 * Does this Game need information to set itself up before the first move is
	 * made?  Yes, will need setup to set the values of C,M,N,
	 * unless already done.
	 * 
	 * @return true if so.
	 */
	public boolean needSetup() {
		return needsSetup;
	}
	
	public void setup(int[] params) {
		setCC(params[0]);
		setMM(params[1]);
		setNN(params[2]);
		needsSetup = false;
	}

	/**
	 * Set up the position ready to play.
	 * <p>
	 * This should <i>not<\i> be done in the constructor.
	 */
	public void init()
	// throws GameException
	{
		nextPlayer = Game.FIRST_PLAYER;
		someoneQuit = false;
		onePlayerLeft = null;
		isGameNew = true;
		winningPlayer = Game.GAME_NOT_OVER;
		initializeCMNGame();
		// if (WINS.size() <= 0)
		// throw new GameException ("Unacceptable input C = " + CC +
		// ", M = " + MM + ", N = " + NN +
		// ".\nExiting...");
	}

	/**
	 * Will initialize the arrays.
	 */
	private void initializeCMNGame() {
		// populate the moveList with all the possible moves for the
		// given values of C,M,N

		NUMBER_OF_MOVES = MM + 1;

		moves1 = new TreeSet<CMNMove>();
		moves2 = new TreeSet<CMNMove>();
		movesLeft = new TreeSet<CMNMove>();

		for (int i = 0; i < NUMBER_OF_MOVES; i++)
			moveList.add(new CMNMove(i));

		moves1.clear();
		moves2.clear();
		movesLeft.clear();
		movesLeft.addAll(moveList);

		setWinningTuples();
	}

	/**
	 * This method Returns CC (setup param)
	 **/
	public int getCC() {
		return CC;
	}

	/**
	 * This method sets CC
	 **/
	private void setCC(int CC) {
		CMN.CC = CC;
	}

	/**
	 * This method Returns MM
	 **/
	public int getMM() {
		return MM;
	}

	/**
	 * This method sets MM
	 **/
	private void setMM(int MM) {
		CMN.MM = MM;
	}

	/**
	 * This method Returns NN
	 **/
	public int getNN() {
		return NN;
	}

	/**
	 * This method sets NN
	 **/
	private void setNN(int NN) {
		CMN.NN = NN;
	}

	/**
	 * Inform this Game that it is over.
	 */
	public void gameOver() {
	}

	/**
	 * Is this game over?
	 * 
	 * @return true if yes.
	 */
	public boolean isGameOver() {
		return (movesLeft() == 0) || someoneQuit
				|| (winningPlayer != Game.GAME_NOT_OVER);
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
		if (winningPlayer == Game.FIRST_PLAYER) {
			return Game.FIRST_PLAYER;
		}
		if (winningPlayer == Game.SECOND_PLAYER) {
			return Game.SECOND_PLAYER;
		}
		if (movesLeft() > 0) {
			return Game.GAME_NOT_OVER;
		}
		return Game.DRAW;
	}

	public void make(Move m) throws IllegalMoveException {
		CMNMove cm = (CMNMove) m;
		if (!isLegal(cm))
			throw new IllegalMoveException(cm);
		isGameNew = false;
		if (cm.isQuit()) {
			resignation(nextPlayer);
			return;
		}// end IF

		if (nextPlayer == Game.FIRST_PLAYER) {
			moves1.add(cm);
			movesLeft.remove(cm);
			if (isWinningMove(nextPlayer, moves1))
				nextPlayer = Game.GAME_OVER;
			else
				nextPlayer = Game.SECOND_PLAYER;
		} else {
			moves2.add(cm);
			movesLeft.remove(cm);
			if (isWinningMove(nextPlayer, moves2))
				nextPlayer = Game.GAME_OVER;
			else
				nextPlayer = Game.FIRST_PLAYER;
		}// end IF-ELSE
	}

	/**
	 * Check if the move made by the player is winning move or not
	 * 
	 * @param PlayerNumber
	 *            who is making the move and the moves made till now
	 * @return returns true if the moves made till now have a winner
	 */
	private boolean isWinningMove(PlayerNumber thisPlayer, Set<CMNMove> moves) {
		boolean isWinner = false;
		Iterator<Set<CMNMove>> WINSIterator = WINS.iterator();
		while (WINSIterator.hasNext()) {
			Set<CMNMove> WINSbits = WINSIterator.next();
			if (moves.containsAll(WINSbits)) {
				isWinner = true;
				winningPlayer = thisPlayer;
			}
		}// end WHILE
		return isWinner;
	}

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		CMN g = new CMN();
		g.isGameNew = isGameNew;
		CMN.WINS = new ArrayList<Set<CMNMove>>(WINS);
		g.movesLeft = new TreeSet<CMNMove>(movesLeft);
		g.moves1 = new TreeSet<CMNMove>(moves1);
		g.moves2 = new TreeSet<CMNMove>(moves2);
		g.nextPlayer = nextPlayer;
		g.someoneQuit = someoneQuit;
		g.onePlayerLeft = onePlayerLeft;
		g.winningPlayer = winningPlayer;
		return g;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.
	 * 
	 * @return the hashCode.
	 */
	public int hashCode() {
		int hash = 0;
		if (isGameOver()) {
			return (1 + (winner() == Game.FIRST_PLAYER ? 1 : 2));
		}
		hash = 4;
		for (int i = 1; i < NUMBER_OF_MOVES; i++) {
			hash *= 3;
			if (contains(moves1, i)) {
				hash++;
			} else if (contains(moves2, i)) {
				hash += 2;
			}
		}
		return hash;
	}

	/**
	 * Two instances represent the same position when they have the same
	 * hashCode.
	 * 
	 * @return true when hashCodes are ==.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CMN)) {
			return false;
		}
		CMN g = (CMN) obj;
		return (g.hashCode() == this.hashCode());
	}

	/**
	 * View this Game using String i/o.
	 * 
	 * @return a String api.
	 * 
	 */
	public GameStrings getGameStrings() {
		GameStrings view = new CMNGameStrings();
		return view;
	}

	/**
	 * method to check whether a move is there in the moveList
	 * 
	 */
	private boolean contains(Collection<CMNMove> c, int i) {
		return c.contains(moveList.get(i));
	}

	/**
	 * this method finds out the number of moves left
	 * 
	 */
	private int movesLeft() {
		return movesLeft.size() - 1; // don't count quit
	}

	private void resignation(PlayerNumber resigner) {
		someoneQuit = true;
		onePlayerLeft = (resigner == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	public boolean isLegal(CMNMove m) {
		return !(moves1.contains(m) || moves2.contains(m));
	}

	/**
	 * This method will populate the WINS list with all the winning tuples for
	 * the given values of C,M,N.
	 * 
	 * @param the
	 *            bitsmap bits, the number of summands, maximum number to use ,
	 *            summand to sum to
	 */
	private void knapsack(Set<CMNMove> winSet, int count, int maxnum, int target) {
		int i;
		target -= maxnum; /* use maxnum in sum */
		count--; /* now need one less summand */

		Set<CMNMove> tempSet = new TreeSet<CMNMove>(winSet);
		tempSet.add(moveList.get(maxnum)); /* adding the winning element */

		if (target == 0 && count == 0) { /* we have a win */
			WINS.add(tempSet);
			return;
		}

		if (target < 0 || count == 0) { /* hopeless in either case */
			return;
		}

		for (i = maxnum - 1; i > 0; i--) { /* try to finish recursively */
			knapsack(tempSet, count, i, target);
		}
		return;
	}

	/**
	 * This method will set the winning tuples for the values of C,M,N given by
	 * the player.
	 */
	private void setWinningTuples() {
		WINS = new ArrayList<Set<CMNMove>>();
		Set<CMNMove> winSet = new TreeSet<CMNMove>();
		for (int i = MM; i > 0; i--) {
			knapsack(winSet, CC, i, NN);
		}// end for

	}// end of setWinningTuples
	
	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return an Iterator for the legal moves.
	 */
	public Iterator<Move> getMoves() {
		return new MoveSetIterator<CMNMove>(movesLeft);
	}

	
	/**
	 * Subclass the public class IntegerMove with this private class
	 * so that clients can't create CMNMoves: they have to call
	 * game.getMoves() to get access to Move objects for this game.
	 */
	private static class CMNMove extends IntegerMove {
		public CMNMove(int spot) {
			super(spot);
		}	
		public Boolean isQuit() {
			return equals(new CMNMove(CMN.QUIT));
		}
	}

	
	// ------------------------------------------------
	private class CMNGameStrings extends GameStrings {

		/**
		 * Construct an instance of the c-m-n GameStrings with 
		 * the instance of the game.
		 * 
		 */
		public CMNGameStrings() {
		}

		/**
		 * What are the rules of the game? How are moves entered interactively?
		 * 
		 */
		public String help() {
			return "\nc-m-n backtracking test game.\n"
							+ "Moves are integers in the range 0.."
							+ getMM()
							+ "\nFind "
							+ getCC()
							+ " numbers that sum to "
							+ getNN()
							+ "\nIf both or neither succeed, game is a draw. 0 quits.\n";
		}
		
		public String[] getSetupParamDefinitions() {
			return new String[] {"C (positive)","M (positive)","N (positive)"};
		}

		/**
		 * Displays the current position of the game
		 */
		public String position() {
			return "\n"
			+ "Player 1 owns: "
			+ printSet(moves1)
			+ "Player 2 owns: "
			+ printSet(moves2)
			+ (isGameOver() ? "Game is over.\n" : "Player "
					+ whoseTurn() + " moves next.\n");
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
			try {
				s = s.split("\\s+")[0];  // first token
				int i = Integer.parseInt(s);
				CMN.CMNMove m = CMN.moveList.get(i);
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

		/**
		 * Displays the Set of Moves passed to it
		 * 
		 * @param Set
		 *            of Moves to be displayed
		 */
		public String printSet(Set<CMNMove> moves) {
			String s = "";
			Iterator<CMNMove> setIterator = moves.iterator();
			while (setIterator.hasNext()) {
				s += setIterator.next() + " ";
			}
			s +="\n";
			return s;
		}
	}

	// unit test
	public static void main(String[] args) {
		CMN g = new CMN();
		GameStrings gameStrings = g.getGameStrings();
		Terminal t = new Terminal();
		g.init();
		t.println("Start of game:");
		t.println(gameStrings.position());
		t.println("winner is" + g.winner());
		t.println("Is game new? " + g.isGameNew());

		t
				.println("\nplay with hard coded moves 5 9 4(observable) 8 3 7 2(observable) 1 6");
		try {
			g.make(gameStrings.parseMove("5"));
			g.make(gameStrings.parseMove("9"));
			g.makeObservable(gameStrings.parseMove("4"));
			t.println("Is game new? " + g.isGameNew());
			g.make(gameStrings.parseMove("8"));
			g.make(gameStrings.parseMove("3"));
			g.make(gameStrings.parseMove("7"));
			g.makeObservable(gameStrings.parseMove("2"));
			g.make(gameStrings.parseMove("1"));
			g.make(gameStrings.parseMove("6"));

		} catch (GameException e) {
			t.println(e);
		}
		t.println("final position:");
		t.println("winner is" + g.winner());

		t.println("\ntest hash and copy");
		t.println("hash of finished CMN: " + g.hashCode());
		t.println("view position before and after init():");
		t.println(gameStrings.position());
		g.init();
		t.println(gameStrings.position());

		t.println("hash of new CMN: " + g.hashCode());
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
		t.println("hash of new CMN after moves 1 and 3: "
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
			t.print("" + i.next() + " ");
		}
		t.println();
		try {
			g.makeObservable(gameStrings.parseMove("2"));
		} catch (GameException e) {
		}

		t.print("0 1 3 4 5 6 7 8 9? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("4"));
		} catch (GameException e) {
		}
		t.print("0 1 3 5 6 7 8 9? ");
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
}// end of class CMN
