// GameFactory.java
//
// Ethan Bolker, February 2003
//
// To do: create a more specific error message depending on 
// the kind of Exception thrown by the dynamic instance creation
// (class not found, class not the right type, ...).

package edu.umb.cs.game;

import java.util.*;

/**
 * Factory class for constructing Game instances.
 */
public class GameFactory extends AbstractFactory {
	// Hard code the concrete Games in the package, which
	// are the ones we know about.
	private static Set<String> inventory;

	static {
		inventory = new TreeSet<String>();
		inventory.add("Easy");
		inventory.add("Fifteen");
		inventory.add("Putnam");
		inventory.add("CMN");
		inventory.add("Sticks");
		inventory.add("Sticks5");
		inventory.add("Sticks7");
		inventory.add("Game0");
		inventory.add("GameZero");
		inventory.add("Nim");
		inventory.add("BigTictactoe");
		inventory.add("Tictactoe");
	}

	/**
	 * Create a Game given its name.
	 * 
	 * If the Game requested is not in the package, try to find the
	 * corresponding class dynamically.
	 * 
	 * @param gamename
	 *            the name of the Game.
	 * @return the requested Game.
	 * @throws GameException
	 *             if Game not found.
	 */
	public static Game create(String gamename) throws GameException {
		if (inventory.contains(gamename)) {
			gamename = "edu.umb.cs.game." + gamename;
		}
		try {
			return (Game) createObject(gamename);
		} catch (Exception e) {
			throw new GameException("Game " + gamename + " not found");
		}
	}

	/**
	 * Which Games do we know about?
	 * 
	 * @return the Set of Game names.
	 */
	public static Set<String> inventory() {
		return new TreeSet<String>(inventory);
	}
}
