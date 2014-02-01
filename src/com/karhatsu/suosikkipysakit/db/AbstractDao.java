package com.karhatsu.suosikkipysakit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractDao {
	private OwnStopsDbHelper dbHelper;

	protected AbstractDao(Context context) {
		this.dbHelper = new OwnStopsDbHelper(context);
	}

	protected SQLiteDatabase getReadableDatabase() {
		return dbHelper.getReadableDatabase();
	}

	protected SQLiteDatabase getWritableDatabase() {
		return dbHelper.getWritableDatabase();
	}

	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
