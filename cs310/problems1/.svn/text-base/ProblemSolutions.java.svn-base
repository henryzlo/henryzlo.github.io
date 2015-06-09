import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ProblemSolutions {

	// Solution for problem 1
	public static int countInversions(int[] a) {
		if (a.length <= 1)
			return 0;
		int[] left = F.leftSubArray(a);
		int[] right = F.rightSubArray(a);
		int invs = countInversions(left) + countInversions(right);

		int j = 0;
		int k = 0;
		for (int i = 0; i < a.length; i++) {
			if (j == left.length) {
				a[i] = right[k++];
			} else if (k == right.length) {
				a[i] = left[j++];
			} else if (left[j] <= right[k]) {
				a[i] = left[j++];
			} else if (right[k] < left[j]) {
				a[i] = right[k++];
				invs += left.length - j;
			}
		}
		return invs;
	}

	// Solution for problem 2
	public static int maxProfit(int[] a) {
		if (a.length <= 1)
			return 0;
		int[] left = F.leftSubArray(a);
		int[] right = F.rightSubArray(a);
		int profit = Math.max(maxProfit(left), maxProfit(right));

		int leftMin = left[0];
		int rightMax = right[0];
		for (int i = 1; i < left.length; i++) {
			leftMin = Math.min(left[i], leftMin);
		}
		for (int i = 1; i < right.length; i++) {
			rightMax = Math.max(right[i], rightMax);
		}
		return Math.max(profit, rightMax - leftMin);
	}

	// Solution for problem 3
	public static int matchmaking(int[] f, int[] m) {
		Arrays.sort(f);
		Arrays.sort(m);
		int total = 0;
		for (int i = 0; i < f.length; i++) {
			total += Math.abs(f[i] - m[i]);
		}
		return total;
	}

	// Memoizing solution for problem 4
	// Assume prices[0] is 0, prices[1] is the price of a 1-inch rod, etc.
	public static int maxRodMemo(int[] prices) {
		HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
		return maxRodMemoRecursive(prices, prices.length, m);
	}

	static int maxRodMemoRecursive(int[] prices, int length,
			HashMap<Integer, Integer> m) {
		int newLength, result;
		int best = 0;
		if (length == 0)
			return 0;
		for (int i = 1; i < length; i++) {
			newLength = length - i;
			if (newLength >= 0) {
				if (m.get(newLength) == null) {
					result = maxRodMemoRecursive(prices, newLength, m);
				} else {
					result = m.get(newLength);
				}
				best = Math.max(result + prices[i], best);
			}
		}
		m.put(length, best);
		return best;
	}

	// Dynamic programming solution for problem 4
	// Assume prices[0] is 0, prices[1] is the price of a 1-inch rod, etc.
	public static int maxRodDP(int[] prices) {
		HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
		m.put(0, 0);
		int newLength, value;

		for (int i = 1; i < prices.length; i++) {
			for (int j = 0; j < i; j++) {
				newLength = i + j;
				if (newLength <= prices.length) {
					value = prices[i] + m.get(j);
					if (m.get(newLength) == null) {
						m.put(newLength, 0);
					}
					m.put(newLength, Math.max(m.get(newLength), value));
				}
			}
		}
		return m.get(prices.length - 1);
	}

	// Solution for problem 5
	public static Set<Integer> unique(int[] i1, int[] i2, int[] i3) {
		HashSet<Integer> s1 = new HashSet<Integer>();
		HashSet<Integer> s2 = new HashSet<Integer>();
		HashSet<Integer> s3 = new HashSet<Integer>();

		for (int i = 0; i < i1.length; i++)
			s1.add(i1[i]);
		for (int i = 0; i < i2.length; i++)
			s2.add(i2[i]);
		for (int i = 0; i < i3.length; i++)
			s3.add(i3[i]);

		for (int i = 0; i < i1.length; i++) {
			s2.remove(i1[i]);
			s3.remove(i1[i]);
		} for (int i = 0; i < i2.length; i++) {
			s1.remove(i2[i]);
			s3.remove(i2[i]);
		} for (int i = 0; i < i3.length; i++) {
			s1.remove(i2[i]);
			s2.remove(i2[i]);
		}
		
		s1.addAll(s2);
		s1.addAll(s3);
		return s1;
	}
}
