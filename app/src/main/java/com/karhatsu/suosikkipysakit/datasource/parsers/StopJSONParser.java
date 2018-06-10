package com.karhatsu.suosikkipysakit.datasource.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.karhatsu.suosikkipysakit.domain.Departure;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopJSONParser implements JSONParser<Stop> {
	private TimeParser timeParser = new TimeParser();

	public ArrayList<Stop> parse(String json) throws JSONException {
		ArrayList<Stop> stops = new ArrayList<Stop>();
		JSONArray jsonStops = new JSONObject(json).getJSONObject("data").getJSONArray("stops");
		for (int i = 0; i < jsonStops.length(); i++) {
			JSONObject jsonStop = jsonStops.getJSONObject(i);
			Stop stop = parseStop(jsonStop);
			stop.setDepartures(parseDepartures(jsonStop));
			stops.add(stop);
		}
		return stops;
	}

	private Stop parseStop(JSONObject jsonStop) throws JSONException {
		String code = jsonStop.getString("code");
		String name = jsonStop.getString("name");
		return new Stop(code, name);
	}

	private List<Departure> parseDepartures(JSONObject jsonStop)
			throws JSONException {
		JSONArray jsonDepartures = jsonStop.getJSONArray("stoptimesWithoutPatterns");
		List<Departure> departures = new ArrayList<Departure>(10);
		for (int i = 0; i < jsonDepartures.length(); i++) {
			JSONObject jsonDeparture = jsonDepartures.getJSONObject(i);
			departures.add(parseDeparture(jsonDeparture));
		}
		return departures;
	}

	private Departure parseDeparture(JSONObject jsonDeparture) throws JSONException {
		JSONObject pattern = jsonDeparture.getJSONObject("trip").getJSONObject("pattern");
		String line = pattern.getJSONObject("route").getString("shortName");
		String time = timeParser.format(jsonDeparture.getInt("realtimeDeparture"));
		String endStop = pattern.getString("headsign");
		return new Departure(line, time, endStop);
	}

}
