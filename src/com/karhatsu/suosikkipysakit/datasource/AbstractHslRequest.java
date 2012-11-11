package com.karhatsu.suosikkipysakit.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;

public abstract class AbstractHslRequest<R> extends AsyncTask<String, Void, R> {

	protected static final String BASE_URL = "http://api.reittiopas.fi/hsl/prod/";

	private OnHslRequestReady<R> notifier;
	private boolean connectionFailed;
	private boolean ready;
	private boolean running;
	private R result;

	protected AbstractHslRequest(OnHslRequestReady<R> notifier) {
		this.notifier = notifier;
	}

	@Override
	protected R doInBackground(String... stopCode) {
		running = true;
		if (!connectionAvailable()) {
			connectionFailed = true;
			return null;
		}
		try {
			return getData(stopCode[0]);
		} catch (DataNotFoundException e) {
			return null;
		}
	}

	private R getData(String stopCode) throws DataNotFoundException {
		try {
			String json = readStopDataAsJson(stopCode);
			return getJSONParser().parse(json);
		} catch (DataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean connectionAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) notifier
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	protected abstract JSONParser<R> getJSONParser();

	@Override
	protected void onPostExecute(R result) {
		this.result = result;
		ready = true;
		notifyAboutResult();
		running = false;
	}

	public void setOnHslRequestReady(OnHslRequestReady<R> notifier) {
		this.notifier = notifier;
		if (ready) {
			notifyAboutResult();
		}
	}

	private void notifyAboutResult() {
		if (notifier != null) {
			if (connectionFailed) {
				notifier.notifyConnectionProblem();
			} else {
				notifier.notifyAboutResult(result);
			}
		}
	}

	private String readStopDataAsJson(String stopCode)
			throws ClientProtocolException, IOException {
		HttpClient client = AndroidHttpClient.newInstance("Android");
		HttpResponse response = client.execute(new HttpGet(
				getRequestUrl(stopCode)));
		InputStream is = null;
		try {
			return readStream(response.getEntity().getContent());
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	protected abstract String getRequestUrl(String searchParam);

	public String readStream(InputStream stream) throws IOException,
			UnsupportedEncodingException {
		Scanner scanner = new Scanner(stream).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}

	public boolean isRunning() {
		return running;
	}
}
