package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class LineStopsActivity extends AppCompatActivity implements OnStopEditCancel {

	protected static final String LINE_STOPS = "com.karhatsu.suosikkipysakit.ui.LINE_STOPS";

	private SaveStopDialog saveStopDialog;
	private Stop stopToBeSaved;

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.activity_line_stops);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		Object retained = getLastNonConfigurationInstance();
		if (retained instanceof Stop) {
			stopToBeSaved = (Stop) retained;
			showSaveStopDialog();
		}
		setupLineStopsView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (saveStopDialog != null) {
			saveStopDialog.dismiss();
		}
	}

	/*@Override
	public Object onRetainNonConfigurationInstance() {
		return stopToBeSaved;
	}*/

	private void setupLineStopsView() {
		List<Stop> lineStops = getIntent().getParcelableArrayListExtra(
				LINE_STOPS);
		final LineStopListAdapter lineStopListAdapter = new LineStopListAdapter(
				this, lineStops);
		ListView linesListView = findViewById(R.id.line_stops_list);
		linesListView.setAdapter(lineStopListAdapter);
		linesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				stopToBeSaved = lineStopListAdapter.getItem(position);
				showSaveStopDialog();
			}
		});
	}

	private void showSaveStopDialog() {
		saveStopDialog = new NewStopDialog(this, this, stopToBeSaved);
		saveStopDialog.show();
	}

	@Override
	public void stopEditCancelled() {
		stopToBeSaved = null;
	}

}
