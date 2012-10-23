package com.karhatsu.omatpysakit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karhatsu.omatpysakit.domain.Stop;

public class StopDao {

	public void save(Context context, Stop stop) {
		OwnStopsDbHelper dbHelper = new OwnStopsDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(OwnStopsContract.StopEntry.COLUMN_NAME_CODE, stop.getCode());
		db.insert(OwnStopsContract.StopEntry.TABLE_NAME, null, values);
	}

	public Cursor findAll(Context context) {
		OwnStopsDbHelper dbHelper = new OwnStopsDbHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = { OwnStopsContract.StopEntry._ID,
				OwnStopsContract.StopEntry.COLUMN_NAME_CODE };
		String sortBy = OwnStopsContract.StopEntry.COLUMN_NAME_CODE;
		return db.query(OwnStopsContract.StopEntry.TABLE_NAME, projection,
				null, null, null, null, sortBy);
	}

}
