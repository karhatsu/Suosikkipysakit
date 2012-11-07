package com.karhatsu.suosikkipysakit.db;

import android.provider.BaseColumns;

public class OwnStopsContract {
	static final int DATABASE_VERSION = 1;
	static String[] MIGRATIONS = new String[DATABASE_VERSION];
	static {
		MIGRATIONS[0] = StopEntry.CREATE_TABLE;
	}

	public static abstract class StopEntry implements BaseColumns {
		public static final String TABLE_NAME = "stops";
		public static final String COLUMN_CODE = "code";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_NAME_BY_USER = "name_by_user";
		public static final String COLUMN_COORDINATES = "coords";
		public static final String CREATE_TABLE = //
		"create table " + TABLE_NAME + " (" //
				+ _ID + " integer primary key, " //
				+ COLUMN_CODE + " varchar(7) not null," //
				+ COLUMN_NAME + " varchar(100) not null," //
				+ COLUMN_NAME_BY_USER + " varchar(100) not null," //
				+ COLUMN_COORDINATES + " varchar(15) not null" //
				+ ")";
	}

	private OwnStopsContract() {
	}
}
