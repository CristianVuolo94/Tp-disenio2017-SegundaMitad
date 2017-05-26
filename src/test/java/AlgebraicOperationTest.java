import static org.junit.Assert.assertEquals;
import org.junit.Test;

import modelo.Enterprise;
import math.*;

public class AlgebraicOperationTest {
	Constant firstOperand = new Constant(4);
	Constant secondOperand = new Constant(8);
	Enterprise dummyEnterprise = new Enterprise();
	
	@Test
	public void addition() {
		assertEquals(new Addition(firstOperand, secondOperand).reduce(dummyEnterprise, 0), 12, 0);
	}
	
	@Test
	public void subtraction() {	
		assertEquals(new Subtraction(firstOperand, secondOperand).reduce(dummyEnterprise, 0), -4, 0);
	}
	
	@Test
	public void multiplication() {		
		assertEquals(new Multiplication(firstOperand, secondOperand).reduce(dummyEnterprise, 0), 32, 0);
	}
	
	@Test
	public void division() {
		assertEquals(new Division(firstOperand, secondOperand).reduce(dummyEnterprise, 0), 0.5, 0);
	}
	
	@Test
	public void complex(){
		Addition add = new Addition(firstOperand, secondOperand);
		Multiplication mult = new Multiplication(add, new Constant(2));
		
		assertEquals(mult.reduce(dummyEnterprise, 0), 24, 0);
	}
}