package com.karhatsu.omatpysakit.domain.test;

import junit.framework.TestCase;

import com.karhatsu.omatpysakit.domain.Stop;

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

}
