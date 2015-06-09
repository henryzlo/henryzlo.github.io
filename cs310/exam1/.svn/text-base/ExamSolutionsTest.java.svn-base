import static org.junit.Assert.*;

import org.junit.Test;

public class ExamSolutionsTest {

	@Test
	public void test1() {
		int[] s = { 120, 80, 120, 60 };
		int[] p = { 6, 4, 8, 3 };
		int max = 200;
		assertEquals(12, ExamSolutions.solveSong(s, p, max));
	}

	@Test
	public void test2() {
		assertEquals(9, ExamSolutions.aThenB("aaabbb"));
		assertEquals(0, ExamSolutions.aThenB("sfgnadfha"));
		assertEquals(10, ExamSolutions.aThenB("accabaccabb"));
	}

	@Test
	public void test3() {
		String[] names1 = { "Agatha Christie", "Christie Ann" };
		String[] results1 = { "Agatha Christie" };
		String[] names2 = { "Anna Caroline", "Caroline Anna", "Susan Sunday",
				"Daphne Green", "Joanna Brown", "Shirley Judy", "Judy Jetson" };
		String[] results2 = { "Anna Caroline", "Caroline Anna", "Shirley Judy" };
		assertArrayEquals(results1, ExamSolutions.potentialDates(names1));
		assertArrayEquals(results2, ExamSolutions.potentialDates(names2));
	}
	
	@Test
	public void test4() {
		int[] cities = { 6000,2400,3600 };
		assertEquals(16800, ExamSolutions.salesForce(cities, 5));
	}
}
