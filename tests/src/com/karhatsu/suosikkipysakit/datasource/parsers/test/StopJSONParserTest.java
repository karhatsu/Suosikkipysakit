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
	private static final String TEST_JSON_FILE_MULTIPLE_STOPS = "test-stop-response-multiple-stops.json";
	private static String jsonString;
	private StopJSONParser parser = new StopJSONParser();

	@Override
	protected void setUp() throws Exception {
		if (jsonString == null) {
			jsonString = readTestJson(TEST_JSON_FILE);
		}
	}

	public void testStopCode() throws JSONException, DataNotFoundException {
		assertEquals("1230101", getParsedStop().getCode());
	}

	public void testStopNameIsFinnishNameWithFinnishAddress()
			throws JSONException, DataNotFoundException {
		assertEquals("Kaironkatu (Hämeentie)", getParsedStop().getName());
	}

	public void testStopCoordinates() throws JSONException,
			DataNotFoundException {
		assertEquals("2554317,6678028", getParsedStop().getCoordinates());
	}

	public void testDeparturesCount() throws JSONException,
			DataNotFoundException {
		assertEquals(10, getParsedDepartures().size());
	}

	public void testDepartureLine() throws JSONException, DataNotFoundException {
		assertEquals("68", getParsedDepartures().get(0).getLine());
	}

	public void testDepartureTime() throws JSONException, DataNotFoundException {
		assertEquals("09:07", getParsedDepartures().get(0).getTime());
	}

	public void testDepartureEndStop() throws JSONException,
			DataNotFoundException {
		assertEquals("Rautatientori", getParsedDepartures().get(0).getEndStop());
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

	public void testMultipleStops() throws IOException, DataNotFoundException,
			JSONException {
		String json = readTestJson(TEST_JSON_FILE_MULTIPLE_STOPS);
		List<Stop> stops = parser.parse(json);
		assertEquals(4, stops.size());
	}

	private Stop getParsedStop() throws JSONException, DataNotFoundException {
		return parser.parse(jsonString).get(0);
	}

	private List<Departure> getParsedDepartures() throws JSONException,
			DataNotFoundException {
		return getParsedStop().getDepartures();
	}

}
