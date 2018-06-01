package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class RenameStopDialog extends SaveStopDialog {

	private final long id;

	public RenameStopDialog(OnStopEditCancel onStopEditCancel,
			Activity activity, final long stopId) {
		super(activity, onStopEditCancel);
		this.id = stopId;
		Stop stop = new StopDao(activity).findById(stopId);
		setTitle(R.string.dialog_rename_stop_title);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_rename_stop, null);
		setView(view);
		EditText stopNameField = (EditText) view
				.findViewById(R.id.dialog_rename_stop_name);
		addEnterListener(stopNameField, stop);
		setInitialName(stop, stopNameField);
		setButtons(stop, stopNameField, R.string.dialog_rename_stop_save);
	}

	@Override
	protected void submit(Stop stop) {
		new StopDao(activity).updateName(id, stop.getNameByUser());
		if (activity instanceof MainActivity) {
			((MainActivity) activity).refreshStopList();
		}
	}
}
