package com.karhatsu.suosikkipysakit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.karhatsu.suosikkipysakit.domain.StopCollection;

import java.util.ArrayList;
import java.util.List;

import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.CollectionEntry.*;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.CollectionStopEntry.*;

public class StopCollectionDao extends AbstractDao {
	private static final String[] STOP_COLLECTION_PROJECTION = { _ID, COLUMN_NAME };

	public StopCollectionDao(Context context) {
		super(context);
	}

	public void saveCollectionAndAddStop(String collectionName, long stopId) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, collectionName);
		long collectionId = db.insert(OwnStopsContract.CollectionEntry.TABLE_NAME, null, values);
		insertStopToCollection(db, collectionId, stopId);
		db.close();
	}

	public void insertStopToCollection(long collectionId, long stopId) {
		SQLiteDatabase db = getWritableDatabase();
		if (!collectionContainsStop(db, collectionId, stopId)) {
			insertStopToCollection(db, collectionId, stopId);
		}
		db.close();
	}

	private boolean collectionContainsStop(SQLiteDatabase db, long collectionId, long stopId) {
		String sql = "select count(*) from " + OwnStopsContract.CollectionStopEntry.TABLE_NAME +
				" where " + COLUMN_COLLECTION_ID + "=? and " + COLUMN_STOP_ID + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(collectionId), String.valueOf(stopId)});
		cursor.moveToNext();
		return cursor.getInt(0) > 0;
	}

	private void insertStopToCollection(SQLiteDatabase db, long collectionId, long stopId) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_COLLECTION_ID, collectionId);
		values.put(COLUMN_STOP_ID, stopId);
		db.insert(OwnStopsContract.CollectionStopEntry.TABLE_NAME, null, values);
	}

	public StopCollection findById(long collectionId) {
		SQLiteDatabase db = getReadableDatabase();
		String selection = _ID + "=?";
		String[] selectionArgs = new String[] { String.valueOf(collectionId) };
		Cursor cursor = db.query(OwnStopsContract.CollectionEntry.TABLE_NAME, STOP_COLLECTION_PROJECTION, selection,
				selectionArgs, null, null, null);
		cursor.moveToNext();
		StopCollection stopCollection = buildStopCollection(cursor);
		db.close();
		return stopCollection;
	}

	public List<StopCollection> findAll() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(OwnStopsContract.CollectionEntry.TABLE_NAME, STOP_COLLECTION_PROJECTION, null, null, null, null, COLUMN_NAME);
		List<StopCollection> stopCollections = new ArrayList<StopCollection>();
		while (cursor.moveToNext()) {
			stopCollections.add(buildStopCollection(cursor));
		}
		db.close();
		return stopCollections;
	}

	private StopCollection buildStopCollection(Cursor cursor) {
		return new StopCollection(cursor.getLong(0), cursor.getString(1));
	}

	public void delete(long id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(OwnStopsContract.CollectionEntry.TABLE_NAME, _ID + "=?", new String[] { String.valueOf(id) });
		db.close();
	}

	public boolean containsStops(long collectionId) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select count(*) from " + OwnStopsContract.CollectionStopEntry.TABLE_NAME + " where " + _ID + "=?";
		Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(collectionId)});
		cursor.moveToNext();
		int stopCount = cursor.getInt(0);
		db.close();
		return stopCount > 0;
	}

	public void updateName(long collectionId, String name) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		db.update(OwnStopsContract.CollectionEntry.TABLE_NAME, values, _ID + "=?",
				new String[]{String.valueOf(collectionId)});
		db.close();
	}
}
