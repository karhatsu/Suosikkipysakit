package com.karhatsu.suosikkipysakit.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.datasource.LinesRequest;
import com.karhatsu.suosikkipysakit.datasource.OnHslRequestReady;
import com.karhatsu.suosikkipysakit.datasource.StopRequest;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Line;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class AddStopActivity extends Activity {

	private ProgressDialog progressDialog;

	private StopRequest stopRequest;
	private StopRequestNotifier stopRequestNotifier = new StopRequestNotifier();

	private LinesRequest linesRequest;
	private LinesRequestNotifier linesRequestNotifier = new LinesRequestNotifier();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_stop);
		Object retained = getLastNonConfigurationInstance();
		if (retained instanceof StopRequest) {
			stopRequest = (StopRequest) retained;
			stopRequest.setOnHslRequestReady(stopRequestNotifier);
		} else if (retained instanceof LinesRequest) {
			linesRequest = (LinesRequest) retained;
			linesRequest.setOnHslRequestReady(linesRequestNotifier);
		} else {
			initializeRequests();
		}
	}

	private void initializeRequests() {
		stopRequest = new StopRequest(stopRequestNotifier);
		linesRequest = new LinesRequest(linesRequestNotifier);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (stopRequest.isRunning()) {
			stopRequest.setOnHslRequestReady(null);
			return stopRequest;
		} else {
			linesRequest.setOnHslRequestReady(null);
			return linesRequest;
		}
	}

	public void searchStop(View button) {
		if (stopRequest.isRunning()) {
			return; // prevent double-clicks
		}
		String code = getTextFromField(R.id.add_stop_code);
		if (!Stop.isValidCode(code)) {
			ToastHelper
					.showToast(this, R.string.activity_add_stop_invalid_code);
			return;
		}
		showPleaseWait();
		stopRequest.execute(code);
	}

	public void searchLine(View button) {
		if (linesRequest.isRunning()) {
			return;
		}
		String line = getTextFromField(R.id.add_stop_line);
		showPleaseWait();
		linesRequest.execute(line);
	}

	private void showPleaseWait() {
		progressDialog = new PleaseWaitDialog(this);
		progressDialog.show();
	}

	private String getTextFromField(int textFieldId) {
		return ((EditText) findViewById(textFieldId)).getText().toString();
	}

	private void showSaveDialog(final Stop stop) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_save_stop, null);
		builder.setView(view);
		final EditText stopName = (EditText) view
				.findViewById(R.id.dialog_save_stop_name);
		stopName.setText(stop.getName());
		builder.setTitle(R.string.dialog_save_stop_title);
		builder.setPositiveButton(R.string.dialog_save_stop_save,
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
		new StopDao(this).save(stop);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private void hideProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	private void afterConnectionProblem() {
		hideProgressDialog();
		ToastHelper
				.showToast(AddStopActivity.this, R.string.connection_problem);
		initializeRequests();
	}

	private class StopRequestNotifier implements OnHslRequestReady<Stop> {
		@Override
		public void notifyAboutResult(Stop stop) {
			hideProgressDialog();
			if (stop != null) {
				showSaveDialog(stop);
			} else {
				ToastHelper.showToast(AddStopActivity.this,
						R.string.activity_add_stop_stop_not_found);
				initializeRequests();
			}
		}

		@Override
		public void notifyConnectionProblem() {
			afterConnectionProblem();
		}

		@Override
		public Context getContext() {
			return AddStopActivity.this;
		}
	}

	private class LinesRequestNotifier implements
			OnHslRequestReady<ArrayList<Line>> {
		@Override
		public void notifyAboutResult(ArrayList<Line> lines) {
			hideProgressDialog();
			if (lines != null) {
				Log.d("result", lines.toString());
			} else {
				ToastHelper.showToast(AddStopActivity.this,
						R.string.activity_add_stop_line_not_found);
				initializeRequests();
			}
		}

		@Override
		public void notifyConnectionProblem() {
			afterConnectionProblem();
		}

		@Override
		public Context getContext() {
			return AddStopActivity.this;
		}
	}
}
