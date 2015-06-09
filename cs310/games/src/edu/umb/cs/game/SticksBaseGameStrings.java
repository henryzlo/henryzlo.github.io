// SticksBaseGameStrings.java, March 2003
//
// Created by Qian Huang

package edu.umb.cs.game;

import java.lang.StringBuffer;
import java.util.Iterator;

/**
 * SticksBaseGameStrings is a view for sticks games in String format. It extends
 * GameStrings and implements all methods defined in that class. But it is
 * still marked abstract to indicate that it is not meant for separate use.
 * 
 * @see GameStrings
 */
public abstract class SticksBaseGameStrings extends GameStrings {
	// can only be a GameStrings for Sticks game
	protected SticksBase sticksGame;

	/**
	 * Construct a string view for all sticks games.
	 */
	protected SticksBaseGameStrings(SticksBase g) {
		sticksGame = (SticksBase) g;
	}

	/**
	 * Method to print out help information
	 */
	public String help() {
		return sticksGame.getHelp();
	}

	/**
	 * Display the current position.
	 */
	public String position() {
		int sticksLeft = sticksGame.getSticksLeft();

		StringBuffer s = new StringBuffer();
		for (int i = 1; i <= sticksLeft; i++)
			s.append(" | ");

		if (sticksGame.getSticksLeft() > 0) {
			if (sticksLeft > 1)
				s.append("\nThere are " + sticksLeft + " sticks left: ");
			else
				s.append("\nThere is " + sticksLeft + " stick left: ");
			s.append("Possible moves: "
							+ possibleMoves(sticksGame.getMoves()));
		}
		return s.toString();
	}

	/**
	 * Method to print out possible moves
	 */
	private String possibleMoves(Iterator<Move> m) {
		StringBuffer buffer = new StringBuffer();
		while (m.hasNext()) {
			buffer.append(", " + m.next());
		}
		buffer.replace(0, 2, "[");
		buffer.append(']');

		return buffer.toString();
	}

	/**
	 * When Players enter moves interactively the Game must be able to interpret
	 * what they say.
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
			SticksBase.SticksMove m = (SticksBase.SticksMove) SticksBase.moveList[i];
			if (sticksGame.isLegal(m)) {
				return m;
			}
			throw new IllegalMoveException(m);
		} catch (NumberFormatException e) {
			throw new NoSuchMoveException(s);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchMoveException(s);
		}
	}
}
