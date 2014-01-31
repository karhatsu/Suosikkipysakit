package com.karhatsu.suosikkipysakit.domain;

public class StopCollection {
	public static final String COLLECTION_ID_KEY = "com.karhatsu.suosikkipysakit.domain.STOP_COLLECTION_ID";

	public static final int NO_COLLECTION_ID = 0;
	public static final StopCollection NO_COLLECTION = new StopCollection(NO_COLLECTION_ID, null);

	private final long id;
	private final String name;

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
}
