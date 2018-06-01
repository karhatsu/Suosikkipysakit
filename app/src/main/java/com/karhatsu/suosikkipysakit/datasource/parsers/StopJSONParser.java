package com.karhatsu.suosikkipysakit.datasource.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.karhatsu.suosikkipysakit.datasource.DataNotFoundException;
import com.karhatsu.suosikkipysakit.domain.Departure;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopJSONParser implements JSONParser<Stop> {
	private LineParser lineParser = new LineParser();
	private TimeParser timeParser = new TimeParser();

	public ArrayList<Stop> parse(String json) throws DataNotFoundException,
			JSONException {
		if (json.equals("")) {
			throw new DataNotFoundException();
		}
		ArrayList<Stop> stops = new ArrayList<Stop>();
		JSONArray jsonStops = new JSONArray(json);
		for (int i = 0; i < jsonStops.length(); i++) {
			JSONObject jsonStop = jsonStops.getJSONObject(i);
			Stop stop = parseStop(jsonStop);
			stop.setDepartures(parseDepartures(jsonStop));
			stops.add(stop);
		}
		return stops;
	}

	private Map<String, String> getEndStops(JSONObject jsonStop)
			throws JSONException {
		Map<String, String> endStops = new HashMap<String, String>();
		JSONArray jsonLines = jsonStop.getJSONArray("lines");
		for (int i = 0; i < jsonLines.length(); i++) {
			String[] lineAndTarget = jsonLines.getString(i).split(":");
			endStops.put(lineAndTarget[0], lineAndTarget[1]);
		}
		return endStops;
	}

	private Stop parseStop(JSONObject jsonStop) throws JSONException {
		String code = jsonStop.getString("code");
		String name = parseStopName(jsonStop);
		String coordinates = jsonStop.getString("coords");
		return new Stop(code, name, coordinates);
	}

	private String parseStopName(JSONObject jsonStop) throws JSONException {
		String name = jsonStop.getString("name_fi");
		String address = jsonStop.getString("address_fi");
		if (address.length() > 0) {
			name += " (" + jsonStop.getString("address_fi") + ")";
		}
		return name;
	}

	private List<Departure> parseDepartures(JSONObject jsonStop)
			throws JSONException {
		if (jsonStop.isNull("departures")) {
			return new ArrayList<Departure>();
		}
		Map<String, String> endStops = getEndStops(jsonStop);
		JSONArray jsonDepartures = jsonStop.getJSONArray("departures");
		List<Departure> departures = new ArrayList<Departure>(10);
		for (int i = 0; i < jsonDepartures.length(); i++) {
			JSONObject jsonDeparture = jsonDepartures.getJSONObject(i);
			departures.add(parseDeparture(endStops, jsonDeparture));
		}
		return departures;
	}

	private Departure parseDeparture(Map<String, String> endStops,
			JSONObject jsonDeparture) throws JSONException {
		String lineCode = jsonDeparture.getString("code");
		String line = lineParser.format(lineCode);
		String time = timeParser.format(String.valueOf(jsonDeparture
				.getInt("time")));
		String endStop = endStops.get(lineCode);
		Departure departure = new Departure(line, time, endStop);
		return departure;
	}

}