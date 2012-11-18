package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class SaveStopDialog {

	private final Activity activity;
	private final boolean isNew;
	private long id;

	public SaveStopDialog(Activity activity, final long stopId) {
		this.activity = activity;
		this.id = stopId;
		this.isNew = false;
		Stop stop = new StopDao(activity).findById(stopId);
		buildAndShowDialog(stop);
	}

	public SaveStopDialog(Activity activity, final Stop stop) {
		this.activity = activity;
		this.isNew = true;
		buildAndShowDialog(stop);
	}

	private void buildAndShowDialog(final Stop stop) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_save_stop, null);
		builder.setView(view);
		builder.setTitle(R.string.dialog_save_stop_title);
		EditText stopName = (EditText) view
				.findViewById(R.id.dialog_save_stop_name);
		setInitialValue(stop, stopName);
		setPositiveButton(stop, builder, stopName);
		setNegativeButton(builder);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void setInitialValue(final Stop stop, EditText stopName) {
		String name = stop.getNameByUser() != null ? stop.getNameByUser()
				: stop.getName();
		stopName.setText(name);
	}

	private void setPositiveButton(final Stop stop,
			AlertDialog.Builder builder, final EditText stopName) {
		builder.setPositiveButton(R.string.dialog_save_stop_save,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						stop.setNameByUser(stopName.getText().toString());
						dialog.dismiss();
						saveStopAndShowAll(stop);
					}
				});
	}

	private void saveStopAndShowAll(Stop stop) {
		if (isNew) {
			new StopDao(activity).save(stop);
		} else {
			new StopDao(activity).updateName(id, stop.getNameByUser());
		}
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
	}

	private void setNegativeButton(AlertDialog.Builder builder) {
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}
}
