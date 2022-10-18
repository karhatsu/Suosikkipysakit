package com.karhatsu.suosikkipysakit.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

import java.util.List;

public class StopsVisibilityListAdapter extends ArrayAdapter<Stop> {

	private final List<Stop> stops;

	public StopsVisibilityListAdapter(Context context, List<Stop> stops) {
		super(context, R.layout.list_item_stop_visibility, stops);
		this.stops = stops;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.list_item_stop_visibility, null);
		}
		Stop stop = stops.get(position);
		setStopName(view, stop);
		setStopVisibility(view, stop);
		return view;
	}

	protected void setStopName(View view, Stop stop) {
		TextView textView = view.findViewById(R.id.list_item_stop_visibility_stop);
		textView.setText(stop.getVisibleName());
	}

	protected void setStopVisibility(View view, Stop stop) {
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.list_item_stop_visibility_checkbox);
		checkbox.setChecked(stop.isVisible());
		checkbox.setTag(stop);
		checkbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CheckBox checkBox = (CheckBox) view;
				Stop stop = (Stop) checkBox.getTag();
				new StopDao(getContext()).changeVisibility(stop);
				int text = stop.isHidden() ? R.string.activity_stops_visibility_changed_to_hidden :
						R.string.activity_stops_visibility_changed_to_visible;
				Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
			}
		});
	}
}
