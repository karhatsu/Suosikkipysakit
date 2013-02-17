package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class SaveStopDialog extends AlertDialog {

	private final OnStopSaveCancel onStopSaveCancel;
	private final Activity activity;
	private final boolean isNew;
	private long id;

	public SaveStopDialog(OnStopSaveCancel onStopSaveCancel, Activity activity,
			final long stopId) {
		super(activity);
		this.onStopSaveCancel = onStopSaveCancel;
		this.activity = activity;
		this.id = stopId;
		this.isNew = false;
		Stop stop = new StopDao(activity).findById(stopId);
		buildDialog(stop);
	}

	public SaveStopDialog(OnStopSaveCancel onStopSaveCancel, Activity activity,
			final Stop stop) {
		super(activity);
		this.onStopSaveCancel = onStopSaveCancel;
		this.activity = activity;
		this.isNew = true;
		buildDialog(stop);
	}

	private void buildDialog(final Stop stop) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_save_stop, null);
		setView(view);
		setTitle(R.string.dialog_save_stop_title);
		EditText stopNameField = (EditText) view
				.findViewById(R.id.dialog_save_stop_name);
		addEnterListener(stopNameField, stop);
		setInitialName(stop, stopNameField);
		setButtons(stop, stopNameField);
	}

	private void addEnterListener(EditText stopNameField, final Stop stop) {
		stopNameField.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					dismiss();
					saveStopAndShowAll(stop);
				}
				return true;
			}
		});
	}

	private void setInitialName(final Stop stop, EditText stopName) {
		String name = stop.getNameByUser() != null ? stop.getNameByUser()
				: stop.getName();
		stopName.setText(name);
	}

	private void setButtons(final Stop stop, EditText stopName) {
		setButton(BUTTON_POSITIVE,
				activity.getString(R.string.dialog_save_stop_save),
				new SaveButtonListener(stopName, stop));
		setButton(BUTTON_NEGATIVE, activity.getString(R.string.cancel),
				new CancelButtonListener());
	}

	private class SaveButtonListener implements DialogInterface.OnClickListener {
		private EditText stopName;
		private Stop stop;

		private SaveButtonListener(EditText stopName, Stop stop) {
			this.stopName = stopName;
			this.stop = stop;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			stop.setNameByUser(stopName.getText().toString());
			dialog.dismiss();
			saveStopAndShowAll(stop);
		}
	}

	private void saveStopAndShowAll(Stop stop) {
		if (isNew) {
			new StopDao(activity).save(stop);
			showMainActivity(stop);
		} else {
			new StopDao(activity).updateName(id, stop.getNameByUser());
			if (activity instanceof MainActivity) {
				((MainActivity) activity).refreshStopList();
			}
		}
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

	private class CancelButtonListener implements
			DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			onStopSaveCancel.stopSaveCancelled();
			dialog.dismiss();
		}
	}
}
