package com.karhatsu.suosikkipysakit.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.datasource.OnStopRequestReady;
import com.karhatsu.suosikkipysakit.datasource.StopRequest;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class DeparturesActivity extends ListActivity implements
		OnStopRequestReady {

	private ProgressDialog progressDialog;
	private StopRequest stopRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Object retained = getLastNonConfigurationInstance();
		if (retained instanceof StopRequest) {
			stopRequest = (StopRequest) retained;
			stopRequest.setOnStopRequestReady(this);
		} else {
			stopRequest = new StopRequest(this);
			queryDepartures();
		}
	}

	private void queryDepartures() {
		String stopCode = getIntent().getStringExtra(Stop.CODE_KEY);
		showProgressDialog();
		stopRequest.execute(stopCode);
	}

	private void showProgressDialog() {
		progressDialog = new PleaseWaitDialog(this);
		progressDialog.show();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		stopRequest.setOnStopRequestReady(null);
		return stopRequest;
	}

	@Override
	public void notifyStopRequested(Stop stop) {
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
}
