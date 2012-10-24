package com.karhatsu.omatpysakit.datasource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.karhatsu.omatpysakit.domain.Departure;
import com.karhatsu.omatpysakit.domain.Stop;

public class StopRequest {

	public Stop getData(int stopCode) {
		Stop stop = new Stop(stopCode);
		addDepartures(stop);
		return stop;
	}

	private void addDepartures(Stop stop) {
		List<Departure> departures = new ArrayList<Departure>(5);
		departures.add(new Departure("78", new Date()));
		departures.add(new Departure("79", new Date()));
		departures.add(new Departure("12", new Date()));
		departures.add(new Departure("94A", new Date()));
		departures.add(new Departure("519", new Date()));
		stop.setDepartures(departures);
	}

}
