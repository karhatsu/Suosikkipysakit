package com.karhatsu.suosikkipysakit.datasource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.karhatsu.suosikkipysakit.domain.Line;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class LinesJSONParser {

	public List<Line> parse(String json) throws JSONException {
		List<Line> lines = new ArrayList<Line>();
		JSONArray jsonLines = new JSONArray(json);
		for (int i = 0; i < jsonLines.length(); i++) {
			JSONObject jsonLine = jsonLines.getJSONObject(i);
			lines.add(parseLine(jsonLine));
		}
		return lines;
	}

	private Line parseLine(JSONObject jsonLine) throws JSONException {
		String lineCode = jsonLine.getString("code_short");
		String name = jsonLine.getString("name");
		String lineStart = jsonLine.getString("line_start");
		String lineEnd = jsonLine.getString("line_end");
		Line line = new Line(lineCode, name, lineStart, lineEnd);
		line.setStops(parseStops(jsonLine));
		return line;
	}

	private List<Stop> parseStops(JSONObject jsonLine) throws JSONException {
		List<Stop> stops = new ArrayList<Stop>();
		JSONArray jsonStops = jsonLine.getJSONArray("line_stops");
		for (int i = 0; i < jsonStops.length(); i++) {
			JSONObject jsonStop = jsonStops.getJSONObject(i);
			String code = jsonStop.getString("codeShort");
			String name = jsonStop.getString("name");
			String coordinates = jsonStop.getString("coords");
			stops.add(new Stop(code, name, coordinates));
		}
		return stops;
	}

}
