package com.karhatsu.omatpysakit.datasource;

public class LineParser {

	public String format(String lineCode) {
		if (lineCode.startsWith("2")) {
			return formatRegionalLine(lineCode);
		}
		return formatInternalLine(lineCode);
	}

	private String formatRegionalLine(String lineCode) {
		return lineCode.substring(1, 5).trim();
	}

	private String formatInternalLine(String lineCode) {
		return lineCode.substring(2, 5).trim();
	}

}
