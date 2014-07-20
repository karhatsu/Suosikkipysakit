package com.karhatsu.suosikkipysakit.datasource.parsers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
		JSONArray jsonLines = new JSONArray(json);
		Set<String> lineCodes = new HashSet<String>();
		for (int i = 0; i < jsonLines.length(); i++) {
			JSONObject jsonLine = jsonLines.getJSONObject(i);
			Line line = parseLine(jsonLine);
			if (!lineCodes.contains(line.getLongCode())) {
				lines.add(line);
				lineCodes.add(line.getLongCode());
			}
		}
		return lines;
	}

	private Line parseLine(JSONObject jsonLine) throws JSONException {
		String longCode = jsonLine.getString("code");
		String lineCode = jsonLine.getString("code_short");
		String name = jsonLine.getString("name");
		String lineStart = jsonLine.getString("line_start");
		String lineEnd = jsonLine.getString("line_end");
		Line line = new Line(longCode, lineCode, name, lineStart, lineEnd);
		line.setStops(parseStops(jsonLine));
		return line;
	}

	private ArrayList<Stop> parseStops(JSONObject jsonLine)
			throws JSONException {
		ArrayList<Stop> stops = new ArrayList<Stop>();
		JSONArray jsonStops = jsonLine.getJSONArray("line_stops");
		for (int i = 0; i < jsonStops.length(); i++) {
			JSONObject jsonStop = jsonStops.getJSONObject(i);
			String code = jsonStop.getString("code");
			String name = parseStopName(jsonStop);
			String coordinates = jsonStop.getString("coords");
			stops.add(new Stop(code, name, coordinates));
		}
		return stops;
	}

	private String parseStopName(JSONObject jsonStop) throws JSONException {
		String name = jsonStop.getString("name");
		if (!jsonStop.isNull("address")) {
			name += " (" + jsonStop.getString("address") + ")";
		}
		return name;
	}

}
