package com.karhatsu.suosikkipysakit.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.datasource.LinesRequest;
import com.karhatsu.suosikkipysakit.datasource.OnHslRequestReady;
import com.karhatsu.suosikkipysakit.datasource.StopRequest;
import com.karhatsu.suosikkipysakit.domain.Line;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class AddStopActivity extends Activity implements OnStopSaveCancel {

	private ProgressDialog progressDialog;

	private StopRequest stopRequest;
	private StopRequestNotifier stopRequestNotifier = new StopRequestNotifier();

	private LinesRequest linesRequest;
	private LinesRequestNotifier linesRequestNotifier = new LinesRequestNotifier();

	private SaveStopDialog saveStopDialog;
	private Stop stopToBeSaved;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_stop);
		addEnterListeners();
		initializeRequests();
		restoreState();
	}

	private void restoreState() {
		Object retained = getLastNonConfigurationInstance();
		if (retained instanceof Stop) {
			stopToBeSaved = (Stop) retained;
			showSaveStopDialog();
		} else if (retained instanceof StopRequest) {
			showPleaseWait();
			stopRequest = (StopRequest) retained;
			stopRequest.setOnHslRequestReady(stopRequestNotifier);
		} else if (retained instanceof LinesRequest) {
			showPleaseWait();
			linesRequest = (LinesRequest) retained;
			linesRequest.setOnHslRequestReady(linesRequestNotifier);
		}
	}

	private void addEnterListeners() {
		getStopCodeField().setOnEditorActionListener(
				createStopCodeEnterListener());
		getLineCodeField().setOnEditorActionListener(
				createLineCodeEnterListener());
	}

	private TextView getStopCodeField() {
		return (TextView) findViewById(R.id.add_stop_code);
	}

	private TextView getLineCodeField() {
		return (TextView) findViewById(R.id.add_stop_line);
	}

	private OnEditorActionListener createStopCodeEnterListener() {
		return new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					View searchButton = findViewById(R.id.add_stop_code_button);
					searchStop(searchButton);
				}
				return true;
			}
		};
	}

	private OnEditorActionListener createLineCodeEnterListener() {
		return new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					View searchButton = findViewById(R.id.add_stop_line_button);
					searchLine(searchButton);
				}
				return true;
			}
		};
	}

	private void initializeRequests() {
		stopRequest = new StopRequest(stopRequestNotifier);
		linesRequest = new LinesRequest(linesRequestNotifier);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (stopToBeSaved != null) {
			return stopToBeSaved;
		} else if (stopRequest != null && stopRequest.isRunning()) {
			stopRequest.setOnHslRequestReady(null);
			return stopRequest;
		} else if (linesRequest != null && linesRequest.isRunning()) {
			linesRequest.setOnHslRequestReady(null);
			return linesRequest;
		}
		return null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (saveStopDialog != null) {
			saveStopDialog.dismiss();
		}
		hideProgressDialog();
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
		if (line.trim().equals("")) {
			ToastHelper.showToast(this, R.string.activity_add_stop_empty_line);
			return;
		} else if (line.trim().contains(" ")) {
			ToastHelper.showToast(this,
					R.string.activity_add_stop_spaces_in_line);
			return;
		}
		showPleaseWait();
		linesRequest.execute(line);
	}

	private void showPleaseWait() {
		if (progressDialog == null) {
			progressDialog = new PleaseWaitDialog(this);
		}
		progressDialog.show();
	}

	private String getTextFromField(int textFieldId) {
		return ((EditText) findViewById(textFieldId)).getText().toString();
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

	private class StopRequestNotifier implements OnHslRequestReady<List<Stop>> {
		@Override
		public void notifyAboutResult(List<Stop> stops) {
			hideProgressDialog();
			initializeRequests();
			if (stops == null || stops.size() == 0) {
				ToastHelper.showToast(AddStopActivity.this,
						R.string.activity_add_stop_stop_not_found);
			} else if (stops.size() == 1) {
				stopToBeSaved = stops.get(0);
				showSaveStopDialog();
			} else {
				ArrayList<Stop> stopsAL = (ArrayList<Stop>) stops;
				Intent intent = new Intent(AddStopActivity.this,
						LineStopsActivity.class);
				intent.putParcelableArrayListExtra(
						LineStopsActivity.LINE_STOPS, stopsAL);
				startActivity(intent);
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

	private void showSaveStopDialog() {
		saveStopDialog = new SaveStopDialog(this, this, stopToBeSaved);
		saveStopDialog.show();
	}

	private class LinesRequestNotifier implements
			OnHslRequestReady<ArrayList<Line>> {
		@Override
		public void notifyAboutResult(ArrayList<Line> lines) {
			hideProgressDialog();
			initializeRequests();
			if (lines != null) {
				Intent intent = new Intent(AddStopActivity.this,
						LinesActivity.class);
				intent.putParcelableArrayListExtra(LinesActivity.LINES_LIST,
						lines);
				startActivity(intent);
			} else {
				ToastHelper.showToast(AddStopActivity.this,
						R.string.activity_add_stop_line_not_found);
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

	@Override
	public void stopSaveCancelled() {
		stopToBeSaved = null;
	}
}
