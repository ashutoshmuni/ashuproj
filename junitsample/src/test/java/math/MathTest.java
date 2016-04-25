package math;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class MathTest{
	private int value1;
	private int value2;
	public MathTest() {
	}
	@Before
	public void setUp() throws Exception {
		value1 = 3;
		value2 = 5;
	}
	@After
	public void tearDown() throws Exception {
		value1 = 0;
		value2 = 0;
	}
	@Test
	public void testAdd() {
		int total = 8;
		int sum = Calculation.add(value1, value2);
		assertEquals(sum, total);
	}
	@Test
	public void testFailedAdd() {
		int total = 9;
		int sum = Calculation.add(value1, value2);
		assertNotSame(sum, total);
	}
	@Test
	public void testSub() {
		int total = 0;
		int sub = Calculation.sub(4, 4);
		assertEquals(sub, total);
	}
}