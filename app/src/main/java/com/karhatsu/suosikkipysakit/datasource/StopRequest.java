package com.karhatsu.suosikkipysakit.datasource;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;
import com.karhatsu.suosikkipysakit.datasource.parsers.StopJSONParser;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopRequest extends AbstractHslRequest<Stop> {

	public StopRequest(OnHslRequestReady<Stop> notifier) {
		super(notifier);
	}

	protected JSONParser<Stop> getJSONParser() {
		return new StopJSONParser();
	}

	@Override
	protected String getRequestBody(String searchParam) {
		return new StringBuilder()
				.append("{")
				.append("  stops(name: \"" + searchParam + "\") {")
				.append("    name")
				.append("    code")
				.append("    stoptimesWithoutPatterns(numberOfDepartures: 15) {")
				.append("      scheduledDeparture")
				.append("      realtimeDeparture")
				.append("      realtime")
				.append("      trip {")
				.append("        pattern {")
				.append("          route {")
				.append("            shortName")
				.append("          }")
				.append("          headsign")
				.append("        }")
				.append("      }")
				.append("    }")
				.append("  }")
				.append("}")
				.toString();
	}
}
