package com.karhatsu.suosikkipysakit.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import com.karhatsu.suosikkipysakit.datasource.parsers.JSONParser;

public abstract class AbstractHslRequest<R> extends AsyncTask<String, Void, ArrayList<R>> {

	protected static final String BASE_URL = "http://api.reittiopas.fi/hsl/prod/";

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
		try {
			ArrayList<R> list = new ArrayList<R>();
			String json = queryDataAsJson(searchParams[0].trim());
			list.addAll(getJSONParser().parse(json));
			return list;
		} catch (DataNotFoundException e) {
			return null;
		} catch (Exception e) {
			connectionFailed = true;
			return null;
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

	private String queryDataAsJson(String searchParam) throws IOException {
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		try {
			HttpResponse response = client.execute(new HttpGet(
					getRequestUrl(searchParam)));
			return readStream(response.getEntity().getContent());
		} finally {
			client.close();
		}
	}

	protected abstract String getRequestUrl(String searchParam);

	public String readStream(InputStream stream) throws IOException {
		Scanner scanner = new Scanner(stream).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}

	public boolean isRunning() {
		return running;
	}
}
