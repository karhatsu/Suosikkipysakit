package com.karhatsu.omatpysakit.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.karhatsu.omatpysakit.R;
import com.karhatsu.omatpysakit.db.StopDao;
import com.karhatsu.omatpysakit.domain.Stop;
import com.karhatsu.omatpysakit.util.AccountInformation;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		AccountInformation.initialize(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setupStopListView();
	}

	private void setupStopListView() {
		final ListView stopListView = getStopListView();
		final ListAdapter stopListAdapter = createStopListAdapter();
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
		});
		registerForContextMenu(stopListView);
	}

	private ListAdapter createStopListAdapter() {
		Cursor cursor = new StopDao().findAll(this);
		return new StopListAdapter(this, cursor);
	}

	private String getSelectedStopCode(final ListAdapter stopListAdapter,
			int position) {
		Cursor cursor = (Cursor) stopListAdapter.getItem(position);
		cursor.moveToPosition(position);
		return cursor.getString(1);
	}

	public void addStop(View button) {
		Intent intent = new Intent(this, AddStopActivity.class);
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_stop_item, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.menu_stop_item_delete:
			new StopDao().delete(this, info.id);
			refreshStopList();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void refreshStopList() {
		ListView stopListView = getStopListView();
		stopListView.setAdapter(createStopListAdapter());
	}

	private ListView getStopListView() {
		return (ListView) findViewById(R.id.stop_list);
	}
}
