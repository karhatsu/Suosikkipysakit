package com.karhatsu.omatpysakit;

import java.util.ArrayList;
import java.util.List;

public class Stops {

	private List<Stop> stops;

	public Stops() {
		stops = new ArrayList<Stop>();
		stops.add(new Stop("123"));
		stops.add(new Stop("145"));
		stops.add(new Stop("212"));
		stops.add(new Stop("333"));
		stops.add(new Stop("411"));
	}

	public List<String> getTitles() {
		List<String> titles = new ArrayList<String>(stops.size());
		for (Stop stop : stops) {
			titles.add(stop.getCode());
		}
		return titles;
	}

}
