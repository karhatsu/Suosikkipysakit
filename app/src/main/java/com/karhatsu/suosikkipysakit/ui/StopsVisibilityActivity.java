package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

import java.util.List;

public class StopsVisibilityActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stops_visibility);
		setupStopListView();
	}

	private void setupStopListView() {
		final ListView stopListView = getStopListView();
		final ListAdapter stopListAdapter = createStopListAdapter();
		stopListView.setAdapter(stopListAdapter);
	}

	private ListView getStopListView() {
		return (ListView) findViewById(R.id.stops_visibility_list);
	}

	private ListAdapter createStopListAdapter() {
		StopDao stopDao = new StopDao(this);
		List<Stop> stops = stopDao.findAllStops();
		return new StopsVisibilityListAdapter(this, stops);
	}
}
