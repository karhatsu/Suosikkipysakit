package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.domain.Departure;

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
		setText(view, R.id.departure_list_item_time, departure.getTime());
		setText(view, R.id.departure_list_item_min, getMinutesToGo(departure));
		setText(view, R.id.departure_list_item_line, departure.getLine());
		setText(view, R.id.departure_list_item_end_stop, departure.getEndStop());
		return view;
	}

	private String getMinutesToGo(Departure departure) {
		int min = departure.getMinutesToGo();
		if (min < 10) {
			return " " + min;
		} else if (min > 99) {
			return "**";
		}
		return String.valueOf(min);
	}

	private void setText(View view, int resourceId, String text) {
		TextView textView = (TextView) view.findViewById(resourceId);
		textView.setText(text);
	}
}
