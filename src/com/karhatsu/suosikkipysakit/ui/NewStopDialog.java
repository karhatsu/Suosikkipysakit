package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class NewStopDialog extends SaveStopDialog {

	public NewStopDialog(OnStopSaveCancel onStopSaveCancel, Activity activity,
			final Stop stop) {
		super(activity, onStopSaveCancel);
		setTitle(R.string.dialog_new_stop_title);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_new_stop, null);
		setView(view);
		EditText stopNameField = (EditText) view
				.findViewById(R.id.dialog_new_stop_name);
		addEnterListener(stopNameField, stop);
		setInitialName(stop, stopNameField);
		setButtons(stop, stopNameField, R.string.dialog_new_stop_save);
	}

	@Override
	protected void submit(Stop stop) {
		new StopDao(activity).save(stop);
		showMainActivity(stop);
	}

	private void showMainActivity(Stop stop) {
		Intent intent = new Intent(activity, MainActivity.class);
		if (stop.getDepartures() != null) {
			intent.putExtra(Stop.STOP_KEY, stop);
		} else {
			intent.putExtra(Stop.CODE_KEY, stop.getCode());
		}
		activity.startActivity(intent);
	}
}
