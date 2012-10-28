package com.karhatsu.omatpysakit.domain;

import java.util.List;

public class Stop {

	public static final String CODE_KEY = "com.karhatsu.omatpysakit.domain.CODE";

	private final int code;
	private final String nameFi;
	private final String nameSv;

	private List<Departure> departures;

	public Stop(int code, String nameFi, String nameSv) {
		this.code = code;
		this.nameFi = nameFi;
		this.nameSv = nameSv;
	}

	public int getCode() {
		return code;
	}

	public String getNameFi() {
		return nameFi;
	}

	public String getNameSv() {
		return nameSv;
	}

	public List<Departure> getDepartures() {
		return departures;
	}

	public void setDepartures(List<Departure> departures) {
		this.departures = departures;
	}
}
