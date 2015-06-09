package cs310;

// Backtrack.java
//
// Stub version--you fill in real implementation
// Ethan Bolker, February 2003

import edu.umb.cs.game.*;
import edu.umb.cs.io.Terminal;

/**
 * Backtracking ComputerPlayer chooses the best move by recursively exploring
 * the entire Game tree below the current position.
 * 
 * That algorithm isn't implemented here yet - this version goes to the terminal
 * and asks the user to choose the computer's move.
 */

public class Backtrack extends ComputerPlayer {
	// Use interactive input to fake backtracking algorithm
	Terminal t = new Terminal();

	public Backtrack() {
		super("fake backtracking findbest");
	}

	/**
	 * This supposedly smart ComputerPlayer consulting his (interactive) oracle.
	 * 
	 * @param g
	 *            the current Game position.
	 * @return the best Move to make.
	 * @throws GameException.
	 */
	public Move findbest(Game g) throws GameException {
		GameStrings view = g.getGameStrings();
		while (true) {
			try {
				String s = t.readLine("computer's move: ").trim();
				return view.parseMove(s);
			} catch (GameException e) {
				t.println(e.toString());
			}
		}
	}
}
