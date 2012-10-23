package com.karhatsu.omatpysakit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OwnStopsDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "OwnStops.db";
	private static final int DATABASE_VERSION = 1;

	public OwnStopsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(OwnStopsContract.StopEntry.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new RuntimeException("Running version 1, nothing to upgrade");
	}

}
