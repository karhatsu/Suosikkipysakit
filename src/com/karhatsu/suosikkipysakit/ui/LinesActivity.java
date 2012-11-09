package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.app.ListActivity;

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
		getListView().setAdapter(new LineListAdapter(this, lines));
	}

}
