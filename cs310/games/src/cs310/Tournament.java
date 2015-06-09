package cs310;

// Tournament.java
//
// Ethan Bolker
// February 2003

import edu.umb.cs.game.*;

/**
 * A Tournament between two Players, both computer (or fake computer) Doesn't
 * require a view, runs without displaying board.
 * Equivalent in play to PlayGame GameName ComputerPlayer1 ComputerPlayer2
 */
public class Tournament {
	/**
	 * main drives the Tournament.
	 * 
	 * @param args
	 *            Game and two Players.
	 */
	public static void main(String[] args) {
		new Tournament(args);
	}

	/**
	 * Construct a Tournament.
	 * 
	 * @param args
	 *            Game and two Players.
	 */
	public Tournament(String[] args) {
		Game game = null;
		ComputerPlayer one = null;
		ComputerPlayer two = null;
		GameTimer timer = null;
		PlayOneGame playGame = null;
		if (args.length != 3) {
			System.out
					.println("usage: java Tournament GameName ComputerPlayer ComputerPlayer");
			return;
		}
		try {
			game = GameFactory.create(args[0]);
			game.init();
			one = PlayerFactory.createComputerPlayer(args[1]);
			two = PlayerFactory.createComputerPlayer(args[2]);
			timer = new GameTimer(game);
			playGame = new PlayOneGame(game, one, two);
			playGame.go();
			reportWinner(playGame, timer);
		} catch (GameException e) {
			System.err.println(e);
			return;
		} catch (Exception e) {
			System.err.println("unexpected error: " + e);
			return;
		}
	}

	private static void reportWinner(PlayOneGame pog, GameTimer timer) {
		Game g = pog.getGame();
		Player one = pog.getFirstPlayer();
		Player two = pog.getFirstPlayer();
		if (!g.isGameOver()) {
			System.out.println("game is not over");
		}
		if (g.winner() == pog.getPlayerNumber(one)) {
			System.out.println("winner is first player (" + one + ")");
		} else if (g.winner() == pog.getPlayerNumber(two)) {
			System.out.println("winner is second player (" + two + ")");
		} else {
			System.out.println("game is a draw");
		}
		System.out.println(timer.report());

	}
}
