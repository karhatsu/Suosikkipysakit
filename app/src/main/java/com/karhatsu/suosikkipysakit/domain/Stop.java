package com.karhatsu.suosikkipysakit.domain;

import java.util.List;
import java.util.regex.Pattern;

import android.os.Parcel;
import android.os.Parcelable;

public class Stop implements Parcelable {

	public static final String STOP_KEY = "com.karhatsu.suosikkipysakit.domain.STOP";
	public static final String CODE_KEY = "com.karhatsu.suosikkipysakit.domain.CODE";

	private static final Pattern PATTERN = Pattern
			.compile("(V|E|Ki|Ke|Jä|Tu)?\\d{4}");

	private final String code;
	private final String name;
	private final String coordinates;
	private String nameByUser = null;
	private boolean hidden;
	private long id;

	private List<Departure> departures;

	public Stop(String code, String name, String coordinates) {
		this.code = code;
		this.name = name;
		this.coordinates = coordinates;
	}

	@SuppressWarnings("unchecked")
	private Stop(Parcel in) {
		this.id = in.readLong();
		this.code = in.readString();
		this.name = in.readString();
		this.coordinates = in.readString();
		this.nameByUser = in.readString();
		this.hidden = (in.readInt() == 1);
		this.departures = in.readArrayList(Departure.class.getClassLoader());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public boolean isHidden() {
		return hidden;
	}

	public boolean isVisible() {
		return !isHidden();
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void changeVisibility() {
		this.hidden = !this.hidden;
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
		out.writeLong(id);
		out.writeString(code);
		out.writeString(name);
		out.writeString(coordinates);
		out.writeString(nameByUser);
		out.writeInt(hidden ? 1 : 0);
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
