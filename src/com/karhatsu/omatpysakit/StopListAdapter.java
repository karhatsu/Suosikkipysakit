package com.karhatsu.omatpysakit;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StopListAdapter extends ArrayAdapter<Stop> {

	private List<Stop> stops;

	public StopListAdapter(Context context) {
		super(context, R.layout.stop_list_item, Stops.get().getAll());
		this.stops = Stops.get().getAll();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.stop_list_item, null);
		}
		Stop stop = stops.get(position);
		TextView text = (TextView) view.findViewById(R.id.stop_list_item_text);
		text.setText(stop.getCode());
		return view;
	}

}
