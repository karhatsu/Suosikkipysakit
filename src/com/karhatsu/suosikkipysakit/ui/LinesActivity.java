package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.karhatsu.suosikkipysakit.domain.Line;

public class LinesActivity extends ListActivity {

	public static final String LINES_LIST = "com.karhatsu.suosikkipysakit.ui.LINES_LIST";

	@Override
	protected void onStart() {
		super.onStart();
		setupLinesView();
	}

	private void setupLinesView() {
		List<Line> lines = getIntent().getParcelableArrayListExtra(LINES_LIST);
		final LineListAdapter lineListAdapter = new LineListAdapter(this, lines);
		getListView().setAdapter(lineListAdapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Line line = lineListAdapter.getItem(position);
				Intent intent = new Intent(LinesActivity.this,
						LineStopsActivity.class);
				intent.putParcelableArrayListExtra(
						LineStopsActivity.LINE_STOPS, line.getStops());
				startActivity(intent);
			}
		});
	}

}
