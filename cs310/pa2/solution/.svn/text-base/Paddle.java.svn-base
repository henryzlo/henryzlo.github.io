public class Paddle {
	private double x, y; // position
	private double vy; // velocity
	private double width; // paddle width

	// Should be two paddles - one with side="left" and side="right" (or
	// anything)
	public Paddle(String side) {
		if (side.equals("left"))
			x = 0.0;
		else
			x = 1.0;
		y = Math.random();
		vy = 0.0;
		width = 0.05;
	}

	// Updates position based on time
	public void move(double dt) {
		y += vy * dt;
		if (y < 0)
			y = 0;
		else if (y > 1)
			y = 1;
	}

	public void meet(Ball b) {
		double ball_y = b.getY();
		double ball_vy = b.getVY();

		vy = ball_vy + (ball_y - y) / b.timeToHitPaddle();
	}

	public void stop() {
		vy = 0;
	}

	// draw the ball
	public void draw() {
		StdDraw.filledRectangle(x, y, 0.005, width / 2);
	}
}
