// file name: Putnam.java
// author: Yan Sun 
// date: Mar. 2003

package edu.umb.cs.game;

import java.util.*;

import edu.umb.cs.io.Terminal;

public class Putnam extends Game {
	private final static int QUIT = 0;

	// default set up for Tournament.
	private int number = 5;
	private int number_of_moves = 2 * number;

	// contains all the moves for this game
	private ArrayList<PutnamMove> moveList = createMoveList(number_of_moves);

	// the flag for seeded random number generator.
	private boolean forSeeded = false;

	// contain the moves except discarded moves.
	private PutnamSet movesSet = generator(number, true);

	// discarded moves by players and put the sum of discarded moves in sum
	private TreeSet<PutnamMove> discards = new TreeSet<PutnamMove>();
	private int sum;
	private PlayerNumber nextPlayer;

	// because this game has no draw, so the winner should always
	// be the player who just finishes one move.
	private PlayerNumber winner;
	private boolean someoneQuit = false;

	private boolean isGameNew;
	private boolean needsSetup = true;
	
	public Putnam () {	
	}

	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return "Putnam";
	}

	/**
	 * Who wrote this Game?
	 * 
	 * @return the name of the author.
	 */
	public String getAuthor() {
		return "Yan Sun";
	}

	/**
	 * Does this Game need information to set itself up before the first move is
	 * made? Yes, unless already done.
	 * 
	 * @return true if so.
	 */
	public boolean needSetup() {
		return needsSetup;
	}
	
	// call this before init
	public void setup(int[] params) throws GameException  {
		number = params[0];
		if (!(number > 0))
			throw new GameException("Integer " + number
					+ " is not positive.");
		number_of_moves = 2 * number;
		moveList = createMoveList(number_of_moves);
		movesSet = generator(number, forSeeded);
		needsSetup = false;
	}

	public void init() {
		sum = 0;
		nextPlayer = Game.FIRST_PLAYER;
		discards.clear();
		winner = null;
		someoneQuit = false;
		isGameNew = true;
		moveList = createMoveList(number_of_moves);
		movesSet = generator(number, forSeeded);
	}

	// is this game new?
	public boolean isGameNew() {
		return isGameNew;
	}

	/**
	 * Is this game over? two cases for the game is over.
	 * 
	 * @return true if yes.
	 */
	public boolean isGameOver() {
		return (((sum % (number_of_moves + 1)) == 0) && !(sum == 0))
				|| someoneQuit;
	}

	// information when game is over
	public void gameOver() {
	}

	/**
	 * Since Players need not alternate making moves, you must be able to query
	 * the current positionto find out whose turn it is.
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
	 * Who has won the game? this game have no draw. so the possible return is
	 * winner or GAME_NOT_OVER.
	 */
	public PlayerNumber winner() {

		if (isGameOver())
			return winner;
		else
			return Game.GAME_NOT_OVER;
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
		PutnamMove em = (PutnamMove) m;
		if (!isLegal(em)) {
			throw new IllegalMoveException(em);
		}
		isGameNew = false;
		if (em.isQuit()) {
			resignation(nextPlayer);
			return;
		}
		movesSet.remove(new PutnamWrapper(em, nextPlayer));
		discards.add(em);
		sum += em.value();

		winner = (nextPlayer == Game.FIRST_PLAYER ? Game.FIRST_PLAYER
				: Game.SECOND_PLAYER);

		nextPlayer = (nextPlayer == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	/**
	 * Create a copy of the current position of this Game.
	 * 
	 * @return the copy.
	 */
	public Game copy() {
		Putnam g = new Putnam();
		g.isGameNew = isGameNew;
		g.movesSet = new PutnamSet(movesSet);
		g.discards = new TreeSet<PutnamMove>(discards);
		g.sum = this.sum;
		g.number = this.number;
		g.number_of_moves = number_of_moves;
		g.nextPlayer = nextPlayer;
		g.someoneQuit = someoneQuit;
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
		for (int i = 1; i <= number_of_moves; i++) {
			hash *= 3;
			if (containsInt(movesSet.specificKeySet(Game.FIRST_PLAYER), i)) {
				hash++;
			} else if (containsInt(movesSet.specificKeySet(Game.SECOND_PLAYER), i)) {
				hash += 2;
			}
		}
		return hash + 4;
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
		if (!(obj instanceof Putnam)) {
			return false;
		}
		Putnam g = (Putnam) obj;
		return (g.hashCode() == this.hashCode());
	}

	public GameStrings getGameStrings() {
		return new PutnamGameStrings();
	}

	private void setSeed() {
		forSeeded = true;
	}

	private boolean containsInt(Collection<PutnamMove> c, int i) {
		return c.contains(moveList.get(i));
	}

	private void resignation(PlayerNumber resigner) {
		someoneQuit = true;
		winner = (resigner == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
				: Game.FIRST_PLAYER);
	}

	public boolean isLegal(PutnamMove m) {
		if (m.isQuit())
			return true;
		return movesSet.specificKeySet(nextPlayer).contains(m);
	}

	// create a list containing all the possible moves for the game.
	// from 0 to 2*n
	private ArrayList<PutnamMove> createMoveList(int num) {
		ArrayList<PutnamMove> moveList = new ArrayList<PutnamMove>();
		for (int i = 0; i <= num; i++) {
			moveList.add(i, new PutnamMove(i));
		}
		return moveList;
	}

	// generate 2*n numbers from 1 to 2*n, and let each player
	// hold half of them randomly.
	private PutnamSet generator(int num, boolean flag) {
		PutnamSet set = new PutnamSet();
		int i = 0;
		long seed = 1000;
		Random rand = new Random();
		if (flag)
			rand = new Random(seed);

		// when the move is 0, game value is GAME_OVER.
		set.add(new PutnamWrapper(new PutnamMove(0), Game.GAME_OVER));
		while (i < 2 * num) {
			int card = 1 + (Math.abs(rand.nextInt())) % (2 * num);
			PutnamMove m = new PutnamMove(card);
			// make sure no duplicated number and less than zero
			// number generated.
			if (containsInt(set.keySet(), card))
				;
			else {
				if (i % 2 == 0) {
					set.add(new PutnamWrapper(m, Game.FIRST_PLAYER));
				} else {
					set.add(new PutnamWrapper(m, Game.SECOND_PLAYER));
				}
				i++;
			}
			if (flag) {
				seed = seed + card;
				rand = new Random(seed);
			}

		}
		return set;
	}
	
	/**
	 * The user can loop on the legal moves in any position.
	 * 
	 * @return an Iterator for the legal moves.
	 * In Putnam, it depends on whose playing next
	 */
	public Iterator<Move> getMoves() {
		return new MoveSetIterator<PutnamMove>(movesSet.specificKeySet(nextPlayer));
	}

	
	/**
	 * Subclass the public class IntegerMove with this private class
	 * so that clients can't create PutnamMoveMoves: they have to call
	 * game.getMoves() to get access to Move objects for this game.
	 */
	private static class PutnamMove extends IntegerMove {
		public PutnamMove(int spot) {
			super(spot);
		}	
		public Boolean isQuit() {
			return equals(new PutnamMove(Putnam.QUIT));
		}
		public int value() {
			return getSpot();
		}
	}

	/*
	 * this is a wrapper for PutnamMove and PlayerNumber, so the player and its
	 * moves can be stored in TreeSet together.
	 */
	private class PutnamWrapper implements Comparable<PutnamWrapper> {
		public PutnamMove move;
		public PlayerNumber player;

		public PutnamWrapper(PutnamMove move, PlayerNumber player) {
			this.move = move;
			this.player = player;
		}

		public int compareTo(PutnamWrapper o) {
			return move.compareTo(((PutnamWrapper) o).move);
		}
	}

	/*
	 * this is a TreeSet for this specific game, so it can decompose
	 * PutnamWrapper, get the moves we want as a TreeSet.
	 */
	private class PutnamSet extends TreeSet<PutnamWrapper> {
		private static final long serialVersionUID = 1L;

		public PutnamSet() {
			super();
		}

		public PutnamSet(TreeSet<PutnamWrapper> s) {
			super(s);
		}

		// decompose PutnamWrapper, return moves as a TreeSet.
		public Set<PutnamMove> keySet() {
			return specificKeySet(null);
		}

		// return moves belonging to the specific player as a TreeSet
		public Set<PutnamMove> specificKeySet(PlayerNumber player) {
			TreeSet<PutnamMove> mySet = new TreeSet<PutnamMove>();
			Iterator<PutnamWrapper> iterator = this.iterator();
			while (iterator.hasNext()) {
				PutnamWrapper wrapper = iterator.next();
				PutnamMove move = wrapper.move;
				PlayerNumber myPlayer = wrapper.player;
				if (myPlayer == player)
					mySet.add(move);
				if (player == null)
					mySet.add(move);
			}
			return mySet;
		}
	}

	private class PutnamGameStrings extends GameStrings {

		/**
		 * Construct a String view of Putnam.
		 * 
		 */
		public PutnamGameStrings() {
		}

		/**
		 * What are the rules of the game? How are moves entered interactively?
		 */
		public String help() {
			return "Each player begins the game holding a random half of\n"
					+ "the numbers 1,2,...,"
					+ (2 * number)
					+ ".\n"
					+ "To move, discard any number you hold.The object of the game\n"
					+ "is to make the sum of all the discards divisible by "
					+ (2 * number + 1) + "\n\n";
		}

		/**
		 * Information from a user setting up this Game.
		 * 
		 * @param s
		 *            a String containing the information.
		 */
		public String[] getSetupParamDefinitions() {
			return new String[] {"halfNumberMoves (positive)"};
		}
	
		/**
		 * Display the current position.
		 */
		public String position() {
			StringBuffer s = new StringBuffer("\n");
			s.append("Current position:\n\n");
			s.append("Player 1 holds: "
					+ movesSet.specificKeySet(Game.FIRST_PLAYER));
			s.append("\n");
			s.append("Player 2 holds: "
					+ movesSet.specificKeySet(Game.SECOND_PLAYER));
			s.append("\n\n");
			s.append("discards: " + discards);
			s.append("\n");
			if (sum != 0)
				s.append("sum:         " + sum);
			else
				s.append("sum:");
			s.append("\n");
			s.append(isGameOver() ? "Game is over.\n" : "Player " + nextPlayer
					+ " moves next.\n");
			if (someoneQuit) {
				PlayerNumber resigner = (winner == Game.FIRST_PLAYER ? Game.SECOND_PLAYER
						: Game.FIRST_PLAYER);
				s.append("resigner: " + resigner);
				s.append("\n");
				s.append("winner: " + winner());
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
				Putnam.PutnamMove m = moveList.get(i);
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
		Game g = new Putnam();
		GameStrings gameStrings = g.getGameStrings();
		((Putnam) g).setSeed();
		Terminal t = new Terminal();
		g.init();
		t.println("Start of game:");
		t.println(gameStrings.position());
		t.println("winner is" + g.winner());
		t.println("Is game new? " + g.isGameNew());
		t.println("\nplay with hard coded moves 1 3 2 4 6 5 9 7 10 8");
		try {
			g.make(gameStrings.parseMove("1"));
			g.make(gameStrings.parseMove("3"));
			g.makeObservable(gameStrings.parseMove("2"));
			t.println("Is game new? " + g.isGameNew());
			g.make(gameStrings.parseMove("4"));
			g.make(gameStrings.parseMove("6"));
			g.make(gameStrings.parseMove("5"));
			g.make(gameStrings.parseMove("9"));
			g.make(gameStrings.parseMove("7"));
			g.make(gameStrings.parseMove("10"));
			g.make(gameStrings.parseMove("8"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("final position:");
		t.println("winner is" + g.winner());

		t.println("\ntest hash and copy");
		t.println("hash of finished Putnam: " + g.hashCode());
		t.println("view position before and after init():");
		t.println(gameStrings.position());
		g.init();
		t.println(gameStrings.position());

		t.println("hash of new Putnam: " + g.hashCode());
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
		t.println("hash of new Putnam after moves 1 and 3: "
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
		t.print("0 1 2 3 4 5 6 7 8 9 10? ");
		Iterator<Move> i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.makeObservable(gameStrings.parseMove("1"));
		} catch (GameException e) {
		}

		t.print("0 2 3 4 5 6 7 8 9 10? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("3"));
		} catch (GameException e) {
		}
		t.print("0 2 4 5 6 7 8 9 10? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("2"));
		} catch (GameException e) {
		}
		t.print("0 4 5 6 7 8 9 10? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("4"));
		} catch (GameException e) {
		}
		t.print("0 5 6 7 8 9 10? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("6"));
		} catch (GameException e) {
		}
		t.print("0 5 7 8 9 10? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("5"));
		} catch (GameException e) {
		}
		t.print("0 7 8 9 10? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("9"));
		} catch (GameException e) {
		}
		t.print("0 7 8 10? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("7"));
		} catch (GameException e) {
		}
		t.print("0 8 10? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("10"));
		} catch (GameException e) {
		}
		t.print("0 8? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.make(gameStrings.parseMove("8"));
		} catch (GameException e) {
		}
		t.print("0? ");
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
