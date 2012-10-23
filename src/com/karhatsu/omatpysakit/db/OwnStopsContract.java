package com.karhatsu.omatpysakit.db;

import android.provider.BaseColumns;

public class OwnStopsContract {
	public static abstract class StopEntry implements BaseColumns {
		public static final String TABLE_NAME = "stops";
		public static final String COLUMN_NAME_CODE = "code";
		public static final String CREATE_TABLE = //
		"create table " + TABLE_NAME + " (" //
				+ _ID + " integer primary key, " //
				+ COLUMN_NAME_CODE + " integer not null" //
				+ ")";
	}

	private OwnStopsContract() {
	}
}
