package com.karhatsu.suosikkipysakit.datasource.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.karhatsu.suosikkipysakit.datasource.DataNotFoundException;
import com.karhatsu.suosikkipysakit.domain.Departure;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopJSONParser implements JSONParser<List<Stop>> {
	private LineParser lineParser = new LineParser();
	private TimeParser timeParser = new TimeParser();

	public List<Stop> parse(String json) throws DataNotFoundException,
			JSONException {
		if (json.equals("")) {
			throw new DataNotFoundException();
		}
		JSONObject jsonStop = parseFirstJSONStop(json);
		Stop stop = parseStop(jsonStop);
		stop.setDepartures(parseDepartures(jsonStop));
		return Arrays.asList(stop);
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

	private JSONObject parseFirstJSONStop(String json) throws JSONException {
		JSONArray jsonStops = new JSONArray(json);
		return jsonStops.getJSONObject(0);
	}

	private Stop parseStop(JSONObject jsonStop) throws JSONException {
		String code = jsonStop.getString("code");
		String name = jsonStop.getString("name_fi");
		String coordinates = jsonStop.getString("coords");
		return new Stop(code, name, coordinates);
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
