package com.karhatsu.suosikkipysakit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractDao {
	protected OwnStopsDbHelper dbHelper;
	protected final Context context;

	protected AbstractDao(Context context) {
		this.context = context;
		this.dbHelper = new OwnStopsDbHelper(context);
	}

	protected SQLiteDatabase getWritableDatabase(Context context) {
		return dbHelper.getWritableDatabase();
	}

	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
