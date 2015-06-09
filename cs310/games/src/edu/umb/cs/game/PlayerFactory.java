// PlayerFactory.java
//
// Ethan Bolker, February 2003
//
// To do: create a more specific error message depending on 
// the kind of Exception thrown by the dynamic instance creation
// (class not found, class not the right type, ...).

package edu.umb.cs.game;

/**
 * Factory class for constructing Player instances, except human players,
 * which need access to a view. They are separately constructed. See HumanPlayer.
 * 
 */
public class PlayerFactory extends AbstractFactory {
	/**
	 * Create a Player given its name.
	 * 
	 * If the Player requested is not in the package, try to find the
	 * corresponding class dynamically.
	 * 
	 * @param playername
	 *            the name of the Player.
	 * @return the requested Player.
	 * @throws GameException
	 *             if Player not found.
	 */
	private static Player create(String playername) throws GameException {
		try {
			return (Player) createObject(playername);
		} catch (Exception e) {
			throw new GameException("Player " + playername + " not found");
		}
	}

	/**
	 * Public: Create a Computer player
	 * 
	 * @return a the ComputerPlayer
	 */
	public static ComputerPlayer createComputerPlayer(String s)
			throws GameException {
		try {
			return (ComputerPlayer) create(s);
		} catch (ClassCastException e) {
			throw new GameException(s + " is not a ComputerPlayer");
		}
	}
}
