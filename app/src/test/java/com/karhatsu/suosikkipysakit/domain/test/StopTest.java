package com.karhatsu.suosikkipysakit.domain.test;

import junit.framework.TestCase;

import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopTest extends TestCase {

	public void testWithoutPrefix() {
		assertFalse(Stop.isValidCode("1234"));
	}

	public void testHWithNonNumber() {
		assertFalse(Stop.isValidCode("H123a"));
	}

	public void testHWithThreeDigits() {
		assertFalse(Stop.isValidCode("H123"));
	}

	public void testHWithFiveDigits() {
		assertFalse(Stop.isValidCode("H12345"));
	}

	public void testHAndFourDigits() {
		assertTrue(Stop.isValidCode("H1234"));
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

	public void testKerava() {
		assertTrue(Stop.isValidCode("Ke0401"));
	}

	public void testJarvenpaa() {
		assertTrue(Stop.isValidCode("Jä0201"));
	}

	public void testSipoo() {
		assertTrue(Stop.isValidCode("Si2239"));
	}

	public void testTuusula() {
		assertTrue(Stop.isValidCode("Tu0104"));
	}

	public void testSomeOtherLetterPrefix() {
		assertFalse(Stop.isValidCode("F1234"));
		assertFalse(Stop.isValidCode("A1234"));
		assertFalse(Stop.isValidCode("Ti1234"));
		assertFalse(Stop.isValidCode("Ko1234"));
		assertFalse(Stop.isValidCode("Ja1234"));
	}

}
