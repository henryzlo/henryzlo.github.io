import java.util.PriorityQueue;

public class Simulation {
	private PriorityQueue<Event> pq; // the priority queue
	private double t = 0.0; // simulation clock time
	private Ball ball;
	private Paddle leftPaddle;
	private Paddle rightPaddle;

	// create a new collision system with the given set of particles
	public Simulation() {
		this.ball = new Ball();
		this.leftPaddle = new Paddle("left");
		this.rightPaddle = new Paddle("right");
	}

	public void simulate() {
		while (true) {		
			// move everything
			ball.move(2);
			leftPaddle.move(2);
			rightPaddle.move(2);
			
                        StdDraw.clear();
                        ball.draw();
                        leftPaddle.draw();
                        rightPaddle.draw();
                        StdDraw.show(20);

			// check for collisions
			if (ball.getY() - ball.getRadius() <= 0 || ball.getY() + ball.getRadius() >= 1) 
				ball.wallBounce();
			if (ball.getX() - ball.getRadius() <= 0 || ball.getX() + ball.getRadius() >= 1)
				ball.paddleBounce();

			// make paddles follow ball
			if (ball.movingLeft()) {
				leftPaddle.meet(ball);
				rightPaddle.stop();
			} else {
				rightPaddle.meet(ball);
				leftPaddle.stop();
			}
			
			// update time
			t = t + 2;
		}
	}

	public static void main(String[] args) {
		StdDraw.show(0);
		Simulation system = new Simulation();
		system.simulate();
	}

	// inner classes
	public abstract class Event implements Comparable<Event> {
		protected final double time; // time that event is scheduled to occur

		// create a new event to occur at time t involving a and b
		public Event(double t) {
			this.time = t;
		}

		// compare times when two events will occur
		public int compareTo(Event that) {
			if (this.time < that.time)
				return -1;
			else if (this.time > that.time)
				return +1;
			else
				return 0;
		}

		public abstract void process();
	}

	// Event for redrawing
	public class RedrawEvent extends Event {
		double hz = 0.5; // redraw rate

		public RedrawEvent(double t) {
			super(t);
		}

		public void process() {
			StdDraw.clear();
			ball.draw();
			leftPaddle.draw();
			rightPaddle.draw();
			StdDraw.show(20);
			pq.add(new RedrawEvent(t + 1.0 / hz));
		}
	}
}
