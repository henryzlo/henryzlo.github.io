import java.util.Arrays;

public class F {
	public static int[] removeInt(int[] array, int index) {
		/*
		 * Convenience function to remove an index from an array
		 */
		int[] result = new int[array.length - 1];
		int j = 0;
		for (int i = 0; i < array.length; i++)
			if (i != index)
				result[j++] = array[i];
		return result;
	}

	public static int[] leftSubArray(int[] array) {
		/*
		 * Convenience function to get left subarray
		 */
		return Arrays.copyOfRange(array, 0, (int) Math.floor(array.length / 2));
	}

	public static int[] rightSubArray(int[] array) {
		/*
		 * Convenience function to get left subarray
		 */
		return Arrays.copyOfRange(array,
				(int) Math.floor(array.length / 2), array.length);
	}
}
