package com.karhatsu.omatpysakit.datasource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.karhatsu.omatpysakit.domain.Departure;
import com.karhatsu.omatpysakit.domain.Stop;

public class StopJSONParser {

	public Stop parse(String json) throws JSONException {
		JSONObject jsonStop = parseFirstStop(json);
		Stop stop = new Stop(jsonStop.getInt("code"));
		stop.setDepartures(parseDepartures(jsonStop));
		return stop;
	}

	private JSONObject parseFirstStop(String json) throws JSONException {
		JSONArray jsonStops = new JSONArray(json);
		JSONObject jsonStop = jsonStops.getJSONObject(0);
		return jsonStop;
	}

	private List<Departure> parseDepartures(JSONObject jsonStop)
			throws JSONException {
		JSONArray jsonDepartures = jsonStop.getJSONArray("departures");
		List<Departure> departures = new ArrayList<Departure>(10);
		LineParser lineParser = new LineParser();
		TimeParser timeParser = new TimeParser();
		for (int i = 0; i < jsonDepartures.length(); i++) {
			JSONObject jsonDeparture = jsonDepartures.getJSONObject(i);
			String line = lineParser.format(jsonDeparture.getString("code"));
			String time = timeParser.format(String.valueOf(jsonDeparture
					.getInt("time")));
			Departure departure = new Departure(line, time);
			departures.add(departure);
		}
		return departures;
	}

}
