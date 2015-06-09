// ConsoleView.java
//
// orig. by Ethan Bolker, March 2003

package edu.umb.cs.gamesui;

import edu.umb.cs.game.Game;
import edu.umb.cs.game.GameException;
import edu.umb.cs.game.IllegalMoveException;
import edu.umb.cs.game.Move;
import edu.umb.cs.game.NoSuchMoveException;
import edu.umb.cs.game.GameStrings;
import edu.umb.cs.io.Terminal;

/**
 * Support for a line-oriented view of a Game.
 * This one source handles all the games, because line-oriented i/o
 * is so simple: it's all in the Strings, supplied by the GameStrings.
 */
public class ConsoleView extends GameView {
	private Terminal t;
	private GameStrings gameStrings;

	@Override
	public void setGame(Game g) {
		super.setGame(g);
		gameStrings = g.getGameStrings();
	}
	/**
	 * Construct a line-oriented view of a Game.
	 * 
	 * @param g
	 *            the Game to view.
	 * @param t
	 *            the Terminal on which to display results.
	 */
	public ConsoleView(Game g, Terminal t) {
		setGame(g);  // complete GameView setup
		this.t = t;
	}
	
	public Terminal getTerminal() {
		return t;
	}
	
	@Override
	public void init() {
		// nothing needed
	}

	// for games needing params for setup specification
	@Override
	public void setup() throws GameException {
		Game g = getGame();
		if (!g.needSetup()) {
			String[] defs = gameStrings.getSetupParamDefinitions();
			if (defs == null || defs.length == 0)
				throw new GameException("Game " + g.getName()
						+ " needs setup but has bad SetupParamDefs");

			int[] params = new int[defs.length];

			for (int i = 0; i < defs.length; i++) {
				String input = t.readLine("Please enter value for " + defs[i]);
				params[i] = Integer.parseInt(input);
			}
			g.setup(params);
		}
	}
	
	/**
	 * Display the current position, given the new move
	 * for update to call.
	 * @param newMove Move
	 */
	@Override
	protected void displayNewPosition(Move newMove) {
		// move has been made, so just display the current position
		t.println(gameStrings.position());
	}

	@Override
	public Move requestMove(String playerName) {
		Move m;
		String s = "";
		while (true) {
			try {
				s = t.readLine(playerName + "'s move: ").trim();
				if (s.startsWith("help")|| s.startsWith("?")) {
					t.println(gameStrings.help());
					t.println(gameStrings.position());
					continue;
				}
				m = gameStrings.parseMove(s);
				return m;
			} catch (IllegalMoveException e) {
				t.println("illegal move: " + s);
			} catch (NoSuchMoveException e) {
				t.println("no such move: " + s);
			} catch (Exception e) {
				t.errPrintln("unexpected error " + e);
			}
		}

	}	

	@Override
	public void displayMessage(String message, String title) {
		t.println(message);
	}
}
