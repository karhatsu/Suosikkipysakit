package com.karhatsu.omatpysakit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.karhatsu.omatpysakit.R;
import com.karhatsu.omatpysakit.datasource.OnStopRequestReady;
import com.karhatsu.omatpysakit.datasource.StopRequest;
import com.karhatsu.omatpysakit.db.StopDao;
import com.karhatsu.omatpysakit.domain.Stop;

public class AddStopActivity extends Activity implements OnStopRequestReady {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_stop);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_stop, menu);
		return true;
	}

	public void searchStop(View button) {
		String code = getCode();
		if (!Stop.isValidCode(code)) {
			showToast(R.string.add_stop_invalid_code);
			return;
		}
		new StopRequest(this).execute(Integer.valueOf(getCode()));
	}

	private void showToast(int resourceId) {
		Toast.makeText(this, resourceId, Toast.LENGTH_SHORT).show();
	}

	private String getCode() {
		return ((EditText) findViewById(R.id.add_stop_code)).getText()
				.toString();
	}

	private void showSaveDialog(final Stop stop) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_save_stop, null);
		builder.setView(view);
		final EditText stopName = (EditText) view
				.findViewById(R.id.dialog_save_stop_name);
		stopName.setText(stop.getNameFi());
		builder.setTitle(R.string.save_stop_title);
		builder.setPositiveButton(R.string.save_stop_save,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						stop.setNameByUser(stopName.getText().toString());
						dialog.dismiss();
						saveStopAndShowAll(stop);
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void saveStopAndShowAll(Stop stop) {
		new StopDao().save(this, stop);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void notifyStopRequested(Stop stop) {
		if (stop != null) {
			showSaveDialog(stop);
		} else {
			showToast(R.string.add_stop_not_found);
		}
	}

}
