package com.karhatsu.suosikkipysakit.datasource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.karhatsu.suosikkipysakit.domain.Line;

public class LinesJSONParser {

	public List<Line> parse(String json) throws JSONException {
		List<Line> lines = new ArrayList<Line>();
		JSONArray jsonLines = new JSONArray(json);
		for (int i = 0; i < jsonLines.length(); i++) {
			JSONObject jsonLine = jsonLines.getJSONObject(i);
			Line line = parseLine(jsonLine);
			lines.add(line);
		}
		return lines;
	}

	private Line parseLine(JSONObject jsonLine) throws JSONException {
		String lineCode = jsonLine.getString("code_short");
		String name = jsonLine.getString("name");
		String lineStart = jsonLine.getString("line_start");
		String lineEnd = jsonLine.getString("line_end");
		return new Line(lineCode, name, lineStart, lineEnd);
	}

}
