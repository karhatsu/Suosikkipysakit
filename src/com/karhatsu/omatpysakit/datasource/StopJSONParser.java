package com.karhatsu.omatpysakit.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.karhatsu.omatpysakit.domain.Departure;
import com.karhatsu.omatpysakit.domain.Stop;

public class StopJSONParser {
	private LineParser lineParser = new LineParser();
	private TimeParser timeParser = new TimeParser();

	public Stop parse(String json) throws JSONException {
		JSONObject jsonStop = parseFirstJSONStop(json);
		Stop stop = parseStop(jsonStop);
		stop.setDepartures(parseDepartures(jsonStop));
		return stop;
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
		return new Stop(jsonStop.getInt("code_short"),
				jsonStop.getString("name_fi"), jsonStop.getString("name_sv"));
	}

	private List<Departure> parseDepartures(JSONObject jsonStop)
			throws JSONException {
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
