package com.karhatsu.omatpysakit.domain;

public class Departure {
	private final String line;
	private final String time;
	private final String endStop;

	public Departure(String line, String time, String endStop) {
		this.line = line;
		this.time = time;
		this.endStop = endStop;
	}

	public String getLine() {
		return line;
	}

	public String getTime() {
		return time;
	}

	public String getEndStop() {
		return endStop;
	}
}
