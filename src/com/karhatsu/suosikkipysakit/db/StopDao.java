package com.karhatsu.suosikkipysakit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopDao {

	private OwnStopsDbHelper dbHelper;
	private Context context;

	public StopDao(Context context) {
		this.context = context;
		dbHelper = new OwnStopsDbHelper(context);
	}

	public void save(Stop stop) {
		SQLiteDatabase db = getWritableDatabase(context);
		ContentValues values = new ContentValues();
		values.put(OwnStopsContract.StopEntry.COLUMN_CODE, stop.getCode());
		values.put(OwnStopsContract.StopEntry.COLUMN_NAME, stop.getName());
		values.put(OwnStopsContract.StopEntry.COLUMN_NAME_BY_USER,
				stop.getNameByUser());
		values.put(OwnStopsContract.StopEntry.COLUMN_COORDINATES,
				stop.getCoordinates());
		db.insert(OwnStopsContract.StopEntry.TABLE_NAME, null, values);
		db.close();
	}

	public void delete(long id) {
		SQLiteDatabase db = getWritableDatabase(context);
		db.delete(OwnStopsContract.StopEntry.TABLE_NAME,
				OwnStopsContract.StopEntry._ID + "=?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public Cursor findAll() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = { OwnStopsContract.StopEntry._ID,
				OwnStopsContract.StopEntry.COLUMN_CODE,
				OwnStopsContract.StopEntry.COLUMN_NAME_BY_USER };
		String sortBy = OwnStopsContract.StopEntry.COLUMN_CODE;
		return db.query(OwnStopsContract.StopEntry.TABLE_NAME, projection,
				null, null, null, null, sortBy);
	}

	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	private SQLiteDatabase getWritableDatabase(Context context) {
		return dbHelper.getWritableDatabase();
	}

}
