package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopCollectionDao;
import com.karhatsu.suosikkipysakit.domain.StopCollection;

import java.util.List;

public class AddToCollectionDialog extends AlertDialog implements AdapterView.OnItemSelectedListener {
	public static final int NO_COLLECTION_ID = 0;

	private OnStopEditCancel onCancel;
	private StopCollection stopCollection;

	public AddToCollectionDialog(Activity activity, OnStopEditCancel onCancel, long stopId) {
		super(activity);
		this.onCancel = onCancel;
		setTitle(R.string.dialog_add_to_collection_title);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_add_to_collection, null);
		setView(view);
		createCollectionsSpinner(view);
		EditText collectionNameField = (EditText) view.findViewById(R.id.dialog_new_collection_name);
		setButtons(collectionNameField, stopId);
	}

	private void createCollectionsSpinner(View view) {
		Spinner collectionsSpinner = (Spinner) view.findViewById(R.id.dialog_existing_collection_id);
		collectionsSpinner.setOnItemSelectedListener(this);
		List<StopCollection> names = new StopCollectionDao(getContext()).findAll();
		if (names.isEmpty()) {
			collectionsSpinner.setVisibility(View.INVISIBLE);
		} else {
			StopCollection noStopCollection = new StopCollection(NO_COLLECTION_ID,
					getContext().getString(R.string.dialog_add_to_collection_spinner_default));
			names.add(0, noStopCollection);
		}
		collectionsSpinner.setAdapter(createStopCollectionArrayAdapter(names));
	}

	private ArrayAdapter<StopCollection> createStopCollectionArrayAdapter(List<StopCollection> names) {
		ArrayAdapter<StopCollection> adapter = new ArrayAdapter<StopCollection>(getContext(),
				android.R.layout.simple_spinner_item, names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	private void setButtons(EditText collectionNameField, long stopId) {
		setButton(BUTTON_POSITIVE, getContext().getString(R.string.save), new SaveButtonListener(collectionNameField, stopId));
		setButton(BUTTON_NEGATIVE, getContext().getString(R.string.cancel), new CancelButtonListener());
	}

	private boolean isExistingStopSelected() {
		return stopCollection != null && stopCollection.getId() != NO_COLLECTION_ID;
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		stopCollection = (StopCollection) adapterView.getItemAtPosition(position);
		EditText newCollectionNameEditText = (EditText) findViewById(R.id.dialog_new_collection_name);
		if (isExistingStopSelected()) {
			newCollectionNameEditText.setVisibility(View.INVISIBLE);
		} else {
			newCollectionNameEditText.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {
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
			if (isExistingStopSelected()) {
				addToSelectedCollection(stopId);
			} else {
				createCollection(collectionName.getText(), stopId);
			}
		}

		private void createCollection(Editable text, long stopId) {
			new StopCollectionDao(getContext()).saveCollectionAndAddStop(text.toString(), stopId);
		}

		private void addToSelectedCollection(long stopId) {
			new StopCollectionDao(getContext()).insertStopToCollection(stopCollection.getId(), stopId);
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
