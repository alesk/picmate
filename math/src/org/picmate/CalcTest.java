package org.picmate;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalcTest {

	@Test
	public void testLine() {
		Circle c = Calc.radiusFun(1,2, 2,4, 3,6);
		assertTrue(c.equals(new Circle(0,0,0)));
	}
	
	@Test
	public void testRightOrientation() {
		Circle c = Calc.radiusFun(0,1, 1,0, 2,1);
		assertTrue(c.equals(new Circle(1d,1d,1d)));
	}
	
	@Test
	public void testWrongOrientation() {
		Circle c = Calc.radiusFun(0,0, 1,1, 2,0);
		assertTrue(c.equals(new Circle(1d,1d,0d)));
	}
	

}
