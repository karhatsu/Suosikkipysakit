package com.karhatsu.suosikkipysakit.domain;

public enum City {
	HELSINKI("Helsinki", ""),
	ESPOO("Espoo", "E"),
	VANTAA("Vantaa", "V"),
	KERAVA("Kerava", "Ke"),
	JARVENPAA("Järvenpää", "Jä"),
	TUUSULA("Tuusula", "Tu");

	private final String name;
	private final String prefix;

	private City(String name, String prefix) {
		this.name = name;
		this.prefix = prefix;
	}

	public static String getPrefixByName(String name) {
		return getByName(name).getPrefix();
	}

	private static City getByName(String name) {
		for (City city : values()) {
			if (name.equals(city.getName())) {
				return city;
			}
		}
		throw new IllegalArgumentException(name);
	}

	public String getPrefix() {
		return prefix;
	}

	public String getName() {
		return name;
	}
}
