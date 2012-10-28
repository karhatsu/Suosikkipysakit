package com.karhatsu.omatpysakit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karhatsu.omatpysakit.domain.Stop;

public class StopDao {

	public void save(Context context, Stop stop) {
		SQLiteDatabase db = getWritableDatabase(context);
		ContentValues values = new ContentValues();
		values.put(OwnStopsContract.StopEntry.COLUMN_CODE, stop.getCode());
		values.put(OwnStopsContract.StopEntry.COLUMN_NAME_FI, stop.getNameFi());
		values.put(OwnStopsContract.StopEntry.COLUMN_NAME_SV, stop.getNameSv());
		values.put(OwnStopsContract.StopEntry.COLUMN_NAME_BY_USER,
				stop.getNameByUser());
		db.insert(OwnStopsContract.StopEntry.TABLE_NAME, null, values);
		db.close();
	}

	public void delete(Context context, long id) {
		SQLiteDatabase db = getWritableDatabase(context);
		db.delete(OwnStopsContract.StopEntry.TABLE_NAME,
				OwnStopsContract.StopEntry._ID + "=?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public Cursor findAll(Context context) {
		OwnStopsDbHelper dbHelper = new OwnStopsDbHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = { OwnStopsContract.StopEntry._ID,
				OwnStopsContract.StopEntry.COLUMN_CODE,
				OwnStopsContract.StopEntry.COLUMN_NAME_BY_USER };
		String sortBy = OwnStopsContract.StopEntry.COLUMN_CODE;
		return db.query(OwnStopsContract.StopEntry.TABLE_NAME, projection,
				null, null, null, null, sortBy);
	}

	private SQLiteDatabase getWritableDatabase(Context context) {
		OwnStopsDbHelper dbHelper = new OwnStopsDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return db;
	}

}
