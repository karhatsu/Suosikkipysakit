package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class LineStopsActivity extends AppCompatActivity {

	protected static final String LINE_STOPS = "com.karhatsu.suosikkipysakit.ui.LINE_STOPS";

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
		ToolbarUtil.setToolbarPadding(toolbar);
		Object retained = getLastNonConfigurationInstance();
		if (retained instanceof Stop) {
			stopToBeSaved = (Stop) retained;
			showSaveStopDialog();
		}
		setupLineStopsView();
	}

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
		DialogFragment dialogFragment = new NewStopDialog();
		Bundle args = new Bundle();
		args.putParcelable(NewStopDialog.STOP, stopToBeSaved);
		dialogFragment.setArguments(args);
		dialogFragment.show(getSupportFragmentManager(), "newStop");
	}
}
