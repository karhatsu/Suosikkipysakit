package com.karhatsu.suosikkipysakit.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopCollectionDao;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;
import com.karhatsu.suosikkipysakit.domain.StopCollection;

public class MainActivity extends AppCompatActivity {

	private StopDao stopDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		setupStopListView();
		setupNoStopsText();
		resumeToPreviousState();
	}

	protected void resumeToPreviousState() {
		redirectToDeparturesIfRequested();
	}

	private void redirectToDeparturesIfRequested() {
		Stop stop = getIntent().getParcelableExtra(Stop.STOP_KEY);
		String stopCode = getIntent().getStringExtra(Stop.CODE_KEY);
		if (stop != null) {
			showDepartures(stop, null);
		} else if (stopCode != null) {
			showDepartures(null, stopCode);
		}
	}

	private void setupStopListView() {
		final ListView stopListView = getStopListView();
		final ListAdapter stopListAdapter = createStopListAdapter();
		stopListView.setAdapter(stopListAdapter);
		stopListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selectedStopCode = getSelectedStopCode(stopListAdapter,
						position);
				if (selectedStopCode != null) {
					showDepartures(null, selectedStopCode);
				} else {
					long selectedCollectionId = getSelectedCollectionId(stopListAdapter, position);
					if (new StopCollectionDao(MainActivity.this).containsStops(selectedCollectionId)) {
						showCollectionDepartures(selectedCollectionId);
					} else {
						ToastHelper.showToast(MainActivity.this, R.string.activity_main_no_stops_in_collection);
					}
				}
			}
		});
		registerForContextMenu(stopListView);
	}

	private void showDepartures(Stop stop, String stopCode) {
		Intent intent = new Intent(MainActivity.this, DeparturesActivity.class);
		if (stop != null) {
			intent.putExtra(Stop.STOP_KEY, stop);
		} else {
			intent.putExtra(Stop.CODE_KEY, stopCode);
		}
		startActivity(intent);
	}

	private void showCollectionDepartures(long id) {
		Intent intent = new Intent(MainActivity.this, DeparturesActivity.class);
		intent.putExtra(StopCollection.COLLECTION_ID_KEY, id);
		startActivity(intent);
	}

	private void setupNoStopsText() {
		final ListView stopListView = getStopListView();
		TextView noStopsText = findViewById(R.id.no_stops_text);
		int stopsCount = stopListView.getAdapter().getCount();
		noStopsText.setVisibility(stopsCount > 0 ? View.INVISIBLE : View.VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshStopList();
		setupNoStopsText();
	}

	private ListAdapter createStopListAdapter() {
		stopDao = new StopDao(this);
		Cursor cursor = stopDao.findAllStopsAndCollections();
		return new StopListAdapter(this, cursor);
	}

	private String getSelectedStopCode(final ListAdapter stopListAdapter,
			int position) {
		Cursor cursor = (Cursor) stopListAdapter.getItem(position);
		cursor.moveToPosition(position);
		return cursor.getString(1);
	}

	private long getSelectedCollectionId(final ListAdapter stopListAdapter,
									   int position) {
		Cursor cursor = (Cursor) stopListAdapter.getItem(position);
		cursor.moveToPosition(position);
		return cursor.getLong(0);
	}

	public void addStop(View button) {
		Intent intent = new Intent(this, AddStopActivity.class);
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Cursor cursor = (Cursor) getStopListAdapter().getItem(info.position);
		String columnCode = cursor.getString(1);
		MenuInflater menuInflater = getMenuInflater();
		if (columnCode != null) {
			menuInflater.inflate(R.menu.menu_stop_item, menu);
		} else {
			menuInflater.inflate(R.menu.menu_collection_item, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.menu_stop_item_rename:
			showStopRenameDialog(info.id);
			refreshStopList();
			return true;
		case R.id.menu_stop_item_add_to_collection:
			showAddToCollectionDialog(info.id);
			refreshStopList();
			return true;
		case R.id.menu_stop_item_hide:
			new StopDao(this).hideStop(info.id);
			refreshStopList();
			return true;
		case R.id.menu_stop_item_delete:
			new StopDao(this).delete(info.id);
			refreshStopList();
			setupNoStopsText();
			showSnackbar(R.string.activity_main_stop_deleted);
			return true;
		case R.id.menu_collection_item_rename:
			showCollectionRenameDialog(info.id);
			refreshStopList();
			return true;
		case R.id.menu_collection_item_delete:
			new StopCollectionDao(this).delete(info.id);
			refreshStopList();
			setupNoStopsText();
			showSnackbar(R.string.activity_main_collection_deleted);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void showSnackbar(int text) {
		Snackbar.make(getStopListView(), text, Snackbar.LENGTH_SHORT).show();
	}

	private void showStopRenameDialog(long stopId) {
		DialogFragment dialogFragment = new RenameStopDialog();
		Bundle args = new Bundle();
		args.putLong(RenameStopDialog.STOP_ID, stopId);
		dialogFragment.setArguments(args);
		dialogFragment.show(getSupportFragmentManager(), "stopRename");
	}

	private void showCollectionRenameDialog(long collectionId) {
		DialogFragment dialogFragment = new RenameCollectionDialog();
		Bundle args = new Bundle();
		args.putLong(RenameCollectionDialog.COLLECTION_ID, collectionId);
		dialogFragment.setArguments(args);
		dialogFragment.show(getSupportFragmentManager(), "collectionRename");
	}

	private void showAddToCollectionDialog(long stopId) {
		DialogFragment dialogFragment = new AddToCollectionDialog();
		Bundle args = new Bundle();
		args.putLong(AddToCollectionDialog.STOP_ID, stopId);
		dialogFragment.setArguments(args);
		dialogFragment.show(getSupportFragmentManager(), "addToCollection");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeDbConnections();
	}

	public void refreshStopList() {
		closeDbConnections();
		stopDao = new StopDao(this);
		Cursor cursor = stopDao.findAllStopsAndCollections();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_main_stops_visibility:
				Intent intent = new Intent(this, StopsVisibilityActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
