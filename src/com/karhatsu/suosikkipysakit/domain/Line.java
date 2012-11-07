package com.karhatsu.suosikkipysakit.domain;

public class Line {

	private final String code;
	private final String name;
	private final String lineStart;
	private final String lineEnd;

	public Line(String code, String name, String lineStart, String lineEnd) {
		this.code = code;
		this.name = name;
		this.lineStart = lineStart;
		this.lineEnd = lineEnd;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getLineStart() {
		return lineStart;
	}

	public String getLineEnd() {
		return lineEnd;
	}

}
