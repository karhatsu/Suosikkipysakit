package com.karhatsu.omatpysakit.ui;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.karhatsu.omatpysakit.datasource.StopRequest;
import com.karhatsu.omatpysakit.domain.Departure;
import com.karhatsu.omatpysakit.domain.Stop;

public class DeparturesActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView departuresListView = getListView();
		departuresListView.setAdapter(getDepartureListAdapter());
	}

	private DepartureListAdapter getDepartureListAdapter() {
		return new DepartureListAdapter(this, getDepartures());
	}

	private List<Departure> getDepartures() {
		int stopCode = getIntent().getIntExtra(Stop.CODE_KEY, -1);
		Stop stop = new StopRequest().getData(stopCode);
		return stop.getDepartures();
	}
}
