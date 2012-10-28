package com.karhatsu.omatpysakit.datasource.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.json.JSONException;

import com.karhatsu.omatpysakit.datasource.StopJSONParser;
import com.karhatsu.omatpysakit.domain.Departure;
import com.karhatsu.omatpysakit.domain.Stop;

public class StopJSONParserTest extends TestCase {

	private static final String TEST_JSON_FILE = "src/com/karhatsu/omatpysakit/datasource/test/test-stop-response.json";
	private static String jsonString;
	private StopJSONParser parser = new StopJSONParser();

	@Override
	protected void setUp() throws Exception {
		if (jsonString == null) {
			jsonString = readTestJson();
		}
	}

	public void testStopCode() throws JSONException {
		assertEquals(1230101, getParsedStop().getCode());
	}

	public void testStopNameFi() throws JSONException {
		assertEquals("Kaironkatu", getParsedStop().getNameFi());
	}

	public void testStopNameSv() throws JSONException {
		assertEquals("Kairogatan", getParsedStop().getNameSv());
	}

	public void testDeparturesCount() throws JSONException {
		assertEquals(10, getParsedDepartures().size());
	}

	public void testDepartureLine() throws JSONException {
		assertEquals("68", getParsedDepartures().get(0).getLine());
	}

	public void testDepartureTime() throws JSONException {
		assertEquals("09:07", getParsedDepartures().get(0).getTime());
	}

	private Stop getParsedStop() throws JSONException {
		return parser.parse(jsonString);
	}

	private List<Departure> getParsedDepartures() throws JSONException {
		return getParsedStop().getDepartures();
	}

	private String readTestJson() throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(TEST_JSON_FILE));
			byte[] buffer = new byte[10000];
			fis.read(buffer);
			return new String(buffer);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

}
