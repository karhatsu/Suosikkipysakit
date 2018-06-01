package com.karhatsu.suosikkipysakit.domain;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Line implements Parcelable {

	private final String longCode;
	private final String code;
	private final String name;
	private final String lineStart;
	private final String lineEnd;

	private ArrayList<Stop> stops;

	public Line(String longCode, String code, String name, String lineStart, String lineEnd) {
		this.longCode = longCode;
		this.code = code;
		this.name = name;
		this.lineStart = lineStart;
		this.lineEnd = lineEnd;
	}

	@SuppressWarnings("unchecked")
	private Line(Parcel in) {
		this.longCode = in.readString();
		this.code = in.readString();
		this.name = in.readString();
		this.lineStart = in.readString();
		this.lineEnd = in.readString();
		this.stops = in.readArrayList(Stop.class.getClassLoader());
	}

	public String getLongCode() {
		return longCode;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getLineStart() {
		return lineStart;
	}

	public String getLineEnd() {
		return lineEnd;
	}

	public ArrayList<Stop> getStops() {
		return stops;
	}

	public void setStops(ArrayList<Stop> stops) {
		this.stops = stops;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(longCode);
		out.writeString(code);
		out.writeString(name);
		out.writeString(lineStart);
		out.writeString(lineEnd);
		out.writeList(stops);
	}

	public static final Parcelable.Creator<Line> CREATOR = new Parcelable.Creator<Line>() {
		@Override
		public Line createFromParcel(Parcel in) {
			return new Line(in);
		}

		@Override
		public Line[] newArray(int size) {
			return new Line[size];
		}
	};

}
