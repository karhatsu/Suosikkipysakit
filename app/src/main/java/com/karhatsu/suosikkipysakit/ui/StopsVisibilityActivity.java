package com.karhatsu.suosikkipysakit.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

import java.util.List;

public class StopsVisibilityActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stops_visibility);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
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
