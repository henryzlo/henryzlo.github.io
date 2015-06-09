// GameView.java
//
// Ethan Bolker, March 2003

package edu.umb.cs.gamesui;

import java.util.Observer;
import java.util.Observable;

import edu.umb.cs.game.Game;
import edu.umb.cs.game.GameException;
import edu.umb.cs.game.Move;

/**
 * The view API for a Game.
 */
public abstract class GameView implements Observer {
	private Game g;
	// for GUI-compatible views, need to know the (GUI) controller
	// null for line-oriented UI, unless using GUI-compatible controller
	private GameController controller = null;
	
	/**
	 * Construct a view of a Game.
	 * --new view needs call to setGame to be useful
	 */
	public GameView() {
	}

	/**
	 * Set the Game are we viewing.
	 * 
	 * @param g
	 *            the Game.
	 */
	public void setGame(Game g) {
		this.g = g;
		g.addObserver(this);
	}

	/**
	 * Get the Game are we viewing.
	 * 
	 * @return the Game.
	 */
	public Game getGame() {
		return g;
	}
	
	/**
	 * Init the GameView: needed for GUIs, after the game itself
	 * has been init'd.
	 * 
	 */
	public abstract void init();

	public GameController getController() {
		return controller;
	}

	/**
	 * Set a controller if in use.
	 * With a controller, a call the requestMove is allowed to
	 * return a null Move, signifying that the Move will
	 * submitted to the controller later on.
	 * @param controller
	 */
	public void setController(GameController controller) {
		this.controller = controller;
	}
	
	/**
	 * Display a message in the UI.
	 * @param message
	 * @param title
	 */
	public abstract void displayMessage(String message, String title);


	/**
	 * Get information from a user for setting up this Game.
	 */
	public abstract void setup() throws GameException;
	
	
	/**
	 * Request a Move for a certain player.
	 * @param playerName
	 * @return Move, or null if view has a controller
	 * and Move will be submitted later via controller's submitMove
	 */
	public abstract Move requestMove(String playerName);
	
	/**
	 * Display the new position, given the new move
	 * that has just been processed by the game.
	 * For update to call, not the general public.
	 * @param newMove Move
	 */
	protected abstract void displayNewPosition(Move newMove);

	/**
	 * Show/update the position when a message comes from the Game being observed,
	 * and if the view has a controller, notify it.
	 * 
	 * @param obj
	 *            the Game.
	 * @param move
	 *            the new move
	 */
	public void update(Observable obj, Object move) {
		Move m = (Move) move;
		displayNewPosition(m); // display position (initial if move null)
	}
	
}
