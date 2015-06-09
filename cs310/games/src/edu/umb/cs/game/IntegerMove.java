// Title: IntegerMove.java
// Author: Sumana Adma, Saritha Janaswamy, Chitra Karki.
// Spring 2003

package edu.umb.cs.game;

/**
 * Abstract class to help Games implement moves that are just wrapped integers.
 *
 */
public abstract class IntegerMove extends Move implements Comparable<IntegerMove> {
	private final int spot;

	public IntegerMove(int spot) {
		this.spot = spot;
	}
	
	protected int getSpot() {
		return spot;
	}
	/**
	 * Equals based on int's equality
	 * Note that this still works for two objects of a subclass
	 * of this class, so subclasses don't have to override equals.
	 * @return boolean
	 */
	public boolean equals(Object x) {
		if (x == null || x.getClass() != getClass())
			return false;
		return spot == ((IntegerMove) x).spot;
	}
	
	/**
	 * This is a perfect hash, and consistent with equals
	 * @return integer
	 */
	public int hashCode() { return spot; }
	
	/**
	 * This is consistent with equals
	 * @return integer
	 */
	public int compareTo(IntegerMove o) {
		return spot - o.spot;
	}

	public String toString() {
		return "" + spot;
	}
	/**
     * Required by API: A client must be able to copy a move.
     * Because these objects are immutable, and compared by equals, compareTo
     * (not ==), an object and its "copy" can be the same object--
     */
	public Move copy() {
		return this;
	}


}
