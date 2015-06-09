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

	/********************************************************************************
	 * Event based simulation
	 ********************************************************************************/
	public void simulate() {
		// initialize PQ with collision events and redraw event
		pq = new PriorityQueue<Event>();

		pq.add(new RedrawEvent(0)); // redraw event
		pq.add(new PaddleBounceEvent(0)); // paddle bounce event
		pq.add(new WallBounceEvent(0)); // wall bounce event

		// the main event-driven simulation loop
		while (!pq.isEmpty()) {
			// get impending event, discard if invalidated
			Event e = pq.poll();
			// move ball, move time
			ball.move(e.time - t);
			leftPaddle.move(e.time - t);
			rightPaddle.move(e.time - t);
			t = e.time;
			// process event (should add new events)
			e.process();
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

	// Event for bouncing off walls
	public class WallBounceEvent extends Event {
		public WallBounceEvent(double t) {
			super(t);
		}

		public void process() {
			ball.wallBounce();
			if (ball.movingLeft())
				leftPaddle.meet(ball);
			else
				rightPaddle.meet(ball);
			pq.add(new WallBounceEvent(t + ball.timeToHitWall()));
		}
	}

	// Event for bouncing off paddles
	public class PaddleBounceEvent extends Event {
		public PaddleBounceEvent(double t) {
			super(t);
		}

		public void process() {
			ball.paddleBounce();
			if (ball.movingLeft()) {
				leftPaddle.meet(ball);
				rightPaddle.stop();
			} else {
				rightPaddle.meet(ball);
				leftPaddle.stop();
			}
			pq.add(new PaddleBounceEvent(t + ball.timeToHitPaddle()));
		}
	}
}
