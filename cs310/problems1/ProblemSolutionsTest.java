import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class ProblemSolutionsTest {

	@Test
	public void problem1() {
		int[] s1 = { 6, 3, 4, 5, 1 };
		int[] s2 = { 1, 2, 3, 4, 5, 6 };
		int[] s3 = { 10, 6, 3, 1 };
		assertEquals(7, ProblemSolutions.countInversions(s1));
		assertEquals(0, ProblemSolutions.countInversions(s2));
		assertEquals(6, ProblemSolutions.countInversions(s3));
	}

	@Test
	public void problem2() {
		int[] s1 = { 2, 10, 12, 4, 6, 2, 10 };
		int[] s2 = { 10, 6, 4, 3 };
		int[] s3 = { 1, 5, 3, 6, -3, 5, 7, 8, 3 };
		assertEquals(10, ProblemSolutions.maxProfit(s1));
		assertEquals(0, ProblemSolutions.maxProfit(s2));
		assertEquals(11, ProblemSolutions.maxProfit(s3));
	}

	@Test
	public void problem3() {
		int[] f1 = { 4, 6, 3, 1, 6 };
		int[] m1 = { 15, 5, 3, 7, 4 };
		int[] f2 = { 6, 9, 13 };
		int[] m2 = { 3, 7, 8 };
		assertEquals(14, ProblemSolutions.matchmaking(f1, m1));
		assertEquals(10, ProblemSolutions.matchmaking(f2, m2));
	}

	@Test
	public void problem4memo() {
		int[] s1 = { 0, 1, 2, 4, 5, 10 };
		int[] s2 = { 0, 1, 3, 5, 7, 8, 10, 11 };
		int[] s3 = { 0, -1, -2 };
		assertEquals(10, ProblemSolutions.maxRodMemo(s1));
		assertEquals(12, ProblemSolutions.maxRodMemo(s2));
		assertEquals(0, ProblemSolutions.maxRodMemo(s3));
	}
	
	@Test
	public void problem4dp() {
		int[] s1 = { 0, 1, 2, 4, 5, 10 };
		int[] s2 = { 0, 1, 3, 5, 7, 8, 10, 11 };
		int[] s3 = { 0, -1, -2 };
		assertEquals(10, ProblemSolutions.maxRodDP(s1));
		assertEquals(12, ProblemSolutions.maxRodDP(s2));
		assertEquals(0, ProblemSolutions.maxRodDP(s3));
	}

	@Test
	public void problem5() {
		int[] s1 = { 0, 1, 2, 3, 5 };
		int[] s2 = { 0, 1, 3, 5, 8 };
		int[] s3 = { 0, -1, -2 };
		assertEquals(new HashSet<Integer>(Arrays.asList(-1, -2, 2, 8)),
				ProblemSolutions.unique(s1, s2, s3));
	}
}
