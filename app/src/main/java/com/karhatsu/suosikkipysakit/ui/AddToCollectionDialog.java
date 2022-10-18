package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

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

public class AddToCollectionDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    public static final String STOP_ID = "stopId";
    public static final int NO_COLLECTION_ID = 0;

    private long selectedCollectionId = NO_COLLECTION_ID;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_to_collection, null);
        createCollectionsSpinner(view);
        builder.setView(view);
        builder.setTitle(R.string.dialog_add_to_collection_title);
        addPositiveButton(builder, view);
        addNegativeButton(builder);
        return builder.create();
    }

    private void addPositiveButton(AlertDialog.Builder builder, final View view) {
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save(view);
            }
        });
    }

    private void addNegativeButton(AlertDialog.Builder builder) {
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    private void createCollectionsSpinner(View view) {
        Spinner collectionsSpinner = view.findViewById(R.id.dialog_existing_collection_id);
        collectionsSpinner.setOnItemSelectedListener(this);
        List<StopCollection> names = new StopCollectionDao(view.getContext()).findAll();
        if (names.isEmpty()) {
            collectionsSpinner.setVisibility(View.INVISIBLE);
        } else {
            String noStopName = view.getContext().getString(R.string.dialog_add_to_collection_spinner_default);
            StopCollection noStopCollection = new StopCollection(NO_COLLECTION_ID, noStopName);
            names.add(0, noStopCollection);
        }
        collectionsSpinner.setAdapter(createStopCollectionArrayAdapter(view, names));
    }

    private ArrayAdapter<StopCollection> createStopCollectionArrayAdapter(View view, List<StopCollection> names) {
        ArrayAdapter<StopCollection> adapter = new ArrayAdapter<StopCollection>(view.getContext(),
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private boolean isExistingStopSelected() {
        return selectedCollectionId != NO_COLLECTION_ID;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        selectedCollectionId = ((StopCollection) adapterView.getItemAtPosition(position)).getId();
        EditText newCollectionNameEditText = getDialog().findViewById(R.id.dialog_new_collection_name);
        if (isExistingStopSelected()) {
            newCollectionNameEditText.setVisibility(View.INVISIBLE);
        } else {
            newCollectionNameEditText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void save(View view) {
        long stopId = getArguments().getLong(STOP_ID);
        if (isExistingStopSelected()) {
            new StopCollectionDao(view.getContext()).insertStopToCollection(selectedCollectionId, stopId);
        } else {
            EditText collectionNameField = view.findViewById(R.id.dialog_new_collection_name);
            String collectionName = collectionNameField.getText().toString();
            new StopCollectionDao(view.getContext()).saveCollectionAndAddStop(collectionName, stopId);
        }
        refreshMainActivity();
    }

    private void refreshMainActivity() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).refreshStopList(R.string.dialog_add_to_collection_stop_added);
        }
    }
}
