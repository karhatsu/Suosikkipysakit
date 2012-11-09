package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.app.ListActivity;

import com.karhatsu.suosikkipysakit.domain.Stop;

public class LineStopsActivity extends ListActivity {

	protected static final String LINE_STOPS = "com.karhatsu.suosikkipysakit.ui.LINE_STOPS";

	@Override
	protected void onStart() {
		super.onStart();
		setupLineStopsView();
	}

	private void setupLineStopsView() {
		List<Stop> lineStops = getIntent().getParcelableArrayListExtra(
				LINE_STOPS);
		getListView().setAdapter(new LineStopListAdapter(this, lineStops));
	}

}
