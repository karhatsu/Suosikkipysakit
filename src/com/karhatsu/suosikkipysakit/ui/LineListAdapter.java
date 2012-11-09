package com.karhatsu.suosikkipysakit.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.domain.Line;

public class LineListAdapter extends ArrayAdapter<Line> {

	private List<Line> lines;

	public LineListAdapter(Context context, List<Line> lines) {
		super(context, R.layout.list_item_line, lines);
		this.lines = lines;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.list_item_line, null);
		}
		Line line = lines.get(position);
		setText(view, R.id.list_item_line_code, line.getCode());
		setText(view, R.id.list_item_line_name, line.getName());
		setText(view, R.id.list_item_line_direction, getDirectionText(line));
		return view;
	}

	private void setText(View view, int resourceId, String text) {
		TextView textView = (TextView) view.findViewById(resourceId);
		textView.setText(text);
	}

	private String getDirectionText(Line line) {
		return line.getLineStart() + " -> " + line.getLineEnd();
	}
}
