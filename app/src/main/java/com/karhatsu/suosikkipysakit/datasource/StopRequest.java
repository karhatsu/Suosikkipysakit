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
				.append("  " + getStopsSearch(searchParam) + " {")
				.append("    name")
				.append("    code")
				.append("    zoneId")
				.append("    stoptimesWithoutPatterns(numberOfDepartures: 30, omitNonPickups: true) {")
				.append("      scheduledDeparture")
				.append("      realtimeDeparture")
				.append("      realtime")
				.append("      serviceDay")
				.append("      trip {")
				.append("        pattern {")
				.append("          route {")
				.append("            mode")
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

	private String getStopsSearch(String searchParam) {
		if (searchParam.length() <= 6) {
			return "stops(name: \"" + searchParam + "\")";
		} else if (searchParam.startsWith("HSL")) {
			return "stop(id: \"" + searchParam + "\")";
		}
		return "stop(id: \"HSL:" + searchParam + "\")";
	}
}
