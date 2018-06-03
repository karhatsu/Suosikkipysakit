package com.karhatsu.suosikkipysakit.datasource.parsers.test;

import junit.framework.TestCase;

import com.karhatsu.suosikkipysakit.datasource.parsers.TimeParser;

public class TimeParserTest extends TestCase {

	private TimeParser timeParser = new TimeParser();

	public void testTwoDigits() {
		assertEquals("13:54", timeParser.format(50040));
	}

	public void testPadding() {
		assertEquals("07:23", timeParser.format(26580));
		assertEquals("09:04", timeParser.format(32640));
		assertEquals("19:01", timeParser.format(68460));
	}

	public void testNextDay() {
		assertEquals("00:26", timeParser.format(87960));
		assertEquals("01:11", timeParser.format(90660));
	}

}
