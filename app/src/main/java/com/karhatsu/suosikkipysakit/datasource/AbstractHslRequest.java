package com.karhatsu.suosikkipysakit.datasource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;

import javax.net.ssl.HttpsURLConnection;

public abstract class AbstractHslRequest<R> extends AsyncTask<String, Void, ArrayList<R>> {

	protected static final String API_URL = "https://api.digitransit.fi/routing/v1/routers/hsl/index/graphql";

	private OnHslRequestReady<R> notifier;
	private boolean connectionFailed;
	private boolean ready;
	private boolean running;
	private ArrayList<R> result;

	protected AbstractHslRequest(OnHslRequestReady<R> notifier) {
		this.notifier = notifier;
	}

	@Override
	protected ArrayList<R> doInBackground(String... searchParams) {
		running = true;
		if (!connectionAvailable()) {
			connectionFailed = true;
			return null;
		}
		return getData(searchParams);
	}

	private ArrayList<R> getData(String... searchParams) {
		ArrayList<R> list = new ArrayList<R>();
		for (String searchParam : searchParams) {
			try {
				String json = queryDataAsJson(searchParam.trim());
				list.addAll(getJSONParser().parse(json));
			} catch (DataNotFoundException e) {
			} catch (Exception e) {
				connectionFailed = true;
				return null;
			}
		}
		return list;
	}

	private boolean connectionAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) notifier
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	protected abstract JSONParser<R> getJSONParser();

	@Override
	protected void onPostExecute(ArrayList<R> result) {
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

	private String queryDataAsJson(String searchParam) {
		try {
			HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(API_URL).openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);
			urlConnection.setRequestProperty("Content-Type", "application/graphql");
			OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.write(getRequestBody(searchParam));
			writer.flush();
			writer.close();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			return readStream(in);
		} catch (Exception e) {
			Log.e("API", e.toString(), e);
			throw new RuntimeException(e);
		}
	}

	protected abstract String getRequestBody(String searchParam);

	public String readStream(InputStream stream) {
		Scanner scanner = new Scanner(stream).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}

	public boolean isRunning() {
		return running;
	}
}
