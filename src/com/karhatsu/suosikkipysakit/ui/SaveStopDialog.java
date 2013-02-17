package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.domain.Stop;

abstract class SaveStopDialog extends AlertDialog {

	protected final OnStopSaveCancel onStopSaveCancel;
	protected final Activity activity;

	protected SaveStopDialog(Activity activity,
			OnStopSaveCancel onStopSaveCancel) {
		super(activity);
		this.onStopSaveCancel = onStopSaveCancel;
		this.activity = activity;
	}

	protected abstract void submit(Stop stop);

	protected void addEnterListener(EditText stopNameField, final Stop stop) {
		stopNameField.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					dismiss();
					submit(stop);
				}
				return true;
			}
		});
	}

	protected void setInitialName(final Stop stop, EditText stopNameField) {
		String name = stop.getNameByUser() != null ? stop.getNameByUser()
				: stop.getName();
		stopNameField.setText(name);
	}

	protected void setButtons(final Stop stop, EditText stopName,
			int submitButtonId) {
		setButton(BUTTON_POSITIVE, activity.getString(submitButtonId),
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
			submit(stop);
		}
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
