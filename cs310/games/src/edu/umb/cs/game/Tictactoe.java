/**
 * < Tictactoe.java >
 *
 * Ported from tictactoe.c by Haibo Liu and Wenjie Jin
 * Due Wed 19, Feb. 2003
 *
 * Tictactoe game: default is 3x3 board and first player
 * who has 3 moves in one line wins.
 */

package edu.umb.cs.game;

import java.util.Iterator;

import edu.umb.cs.io.Terminal;

/**
 * In this Tictactoe, we use the normal 3x3 board, the default for BigTictactoe.
 * TODO: avoid subclassing a concrete class as is done here, by setting up
 * an abstract class and two concrete subclasses, for this and BigTictactoe.
 */
public class Tictactoe extends BigTictactoe {
	
	public Tictactoe () {
	}
	
	public boolean needSetup() {
		return false;
	}

	/**
	 * What is the name of the Game?
	 * 
	 * @return the name of the Game.
	 */
	public String getName() {
		return "Tictactoe";
	}

	// unit test
	public static void main(String[] args) {
		Game g = new Tictactoe();
		GameStrings gameStrings = g.getGameStrings();
		Terminal t = new Terminal();;

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
			g.make(gameStrings.parseMove("1"));
			g.makeObservable(gameStrings.parseMove("3"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("hash of new Tictactoe after moves 1 and 3: "
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
