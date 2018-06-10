package com.karhatsu.suosikkipysakit.datasource;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;
import com.karhatsu.suosikkipysakit.datasource.parsers.LinesJSONParser;
import com.karhatsu.suosikkipysakit.domain.Line;

import java.util.regex.Pattern;

public class LinesRequest extends AbstractHslRequest<Line> {

	private static final Pattern TRAM_PATTERN = Pattern.compile("^[1-9]$");

	public LinesRequest(OnHslRequestReady<Line> notifier) {
		super(notifier);
	}

	@Override
	protected JSONParser<Line> getJSONParser() {
		return new LinesJSONParser();
	}

	@Override
	protected String getRequestBody(String searchParam) {
		return new StringBuilder()
				.append("{")
				.append("  routes(" + getRoutesCondition(searchParam) + ") {")
				.append("    gtfsId")
				.append("    shortName")
				.append("    longName")
				.append("    patterns {")
				.append("      stops {")
				.append("        name")
				.append("        code")
				.append("      }")
				.append("    }")
				.append("  }")
				.append("}")
				.toString();
	}

	private String getRoutesCondition(String searchParam) {
		if (searchParam.equalsIgnoreCase("metro")) {
			return "modes: \"SUBWAY\"";
		} else if (searchParam.equalsIgnoreCase("lautta")) {
			return "modes: \"FERRY\"";
		} else if (TRAM_PATTERN.matcher(searchParam).matches() || searchParam.equals("10")) {
			return "name: \"" + searchParam + "\" modes: \"TRAM\"";
		}
		return "name: \"" + searchParam + "\"";
	}

}
