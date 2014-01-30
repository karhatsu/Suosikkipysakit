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
		SQLiteDatabase db = getWritableDatabase(context);
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, collectionName);
		long collectionId = db.insert(OwnStopsContract.CollectionEntry.TABLE_NAME, null, values);
		insertCollectionStop(db, collectionId, stopId);
		db.close();
	}

	private void insertCollectionStop(SQLiteDatabase db, long collectionId, long stopId) {
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
}
