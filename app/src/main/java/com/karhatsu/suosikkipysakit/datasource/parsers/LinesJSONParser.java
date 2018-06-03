package com.karhatsu.suosikkipysakit.datasource.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.karhatsu.suosikkipysakit.datasource.DataNotFoundException;
import com.karhatsu.suosikkipysakit.domain.Line;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class LinesJSONParser implements JSONParser<Line> {

	@Override
	public ArrayList<Line> parse(String json) throws DataNotFoundException,
			JSONException {
		if (json.equals("")) {
			throw new DataNotFoundException();
		}
		ArrayList<Line> lines = new ArrayList<Line>();
		JSONArray jsonLines = new JSONObject(json).getJSONObject("data").getJSONArray("routes");
		for (int i = 0; i < jsonLines.length(); i++) {
			JSONObject jsonLine = jsonLines.getJSONObject(i);
			Line line = parseLine(jsonLine);
			lines.add(line);
		}
		return lines;
	}

	private Line parseLine(JSONObject jsonLine) throws JSONException {
		String longCode = jsonLine.getString("gtfsId");
		String lineCode = jsonLine.getString("shortName");
		String name = jsonLine.getString("longName");
		JSONArray stops = jsonLine.getJSONArray("stops");
		String lineStart = ((JSONObject) stops.get(0)).getString("name");
		String lineEnd = ((JSONObject) stops.get(stops.length() - 1)).getString("name");
		Line line = new Line(longCode, lineCode, name, lineStart, lineEnd);
		line.setStops(parseStops(jsonLine));
		return line;
	}

	private ArrayList<Stop> parseStops(JSONObject jsonLine)
			throws JSONException {
		ArrayList<Stop> stops = new ArrayList<Stop>();
		JSONArray jsonStops = jsonLine.getJSONArray("stops");
		for (int i = 0; i < jsonStops.length(); i++) {
			JSONObject jsonStop = jsonStops.getJSONObject(i);
			String code = jsonStop.getString("code");
			String name = jsonStop.getString("name");
			stops.add(new Stop(code, name));
		}
		return stops;
	}

}
