package com.karhatsu.omatpysakit.domain;


public class Departure {
	private final String line;
	private final String time;

	public Departure(String line, String time) {
		this.line = line;
		this.time = time;
	}

	public String getLine() {
		return line;
	}

	public String getTime() {
		return time;
	}
}
