package com.karhatsu.omatpysakit.domain;

import java.util.Date;

public class Departure {
	private final String line;
	private final Date time;

	public Departure(String line, Date time) {
		this.line = line;
		this.time = time;
	}

	public String getLine() {
		return line;
	}

	public Date getTime() {
		return time;
	}
}
