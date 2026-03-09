package com.capg.java;

import junit.framework.TestCase;

public class CalculationTest extends TestCase{
	Calculation calculator;
	//Runs before each test method
	protected  void setUp()
	{
		calculator =new Calculation();
		
	}
	//test method (name  start with test
	public void testAdd()
	{
		int res=calculator.addition(2, 3);
		assertEquals(5,res);
	}
	//run after each test case
	protected void tearDown()
	{
		calculator =null;
	}
	
}
