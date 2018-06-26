package com.karhatsu.suosikkipysakit.datasource.parsers.test;

import junit.framework.TestCase;

import com.karhatsu.suosikkipysakit.datasource.parsers.TimeParser;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeParserTest extends TestCase {

	private TimeParser timeParser = new TimeParser();
	private static final int TIME_13_54_00 = 50040;

	public void testFormatTwoDigits() {
		assertEquals("13:54", timeParser.format(50040));
	}

	public void testFormatPadding() {
		assertEquals("07:23", timeParser.format(26580));
		assertEquals("09:04", timeParser.format(32640));
		assertEquals("19:01", timeParser.format(68460));
	}

	public void testFormatNextDay() {
		assertEquals("00:26", timeParser.format(87960));
		assertEquals("01:11", timeParser.format(90660));
	}

	public void testMinutes() {
		Calendar now = createNow(13, 53, 0); // 1:00 -> 1
		assertEquals(1, timeParser.parseMinutes(TIME_13_54_00, now));
	}

	public void testRoundedMinutesDown() {
		Calendar now = createNow(13, 53, 31); // 0:29 -> 0
		assertEquals(0, timeParser.parseMinutes(TIME_13_54_00, now));
	}

	public void testRoundedMinutesUp() {
		Calendar now = createNow(13, 51, 30); // 2:30 -> 3
		assertEquals(3, timeParser.parseMinutes(TIME_13_54_00, now));
	}

	private Calendar createNow(int hours, int minutes, int seconds) {
		Calendar now = new GregorianCalendar();
		now.set(Calendar.HOUR_OF_DAY, hours);
		now.set(Calendar.MINUTE, minutes);
		now.set(Calendar.SECOND, seconds);
		return now;
	}
}
