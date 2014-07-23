package com.karhatsu.suosikkipysakit.db;

import static android.provider.BaseColumns._ID;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.COLUMN_CODE;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.COLUMN_COORDINATES;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.COLUMN_HIDDEN;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.COLUMN_NAME;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.COLUMN_NAME_BY_USER;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.StopEntry.TABLE_NAME;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karhatsu.suosikkipysakit.domain.Stop;

public class StopDao extends AbstractDao {

	public static final String PROJECTION_NAME = "name";

	private static final int HIDDEN_VALUE = 1;

	public StopDao(Context context) {
		super(context);
	}

	public void save(Stop stop) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_CODE, stop.getCode());
		values.put(COLUMN_NAME, stop.getName());
		values.put(COLUMN_NAME_BY_USER, stop.getNameByUser());
		values.put(COLUMN_COORDINATES, stop.getCoordinates());
		db.insert(OwnStopsContract.StopEntry.TABLE_NAME, null, values);
		db.close();
	}

	public void updateName(long id, String name) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_BY_USER, name);
		db.update(TABLE_NAME, values, _ID + "=?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public void hideStop(long id) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_HIDDEN, HIDDEN_VALUE);
		db.update(TABLE_NAME, values, _ID + "=?", new String[] { String.valueOf(id)});
		db.close();
	}

	public void delete(long id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, _ID + "=?", new String[] { String.valueOf(id) });
		db.close();
	}

	public List<Stop> findAllStops() {
		SQLiteDatabase db = getReadableDatabase();
		List<Stop> stops = new ArrayList<Stop>();
		String[] projection = new String[] { COLUMN_CODE, COLUMN_NAME, COLUMN_COORDINATES,
				COLUMN_NAME_BY_USER, COLUMN_HIDDEN };
		String orderBy = COLUMN_NAME_BY_USER;
		Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, orderBy);
		while (cursor.moveToNext()) {
			stops.add(createStop(cursor));
		}
		db.close();
		return stops;
	}

	public Cursor findAllStopsAndCollections() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(buildFindAllQuery(), new String[]{});
	}

	private String buildFindAllQuery() {
		return "select " + _ID + ", " + COLUMN_CODE + ", " + COLUMN_NAME_BY_USER + " as " + PROJECTION_NAME + " from " + TABLE_NAME + " where " + COLUMN_HIDDEN + "=0 " + //
				" union " + //
				"select " + _ID + ", null, " + OwnStopsContract.CollectionEntry.COLUMN_NAME + " as " + PROJECTION_NAME + " from " + OwnStopsContract.CollectionEntry.TABLE_NAME + //
				" order by " + PROJECTION_NAME;
	}

	public Stop findById(long id) {
		SQLiteDatabase db = getReadableDatabase();
		String[] projection = { COLUMN_CODE, COLUMN_NAME, COLUMN_COORDINATES,
				COLUMN_NAME_BY_USER, COLUMN_HIDDEN };
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

	public List<Stop> findByCollectionid(long collectionId) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = buildCollectionIdSql();
		Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(collectionId)});
		List<Stop> stops = new ArrayList<Stop>();
		while (cursor.moveToNext()) {
			stops.add(createStop(cursor));
		}
		db.close();
		return stops;
	}

	private String buildCollectionIdSql() {
		return "select " + COLUMN_CODE + ", " + COLUMN_NAME + ", " + COLUMN_COORDINATES + ", " + COLUMN_NAME_BY_USER //
				+ " from " + TABLE_NAME + " s inner join " + OwnStopsContract.CollectionStopEntry.TABLE_NAME + " cs " //
				+ "on s." + _ID + "=cs." + OwnStopsContract.CollectionStopEntry.COLUMN_STOP_ID //
				+ " where cs." + OwnStopsContract.CollectionStopEntry.COLUMN_COLLECTION_ID + "=?";
	}

	private Stop createStop(Cursor cursor) {
		Stop stop = new Stop(cursor.getString(0), cursor.getString(1),
				cursor.getString(2));
		stop.setNameByUser(cursor.getString(3));
		stop.setHidden(cursor.getInt(4) == HIDDEN_VALUE);
		return stop;
	}
}
