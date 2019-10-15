package com.karhatsu.suosikkipysakit.datasource.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.karhatsu.suosikkipysakit.domain.Line;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class LinesJSONParser implements JSONParser<Line> {

	@Override
	public ArrayList<Line> parse(String json) throws JSONException {
		ArrayList<Line> lines = new ArrayList<Line>();
		JSONArray jsonLines = new JSONObject(json).getJSONObject("data").getJSONArray("routes");
		for (int i = 0; i < jsonLines.length(); i++) {
			JSONObject jsonLine = jsonLines.getJSONObject(i);
			parseLines(lines, jsonLine);
		}
		return lines;
	}

	private void parseLines(ArrayList<Line> lines, JSONObject jsonLine) throws JSONException {
		String longCode = jsonLine.getString("gtfsId");
		String mode = jsonLine.getString("mode");
		String shortName = jsonLine.getString("shortName");
		String lineCode = StopNameParser.parse(mode, shortName);
		String name = jsonLine.getString("longName");
		JSONArray patterns = jsonLine.getJSONArray("patterns");
		for (int i = 0; i < patterns.length(); i++) {
			JSONObject pattern = patterns.getJSONObject(i);
			JSONArray stops = pattern.getJSONArray("stops");
			String lineStart = ((JSONObject) stops.get(0)).getString("name");
			String lineEnd = ((JSONObject) stops.get(stops.length() - 1)).getString("name");
			Line line = new Line(longCode, lineCode, name, lineStart, lineEnd);
			line.setStops(parseStops(pattern));
			lines.add(line);
		}
	}

	private ArrayList<Stop> parseStops(JSONObject pattern) throws JSONException {
		ArrayList<Stop> stops = new ArrayList<Stop>();
		JSONArray jsonStops = pattern.getJSONArray("stops");
		for (int i = 0; i < jsonStops.length(); i++) {
			JSONObject jsonStop = jsonStops.getJSONObject(i);
			String code = jsonStop.getString("gtfsId");
			String name = jsonStop.getString("name");
			stops.add(new Stop(code, name));
		}
		return stops;
	}

}
