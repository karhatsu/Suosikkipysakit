package com.karhatsu.omatpysakit.domain;

import java.util.List;

public class Stop {

	public static final String CODE_KEY = "com.karhatsu.omatpysakit.domain.CODE";

	private static final String VANTAA_PREFIX = "V";
	private static final String ESPOO_PREFIX = "E";

	private final String code;
	private final String nameFi;
	private final String nameSv;
	private String nameByUser;

	private List<Departure> departures;

	public Stop(String code, String nameFi, String nameSv) {
		this.code = code;
		this.nameFi = nameFi;
		this.nameSv = nameSv;
	}

	public String getCode() {
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
}
