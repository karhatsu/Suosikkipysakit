package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class NewStopDialog extends DialogFragment {

    public static final String STOP = "stop";
    private Button positiveButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_stop, null);
        builder.setView(view);
        builder.setTitle(R.string.dialog_new_stop_title);
        initTextField(view);
        addPositiveButton(builder, view);
        addNegativeButton(builder);
        setCheckBoxListener(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
    }

    private void initTextField(View view) {
        EditText stopNameField = view.findViewById(R.id.dialog_new_stop_name);
        stopNameField.setText(getStop().getName());
    }

    private void addPositiveButton(AlertDialog.Builder builder, final View view) {
        builder.setPositiveButton(R.string.dialog_new_stop_save, new DialogInterface.OnClickListener() {
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

    private void setCheckBoxListener(final View view) {
        final CheckBox noSaveCheckBox = view.findViewById(R.id.dialog_new_stop_no_save);
        noSaveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View stopNameField = view.findViewById(R.id.dialog_new_stop_name);
                boolean noSave = noSaveCheckBox.isChecked();
                stopNameField.setVisibility(noSave ? View.INVISIBLE : View.VISIBLE);
                positiveButton.setText(noSave ? R.string.dialog_new_stop_continue
                        : R.string.dialog_new_stop_save);
            }
        });
    }

    private void save(View view) {
        CheckBox noSaveCheckBox = view.findViewById(R.id.dialog_new_stop_no_save);
        Stop stop = getStop();
        if (noSaveCheckBox.isChecked()) {
            showDepartures(stop);
        } else {
            EditText stopNameField = view.findViewById(R.id.dialog_new_stop_name);
            stop.setNameByUser(stopNameField.getText().toString());
            new StopDao(getActivity()).save(stop);
            showMainActivity(stop);
        }
    }

    private void showDepartures(Stop stop) {
        showStopActivity(stop, DeparturesActivity.class);
    }

    private void showMainActivity(Stop stop) {
        showStopActivity(stop, MainActivity.class);
    }

    private Stop getStop() {
        return getArguments().getParcelable(STOP);
    }

    private void showStopActivity(Stop stop, Class<? extends Activity> activityClass) {
        FragmentActivity activity = getActivity();
        Intent intent = new Intent(activity, activityClass);
        if (stop.getDepartures() != null) {
            intent.putExtra(Stop.STOP_KEY, stop);
        } else {
            intent.putExtra(Stop.CODE_KEY, stop.getCode());
        }
        activity.startActivity(intent);
    }
}
