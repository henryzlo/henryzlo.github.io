// TestSticks.java, March 2003 
//
// Created by Qian Huang and Omofolakunmi Ogunlesi for hw5

//Packages and Imports
package edu.umb.cs.game;

import java.util.Iterator;

import edu.umb.cs.io.Terminal;

/**
 * Class TestSticks is used for testing purpose. It tests all methods in
 * SticksGame and Sticks5.
 * 
 * @author Qian Huang, Omofolakunmi Ogunlesi
 */
public class TestSticks {

	// unit test
	public static void main(String[] args) {

		SticksBase g = new Sticks7();
		GameStrings gameStrings = g.getGameStrings();
		Terminal t = new Terminal();
		g.init();
		t.println("Start of game:" + g);
		t.println("name of game:" + g.getName());
		t.println("author of game:" + g.getAuthor());
		t.println("help of game:" + g.getHelp());
		t.println("Start sticks : " + ((Sticks7) g).getStartSticks());
		t.println(gameStrings.position());
		t.println("g.winner(): " + g.winner());
		t.println("\n after making move 1, 2, 1( print position)");
		try {
			g.make(gameStrings.parseMove("1"));
			g.make(gameStrings.parseMove("2"));
			g.makeObservable(gameStrings.parseMove("1"));
			t.println("Is game new? " + g.isGameNew());
		} catch (GameException e) {
			t.println(e);
		}
		t.println("final position:");
		t.println("winner is" + g.winner());

		t.println("\ntest hash and copy");
		t.println("hash of finished Sticks: " + g.hashCode());
		t.println("view position before and after init():");
		t.println(gameStrings.position());
		g.init();
		t.println(gameStrings.position());

		t.println("hash of new Sticks7: " + g.hashCode());
		Game copyOfG = g.copy();
		t.println("hash of copy: " + copyOfG.hashCode());
		t.println("g.equals(copyOfG)? " + g.equals(copyOfG));
		t.println("\nmake move 1 then makeObservable move 1");
		try {
			// get move 1 from iterator
			Iterator<Move> i = g.getMoves();
			i.next();
			Move move1 = i.next();
			g.make(move1);
			g.makeObservable(move1);
		} catch (GameException e) {
			t.println(e);
		}
		t.println("\nhash of new Sticks7 after make move 1 "
				+ "and makeObservable move 1: " + g.hashCode());
		t.println("hash of copy: " + copyOfG.hashCode());

		t.println("\ntest parseMove error handling");
		try {
			gameStrings.parseMove("1");
			t.println("only parse move 1, didn't make move");
			t
					.println("sticksLeft is " + ((Sticks7) g).getSticksLeft());
		} catch (GameException e) {
			t.println(e);
		}
		try {
			t.println("parse move foo");
			gameStrings.parseMove("foo");
		} catch (GameException e) {
			t.println(e);
		}
		try {
			t.println("make move 4");
			g.make(gameStrings.parseMove("4"));
		} catch (GameException e) {
			t.println("should catch parsing move 4");
			t.println(e);
		}
		t.println(gameStrings.position());

		t.println("\ntest quit");
		t.println("player 1 makeObservable move 0");
		try {
			g.makeObservable(gameStrings.parseMove("0"));
		} catch (GameException e) {
			t.println(e);
		}
		t.println("winner is" + g.winner());

		t.println("\ntest Move Iterator after init()");
		g.init();
		t.println(gameStrings.position());
		t.print("0 1 2 ? ");
		Iterator<Move> i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			t.println("makeObservable move 2 and 2");
			g.makeObservable(gameStrings.parseMove("2"));
			g.makeObservable(gameStrings.parseMove("2"));
		} catch (GameException e) {
		}
		t.print("0 1 2 ? ");

		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.makeObservable(gameStrings.parseMove("2"));
			t.println("after parsing one more move 2");
		} catch (GameException e) {
		}
		t.print("0 1 ? ");
		i = g.getMoves();
		while (i.hasNext()) {
			t.print(i.next() + " ");
		}
		t.println();
		try {
			g.makeObservable(gameStrings.parseMove("2"));
		} catch (GameException e) {
			t.println("in exception. OOPs! only one stick left");
		}
		try {
			g.makeObservable(gameStrings.parseMove("1"));
			t.println("take the last one");
		} catch (GameException e) {
		}
		t.print("? ");
		try {
			i.next();
			t.println("no possible move in the iterator");
		} catch (java.util.NoSuchElementException e) {
			t.println(e);
		}
	}
}
