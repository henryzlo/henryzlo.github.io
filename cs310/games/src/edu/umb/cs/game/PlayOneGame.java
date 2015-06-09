// PlayOneGame.java
// The simplest AbstractPlayOneGame implementation: assumes all players
// have a findbest that returns a non-null Move. This works for 
// line-oriented-UI human players and computer players, but not with 
// GUI for human UI. For corresponding GUI-compatible code, 
// see GameController.java
//
// Ethan Bolker
// February 2003

package edu.umb.cs.game;

/**
 * A PlayOneGame instance plays a Game just once.
 * 
 * It's the class that couples Game and Players
 */

public class PlayOneGame extends AbstractPlayOneGame {

	/**
	 * Play a game once.
	 * 
	 * @param g
	 *            the Game.
	 * @param one
	 *            the first Player.
	 * @param two
	 *            the second Player.
	 * 
	 * @throws GameException
	 *             if one of the Players can't play this Game.
	 */
	public PlayOneGame(Game g, Player one, Player two) throws GameException {
		super(g, one, two);
	}


	/**
	 * Play one game, starting from the beginning.
	 * The simple loop version, which assumes that
	 * all players can generate moves by simple calls to
	 * findbest. That works for computer algorithms
	 * and also for line-oriented UI, but not for
	 * graphical UI (GUI), which works via events.
	 * 
	 * @throws GameException
	 *             if anything goes wrong.
	 */
	@Override
	public void go() throws GameException {
		informAllBefore();  // this does initObservable()
		resume();   // plays whole game 
	}

	/**
	 * Play one game, starting from the current state.
	 * 
	 * @throws GameException
	 *             if anything goes wrong.
	 */
	public void resume() throws GameException {
		Game g = getGame();
		while (!g.isGameOver()) {
			Player player = getPlayerByPlayerNumber(g.whoseTurn());
			Move m = player.findbest(g.copy());
			g.makeObservable(m);
		}
		// game over
		informAllAfter();
	}

}
