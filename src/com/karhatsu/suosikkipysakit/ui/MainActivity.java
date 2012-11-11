package com.karhatsu.suosikkipysakit.ui;

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

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;
import com.karhatsu.suosikkipysakit.util.AccountInformation;

public class MainActivity extends Activity {

	private StopDao stopDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		AccountInformation.initialize(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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

	@Override
	protected void onResume() {
		super.onResume();
		refreshStopList();
	}

	private ListAdapter createStopListAdapter() {
		stopDao = new StopDao(this);
		Cursor cursor = stopDao.findAll();
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
			new StopDao(this).delete(info.id);
			refreshStopList();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeDbConnections();
	}

	private void refreshStopList() {
		closeDbConnections();
		stopDao = new StopDao(this);
		Cursor cursor = stopDao.findAll();
		getStopListAdapter().changeCursor(cursor);
	}

	private void closeDbConnections() {
		StopListAdapter adapter = getStopListAdapter();
		if (adapter != null && adapter.getCursor() != null) {
			adapter.getCursor().close();
		}
		if (stopDao != null) {
			stopDao.close();
		}
	}

	private ListView getStopListView() {
		return (ListView) findViewById(R.id.stop_list);
	}

	private StopListAdapter getStopListAdapter() {
		ListView stopListView = getStopListView();
		return (StopListAdapter) stopListView.getAdapter();
	}
}
