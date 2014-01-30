package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopCollectionDao;

public class AddToCollectionDialog extends AlertDialog {
	private OnStopEditCancel onCancel;

	public AddToCollectionDialog(Activity activity, OnStopEditCancel onCancel, long stopId) {
		super(activity);
		this.onCancel = onCancel;
		setTitle(R.string.dialog_add_to_collection_title);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_add_to_collection, null);
		setView(view);
		EditText collectionNameField = (EditText) view.findViewById(R.id.dialog_new_collection_name);
		setButtons(collectionNameField, stopId);
	}

	protected void setButtons(EditText collectionNameField, long stopId) {
		setButton(BUTTON_POSITIVE, getContext().getString(R.string.save), new SaveButtonListener(collectionNameField, stopId));
		setButton(BUTTON_NEGATIVE, getContext().getString(R.string.cancel), new CancelButtonListener());
	}

	private class SaveButtonListener implements DialogInterface.OnClickListener {
		private EditText collectionName;
		private long stopId;

		private SaveButtonListener(EditText collectionName, long stopId) {
			this.collectionName = collectionName;
			this.stopId = stopId;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			createCollection(collectionName.getText(), stopId);
		}

		private void createCollection(Editable text, long stopId) {
			new StopCollectionDao(getContext()).saveCollectionAndAddStop(text.toString(), stopId);
		}
	}

	private class CancelButtonListener implements
			DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			onCancel.stopEditCancelled();
			dialog.dismiss();
		}
	}
}
