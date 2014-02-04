package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopCollectionDao;
import com.karhatsu.suosikkipysakit.domain.StopCollection;

public class RenameCollectionDialog extends AlertDialog {
	private final OnStopEditCancel onStopEditCancel;
	private final Activity activity;
	private final long collectionId;

	protected RenameCollectionDialog(OnStopEditCancel onStopEditCancel, Activity activity, final long collectionId) {
		super(activity);
		this.onStopEditCancel = onStopEditCancel;
		this.activity = activity;
		this.collectionId = collectionId;
		StopCollection stopCollection = new StopCollectionDao(activity).findById(collectionId);
		setTitle(R.string.dialog_rename_collection_title);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_rename_collection, null);
		setView(view);
		EditText collectionNameField = (EditText) view.findViewById(R.id.dialog_rename_collection_name);
		collectionNameField.setOnEditorActionListener(createEnterListener(collectionNameField, stopCollection));
		collectionNameField.setText(stopCollection.getName());
		setButtons(stopCollection, collectionNameField);
	}

	private TextView.OnEditorActionListener createEnterListener(final EditText collectionNameField,
																final StopCollection stopCollection) {
		return new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
					dismiss();
					submit(collectionNameField, stopCollection);
				}
				return true;
			}
		};
	}

	private void setButtons(final StopCollection stopCollection, EditText collectionNameField) {
		setButton(BUTTON_POSITIVE, activity.getString(R.string.save), new SaveButtonListener(collectionNameField, stopCollection));
		setButton(BUTTON_NEGATIVE, activity.getString(R.string.cancel), new CancelButtonListener());
	}

	private class CancelButtonListener implements OnClickListener {
		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
			onStopEditCancel.stopEditCancelled();
		}
	}

	private class SaveButtonListener implements OnClickListener {
		private final EditText collectionNameField;
		private final StopCollection stopCollection;

		public SaveButtonListener(EditText collectionNameField, StopCollection stopCollection) {
			this.collectionNameField = collectionNameField;
			this.stopCollection = stopCollection;
		}

		@Override
		public void onClick(DialogInterface dialog, int i) {
			dialog.dismiss();
			submit(collectionNameField, stopCollection);
		}
	}

	private void submit(EditText collectionNameField, StopCollection stopCollection) {
		stopCollection.setName(collectionNameField.getText().toString());
		new StopCollectionDao(activity).updateName(collectionId, stopCollection.getName());
		if (activity instanceof MainActivity) {
			((MainActivity) activity).refreshStopList();
		}
	}
}
