package com.karhatsu.suosikkipysakit.ui;

import java.util.*;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.datasource.OnHslRequestReady;
import com.karhatsu.suosikkipysakit.datasource.StopRequest;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Departure;
import com.karhatsu.suosikkipysakit.domain.DeparturesComparator;
import com.karhatsu.suosikkipysakit.domain.Stop;
import com.karhatsu.suosikkipysakit.domain.StopCollection;

public class DeparturesActivity extends ListActivity implements OnHslRequestReady<Stop> {

	private ProgressDialog progressDialog;
	private StopRequest stopRequest;

	private Timer timer = new Timer();
	private TimerTask timerTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Object retained = getLastNonConfigurationInstance();
		if (retained instanceof StopRequest) {
			showProgressDialog();
			stopRequest = (StopRequest) retained;
			stopRequest.setOnHslRequestReady(this);
		} else {
			stopRequest = new StopRequest(this);
			queryDepartures();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (timer == null) {
			timer = new Timer();
			timer.schedule(createTimerTask(), 100, 5000);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		hideProgressDialog();
		if (timerTask != null) {
			timerTask.cancel();
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void queryDepartures() {
		Stop stop = getIntent().getParcelableExtra(Stop.STOP_KEY);
		String stopCode = getIntent().getStringExtra(Stop.CODE_KEY);
		long collectionId = getIntent().getLongExtra(StopCollection.COLLECTION_ID_KEY, 0);
		if (stop != null) {
			showDepartures(stop.getDepartures());
		} else if (stopCode != null) {
			showProgressDialog();
			stopRequest.execute(stopCode);
		} else {
			showProgressDialog();
			List<Stop> stops = new StopDao(this).findByCollectionid(collectionId);
			String[] stopCodes = getStopCodesFrom(stops);
			stopRequest.execute(stopCodes);
		}
	}

	private String[] getStopCodesFrom(List<Stop> stops) {
		String[] stopCodes = new String[stops.size()];
		int i = 0;
		for (Stop stop : stops) {
			stopCodes[i] = stop.getCode();
			i++;
		}
		return stopCodes;
	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new PleaseWaitDialog(this,
					R.string.activity_departures_dialog_title);
		}
		progressDialog.show();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		stopRequest.setOnHslRequestReady(null);
		return stopRequest;
	}

	@Override
	public void notifyAboutResult(ArrayList<Stop> stops) {
		hideProgressDialog();
		List<Departure> departures = getDeparturesFrom(stops);
		showDepartures(departures);
	}

	private List<Departure> getDeparturesFrom(ArrayList<Stop> stops) {
		List<Departure> departures = new LinkedList<Departure>();
		for (Stop stop : stops) {
			departures.addAll(stop.getDepartures());
		}
		Collections.sort(departures, new DeparturesComparator());
		return departures;
	}

	private void showDepartures(List<Departure> departures) {
		if (departures.isEmpty()) {
			ToastHelper.showToast(this,
					R.string.activity_departures_nothing_found);
			return;
		}
		ListView departuresListView = getListView();
		DepartureListAdapter adapter = new DepartureListAdapter(this, departures);
		departuresListView.setAdapter(adapter);
		timer.schedule(createTimerTask(), 5000, 5000);
	}

	@Override
	public void notifyConnectionProblem() {
		hideProgressDialog();
		ToastHelper.showToast(this, R.string.connection_problem);
	}

	@Override
	public Context getContext() {
		return this;
	}

	private void hideProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	private TimerTask createTimerTask() {
		timerTask = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((DepartureListAdapter) getListView().getAdapter())
								.notifyDataSetChanged();
					}
				});
			}
		};
		return timerTask;
	}
}
