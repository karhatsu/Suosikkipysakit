package com.karhatsu.omatpysakit.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;

import com.karhatsu.omatpysakit.domain.Stop;
import com.karhatsu.omatpysakit.util.AccountInformation;

public class StopRequest {

	private static final String BASE_URL = "http://api.reittiopas.fi/hsl/prod/";

	public Stop getData(int stopCode) {
		try {
			String json = readStopDataAsJson(stopCode);
			return new StopJSONParser().parse(json);
		} catch (Exception e) {
			throw new RuntimeException(e); // TODO
		}
	}

	private String readStopDataAsJson(int stopCode)
			throws MalformedURLException, IOException, ProtocolException,
			JSONException {
		InputStream is = null;
		try {
			HttpURLConnection conn = createConnection(stopCode);
			conn.connect();
			is = conn.getInputStream();
			return readStream(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	private HttpURLConnection createConnection(int stopCode)
			throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(getStopRequestUrl(stopCode));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		return conn;
	}

	private String getStopRequestUrl(int stopCode) {
		return BASE_URL + "?request=stop&user="
				+ AccountInformation.getUserName() + "&pass="
				+ AccountInformation.getPassword() + "&format=json&code="
				+ stopCode;
	}

	public String readStream(InputStream stream) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[10000];
		reader.read(buffer);
		return new String(buffer);
	}

}
