package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class LineStopListAdapter extends ArrayAdapter<Stop> {

	private List<Stop> stops;

	public LineStopListAdapter(Context context, List<Stop> stops) {
		super(context, R.layout.list_item_stop, stops);
		this.stops = stops;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.list_item_stop, null);
		}
		Stop line = stops.get(position);
		setText(view, R.id.stop_list_item_name, line.getName());
		return view;
	}

	private void setText(View view, int resourceId, String text) {
		TextView textView = (TextView) view.findViewById(resourceId);
		textView.setText(text);
	}

}
