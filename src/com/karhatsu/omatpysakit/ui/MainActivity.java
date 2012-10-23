package com.karhatsu.omatpysakit.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.karhatsu.omatpysakit.R;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView stopListView = (ListView) findViewById(R.id.stop_list);
		stopListView.setAdapter(getStopListAdapter());
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
