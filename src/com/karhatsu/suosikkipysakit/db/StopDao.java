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

import android.util.Log;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopDao extends AbstractDao {

	public static final String PROJECTION_NAME = "name";

	public StopDao(Context context) {
		super(context);
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

	public void updateName(long id, String name) {
		SQLiteDatabase db = getWritableDatabase(context);
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_BY_USER, name);
		db.update(TABLE_NAME, values, _ID + "=?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public void delete(long id) {
		SQLiteDatabase db = getWritableDatabase(context);
		db.delete(TABLE_NAME, _ID + "=?", new String[] { String.valueOf(id) });
		db.close();
	}

	public Cursor findAll() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.rawQuery(buildFindAllQuery(), new String[]{});
	}

	private String buildFindAllQuery() {
		return "select " + _ID + ", " + COLUMN_CODE + ", " + COLUMN_NAME_BY_USER + " as " + PROJECTION_NAME + " from " + TABLE_NAME + //
				" union " + //
				"select " + _ID + ", null, " + OwnStopsContract.CollectionEntry.COLUMN_NAME + " as " + PROJECTION_NAME + " from " + OwnStopsContract.CollectionEntry.TABLE_NAME + //
				" order by " + PROJECTION_NAME;
	}

	public Stop findById(long id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = { COLUMN_CODE, COLUMN_NAME, COLUMN_COORDINATES,
				COLUMN_NAME_BY_USER };
		String selection = _ID + "=?";
		String[] selectionArgs = new String[] { String.valueOf(id) };
		Cursor cursor = db.query(TABLE_NAME, projection, selection,
				selectionArgs, null, null, null);
		cursor.moveToFirst();
		Stop stop = createStop(cursor);
		cursor.close();
		db.close();
		return stop;
	}

	private Stop createStop(Cursor cursor) {
		Stop stop = new Stop(cursor.getString(0), cursor.getString(1),
				cursor.getString(2));
		stop.setNameByUser(cursor.getString(3));
		return stop;
	}

}
