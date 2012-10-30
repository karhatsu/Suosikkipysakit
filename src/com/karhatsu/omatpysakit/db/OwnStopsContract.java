package com.karhatsu.omatpysakit.db;

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
		public static final String COLUMN_NAME_FI = "name_fi";
		public static final String COLUMN_NAME_SV = "name_sv";
		public static final String COLUMN_NAME_BY_USER = "name_by_user";
		public static final String CREATE_TABLE = //
		"create table " + TABLE_NAME + " (" //
				+ _ID + " integer primary key, " //
				+ COLUMN_CODE + " integer not null," //
				+ COLUMN_NAME_FI + " varchar(100) not null," //
				+ COLUMN_NAME_SV + " varchar(100) not null," //
				+ COLUMN_NAME_BY_USER + " varchar(100) not null" //
				+ ")";
	}

	private OwnStopsContract() {
	}
}
