/*************************************************************************
 * Compilation: javac Particle.java
 * 
 * A particle moving in the unit box with a given position, velocity, radius,
 * and mass.
 * 
 *************************************************************************/

public class Ball {
	private static final double INFINITY = Double.POSITIVE_INFINITY;

	private double x, y; // position
	private double vx, vy; // velocity
	private double radius; // radius

	// create a random particle in the unit box (overlaps not checked)
	public Ball() {
		x = Math.random();
		y = Math.random();
		vx = 0.01 * (Math.random() - 0.5);
		vy = 0.01 * (Math.random() - 0.5);
		radius = 0.01;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public double getVY() {
		return vy;
	}
	
	public double getRadius() {
		return radius;
	}

	public boolean movingLeft() {
		return (vx < 0);
	}

	// updates position
	public void move(double dt) {
		x += vx * dt;
		y += vy * dt;
	}

	// time until this ball collides with paddle (supposedly)
	public double timeToHitPaddle() {
		if (vx > 0)
			return (1.0 - x - radius) / vx;
		else if (vx < 0)
			return (radius - x) / vx;
		else
			return INFINITY;
	}

	// time until this ball collides with wall
	public double timeToHitWall() {
		if (vy > 0)
			return (1.0 - y - radius) / vy;
		else if (vy < 0)
			return (radius - y) / vy;
		else
			return INFINITY;
	}

	// update velocity of ball after hitting wall
	public void wallBounce() {
		vy = -vy;
	}

	// update velocity of ball after hitting paddle
	public void paddleBounce() {
		vx = -vx;
	}

	public void draw() {
		StdDraw.filledCircle(x, y, radius);
	}}
