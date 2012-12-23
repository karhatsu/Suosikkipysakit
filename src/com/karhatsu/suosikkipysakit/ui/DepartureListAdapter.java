package com.karhatsu.suosikkipysakit.ui;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.domain.Departure;

public class DepartureListAdapter extends ArrayAdapter<Departure> {

	private List<Departure> departures;
	private int previousMinutes = -1;
	private boolean minutesJustChanged;
	private ColorStateList defaultTextViewColors;

	public DepartureListAdapter(Context context, List<Departure> departures) {
		super(context, R.layout.list_item_departure, departures);
		this.departures = departures;
	}

	@Override
	public void notifyDataSetChanged() {
		recognizeMinuteChanging();
		super.notifyDataSetChanged();
	}

	private void recognizeMinuteChanging() {
		int currentMinutes = Calendar.getInstance().get(Calendar.MINUTE);
		minutesJustChanged = previousMinutes != -1
				&& previousMinutes != currentMinutes;
		previousMinutes = currentMinutes;
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
		setText(view, R.id.departure_list_item_time, departure.getTime(), false);
		setText(view, R.id.departure_list_item_min, getMinutesToGo(departure),
				minutesJustChanged);
		setText(view, R.id.departure_list_item_line, departure.getLine(), false);
		setText(view, R.id.departure_list_item_end_stop,
				departure.getEndStop(), false);
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

	private void setText(View view, int resourceId, String text,
			boolean highLight) {
		TextView textView = (TextView) view.findViewById(resourceId);
		defineDefaultColors(textView);
		textView.setText(text);
		setTextViewColor(highLight, textView);
	}

	private void setTextViewColor(boolean highLight, TextView textView) {
		if (highLight) {
			int highlightColor = Color.parseColor("#f1d243");
			textView.setTextColor(highlightColor);
		} else {
			textView.setTextColor(defaultTextViewColors);
		}
	}

	private void defineDefaultColors(TextView textView) {
		if (defaultTextViewColors == null) {
			defaultTextViewColors = textView.getTextColors();
		}
	}
}
