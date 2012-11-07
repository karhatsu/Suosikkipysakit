package com.karhatsu.suosikkipysakit.datasource.test;

import org.json.JSONException;

import com.karhatsu.suosikkipysakit.datasource.LinesJSONParser;
import com.karhatsu.suosikkipysakit.domain.Line;

public class LinesJSONParserTest extends AbstractJSONParserTest {

	private static final String TEST_JSON_FILE = "test-lines-response.json";

	private static String jsonString;
	private LinesJSONParser parser = new LinesJSONParser();

	@Override
	protected void setUp() throws Exception {
		if (jsonString == null) {
			jsonString = readTestJson(TEST_JSON_FILE);
		}
	}

	public void testLinesCount() throws JSONException {
		assertEquals(6, parser.parse(jsonString).size());
	}

	public void testLineCode() throws JSONException {
		assertEquals("14", getFirstLine().getCode());
	}

	public void testLineName() throws JSONException {
		assertEquals("Hernesaari - Kamppi - Pajamäki", getFirstLine().getName());
	}

	public void testLineStart() throws JSONException {
		assertEquals("Hernesaari", getFirstLine().getLineStart());
	}

	public void testLineEnd() throws JSONException {
		assertEquals("Pajamäki", getFirstLine().getLineEnd());
	}

	private Line getFirstLine() throws JSONException {
		return parser.parse(jsonString).get(0);
	}
}
