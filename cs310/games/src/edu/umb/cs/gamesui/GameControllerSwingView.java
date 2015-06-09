// GameController.java
// Alternative to PlayOneGame.java. Its advantage is that it's
// GUI-compatible, by allowing the GUI to submit moves when it wants to
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

// For use with GUI, where we need to be more event-driven.
// This also works with line-oriented UI, but PlayOneGame is much simpler
// and works fine for line-oriented UI.

/**
 * Alternative to PlayOneGame.java that is GUI-compatible.
 *
 */
public class GameControllerSwingView extends AbstractPlayOneGame {
	private GameView view;
	private boolean debug = true;

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
	public GameControllerSwingView(GameView view, Player one, Player two)
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
		// the following calls initObservable, which causes start-game notification, 
		// received here by moveDone, which kicks off the first move request
		informAllBefore(); 
		resume();      // do some (or all) moves, until GUI needed (GUI will
		               // call resume again to continue game)
		Game g = getGame();
		if (!g.isGameOver())   // may never have needed GUI
			waitForGameEnd();  // let events run the GUI game
		if (!g.isGameOver()) {  // for example because of exception in another thread
			throw new GameException("Game ended abnormally");
		}
	}

	private synchronized void waitForGameEnd() {
		try {
			this.wait();
		} catch (InterruptedException e) {
		}
	}

	// called from last move's notification in update()
	private synchronized void notifyGameEnd() {
		this.notify();
	}
	
	
	/**
	 * Play a sequence of moves, until we need a human GUI move, then
	 * return. Once that human GUI move is processed by the GUI, it
	 * will call resume again in another non-Swing thread
	 * 
	 */
	public void resume() {
		try {
			Game g = getGame();
			while (!g.isGameOver()) {
				Player player = getPlayerByPlayerNumber(g.whoseTurn());
				Move m = player.findbest(g.copy());
				if (m == null) 
					return;   // move is to be submitted by GUI later
				else 
					submitMove(m);  // make the move in the game			
			}
			// game over
			showGameOutcome();
			informAllAfter();
			notifyGameEnd(); // unhang original caller of go(), let it return
		} catch (GameException e) {
			System.err.println(" Exception in resume: " + e);
			notifyGameEnd();
		}
	}

	
	/**
	 * For GUI view to submit a new move, (sometime after getting the move
	 * request via requestMove, and returning null from that call) as well as to
	 * submit a move returned by findbest above. Called from Swing thread in
	 * first case, another thread in second The game will notify the view about
	 * the new move, causing a display request for the new position.
	 * 
	 * @param move
	 */
	public void submitMove(Move move) {
		try {
			if (debug)
				System.out.println("in submitMove for move " + move);
			Game g = getGame();
			g.makeObservable(move); // which in turn causes notifications, display

		} catch (GameException e) {
			System.err.println("Exception in submitMove: " + e);
			notifyGameEnd();
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
