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
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class RenameStopDialog extends DialogFragment {

    public static final String STOP_ID = "stopId";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_rename_stop, null);
        builder.setView(view);
        builder.setTitle(R.string.dialog_rename_stop_title);
        initTextField(view);
        addPositiveButton(builder, view);
        addNegativeButton(builder);
        return builder.create();
    }

    private void initTextField(View view) {
        EditText stopNameField = view.findViewById(R.id.dialog_rename_stop_name);
        stopNameField.setText(findStop().getVisibleName());
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
        EditText stopNameField = view.findViewById(R.id.dialog_rename_stop_name);
        new StopDao(activity).updateName(getStopId(), stopNameField.getText().toString());
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).refreshStopList();
        }
    }

    private Stop findStop() {
        StopDao stopDao = new StopDao(getActivity());
        return stopDao.findById(getStopId());
    }

    private long getStopId() {
        return getArguments().getLong(STOP_ID);
    }
}
