package com.karhatsu.suosikkipysakit.domain;

import java.util.List;
import java.util.regex.Pattern;

import android.os.Parcel;
import android.os.Parcelable;

public class Stop implements Parcelable {

	public static final String STOP_KEY = "com.karhatsu.suosikkipysakit.domain.STOP";
	public static final String CODE_KEY = "com.karhatsu.suosikkipysakit.domain.CODE";

	private static final Pattern PATTERN = Pattern
			.compile("(H|V|E|Ki|Ke|Jä|Si|Tu)\\d{4}");

	private final String code;
	private final String name;
	private final String coordinates;
	private String nameByUser = null;
	private boolean hidden;
	private long id;
	private String zoneId;
	private String legacyCode;

	private List<Departure> departures;

	public Stop(String code, String name) {
		this.code = code;
		this.name = name;
		this.coordinates = "";
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
		this.zoneId = in.readString();
		this.legacyCode = in.readString();
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

	public String getLegacyCode() {
		return legacyCode;
	}

	public void setLegacyCode(String legacyCode) {
		this.legacyCode = legacyCode;
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

	public String getVisibleName() {
		return nameByUser != null ? nameByUser : name;
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

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public static boolean isLegacyCode(String code) {
		return !code.startsWith("HSL:");
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
		out.writeString(zoneId);
		out.writeString(legacyCode);
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
