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

	public List<StopCollection> findAll() {
		SQLiteDatabase db = getReadableDatabase();
		String[] projection = { _ID, COLUMN_NAME };
		Cursor cursor = db.query(OwnStopsContract.CollectionEntry.TABLE_NAME, projection, null, null, null, null, COLUMN_NAME);
		List<StopCollection> stopCollections = new ArrayList<StopCollection>();
		while (cursor.moveToNext()) {
			stopCollections.add(new StopCollection(cursor.getLong(0), cursor.getString(1)));
		}
		db.close();
		return stopCollections;
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
}
