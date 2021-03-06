package com.karhatsu.suosikkipysakit.domain;

public class DeparturesComparator implements java.util.Comparator<Departure> {
	@Override
	public int compare(Departure departure, Departure departure2) {
		if (departure.getServiceDay() != departure2.getServiceDay()) {
			return departure.getServiceDay() - departure2.getServiceDay();
		}
		return departure.getTimeInSeconds() - departure2.getTimeInSeconds();
	}
}
