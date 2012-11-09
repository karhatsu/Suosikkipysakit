package com.karhatsu.suosikkipysakit.domain;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Stop implements Parcelable {

	public static final String CODE_KEY = "com.karhatsu.suosikkipysakit.domain.CODE";

	private static final String VANTAA_PREFIX = "V";
	private static final String ESPOO_PREFIX = "E";

	private final String code;
	private final String name;
	private final String coordinates;
	private String nameByUser = null;

	private List<Departure> departures;

	public Stop(String code, String name, String coordinates) {
		this.code = code;
		this.name = name;
		this.coordinates = coordinates;
	}

	@SuppressWarnings("unchecked")
	private Stop(Parcel in) {
		this.code = in.readString();
		this.name = in.readString();
		this.coordinates = in.readString();
		this.nameByUser = in.readString();
		this.departures = in.readArrayList(Departure.class.getClassLoader());
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public List<Departure> getDepartures() {
		return departures;
	}

	public void setDepartures(List<Departure> departures) {
		this.departures = departures;
	}

	public String getNameByUser() {
		return nameByUser;
	}

	public void setNameByUser(String nameByUser) {
		this.nameByUser = nameByUser;
	}

	public static boolean isValidCode(String code) {
		if (code.startsWith(VANTAA_PREFIX) || code.startsWith(ESPOO_PREFIX)) {
			code = code.substring(1, code.length());
		}
		try {
			Integer.valueOf(code);
		} catch (NumberFormatException e) {
			return false;
		}
		return code.length() == 4;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(code);
		out.writeString(name);
		out.writeString(coordinates);
		out.writeString(nameByUser);
		out.writeList(departures);
	}

	public static final Parcelable.Creator<Stop> CREATOR = new Creator<Stop>() {
		@Override
		public Stop[] newArray(int size) {
			return new Stop[size];
		}

		@Override
		public Stop createFromParcel(Parcel in) {
			return new Stop(in);
		}
	};
}
