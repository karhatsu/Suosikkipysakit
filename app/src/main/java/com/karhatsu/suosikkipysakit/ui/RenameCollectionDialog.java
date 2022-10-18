package com.karhatsu.suosikkipysakit.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopCollectionDao;
import com.karhatsu.suosikkipysakit.domain.StopCollection;

public class RenameCollectionDialog extends DialogFragment {

    public static final String COLLECTION_ID = "collectionId";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_rename_collection, null);
        builder.setView(view);
        builder.setTitle(R.string.dialog_rename_collection_title);
        initTextField(view);
        addPositiveButton(builder, view);
        addNegativeButton(builder);
        return builder.create();
    }

    private void initTextField(View view) {
        EditText collectionNameField = view.findViewById(R.id.dialog_rename_collection_name);
        collectionNameField.setText(findCollection().getName());
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

    private void save(View view) {
        FragmentActivity activity = getActivity();
        StopCollectionDao stopCollectionDao = new StopCollectionDao(activity);
        StopCollection stopCollection = findCollection();
        EditText collectionNameField = view.findViewById(R.id.dialog_rename_collection_name);
        stopCollection.setName(collectionNameField.getText().toString());
        stopCollectionDao.updateName(stopCollection.getId(), stopCollection.getName());
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).refreshStopList();
        }
    }

    private StopCollection findCollection() {
        StopCollectionDao stopCollectionDao = new StopCollectionDao(getActivity());
        long collectionId = getArguments().getLong(COLLECTION_ID);
        return stopCollectionDao.findById(collectionId);
    }
}
