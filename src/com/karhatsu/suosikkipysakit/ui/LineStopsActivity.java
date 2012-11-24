package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.app.ListActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.karhatsu.suosikkipysakit.domain.Stop;

public class LineStopsActivity extends ListActivity {

	protected static final String LINE_STOPS = "com.karhatsu.suosikkipysakit.ui.LINE_STOPS";

	private SaveStopDialog saveStopDialog;
	private Stop stopToBeSaved;

	@Override
	protected void onStart() {
		super.onStart();
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

	@Override
	public Object onRetainNonConfigurationInstance() {
		return stopToBeSaved;
	}

	private void setupLineStopsView() {
		List<Stop> lineStops = getIntent().getParcelableArrayListExtra(
				LINE_STOPS);
		final LineStopListAdapter lineStopListAdapter = new LineStopListAdapter(
				this, lineStops);
		getListView().setAdapter(lineStopListAdapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				stopToBeSaved = lineStopListAdapter.getItem(position);
				showSaveStopDialog();
			}
		});
	}

	private void showSaveStopDialog() {
		saveStopDialog = new SaveStopDialog(LineStopsActivity.this,
				stopToBeSaved);
		saveStopDialog.show();
	}

}
