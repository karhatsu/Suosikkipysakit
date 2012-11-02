package com.karhatsu.omatpysakit.datasource.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import junit.framework.TestCase;

import org.json.JSONException;

import com.karhatsu.omatpysakit.datasource.StopJSONParser;
import com.karhatsu.omatpysakit.datasource.StopRequestException;
import com.karhatsu.omatpysakit.domain.Departure;
import com.karhatsu.omatpysakit.domain.Stop;

public class StopJSONParserTest extends TestCase {

	private static final String TEST_FILES_DIRECTORY = "src/com/karhatsu/omatpysakit/datasource/test/";
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

	public void testStopCode() throws JSONException, StopRequestException {
		assertEquals("3044", getParsedStop().getCode());
	}

	public void testStopNameFi() throws JSONException, StopRequestException {
		assertEquals("Kaironkatu", getParsedStop().getNameFi());
	}

	public void testStopNameSv() throws JSONException, StopRequestException {
		assertEquals("Kairogatan", getParsedStop().getNameSv());
	}

	public void testDeparturesCount() throws JSONException,
			StopRequestException {
		assertEquals(10, getParsedDepartures().size());
	}

	public void testDepartureLine() throws JSONException, StopRequestException {
		assertEquals("68", getParsedDepartures().get(0).getLine());
	}

	public void testDepartureTime() throws JSONException, StopRequestException {
		assertEquals("09:07", getParsedDepartures().get(0).getTime());
	}

	public void testDepartureEndStop() throws JSONException,
			StopRequestException {
		assertEquals("Rautatientori", getParsedDepartures().get(0).getEndStop());
	}

	public void testEmptyJSON() {
		try {
			parser.parse("");
			fail("Did not throw StopRequestException");
		} catch (StopRequestException e) {
		} catch (JSONException e) {
			fail("Threw JSONException");
		}
	}

	public void testNoDepartures() throws IOException, StopRequestException,
			JSONException {
		String json = readTestJson(TEST_JSON_FILE_NO_DEPARTURES);
		Stop stop = parser.parse(json);
		assertEquals(0, stop.getDepartures().size());
	}

	private Stop getParsedStop() throws JSONException, StopRequestException {
		return parser.parse(jsonString);
	}

	private List<Departure> getParsedDepartures() throws JSONException,
			StopRequestException {
		return getParsedStop().getDepartures();
	}

	private String readTestJson(String fileName) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(TEST_FILES_DIRECTORY + fileName));
			Scanner scanner = new Scanner(fis).useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

}
