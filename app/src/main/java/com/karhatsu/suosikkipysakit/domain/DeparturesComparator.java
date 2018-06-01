package com.karhatsu.suosikkipysakit.domain;

public class DeparturesComparator implements java.util.Comparator<Departure> {
	@Override
	public int compare(Departure departure, Departure departure2) {
		return departure.getMinutesToGo() - departure2.getMinutesToGo();
	}
}
