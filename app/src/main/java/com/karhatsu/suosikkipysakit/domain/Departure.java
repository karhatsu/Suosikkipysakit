package com.karhatsu.suosikkipysakit.domain;

import java.util.Calendar;
import java.util.TimeZone;

import android.os.Parcel;
import android.os.Parcelable;

public class Departure implements Parcelable {
	private final String line;
	private final String time;
	private final String endStop;
	private final boolean realtime;

	public Departure(String line, String time, String endStop, boolean realtime) {
		this.line = line;
		this.time = time;
		this.endStop = endStop;
		this.realtime = realtime;
	}

	private Departure(Parcel in) {
		this.line = in.readString();
		this.time = in.readString();
		this.endStop = in.readString();
		this.realtime = in.readInt() == 1;
	}

	public String getLine() {
		return line;
	}

	public String getTime() {
		return time;
	}

	public String getEndStop() {
		return endStop;
	}

	public boolean isRealtime() {
		return realtime;
	}
	public int getMinutesToGo() {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Helsinki"));
		Calendar departure = (Calendar) now.clone();
		departure.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
		departure.set(Calendar.MINUTE, Integer.parseInt(time.substring(3, 5)));
		if (now.get(Calendar.HOUR_OF_DAY) > departure.get(Calendar.HOUR_OF_DAY)) {
			departure.add(Calendar.DAY_OF_MONTH, 1);
		}
		return (int) ((departure.getTimeInMillis() - now.getTimeInMillis()) / 1000 / 60);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(line);
		out.writeString(time);
		out.writeString(endStop);
		out.writeInt(realtime ? 1 : 0);
	}

	public static final Parcelable.Creator<Departure> CREATOR = new Creator<Departure>() {
		@Override
		public Departure[] newArray(int size) {
			return new Departure[size];
		}

		@Override
		public Departure createFromParcel(Parcel in) {
			return new Departure(in);
		}
	};
}
