// HumanPlayer.java
//
// Ethan Bolker, February 2003

package edu.umb.cs.gamesui;

import edu.umb.cs.game.Game;
import edu.umb.cs.game.GameException;
import edu.umb.cs.game.Move;
import edu.umb.cs.game.Player;

/**
 * A HumanPlayer has a findbest that uses the UI
 * (the human) to determine the move to make.
 */
public class HumanPlayer extends Player {
	private GameView view;  // the UI

	/**
	 * Construct an interactive (human) Player.
	 */
	public HumanPlayer(GameView v) {
		super("human");
		view = v;
	}

	/**
	 * This HumanPlayer decides what to do next by UI
	 * 
	 * @param g
	 *            the current Game position.
	 * @return the best Move to make.
	 * @throws GameException.
	 */
	public Move findbest(Game g) throws GameException {
		return view.requestMove("human");
	}
}
