package com.karhatsu.suosikkipysakit.datasource.parsers.test;

import org.json.JSONException;

import com.karhatsu.suosikkipysakit.datasource.DataNotFoundException;
import com.karhatsu.suosikkipysakit.datasource.parsers.LinesJSONParser;
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

	public void testLineCode() throws JSONException, DataNotFoundException {
		assertEquals("94B", getFirstLine().getCode());
	}

	public void testLineName() throws JSONException, DataNotFoundException {
		assertEquals("Kivikonlaita-Kontula(M)", getFirstLine().getName());
	}

	public void testLineStart() throws JSONException, DataNotFoundException {
		assertEquals("Kontula (M)", getFirstLine().getLineStart());
	}

	public void testLineEnd() throws JSONException, DataNotFoundException {
		assertEquals("Keinulaudantie", getFirstLine().getLineEnd());
	}

	public void testStopCount() throws JSONException, DataNotFoundException {
		assertEquals(24, getFirstLine().getStops().size());
	}

	public void testStopCode() throws JSONException, DataNotFoundException {
		assertEquals("4421", getFirstLineFirstStop().getCode());
	}

	public void testStopName() throws JSONException,
			DataNotFoundException {
		assertEquals("Kontula (M)", getFirstLineFirstStop().getName());
	}

	private Line getFirstLine() throws JSONException, DataNotFoundException {
		return parser.parse(jsonString).get(0);
	}

	private Stop getFirstLineFirstStop() throws JSONException,
			DataNotFoundException {
		return getFirstLine().getStops().get(0);
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
}
