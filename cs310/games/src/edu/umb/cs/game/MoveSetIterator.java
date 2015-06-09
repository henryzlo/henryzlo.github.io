// Title: MoveSetIterator.java
// Given a Set<FooMove> movesLeft, etc., construct an Iterator<Move>
// for return from a game.
// We can't just return movesLeft.iterator(), because 
// an Iterator<FooMove> NOT ISA Iterator<Move> 

package edu.umb.cs.game;

import java.util.*;

/**
 * Many games maintain a set of legal moves of some MoveType. This class stamps out
 * an iterator over those moves for return from getMoves() for the game.
 *
 * @param <MoveType>
 */
public class MoveSetIterator<MoveType extends Move>  implements Iterator<Move> {
	private Iterator<MoveType> j;
	private Set<MoveType> movesLeft;

	public MoveSetIterator() {
	}

	public MoveSetIterator(Set<MoveType> movesLeft1) {
		this.movesLeft = movesLeft1;
		j = movesLeft.iterator();
	}

	public boolean hasNext() {
		return j.hasNext();
	}

	public Move next() {
		return j.next();  // This is where the "MoveType extends Move" is used
	}
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
