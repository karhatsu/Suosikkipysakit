package com.karhatsu.suosikkipysakit.ui;

import android.content.Context;
import android.database.Cursor;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;

public class StopListAdapter extends SimpleCursorAdapter {

	private static final String[] FROM_COLUMNS = new String[] { StopDao.PROJECTION_NAME };
	private static final int[] TO_COLUMNS = new int[] { R.id.stop_list_item_name };

	public StopListAdapter(Context context, Cursor cursor) {
		super(context, R.layout.list_item_stop, cursor, FROM_COLUMNS,
				TO_COLUMNS, 0);
	}

}
