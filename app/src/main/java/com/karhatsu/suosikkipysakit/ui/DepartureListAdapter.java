package com.karhatsu.suosikkipysakit.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.domain.Departure;

import java.util.List;

public class DepartureListAdapter extends ArrayAdapter<Departure> {

	private List<Departure> departures;

	public DepartureListAdapter(Context context, List<Departure> departures) {
		super(context, R.layout.list_item_departure, departures);
		this.departures = departures;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
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
		setText(view, R.id.departure_list_item_time, getTime(departure));
		setText(view, R.id.departure_list_item_min, getMinutesToGo(departure));
		setText(view, R.id.departure_list_item_line, departure.getLine());
		setText(view, R.id.departure_list_item_end_stop, departure.getEndStop());
		return view;
	}

	private String getTime(Departure departure) {
		String time = departure.getTime();
		if (!departure.isRealtime()) {
			time += "~";
		}
		return time;
	}

	private String getMinutesToGo(Departure departure) {
		int min = departure.getMinutesToGo();
		if (min < -9 || min > 99) {
			return "**";
		} else if (min >= 0 && min < 10) {
			return " " + min;
		}
		return String.valueOf(min);
	}

	private void setText(View view, int resourceId, String text) {
		TextView textView = view.findViewById(resourceId);
		textView.setText(text);
	}
}
