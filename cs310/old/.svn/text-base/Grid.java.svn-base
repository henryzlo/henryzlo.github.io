// Grid.java, for pa3
// originally by Scott Madin
package cs310;

import java.util.Set;
import java.util.HashSet;

/**
 * Class to demonstrate greedy algorithms
 */
public class Grid {
	private boolean[][] grid = null;

	/**
	 * Very simple constructor
	 * 
	 * @param ingrid
	 *            a two-dimensional array of boolean to be used as the grid to
	 *            search
	 */
	public Grid(boolean[][] ingrid) {
		grid = ingrid;
	}

	/**
	 * Main method, creates a Grid, then asks it for the size of the group
	 * containing the given point.
	 */
	public static void main(String[] args) {
		int i = 0;
		int j = 0;

		// Make sure we've got the right number of arguments
		if (args.length != 2) {
			System.err.println("Incorrect arguments.");
			printUsage();
			return;
		} else {
			i = Integer.parseInt(args[0]);
			j = Integer.parseInt(args[1]);
		}

		// Usage: java Grid 3 7 to search from (3, 7), top occupied square
		// of L-shaped group of Figure 7.30, pg. 281

		boolean[][] gridData = {
				{ false, false, false, false, false, false, false, false,
						false, true },
				{ false, false, false, true, true, false, false, false, false,
						true },
				{ false, false, false, false, false, false, false, false,
						false, false },
				{ false, false, false, false, true, false, false, true, false,
						false },
				{ false, false, false, true, false, false, false, true, false,
						false },
				{ false, false, false, false, false, false, false, true, true,
						false },
				{ false, false, false, false, true, true, false, false, false,
						false },
				{ false, false, false, false, false, false, false, false,
						false, false },
				{ false, false, false, false, false, false, false, false,
						false, false },
				{ false, false, false, false, false, false, false, false,
						false, false } };

		Grid mygrid = new Grid(gridData);

		int size = mygrid.groupSize(i, j);
		System.out.println("Group size: " + size);
	}

	/**
	 * Prints out a usage message
	 */
	private static void printUsage() {
		System.out.println("Usage: java Grid <i> <j>");
		System.out
				.println("Find the size of the cluster of spots including position i,j");
	}

	/**
	 * This calls the recursive method to find the group size
	 * 
	 * @param i
	 *            the first index into grid (i.e. the row number)
	 * @param j
	 *            the second index into grid (i.e. the col number)
	 * @return the size of the group
	 */
	public int groupSize(int i, int j) {
	    // Implement this function which, among other calls a helper recursive function 
	    // to find the group size.
	}


	/**
	 * Nested class to represent a filled spot in the grid
	 */
	private static class Spot {
		int i;
		int j;

		/**
		 * Constructor for a Spot
		 * 
		 * @param i
		 *            the i-coordinate of this Spot
		 * @param j
		 *            the j-coordinate of this Spot
		 */
		public Spot(int i, int j) {
			this.i = i;
			this.j = j;
		}

		/**
		 * Tests whether this Spot is equal (i.e. has the same coordinates) to
		 * another
		 * 
		 * @param o
		 *            an Object
		 * @return true if o is a Spot with the same coordinates
		 */
		public boolean equals(Object o) {
			if (o == null)
				return false;
			if (o.getClass() != getClass())
				return false;
			Spot other = (Spot) o;
			return (other.i == i) && (other.j == j);

		}

		/**
		 * Returns an int based on Spot's contents
		 * another way: (new Integer(i)).hashCode()^(new Integer(j)).hashCode();
		 */
		public int hashCode() {
			return i << 16 + j; // combine i and j two halves of int
		}

		/**
		 * Returns a String representing this Spot
		 */
		public String toString() {
			return "(" + i + " , " + j + ")";
		}
	}
}