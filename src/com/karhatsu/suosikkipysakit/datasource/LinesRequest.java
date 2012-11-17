package com.karhatsu.suosikkipysakit.datasource;

import java.util.ArrayList;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;
import com.karhatsu.suosikkipysakit.datasource.parsers.LinesJSONParser;
import com.karhatsu.suosikkipysakit.domain.Line;
import com.karhatsu.suosikkipysakit.util.AccountInformation;

public class LinesRequest extends AbstractHslRequest<ArrayList<Line>> {

	public LinesRequest(OnHslRequestReady<ArrayList<Line>> notifier) {
		super(notifier);
	}

	@Override
	protected JSONParser<ArrayList<Line>> getJSONParser() {
		return new LinesJSONParser();
	}

	@Override
	protected String getRequestUrl(String line) {
		String url = BASE_URL + "?request=lines&user="
				+ AccountInformation.getUserName() + "&pass="
				+ AccountInformation.getPassword() + "&format=json";
		if (line.equalsIgnoreCase("metro")) {
			url += "&query=ruoholahti&transport_type=6";
		} else if (line.equalsIgnoreCase("lautta")) {
			url += "&query=suomenlinna&transport_type=7";
		} else {
			url += "&query=" + line;
		}
		return url;
	}

}
