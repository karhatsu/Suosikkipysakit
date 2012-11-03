package com.karhatsu.suosikkipysakit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OwnStopsDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "OwnStops.db";

	public OwnStopsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, OwnStopsContract.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i < OwnStopsContract.MIGRATIONS.length; i++) {
			db.execSQL(OwnStopsContract.MIGRATIONS[i]);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (int i = oldVersion; i < newVersion; i++) {
			db.execSQL(OwnStopsContract.MIGRATIONS[i]);
		}
	}

}
