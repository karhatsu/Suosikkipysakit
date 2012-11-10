package com.karhatsu.suosikkipysakit.domain;

import java.util.List;
import java.util.regex.Pattern;

import android.os.Parcel;
import android.os.Parcelable;

public class Stop implements Parcelable {

	public static final String CODE_KEY = "com.karhatsu.suosikkipysakit.domain.CODE";

	private static final Pattern PATTERN = Pattern
			.compile("(V|E|Ke|Jä|Tu)?\\d{4}");

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
		return PATTERN.matcher(code).matches();
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
