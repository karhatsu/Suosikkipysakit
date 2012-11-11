package com.karhatsu.suosikkipysakit.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.OwnStopsContract;

public class StopListAdapter extends SimpleCursorAdapter {

	private static final String[] FROM_COLUMNS = new String[] { OwnStopsContract.StopEntry.COLUMN_NAME_BY_USER };
	private static final int[] TO_COLUMNS = new int[] { R.id.stop_list_item_name };

	public StopListAdapter(Context context, Cursor cursor) {
		super(context, R.layout.list_item_stop, cursor, FROM_COLUMNS,
				TO_COLUMNS, 0);
	}

}
