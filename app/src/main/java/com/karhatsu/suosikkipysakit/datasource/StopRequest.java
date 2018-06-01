package com.karhatsu.suosikkipysakit.datasource;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;
import com.karhatsu.suosikkipysakit.datasource.parsers.StopJSONParser;
import com.karhatsu.suosikkipysakit.domain.Stop;
import com.karhatsu.suosikkipysakit.util.AccountInformation;

public class StopRequest extends AbstractHslRequest<Stop> {

	public StopRequest(OnHslRequestReady<Stop> notifier) {
		super(notifier);
	}

	protected JSONParser<Stop> getJSONParser() {
		return new StopJSONParser();
	}

	protected String getRequestUrl(String stopCode) {
		return BASE_URL + "?request=stop&user="
				+ AccountInformation.getUserName() + "&pass="
				+ AccountInformation.getPassword() + "&format=json&code="
				+ stopCode;
	}

}