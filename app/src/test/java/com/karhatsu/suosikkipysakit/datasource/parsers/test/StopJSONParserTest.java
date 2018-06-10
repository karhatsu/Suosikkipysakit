package com.karhatsu.suosikkipysakit.datasource.parsers.test;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.karhatsu.suosikkipysakit.datasource.parsers.StopJSONParser;
import com.karhatsu.suosikkipysakit.domain.Departure;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopJSONParserTest extends AbstractJSONParserTest {

	private static final String TEST_JSON_FILE = "test-stop-response.json";
	private static final String TEST_JSON_FILE_NO_DEPARTURES = "test-stop-response-no-departures.json";
	private static final String TEST_JSON_FILE_SINGLE_STOP = "test-stop-response-single-stop.json";
	private static String jsonString;
	private StopJSONParser parser = new StopJSONParser();

	@Override
	protected void setUp() throws Exception {
		if (jsonString == null) {
			jsonString = readTestJson(TEST_JSON_FILE);
		}
	}

	public void testStopCode() throws JSONException {
		assertEquals("4004", getParsedStop().getCode());
	}

	public void testStopName()
			throws JSONException {
		assertEquals("Kipparlahti", getParsedStop().getName());
	}

	public void testDeparturesCount() throws JSONException {
		assertEquals(5, getParsedDepartures().size());
	}

	public void testDepartureLine() throws JSONException {
		assertEquals("58", getParsedDepartures().get(0).getLine());
	}

	public void testDepartureTime() throws JSONException {
		assertEquals("14:03", getParsedDepartures().get(0).getTime());
	}

	public void testDepartureEndStop() throws JSONException {
		assertEquals("Munkkivuori", getParsedDepartures().get(0).getEndStop());
	}

	public void testNoDepartures() throws IOException,
			JSONException {
		String json = readTestJson(TEST_JSON_FILE_NO_DEPARTURES);
		List<Stop> stops = parser.parse(json);
		Stop stop = stops.get(0);
		assertEquals(0, stop.getDepartures().size());
	}

	public void testSingleStop() throws IOException, JSONException {
		String json = readTestJson(TEST_JSON_FILE_SINGLE_STOP);
		List<Stop> stops = parser.parse(json);
		Stop stop = stops.get(0);
		assertEquals("Latokartanonsilmukka", stop.getName());
	}

	private Stop getParsedStop() throws JSONException {
		return parser.parse(jsonString).get(0);
	}

	private List<Departure> getParsedDepartures() throws JSONException {
		return getParsedStop().getDepartures();
	}

}
