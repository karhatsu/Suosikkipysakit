package com.karhatsu.suosikkipysakit.datasource;

import java.util.List;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;
import com.karhatsu.suosikkipysakit.datasource.parsers.LinesJSONParser;
import com.karhatsu.suosikkipysakit.domain.Line;
import com.karhatsu.suosikkipysakit.util.AccountInformation;

public class LinesRequest extends AbstractHslRequest<List<Line>> {

	public LinesRequest(OnHslRequestReady<List<Line>> notifier) {
		super(notifier);
	}

	@Override
	protected JSONParser<List<Line>> getJSONParser() {
		return new LinesJSONParser();
	}

	@Override
	protected String getRequestUrl(String line) {
		return BASE_URL + "?request=lines&user="
				+ AccountInformation.getUserName() + "&pass="
				+ AccountInformation.getPassword() + "&format=json&query="
				+ line;
	}

}
