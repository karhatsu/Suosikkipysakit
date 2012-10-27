package com.karhatsu.omatpysakit.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.karhatsu.omatpysakit.R;
import com.karhatsu.omatpysakit.domain.Stop;
import com.karhatsu.omatpysakit.util.AccountInformation;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		AccountInformation.initialize(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupStopListView();
	}

	private void setupStopListView() {
		final ListView stopListView = (ListView) findViewById(R.id.stop_list);
		final ListAdapter stopListAdapter = getStopListAdapter();
		stopListView.setAdapter(stopListAdapter);
		stopListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						DeparturesActivity.class);
				intent.putExtra(Stop.CODE_KEY,
						getSelectedStopCode(stopListAdapter, position));
				startActivity(intent);
			}

			private int getSelectedStopCode(final ListAdapter stopListAdapter,
					int position) {
				Cursor cursor = (Cursor) stopListAdapter.getItem(position);
				cursor.moveToPosition(position);
				return cursor.getInt(1);
			}
		});
	}

	private ListAdapter getStopListAdapter() {
		return new StopListAdapter(this);
	}

	public void addStop(View button) {
		Intent intent = new Intent(this, AddStopActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
