package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
		setCheckBoxListener(view);
	}

	private void setCheckBoxListener(View view) {
		final CheckBox noSaveCheckBox = (CheckBox) view
				.findViewById(R.id.dialog_new_stop_no_save);
		noSaveCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View stopNameField = findViewById(R.id.dialog_new_stop_name);
				stopNameField.setVisibility(noSaveCheckBox.isChecked() ? View.INVISIBLE
						: View.VISIBLE);
				Button submitButton = getButton(BUTTON_POSITIVE);
				submitButton.setText(noSaveCheckBox.isChecked() ? R.string.dialog_new_stop_continue
						: R.string.dialog_new_stop_save);
			}
		});
	}

	@Override
	protected void submit(Stop stop) {
		CheckBox noSaveCheckBox = (CheckBox) findViewById(R.id.dialog_new_stop_no_save);
		if (noSaveCheckBox.isChecked()) {
			showDepartures(stop);
		} else {
			new StopDao(activity).save(stop);
			showMainActivity(stop);
		}
	}

	private void showDepartures(Stop stop) {
		showStopActivity(stop, DeparturesActivity.class);
	}

	private void showMainActivity(Stop stop) {
		showStopActivity(stop, MainActivity.class);
	}

	private void showStopActivity(Stop stop,
			Class<? extends Activity> activityClass) {
		Intent intent = new Intent(activity, activityClass);
		if (stop.getDepartures() != null) {
			intent.putExtra(Stop.STOP_KEY, stop);
		} else {
			intent.putExtra(Stop.CODE_KEY, stop.getCode());
		}
		activity.startActivity(intent);
	}
}
