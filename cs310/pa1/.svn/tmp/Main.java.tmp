import java.util.UUID;
import java.util.ArrayList;

public class Main {

	/**
	 * Convenience function for calculating average.
	 * 
	 * @param ints, an array of integers
	 * @return the average of ints
	 */
	public static float mean(int[] ints) {
		int sum = 0;
		for (int i : ints)
			sum += i;
		return (float) sum / ints.length;
	}

	/**
	 * The main function runs an experiment on the LinearHashSet,
	 * QuadraticHashSet, and ChainingHashSet implementations. The experiment
	 * adds a bunch of objects, then determines the number of total collisions
	 * after each add. Collisions are cumulative, and averaged across multiple
	 * runs.
	 */
	public static void main(String[] args) {
		// We try adding this number of elements.
		final int NUM_ELEMENTS = 1000;
		// We average collision counts for this number of runs.
		final int RUNS = 500;

		ArrayList<AbstractHashSet<String>> hashSets = new ArrayList<AbstractHashSet<String>>(
				RUNS);

		for (int i = 0; i < 2; i++) {
			// initialize array of each type of data structure
			switch (i) {
			case 0:
				for (int j = 0; j < RUNS; j++)
					hashSets.add(j, new LinearHashSet<String>(NUM_ELEMENTS));
				System.out.print("linear");
				break;
			case 1:
				for (int j = 0; j < RUNS; j++)
					hashSets.add(j, new ChainingHashSet<String>(NUM_ELEMENTS));
				System.out.print("chaining");
				break;
			}

			// in each run, we add one element, then get average cumulative
			// collisions over all data structures
			boolean cannot_find = false;
			for (int j = 0; j < NUM_ELEMENTS; j++) {
				// Add random strings to hash set, then count collisions
				int[] collisions = new int[RUNS];
				for (int k = 0; k < RUNS; k++) {
					String value = UUID.randomUUID().toString();
					cannot_find = !hashSets.get(k).add(value);
					if (cannot_find)
					    break;
					collisions[k] = hashSets.get(k).getCollisions();
				}
				if (!cannot_find) 
				    System.out.format(",%.2f", mean(collisions));
				else
				    break;
			}
			System.out.println();
		}
	}
}
