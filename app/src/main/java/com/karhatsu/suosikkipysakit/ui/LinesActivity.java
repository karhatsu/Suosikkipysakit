package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.domain.Line;

public class LinesActivity extends AppCompatActivity {

	public static final String LINES_LIST = "com.karhatsu.suosikkipysakit.ui.LINES_LIST";

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.activity_lines);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		ToolbarUtil.setToolbarPadding(toolbar);
		setupLinesView();
	}

	private void setupLinesView() {
		List<Line> lines = getIntent().getParcelableArrayListExtra(LINES_LIST);
		final LineListAdapter lineListAdapter = new LineListAdapter(this, lines);
		ListView linesListView = findViewById(R.id.lines_list);
		linesListView.setAdapter(lineListAdapter);
		linesListView.setOnItemClickListener(new OnItemClickListener() {
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
