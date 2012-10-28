package com.karhatsu.omatpysakit.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.karhatsu.omatpysakit.datasource.OnStopRequestReady;
import com.karhatsu.omatpysakit.datasource.StopRequest;
import com.karhatsu.omatpysakit.domain.Stop;

public class DeparturesActivity extends ListActivity implements
		OnStopRequestReady {

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queryDepartures();
	}

	private void queryDepartures() {
		int stopCode = getIntent().getIntExtra(Stop.CODE_KEY, -1);
		showProgressDialog();
		new StopRequest(this).execute(stopCode);
	}

	private void showProgressDialog() {
		progressDialog = new PleaseWaitDialog(this);
		progressDialog.show();
	}

	@Override
	public void notifyStopRequested(Stop stop) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		ListView departuresListView = getListView();
		DepartureListAdapter adapter = new DepartureListAdapter(this,
				stop.getDepartures());
		departuresListView.setAdapter(adapter);
	}
}
