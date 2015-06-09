// GameTimer.java
//
// Ethan Bolker, March 2003
//
// Won't work properly once ComputerPlayers begin thinking
// in a separate Thread when it's not their turn.
//
// Doesn't yet report separate statistics for each move.

package edu.umb.cs.game;

import java.util.Observer;
import java.util.Observable;
import edu.umb.cs.timing.*;

/**
 * Time each Player's moves in a Game.
 */
public class GameTimer implements Observer {
	private TimePack tp;
	private Clock onesClock;
	private Clock twosClock;

	/**
	 * Create a GameTimer.
	 * 
	 * @param g
	 *            the Game being timed.
	 */
	public GameTimer(Game g) {
		g.addObserver(this);
		tp = new TimePack();
		onesClock = tp.newClock("one");
		twosClock = tp.newClock("two");
	}

	/**
	 * Action to take when notified of a change in the Game's position: stop
	 * both Players' clocks (since we don't know who last moved) and start the
	 * clock for the Player who moves next.
	 * 
	 * @param game
	 *            the Game being played.
	 * @param args
	 *            ignored.
	 */
	public void update(Observable game, Object args) {
		Game g = (Game) game;
		onesClock.stop();
		twosClock.stop();
		PlayerNumber next = g.whoseTurn();
		if (next == Game.FIRST_PLAYER) {
			onesClock.start();
		} else if (next == Game.SECOND_PLAYER) {
			twosClock.start();
		}
		// else game is over, do nothing
	}

	public String report() {
		return tp.report();
	}

	public double getElapsedSecondsPerMove(PlayerNumber p) {
		Clock c = getClock(p);
		int callCount = c.getCallCount();
		return (callCount == 0) ? 0 : (double) c.getTotalTime()
				/ (1000 * (double) callCount);
	}

	public double getCPUSecondsPerMove(PlayerNumber p) {
		Clock c = getClock(p);
		int callCount = c.getCallCount();
		return (callCount == 0) ? 0 : (double) c.getTotalCPUTime()
				/ (1000 * (double) callCount);
	}

	private Clock getClock(PlayerNumber p) {
		return (p == Game.FIRST_PLAYER) ? onesClock : (p == Game.SECOND_PLAYER ? twosClock : null);
	}

}
