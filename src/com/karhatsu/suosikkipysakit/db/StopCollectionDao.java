package com.karhatsu.suosikkipysakit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
}
