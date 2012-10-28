package com.karhatsu.omatpysakit.ui;

import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter;

import com.karhatsu.omatpysakit.R;
import com.karhatsu.omatpysakit.db.OwnStopsContract;
import com.karhatsu.omatpysakit.db.StopDao;

public class StopListAdapter extends SimpleCursorAdapter {

	private static final String[] FROM_COLUMNS = new String[] {
			OwnStopsContract.StopEntry.COLUMN_CODE,
			OwnStopsContract.StopEntry.COLUMN_NAME_FI };
	private static final int[] TO_COLUMNS = new int[] {
			R.id.stop_list_item_code, R.id.stop_list_item_name };

	public StopListAdapter(Context context) {
		super(context, R.layout.list_item_stop, new StopDao().findAll(context),
				FROM_COLUMNS, TO_COLUMNS, 0);
	}

}
