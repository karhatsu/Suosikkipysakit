package com.karhatsu.suosikkipysakit.domain;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Line implements Parcelable {

	private final String code;
	private final String name;
	private final String lineStart;
	private final String lineEnd;

	private List<Stop> stops;

	public Line(String code, String name, String lineStart, String lineEnd) {
		this.code = code;
		this.name = name;
		this.lineStart = lineStart;
		this.lineEnd = lineEnd;
	}

	private Line(Parcel in) {
		this.code = in.readString();
		this.name = in.readString();
		this.lineStart = in.readString();
		this.lineEnd = in.readString();
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

	public List<Stop> getStops() {
		return stops;
	}

	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(code);
		out.writeString(name);
		out.writeString(lineStart);
		out.writeString(lineEnd);
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
