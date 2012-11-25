package com.karhatsu.suosikkipysakit.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.datasource.OnHslRequestReady;
import com.karhatsu.suosikkipysakit.datasource.StopRequest;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class DeparturesActivity extends ListActivity implements
		OnHslRequestReady<Stop> {

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
	protected void onPause() {
		super.onPause();
		hideProgressDialog();
		timer.cancel();
	}

	private void queryDepartures() {
		String stopCode = getIntent().getStringExtra(Stop.CODE_KEY);
		showProgressDialog();
		stopRequest.execute(stopCode);
	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new PleaseWaitDialog(this);
		}
		progressDialog.show();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		stopRequest.setOnHslRequestReady(null);
		return stopRequest;
	}

	@Override
	public void notifyAboutResult(Stop stop) {
		hideProgressDialog();
		if (stop.getDepartures().isEmpty()) {
			ToastHelper.showToast(this,
					R.string.activity_departures_nothing_found);
			return;
		}
		ListView departuresListView = getListView();
		DepartureListAdapter adapter = new DepartureListAdapter(this,
				stop.getDepartures());
		departuresListView.setAdapter(adapter);
		startTimer();
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

	private void startTimer() {
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
		timer.schedule(timerTask, 10000, 5000);
	}
}
