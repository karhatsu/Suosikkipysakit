package com.karhatsu.omatpysakit;

import java.util.ArrayList;
import java.util.List;

public class Stops {

	private static Stops instance;
	private List<Stop> stops;

	public static Stops get() {
		if (instance == null) {
			instance = new Stops();
		}
		return instance;
	}

	private Stops() {
		stops = new ArrayList<Stop>();
		stops.add(new Stop("123"));
		stops.add(new Stop("145"));
		stops.add(new Stop("212"));
		stops.add(new Stop("333"));
		stops.add(new Stop("411"));
	}

	public List<Stop> getAll() {
		return stops;
	}

	public void save(Stop stop) {
		stops.add(stop);
	}

}
