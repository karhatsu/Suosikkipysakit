package com.karhatsu.suosikkipysakit.domain;

public class StopCollection {
	public static final String COLLECTION_ID_KEY = "com.karhatsu.suosikkipysakit.domain.STOP_COLLECTION_ID";

	private final long id;
	private String name;

	public StopCollection(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
