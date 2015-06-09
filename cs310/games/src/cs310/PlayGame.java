package cs310;

import edu.umb.cs.game.*;
import edu.umb.cs.gamesui.GameController;
import edu.umb.cs.gamesui.GameView;
import edu.umb.cs.gamesui.HumanPlayer;
import edu.umb.cs.gamesui.ViewFactory;
import edu.umb.cs.io.Terminal;

/**
 * Driver for playing games - any two players (implementations of the Player
 * interface) can play any game (implementation of the Game interface).
 * 
 * Written (in Pascal) by Ethan Bolker, April 1984, for Math 301. modified
 * October 1984, October 1986, October 1987 for CS310 converted to C April 1990
 * for CS310 by ? modified Oct 1993 to restore calls to timing package modified
 * Nov 1995 to restore calls to timing package! cleaned up by Dina Goldin, Nov.
 * 1999 modified to use ARM package (~eb/arm) by Szymon Jaroszewicz Mar 2002
 * 
 * converted from C to Java November 2002 - January 2003 modified to fix the bug
 * that initTimer( ) pass Player one, two instead of the first and second player
 * by Rui Yu Sept. 2004
 * 
 * Supports GUI views as well as line-oriented (Terminal) views, by Betty O'Neil
 * March, 2009
 */

public class PlayGame {
	private static Terminal t = new Terminal();

	/**
	 * Play the game interactively
	 * 
	 * @param args
	 *            [-c|-cg] GameName [PlayerName1 PlayerName2] PlayerName1
	 *            defaults to human. PlayerName2 defaults to cs310.Backtrack.
	 * 
	 *            -c: flag for using GUI-compatible controller
	 *            -cg: use GUI view and GUI-compatible controller 
	 */
	public static void main(String[] args) {

		String firstPlayerName, secondPlayerName;
		boolean useController = false; // use simple PlayOneGame, line-oriented
		boolean useGUI = false; // simple case: use line-oriented UI
		int argIndex = 0;
		if (args.length == 0) {
			usage();
			return; 
		}
		// interpret any flags (if none, use simple line-oriented case)
		if (args[0].startsWith("-c")) {
			useController = true; // use GUI-compatible GameController
			if (args[0].equals("-cg"))
				useGUI = true;
			argIndex++;
		}
		String gameName = args[argIndex++];
		// default computer player does BackTrack algorithm
		// or you can use "human" here too, for human vs. human
		if (args.length == argIndex) {
			firstPlayerName = "human"; // human plays first here
			secondPlayerName = "cs310.Backtrack";
		} else if (args.length == argIndex + 2) {  // two more args
			firstPlayerName = args[argIndex++]; // human or computer, plays first
			secondPlayerName = args[argIndex]; // human or computer, plays second
		} else {
			System.out.println("Wrong number of arguments: ");
			usage();
			return;
		}
		try {
			// play the one game as specified by command line arguments
			playGame(gameName, firstPlayerName, secondPlayerName,
					useController, useGUI);
		} catch (GameException e) {
			t.println(e);
			t.println("Failed, exiting");
			System.exit(1);
		}
		System.exit(0); // exit JVM, killing all threads
	}

	// Create Game, GameView, two Players, PlayOneGame, then play one game
	// firstPlayerName, secondPlayerName: "human" or "cs310.Backtrack", or ...
	// For simple line-oriented-UI-only case: useController=false, useGUI=false.
	//  This case uses PlayOneGame for playing the game. (Study this first.)
	// To run a GUI like TictactoeGUI, specify useController=true, useGUI=true
	static void playGame(String gameName, String firstPlayerName,
			String secondPlayerName, boolean useController, boolean useGUI)
			throws GameException {
		// create the Game
		Game game = GameFactory.create(gameName);
	
		// create the view, which receives notifications of moves
		// from the game and displays the new game position.
		// It is also used in getting moves from a human user
		GameView view = ViewFactory.createView(game, useGUI);
		if (game.needSetup())
			view.setup(); // user specifies game parameters in a few games
		t.println("Pit your wits against the machine playing " + game.getName()
				+ (useGUI ? " (with GUI)" : ""));
		if (t.readYesOrNo("Do you need help?")) {
			t.println(game.getGameStrings().help());
		}
		// create two players (human player needs view for move input)
		Player firstPlayer, secondPlayer;
		if (firstPlayerName.equals("human")) 
			firstPlayer = new HumanPlayer(view);
		else  
			firstPlayer = PlayerFactory.createComputerPlayer(firstPlayerName);
		if (secondPlayerName.equals("human")) 
			secondPlayer =  new HumanPlayer(view);
		else
			secondPlayer = PlayerFactory.createComputerPlayer(secondPlayerName);
		
		// create the object that runs one game, a "PlayOneGame" object, or "controller"
		AbstractPlayOneGame playThisGame;
		if (useController) { // GUI-compatible (MVC = model-view-controller)
			GameController controller = new GameController(view, firstPlayer, secondPlayer);
			view.setController(controller);
			playThisGame = controller;  // the controller runs one game
		} else   // simple line-oriented case (NOTE: consider this first)
			playThisGame = new PlayOneGame(game, firstPlayer, secondPlayer);

		GameTimer timer = new GameTimer(game); // to time the game playing
		playThisGame.go(); // init and play the one game
		
		reportWinner(playThisGame);
		reportTimes(timer, firstPlayerName, secondPlayerName);
	}

	/**
	 * Who has won the game? Prints one of first player's name, second player's
	 * name, or other useful information.
	 * 
	 * @param playOnce
	 *            the single game being played
	 * 
	 */
	public static void reportWinner(AbstractPlayOneGame playOnce) {
		Game g = playOnce.getGame();
		if (!g.isGameOver()) {
			t.println("Game is not over");
		}
		// Retrieve full Player object for winner
		Player one = playOnce.getFirstPlayer();
		Player two = playOnce.getSecondPlayer();
		if (g.winner() == playOnce.getPlayerNumber(one)) {
			t.println("winner is " + one);
		} else if (g.winner() == playOnce.getPlayerNumber(two)) {
			t.println("winner is " + two);
		} else {
			t.println("game is a draw");
		}
	}

	public static void reportTimes(GameTimer timer, String firstPlayerName,
			String secondPlayerName) {
		t.println(timer.report());
		String line1 = String
				.format("First player (%s) used %5.3f secs per move (CPU = %5.3f secs)",
						firstPlayerName, 
						timer.getElapsedSecondsPerMove(Game.FIRST_PLAYER),
						timer.getCPUSecondsPerMove(Game.FIRST_PLAYER));
		t.println(line1);
		String line2 = String
				.format("Second player (%s) used %5.3f secs per move (CPU = %5.3f secs)",
						secondPlayerName, 
						timer.getElapsedSecondsPerMove(Game.SECOND_PLAYER),
						timer.getCPUSecondsPerMove(Game.SECOND_PLAYER));
		t.println(line2);
	}
	public static void usage() {
		t.println("Usage: java PlayGame [-c|-cg] GameName <Player1> <Player2>");
		t.println("  Player1 defaults to human");
		t.println("  Player2 defaults to cs310.Backtrack");
		t.println("  -c use GUI-compatible controller");
		t.println("  -cg use GUI and GUI-compatible controller");
		t.println("Examples:");
		t.println("  java PlayGame Easy    runs Easy with human as Player1, cs310.Backtrack as Player2");
		t.println("  java PlayGame Easy cs310.Backtrack human   runs Easy with human as Player2");
		t.println("  java PlayGame Tictactoe runs   Tictactoe with human first, cs310.Backtrack second");
		t.println("  java PlayGame -cg Tictactoe runs Tictactoe with GUI");
	}

}
