package com.karhatsu.suosikkipysakit.datasource.test;

import org.json.JSONException;

import com.karhatsu.suosikkipysakit.datasource.LinesJSONParser;
import com.karhatsu.suosikkipysakit.domain.Line;
import com.karhatsu.suosikkipysakit.domain.Stop;

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

	public void testStopCount() throws JSONException {
		assertEquals(35, getFirstLine().getStops().size());
	}

	public void testStopCode() throws JSONException {
		assertEquals("1195", getFirstLineFirstStop().getCode());
	}

	public void testStopName() throws JSONException {
		assertEquals("Hernesaaren laituri", getFirstLineFirstStop().getName());
	}

	public void testStopCoordinates() throws JSONException {
		assertEquals("2551480,6671094", getFirstLineFirstStop()
				.getCoordinates());
	}

	private Line getFirstLine() throws JSONException {
		return parser.parse(jsonString).get(0);
	}

	private Stop getFirstLineFirstStop() throws JSONException {
		return getFirstLine().getStops().get(0);
	}
}
