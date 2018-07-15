package com.karhatsu.suosikkipysakit.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Departure implements Parcelable {
	private final String line;
	private final int timeInSeconds;
	private final String endStop;
	private final boolean realtime;
	private final int serviceDay;

	public Departure(String line, int timeInSeconds, String endStop, boolean realtime, int serviceDay) {
		this.line = line;
		this.timeInSeconds = timeInSeconds;
		this.endStop = endStop;
		this.realtime = realtime;
		this.serviceDay = serviceDay;
	}

	private Departure(Parcel in) {
		this.line = in.readString();
		this.timeInSeconds = in.readInt();
		this.endStop = in.readString();
		this.realtime = in.readInt() == 1;
		this.serviceDay = in.readInt();
	}

	public String getLine() {
		return line;
	}

	public int getTimeInSeconds() {
		return timeInSeconds;
	}

	public String getEndStop() {
		return endStop;
	}

	public boolean isRealtime() {
		return realtime;
	}

	public int getServiceDay() {
		return serviceDay;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(line);
		out.writeInt(timeInSeconds);
		out.writeString(endStop);
		out.writeInt(realtime ? 1 : 0);
		out.writeInt(serviceDay);
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
