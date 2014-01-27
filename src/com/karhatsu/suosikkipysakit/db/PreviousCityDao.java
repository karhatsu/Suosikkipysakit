package com.karhatsu.suosikkipysakit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.karhatsu.suosikkipysakit.domain.City;

import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.PreviousCityEntry.COLUMN_PREFIX;
import static com.karhatsu.suosikkipysakit.db.OwnStopsContract.PreviousCityEntry.TABLE_NAME;

public class PreviousCityDao extends AbstractDao {

	public PreviousCityDao(Context context) {
		super(context);
	}

	public City findCity() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = { COLUMN_PREFIX };
		Cursor cursor = db.query(TABLE_NAME, projection, "",
				new String[] {}, null, null, null);
		cursor.moveToFirst();
		City city = City.getByPrefix(cursor.getString(0));
		cursor.close();
		db.close();
		return city;
	}

	public void save(City city) {
		SQLiteDatabase db = getWritableDatabase(context);
		ContentValues values = new ContentValues();
		values.put(COLUMN_PREFIX, city.getPrefix());
		db.update(TABLE_NAME, values, "", new String[] {});
		db.close();
	}
}
