package com.karhatsu.suosikkipysakit.datasource.parsers;

import java.util.Calendar;
import java.util.TimeZone;

public class TimeParser {

	public String format(int time) {
		int hours = time / 3600;
		int minutes = (time - hours * 3600) / 60;
		return withPadding(hours % 24) + ":" + withPadding(minutes);
	}

	private String withPadding(int value) {
		return value < 10 ? "0" + value : "" + value;
	}

	public int parseMinutes(int time) {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Helsinki"));
		return parseMinutes(time, now);
	}

	public int parseMinutes(int time, Calendar now) {
		Calendar departure = (Calendar) now.clone();
		int hours = time / 3600;
		int minutes = (time - hours * 3600) / 60;
		int seconds = time - hours * 3600 - minutes * 60;
		departure.set(Calendar.HOUR_OF_DAY, hours);
		departure.set(Calendar.MINUTE, minutes);
		departure.set(Calendar.SECOND, seconds);
		if (now.get(Calendar.HOUR_OF_DAY) > departure.get(Calendar.HOUR_OF_DAY)) {
			departure.add(Calendar.DAY_OF_MONTH, 1);
		}
		return (int) Math.round((departure.getTimeInMillis() - now.getTimeInMillis()) / 1000.0 / 60);
	}
}
