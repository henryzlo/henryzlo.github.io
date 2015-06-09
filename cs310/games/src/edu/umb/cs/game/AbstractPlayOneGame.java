// AbstractPlayOneGame.java
//
// Ethan Bolker
// February 2003

package edu.umb.cs.game;

/**
 * A subclass instance plays a Game just once.
 * 
 * It's the class that couples Game and Players
 */

public abstract class AbstractPlayOneGame {
	private Game g;
	private Player one;
	private Player two;

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
	public AbstractPlayOneGame(Game g, Player one, Player two)
			throws GameException {
		this.g = g;
		this.one = one;
		this.two = two;
		checkCanPlay(g, one);
		checkCanPlay(g, two);
	}

	/**
	 * What is the Game?
	 * 
	 * @return the Game being played.
	 */
	public Game getGame() {
		return g;
	}

	/**
	 * Who played first?
	 * 
	 * @return the first Player.
	 */
	public Player getFirstPlayer() {
		return one;
	}

	/**
	 * Who played second?
	 * 
	 * @return the second Player.
	 */
	public Player getSecondPlayer() {
		return two;
	}

	/**
	 * Did a Player go first or second?
	 * 
	 * @param p
	 *            the Player.
	 * 
	 * @return Game.FIRST_PLAYER or Game.SECOND_PLAYER
	 */
	public PlayerNumber getPlayerNumber(Player p) {
		return (p == one) ? Game.FIRST_PLAYER : Game.SECOND_PLAYER;
	}
	
	/**
	 * Player by PlayerNumber
	 * 
	 * @param p
	 *            the PlayerNumber.
	 * 
	 * @return one or two
	 */
	public Player getPlayerByPlayerNumber(PlayerNumber p) {
		return (p == Game.FIRST_PLAYER? one:two);
	}
	
	/**
	 * Play one game, starting from the beginning.
	 * Does init or initObservable first: for example by call to 
	 * informAllBefore just below.
	 * 
	 * @throws GameException
	 *             if anything goes wrong.
	 */
	public abstract void go() throws GameException;
	
	public void informAllBefore() {
		g.initObservable();
		one.gameStarting(g);
		two.gameStarting(g);
	}

	public void informAllAfter() {
		one.gameOver(g);
		two.gameOver(g);
		g.gameOver();
	}

	private void checkCanPlay(Game g, Player p) throws GameException {
		if (!p.canPlay(g)) {
			throw new GameException("Player " + p.getName()
					+ " can't play Game " + g.getName());
		}
	}
	
}
