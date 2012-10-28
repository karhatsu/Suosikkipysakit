package com.karhatsu.omatpysakit.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karhatsu.omatpysakit.R;
import com.karhatsu.omatpysakit.domain.Departure;

public class DepartureListAdapter extends ArrayAdapter<Departure> {

	private List<Departure> departures;

	public DepartureListAdapter(Context context, List<Departure> departures) {
		super(context, R.layout.list_item_departure, departures);
		this.departures = departures;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.list_item_departure, null);
		}
		Departure departure = departures.get(position);
		TextView text = (TextView) view
				.findViewById(R.id.departure_list_item_text);
		text.setText(departure.getLine() + " / " + departure.getTime());
		return view;
	}
}
