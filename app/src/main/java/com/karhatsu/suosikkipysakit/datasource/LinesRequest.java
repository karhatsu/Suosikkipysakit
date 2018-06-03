package com.karhatsu.suosikkipysakit.datasource;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;
import com.karhatsu.suosikkipysakit.datasource.parsers.LinesJSONParser;
import com.karhatsu.suosikkipysakit.domain.Line;

public class LinesRequest extends AbstractHslRequest<Line> {

	public LinesRequest(OnHslRequestReady<Line> notifier) {
		super(notifier);
	}

	@Override
	protected JSONParser<Line> getJSONParser() {
		return new LinesJSONParser();
	}

	@Override
	protected String getRequestBody(String searchParam) {
		String routeQuery = "name: \"" + searchParam + "\"";
		if (searchParam.equalsIgnoreCase("metro")) {
			routeQuery = "modes: \"SUBWAY\"";
		} else if (searchParam.equalsIgnoreCase("lautta")) {
			routeQuery = "modes: \"FERRY\"";
		}
		return new StringBuilder()
				.append("{")
				.append("  routes(" + routeQuery + ") {")
				.append("    gtfsId")
				.append("    shortName")
				.append("    longName")
				.append("    stops {")
				.append("      name")
				.append("      code")
				.append("    }")
				.append("  }")
				.append("}")
				.toString();
	}

}
