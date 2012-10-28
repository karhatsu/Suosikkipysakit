package com.karhatsu.omatpysakit.datasource.test;

import junit.framework.TestCase;

import com.karhatsu.omatpysakit.datasource.LineParser;

public class LineParserTest extends TestCase {

	private LineParser lineParser = new LineParser();

	public void testTwoNumbers() {
		assertEquals("78", lineParser.format("1078  2"));
		assertEquals("20", lineParser.format("1020  1"));
	}

	public void testTwoNumbersWithLetter() {
		assertEquals("94A", lineParser.format("1094A 1"));
		assertEquals("94B", lineParser.format("1094B 1"));
	}

	public void testJoukoLine() {
		assertEquals("94J", lineParser.format("1294J 1"));
	}

	public void testThreeNumbers() {
		assertEquals("506", lineParser.format("2506  1"));
	}

}
