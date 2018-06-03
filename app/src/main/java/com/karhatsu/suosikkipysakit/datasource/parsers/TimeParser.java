package com.karhatsu.suosikkipysakit.datasource.parsers;

public class TimeParser {

	public String format(int time) {
		int hours = time / 3600;
		int minutes = (time - hours * 3600) / 60;
		return withPadding(hours % 24) + ":" + withPadding(minutes);
	}

	private String withPadding(int value) {
		return value < 10 ? "0" + value : "" + value;
	}
}
