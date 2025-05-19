package com.karhatsu.suosikkipysakit.datasource;

import android.annotation.SuppressLint;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;
import com.karhatsu.suosikkipysakit.datasource.parsers.LinesJSONParser;
import com.karhatsu.suosikkipysakit.domain.Line;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class LinesRequest extends AbstractHslRequest<Line> {

	private static final Pattern TRAM_PATTERN = Pattern.compile("^[1-9]$");
	private static final Pattern TRAIN_PATTERN = Pattern.compile("^[a-zA-Z]$");

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
				.append("    mode")
				.append("    shortName")
				.append("    longName")
				.append("    patterns {")
				.append("      tripsForDate(serviceDate: \"" + getToday() + "\") {")
				.append("        id")
				.append("      }")
				.append("      stops {")
				.append("        name")
				.append("        code")
				.append("        gtfsId")
				.append("        zoneId")
				.append("      }")
				.append("    }")
				.append("  }")
				.append("}")
				.toString();
	}

	private String getRoutesCondition(String searchParam) {
		if (searchParam.equalsIgnoreCase("metro")) {
			return "transportModes: SUBWAY";
		} else if (searchParam.equalsIgnoreCase("lautta")) {
			return "transportModes: FERRY";
		} else if (TRAM_PATTERN.matcher(searchParam).matches() || searchParam.equals("10")) {
			return "name: \"" + searchParam + "\" transportModes: TRAM";
		} else if (TRAIN_PATTERN.matcher(searchParam).matches())  {
			return "name: \"" + searchParam + "\" transportModes: RAIL";
		}
		return "name: \"" + searchParam + "\"";
	}

	@SuppressLint("SimpleDateFormat")
	private String getToday() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
}
