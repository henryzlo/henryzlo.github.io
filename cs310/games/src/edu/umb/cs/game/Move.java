// Move.java
//
// Ethan Bolker, February 2003
// 
// Betty O'Neil, March '09: changed Move from an interface to 
// an abstract class to make the equals and hashCode abstract 
// methods actually require override (interfaces don't have this power.)
// Also note that all these methods are core object methods,
// needing knowledge of full object state, so not 
// "just an interface" kind of functionality.

package edu.umb.cs.game;

/**
 * A Move in an abstract Game.
 * The client must be able to copy a move.
 * 
 */

public abstract class Move
{
	  /**
     * A client must be able to copy a move.
     */
    public abstract Move copy();

    /** 
     * The Move implementation must override equals
     * from class Object. 
     */
    public abstract boolean equals( Object obj );

    /** 
     * The Move implementation must override hashCode
     * from class Object, and be consistent with equals. 
     */
    public abstract int hashCode();
}
