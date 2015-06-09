package edu.umb.cs.gamesui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.umb.cs.game.Game;
import edu.umb.cs.game.GameException;
import edu.umb.cs.game.Move;
import edu.umb.cs.game.PlayerNumber;

/**
 * A sample Swing GUI, for Tictactoe.
 * Note that it is specific to Tictactoe, because the board has its own
 * distinctive look.
 * @author Betty O'Neil, for spring, '09
 * Swing GUIs do all the UI actions in a Swing event dispatch thread, so
 * the developer must take care to understand what is happening in the
 * UI thread and what in the main thread. See discussion in GameController.
 */
public class TictactoeGUI extends GameView {

	public static final int ROWS = 3;
	public static final int COLS = 3;
	private TTTButton buttons[] = new TTTButton[ROWS * COLS];
	private Map<Move, TTTButton> buttonForMove = new TreeMap<Move, TTTButton>();
	private PlayerNumber whoseTurn = Game.FIRST_PLAYER;	

	// needs call to setGame ASAP to complete GameView setup, 
	// also call to init() after game is init'd (accomplished 
	// by using game-starting notification in displayNewPosition)
	public TictactoeGUI() {
	}
	
	// call this after game itself has been init'd	
	// Should be called in the Swing event dispatch thread
	@Override
	public void init() {
		JFrame ticTacToe = new JFrame("Tic Tac Toe");
		ticTacToe.setSize(300, 300);
		ticTacToe.setLocation(200, 200);
		ticTacToe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(ROWS, COLS));

		Iterator<Move> itr = getGame().getMoves();
		int index = 0;
		itr.next(); // skip quit move
		while (itr.hasNext()) {
			Move m = itr.next();
			buttons[index] = new TTTButton(m, "     ");
			buttons[index].addActionListener(new Click());
			buttonForMove.put(m, buttons[index]); // for later display
			// of played move
			buttonPanel.add(buttons[index++]);
		}

		ticTacToe.add(buttonPanel); // adds the panel to the frame.
		ticTacToe.setResizable(false);
		ticTacToe.pack(); // makes the frame work
		ticTacToe.setVisible(true); // makes the frame visible
	}
	
	@Override
	public void setup() throws GameException {
	  // nothing needed		
	}
	
	@Override
	public void displayMessage(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.INFORMATION_MESSAGE);

	}
	/**
	 * Handle click of button for a certain move.
	 * Runs in Swing thread.
	 *
	 */
	private class Click implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			TTTButton button = (TTTButton) event.getSource();
			Move m = button.getMove();

			// Note: the view gets notified by the game about the new move
			// and calls displayNewPosition here, before submitMove returns.
			// Alternatively, we could hand off the *move* to the controller
			// (let it do the submitMove) and return from here sooner.
			// But it's normal to call into the "model" (here the Game)
			// in the Swing thread as this does, for short actions, and 
			// we don't expect game.moveObservable() to take a long time.
			GameController controller = getController();
			controller.submitMove(m);
		}
	}
	@Override
	public Move requestMove(String playerName) {
		// requestMove is called in a non-Swing thread.
		// Swing actions should occur in its event dispatch thread.
		// This construct throws the following work into that thread
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				enableInput();
			}
		});
		return null; // indicate that GUI will later submit move
	}

	/**
	 * Update position display due to new move. 
	 * If move is null, notification of game start (from non-Swing thread) 
	 * If non-null, move has been made and recorded in game position 
	 * and this is called in the Swing event dispatch thread only
	 * if it was a human (GUI) move. So queue the work in the Swing
	 * event queue--OK even if this is already running in the Swing
	 * event queue (just makes a separate event out of this work.)
	 * 
	 * @param move
	 *            new Move, or null for game start
	 */
	@Override
	protected void displayNewPosition(Move move) {
		final Move m = move;
		// Note: need to get snapshot of whoseTurn here, not in run() below
		// whoseTurn may change during display!
		final PlayerNumber previousWhoseTurn = whoseTurn; // turn for this display
		whoseTurn = getGame().whoseTurn(); // for next display action
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				if (m == null) {  // game starting
					init();
				} else {
					// look up the button for this move
					TTTButton button = buttonForMove.get(m);		
					changeButtonDisplay(button, previousWhoseTurn);
					disableInput(); // move completed, disallow UI until next
									// move wanted
				}
			}
		});
	}
	
	
	// change blank to X or O to mark played square
	private void changeButtonDisplay(TTTButton button, PlayerNumber whoseTurn) {
		button.setPlayed();
		button.setEnabled(false);
		if (whoseTurn == Game.FIRST_PLAYER
				&& button.getText() == "     ") {		
			button.setText(" X ");
		} else if (whoseTurn == Game.SECOND_PLAYER
				&& button.getText() == "     ") {
			button.setText(" O ");		
		} else 
			button.setText("XXX"); // error
	}
	private void disableInput() {
		for (TTTButton b: buttons) {
			b.setEnabled(false);
		}
	}
	
	private void enableInput() {
		for (TTTButton b : buttons) {
			if (!b.isPlayed())
				b.setEnabled(true);
		}
	}
	
	private class TTTButton extends JButton {

		private static final long serialVersionUID = 1L;
		private Move move;
		private boolean played;

		public Move getMove() {
			return move;
		}

		public TTTButton(Move move, String label) {
			super(label);
			this.move = move;
			played = false;
			// TODO: no "default button" here, but still see one button
			// with little box on it, next to one just selected by click
			setDefaultCapable(false);
			setEnabled(false);  // enable when program wants input
		}
		public void setPlayed() {
			played = true;
		}
		public boolean isPlayed() {
			return played;
		}
	}

}
