package com.karhatsu.suosikkipysakit.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.OwnStopsContract;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopListAdapter extends SimpleCursorAdapter {

	private static final String[] FROM_COLUMNS = new String[] { StopDao.PROJECTION_NAME };
	private static final int[] TO_COLUMNS = new int[] { R.id.stop_list_item_name };
	private final Context context;

	public StopListAdapter(Context context, Cursor cursor) {
		super(context, R.layout.list_item_stop, cursor, FROM_COLUMNS, TO_COLUMNS, 0);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView stopNameView = view.findViewById(R.id.stop_list_item_name);
		Cursor cursor = getCursor();
		if (cursor.moveToPosition(position)) {
			String stopCode = cursor.getString(cursor.getColumnIndexOrThrow(OwnStopsContract.StopEntry.COLUMN_CODE));
			// no stop code for collections
			int color = stopCode != null && Stop.isLegacyCode(stopCode)
					? android.R.color.darker_gray
					: android.R.color.black;
			stopNameView.setTextColor(ContextCompat.getColor(context, color));
		}
		return view;
	}
}
