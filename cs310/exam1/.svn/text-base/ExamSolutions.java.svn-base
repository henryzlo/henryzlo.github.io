import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class ExamSolutions {

	// Solution for problem 1
	public static int solveSong(int[] s, int[] p, int max) {
		TreeMap<Integer, Integer> memory = new TreeMap<Integer, Integer>();
		recursiveSolveSong(s, p, max, memory);
		return memory.get(memory.lastKey());
	}

	public static int recursiveSolveSong(int[] s, int[] p, int time,
			TreeMap<Integer, Integer> memory) {
		int best = 0;
		int score = 0;
		int timeLeft = 0;
		for (int i = 0; i < s.length; i++) {
			timeLeft = time - s[i];
			if (timeLeft >= 0) {
				if (memory.get(timeLeft) == null) {
					score = recursiveSolveSong(F.removeInt(s, i),
							F.removeInt(p, i), timeLeft, memory);
				} else {
					score = memory.get(timeLeft);
				}
				if (score + p[i] > best)
					best = score + p[i];
			}
		}
		memory.put(time, best);
		return best;
	}

	// Solution for problem 2
	public static int aThenB(String s) {
		if (s.length() == 1)
			return 0;
		int midpoint = s.length() / 2;
		int as = 0;
		int bs = 0;
		String left = s.substring(0, midpoint);
		String right = s.substring(midpoint, s.length());

		for (int i = 0; i < left.length(); i++)
			if (left.charAt(i) == 'a')
				as++;
		for (int i = 0; i < right.length(); i++)
			if (right.charAt(i) == 'b')
				bs++;

		return aThenB(left) + aThenB(right) + (as * bs);
	}

	// Solution for problem 3
	public static String[] potentialDates(String[] names) {
		HashSet<String> firstNames = new HashSet<String>();
		ArrayList<String> result = new ArrayList<String>();
		String[] resultArray;
		String firstName, lastName;

		for (int i = 0; i < names.length; i++) {
			firstName = names[i].split(" ")[0];
			firstNames.add(firstName);
		}
		for (int i = 0; i < names.length; i++) {
			lastName = names[i].split(" ")[1];
			if (firstNames.contains(lastName))
				result.add(names[i]);
		}
		resultArray = result.toArray(new String[result.size()]);
		return resultArray; 
	}
	
	// Solution for problem 4
	public static int salesForce(int[] cities, int numPeople) {
		int result = 0;
		int maxIndex=0;
		while (numPeople>0) {
			for (int i=0;i<cities.length; i++) {
				if (cities[i] > cities[maxIndex])
					maxIndex=i;
			}
			result += cities[maxIndex];
			cities[maxIndex] /= 2; 
			numPeople--;
		}
		return result; 
	}

}
