package com.karhatsu.suosikkipysakit.domain;

public enum City {
	HELSINKI("Helsinki", "H"),
	ESPOO("Espoo", "E"),
	VANTAA("Vantaa", "V"),
	KIRKKONUMMI("Kirkkonummi", "Ki"),
	KERAVA("Kerava", "Ke"),
	JARVENPAA("Järvenpää", "Jä"),
	SIPOO("Sipoo", "Si"),
	TUUSULA("Tuusula", "Tu");

	private final String name;
	private final String prefix;

	City(String name, String prefix) {
		this.name = name;
		this.prefix = prefix;
	}

	public static String getPrefixByName(String name) {
		return getByName(name).getPrefix();
	}

	public static City getByName(String name) {
		for (City city : values()) {
			if (name.equals(city.getName())) {
				return city;
			}
		}
		throw new IllegalArgumentException(name);
	}

	public static City getByPrefix(String prefix) {
		if (prefix.equals("")) return HELSINKI;
		for (City city : values()) {
			if (prefix.equals(city.getPrefix())) {
				return city;
			}
		}
		throw new IllegalArgumentException(prefix);
	}

	public String getPrefix() {
		return prefix;
	}

	public String getName() {
		return name;
	}
}
