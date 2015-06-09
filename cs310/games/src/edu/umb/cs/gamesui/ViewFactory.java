package edu.umb.cs.gamesui;

import edu.umb.cs.game.Game;
import edu.umb.cs.game.GameException;
import edu.umb.cs.io.Terminal;
import edu.umb.cs.game.AbstractFactory;

/**
 * View Factory that creates a ConsoleView, or if useGUI = true,
 * finds and constructs a GUI view for a certain Game if it's there.
 * For example, it finds TictactoeGUI for Tictactoe.
 *
 */
public class ViewFactory extends AbstractFactory {
	public static GameView createView(Game game, boolean useGUI)
			throws GameException {
		GameView view = null;
		// extensible setup: look for GUI class, use it if it's there
		// example: Tictactoe has GUI for moves named "TictactoeGUI"
		if (useGUI) {
			String guiClass = "edu.umb.cs.gamesui." + game.getName() + "GUI";
			try {
				// need specific GUI for game
				view = (GameView) createObject(guiClass);
				view.setGame(game);
			} catch (Exception e) {
				throw new GameException(guiClass + " not found");
			}
		} else { // use simple line-oriented UI, available for all games
			view = new ConsoleView(game, new Terminal());
		}
		return view; // which can supply the game object
	}
}
