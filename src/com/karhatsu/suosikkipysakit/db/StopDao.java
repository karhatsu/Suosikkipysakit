package com.karhatsu.suosikkipysakit.db;

import static android.provider.BaseColumns._ID;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.COLUMN_CODE;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.COLUMN_COORDINATES;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.COLUMN_NAME;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.COLUMN_NAME_BY_USER;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.TABLE_NAME;
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
		values.put(COLUMN_CODE, stop.getCode());
		values.put(COLUMN_NAME, stop.getName());
		values.put(COLUMN_NAME_BY_USER, stop.getNameByUser());
		values.put(COLUMN_COORDINATES, stop.getCoordinates());
		db.insert(OwnStopsContract.StopEntry.TABLE_NAME, null, values);
		db.close();
	}

	public void delete(long id) {
		SQLiteDatabase db = getWritableDatabase(context);
		db.delete(TABLE_NAME, _ID + "=?", new String[] { String.valueOf(id) });
		db.close();
	}

	public Cursor findAll() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = { _ID, COLUMN_CODE, COLUMN_NAME_BY_USER };
		String sortBy = COLUMN_NAME_BY_USER;
		return db.query(TABLE_NAME, projection, null, null, null, null, sortBy);
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
