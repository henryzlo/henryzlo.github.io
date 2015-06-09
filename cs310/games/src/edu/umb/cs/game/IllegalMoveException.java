// IllegalMoveException.java
//
// Ethan Bolker, February 2003

package edu.umb.cs.game;

/**
 * Use instances of this class to report illegal
 * moves.
 */
public class IllegalMoveException extends GameException 
{

	private static final long serialVersionUID = -8277310412645354791L;
	private Move m;
   
    /**
     * Create an Exception.
     *
     * @param m the Move that is illegal.
     */
    public IllegalMoveException( Move m )
    {
	this.m = m;
    }

    public String toString() 
    {
	return super.toString() + " illegal move " + m;
    }
}
