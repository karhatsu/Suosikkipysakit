package com.karhatsu.suosikkipysakit.datasource.parsers.test;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.karhatsu.suosikkipysakit.datasource.DataNotFoundException;
import com.karhatsu.suosikkipysakit.datasource.parsers.StopJSONParser;
import com.karhatsu.suosikkipysakit.domain.Departure;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopJSONParserTest extends AbstractJSONParserTest {

	private static final String TEST_JSON_FILE = "test-stop-response.json";
	private static final String TEST_JSON_FILE_NO_DEPARTURES = "test-stop-response-no-departures.json";
	private static String jsonString;
	private StopJSONParser parser = new StopJSONParser();

	@Override
	protected void setUp() throws Exception {
		if (jsonString == null) {
			jsonString = readTestJson(TEST_JSON_FILE);
		}
	}

	public void testStopCode() throws JSONException, DataNotFoundException {
		assertEquals("4004", getParsedStop().getCode());
	}

	public void testStopName()
			throws JSONException, DataNotFoundException {
		assertEquals("Kipparlahti", getParsedStop().getName());
	}

	public void testDeparturesCount() throws JSONException,
			DataNotFoundException {
		assertEquals(5, getParsedDepartures().size());
	}

	public void testDepartureLine() throws JSONException, DataNotFoundException {
		assertEquals("58", getParsedDepartures().get(0).getLine());
	}

	public void testDepartureTime() throws JSONException, DataNotFoundException {
		assertEquals("14:03", getParsedDepartures().get(0).getTime());
	}

	public void testDepartureEndStop() throws JSONException,
			DataNotFoundException {
		assertEquals("Munkkivuori", getParsedDepartures().get(0).getEndStop());
	}

	public void testEmptyJSON() {
		try {
			parser.parse("");
			fail("Did not throw StopRequestException");
		} catch (DataNotFoundException e) {
		} catch (JSONException e) {
			fail("Threw JSONException");
		}
	}

	public void testNoDepartures() throws IOException, DataNotFoundException,
			JSONException {
		String json = readTestJson(TEST_JSON_FILE_NO_DEPARTURES);
		List<Stop> stops = parser.parse(json);
		Stop stop = stops.get(0);
		assertEquals(0, stop.getDepartures().size());
	}

	private Stop getParsedStop() throws JSONException, DataNotFoundException {
		return parser.parse(jsonString).get(0);
	}

	private List<Departure> getParsedDepartures() throws JSONException,
			DataNotFoundException {
		return getParsedStop().getDepartures();
	}

}
