package com.karhatsu.suosikkipysakit.datasource.parsers;

public class TimeParser {

	public String format(String time) {
		if (time.length() == 3) {
			return "0" + time.substring(0, 1) + ":" + time.substring(1, 3);
		} else if (time.length() == 2) {
			return "00:" + time;
		} else if (time.length() == 1) {
			return "00:0" + time;
		}
		String hour = time.substring(0, 2);
		return stripOvernight(hour) + ":" + time.substring(2, 4);
	}

	private String stripOvernight(String hour) {
		int h = Integer.parseInt(hour);
		if (h >= 24) {
			h -= 24;
		}
		if (h < 10) {
			return "0" + h;
		}
		return String.valueOf(h);
	}

}
