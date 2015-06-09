// GameController.java
// Alternative to PlayOneGame.java. Its advantage is that it's
// GUI-compatible, allowing the GUI view to submit moves when it wants to
// rather than be required to return them from a call to findbest.
// Note: Stick with PlayOneGame.java to start with
//
// Betty O'Neil
// March, 2009

package edu.umb.cs.gamesui;

import edu.umb.cs.game.Game;
import edu.umb.cs.game.GameException;
import edu.umb.cs.game.Move;
import edu.umb.cs.game.Player;
import edu.umb.cs.game.PlayerNumber;
import edu.umb.cs.game.AbstractPlayOneGame;

// This controller works with both GUI and line-oriented UI, but 
// PlayOneGame is a simpler solution for the line-oriented case.
// GUI case:
// This MVC controller is working like a "wizard", that is, it
// is guiding the the human through a process, and only allowing
// human input when appropriate. A more typical MVC controller
// simply reacts to various human commands coming in from the view.
// In particular, with this controller, a human is not allowed to 
// submit a move before seeing the result of the previous move.
// To accommodate the GUI's need to report the human-chosen move 
// in an event, we wait here (in the controller) after requesting 
// the move with findbest and seeing a null return value. 
// The GUI view submits the move when it can, allowing this 
// code to proceed. 
// Threading in the GUI case:
// This implementation works with just the two original
// threads, the one for main and the one for Swing event dispatch,
// which is created at program start for any Swing application.
// The Swing thread runs during UI and during submitMove for a 
// GUI-determined move (including the follow-on move
// notification to the view that requests display of the move.)
// The main thread runs the main loop in resume() here, and is
// waiting when the GUI view is accepting a new move. The two
// threads can run concurrently when the GUI view is displaying 
// one move while the main thread is computing the next move.
// line-oriented case:
// In the line-oriented case, every call to findbest returns a
// non-null Move, so the wait for move-submission never occurs,
// and everything runs in a single thread, the original thread.

/**
 * Alternative to PlayOneGame.java that is GUI-compatible, 
 * that is, an MVC (model-view-controller) controller.
 *
 */
public class GameController extends AbstractPlayOneGame {
	private GameView view;
	private boolean debug = true;
	private GameException savedException;
	private boolean waitingForSubmit;

	/**
	 * Play a game once.
	 * 
	 * @param view
	 *            the GameView.
	 * @param one
	 *            the first Player.
	 * @param two
	 *            the second Player.
	 * 
	 * @throws GameException
	 *             if one of the Players can't play this Game.
	 */
	public GameController(GameView view, Player one, Player two)
			throws GameException {
		super(view.getGame(), one, two);
		this.view = view;
	}

	/**
	 * Play one game, starting from the beginning
	 * 
	 * @throws GameException
	 *             if anything goes wrong.
	 */
	@Override
	public void go() throws GameException {
		if (debug)
			System.out.println("in GameController go");
		savedException = null; // exception in submitMove, can be in another thread
		// the following calls initObservable, which causes start-game notification, 
		informAllBefore(); 
		resume();      
	}
	
	// The following simple wait/notify scheme depends on the strict
	// time ordering of waitForSubmit, then signalSubmit, as is 
	// enforced by this wizard-like controller. 
	// wait for GUI to submit move
	private synchronized void waitForSubmit() {
		try {
			waitingForSubmit = true;
			this.wait();
		} catch (InterruptedException e) {
		}
	}

	// called from submit: move has been submitted
	private synchronized void signalSubmit() {
		if (waitingForSubmit) {
			waitingForSubmit = false;
			this.notify();
		}
	}
	
	public void resume() throws GameException {
		Game g = getGame();
		while (!g.isGameOver() && savedException == null) {
			Player player = getPlayerByPlayerNumber(g.whoseTurn());
			Move m = player.findbest(g.copy());
			if (m == null)
				waitForSubmit(); // that GUI has submitted move	
			else
				submitMove(m); // make the move in the game
		}
		// here when game over or exception in submitMove
		if (savedException != null) {
			throw savedException;
		}
		showGameOutcome();
		informAllAfter();
	}
	
		
	/**
	 * Submit a new move. Called in Swing thread if called from GUI.
	 * 
	 * @param move
	 */
	public void submitMove(Move move) {
		try {
			if (debug)
				System.out.println("in submitMove for move " + move);
			signalSubmit();  // unhang controller thread if necessary
			// make move, which in turn causes notifications, display
			getGame().makeObservable(move); 

		} catch (GameException e) {
			System.err.println("Exception in submitMove: " + e);
			savedException = e;  // indicate trouble to main loop
		}
	}
	
	// When game is over, show it in UI
	private void showGameOutcome() {
		Game g = getGame();
		PlayerNumber whoseTurn = g.whoseTurn();
		if (whoseTurn == Game.GAME_OVER) {
			PlayerNumber winnerNumber = g.winner();
			if (winnerNumber == Game.DRAW) {
				view.displayMessage("Draw", "Game Over");
			} else {
				Player winner = getPlayerByPlayerNumber(winnerNumber);
				view.displayMessage("" + winner + " wins the game!",
						"Game Over");
			}
		}
	}

}
