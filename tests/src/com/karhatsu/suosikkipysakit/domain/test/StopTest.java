package com.karhatsu.suosikkipysakit.domain.test;

import junit.framework.TestCase;

import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopTest extends TestCase {

	public void testFourDigits() {
		assertTrue(Stop.isValidCode("1234"));
	}

	public void testNonNumber() {
		assertFalse(Stop.isValidCode("123a"));
	}

	public void testThreeDigits() {
		assertFalse(Stop.isValidCode("123"));
	}

	public void testFiveDigits() {
		assertFalse(Stop.isValidCode("12345"));
	}

	public void testVAndFourDigits() {
		assertTrue(Stop.isValidCode("V1234"));
	}

	public void testVAndFiveDigits() {
		assertFalse(Stop.isValidCode("V12345"));
	}

	public void testVAndThreeDigits() {
		assertFalse(Stop.isValidCode("V123"));
	}

	public void testEAndFourDigits() {
		assertTrue(Stop.isValidCode("E1234"));
	}

	public void testEAndFiveDigits() {
		assertFalse(Stop.isValidCode("E12345"));
	}

	public void testEAndThreeDigits() {
		assertFalse(Stop.isValidCode("E123"));
	}

	public void testSomeOtherLetterPrefix() {
		assertFalse(Stop.isValidCode("F1234"));
		assertFalse(Stop.isValidCode("H1234"));
		assertFalse(Stop.isValidCode("A1234"));
	}

}
