package com.karhatsu.suosikkipysakit.datasource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.karhatsu.suosikkipysakit.BuildConfig;
import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;

import javax.net.ssl.HttpsURLConnection;

public abstract class AbstractHslRequest<R> extends AsyncTask<String, Void, ArrayList<R>> {

	private static final String API_URL = "https://api.digitransit.fi/routing/v1/routers/hsl/index/graphql";

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
		Context context = notifier.getContext();
		if (context == null) {
			return null;
		} else if (!connectionAvailable(context)) {
			connectionFailed = true;
			return null;
		}
		return getData(searchParams);
	}

	private ArrayList<R> getData(String... searchParams) {
		ArrayList<R> list = new ArrayList<>();
		for (String searchParam : searchParams) {
			try {
				String json = queryDataAsJson(searchParam.trim());
				list.addAll(getJSONParser().parse(json));
			} catch (Exception e) {
				connectionFailed = true;
				return null;
			}
		}
		return list;
	}

	private boolean connectionAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
			} else if (result != null) {
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
			urlConnection.setRequestProperty("digitransit-subscription-key", BuildConfig.API_KEY);
			OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
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

	private String readStream(InputStream stream) {
		Scanner scanner = new Scanner(stream).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}

	public boolean isRunning() {
		return running;
	}
}
