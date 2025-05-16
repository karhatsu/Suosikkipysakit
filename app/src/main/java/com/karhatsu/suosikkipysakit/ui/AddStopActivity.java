package com.karhatsu.suosikkipysakit.ui;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.TextView.OnEditorActionListener;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.datasource.LinesRequest;
import com.karhatsu.suosikkipysakit.datasource.OnHslRequestReady;
import com.karhatsu.suosikkipysakit.datasource.StopRequest;
import com.karhatsu.suosikkipysakit.db.PreviousCityDao;
import com.karhatsu.suosikkipysakit.domain.City;
import com.karhatsu.suosikkipysakit.domain.Line;
import com.karhatsu.suosikkipysakit.domain.Stop;

public class AddStopActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

	private ProgressDialog progressDialog;

	private StopRequest stopRequest;
	private StopRequestNotifier stopRequestNotifier = new StopRequestNotifier();

	private LinesRequest linesRequest;
	private LinesRequestNotifier linesRequestNotifier = new LinesRequestNotifier();

	private Stop stopToBeSaved;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_stop);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ToolbarUtil.setToolbarPadding(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		createCitySpinner();
		addSearchButtonListeners();
		initializeRequests();
		restoreState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSelectedCity();
	}

	private void restoreState() {
		Object retained = getLastNonConfigurationInstance();
		if (retained instanceof Stop) {
			stopToBeSaved = (Stop) retained;
			showSaveStopDialog();
		} else if (retained instanceof StopRequest) {
			showPleaseWaitForStop();
			stopRequest = (StopRequest) retained;
			stopRequest.setOnHslRequestReady(stopRequestNotifier);
		} else if (retained instanceof LinesRequest) {
			showPleaseWaitForLine();
			linesRequest = (LinesRequest) retained;
			linesRequest.setOnHslRequestReady(linesRequestNotifier);
		}
	}

	private void createCitySpinner() {
		Spinner citySpinner = getCitySpinner();
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cities,
			android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		citySpinner.setAdapter(adapter);
		citySpinner.setOnItemSelectedListener(this);
	}

	private void setSelectedCity() {
		String selectedCity = new PreviousCityDao(this).findCity().getName();
		Spinner citySpinner = getCitySpinner();
		ArrayAdapter<CharSequence> adapter = getCitySpinnerAdapter(citySpinner);
		int position = adapter.getPosition(selectedCity);
		citySpinner.setSelection(position);
	}

	private ArrayAdapter<CharSequence> getCitySpinnerAdapter() {
		return getCitySpinnerAdapter(getCitySpinner());
	}

	@SuppressWarnings("unchecked")
	private ArrayAdapter<CharSequence> getCitySpinnerAdapter(Spinner citySpinner) {
		return (ArrayAdapter<CharSequence>) citySpinner.getAdapter();
	}

	private Spinner getCitySpinner() {
		return (Spinner) findViewById(R.id.city_spinner);
	}

	private void addSearchButtonListeners() {
		getStopCodeField().setOnEditorActionListener(createStopCodeSearchListener());
		getLineCodeField().setOnEditorActionListener(createLineCodeSearchListener());
	}

	private TextView getStopCodeField() {
		return (TextView) findViewById(R.id.add_stop_code);
	}

	private TextView getLineCodeField() {
		return (TextView) findViewById(R.id.add_stop_line);
	}

	private OnEditorActionListener createStopCodeSearchListener() {
		return new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchStop(null);
					return true;
				}
				return false;
			}
		};
	}

	private OnEditorActionListener createLineCodeSearchListener() {
		return new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchLine(null);
					return true;
				}
				return false;
			}
		};
	}

	private void initializeRequests() {
		stopRequest = new StopRequest(stopRequestNotifier);
		linesRequest = new LinesRequest(linesRequestNotifier);
	}

	/*@Override
	public Object onRetainNonConfigurationInstance() {
		if (stopRequest != null && stopRequest.isRunning()) {
			stopRequest.setOnHslRequestReady(null);
			return stopRequest;
		} else if (linesRequest != null && linesRequest.isRunning()) {
			linesRequest.setOnHslRequestReady(null);
			return linesRequest;
		}
		return null;
	}*/

	@Override
	protected void onPause() {
		super.onPause();
		hideProgressDialog();
	}

	public void searchStop(View button) {
		if (stopRequest.isRunning()) {
			return; // prevent double-clicks
		}
		String code = getTextFromField(R.id.add_stop_code);
		CharSequence selectedCity = getCitySpinnerAdapter().getItem(getCitySpinner().getSelectedItemPosition());
		code = getCityPrefix(selectedCity) + code;
		if (!Stop.isValidCode(code)) {
			ToastHelper
					.showToast(this, R.string.activity_add_stop_invalid_code);
			return;
		}
		showPleaseWaitForStop();
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
		}
		showPleaseWaitForLine();
		linesRequest.execute(line);
	}

	private void showPleaseWaitForStop() {
		showPleaseWait(R.string.activity_add_stop_code_wait_dialog_title);
	}

	private void showPleaseWaitForLine() {
		showPleaseWait(R.string.activity_add_stop_line_wait_dialog_title);
	}

	private void showPleaseWait(int dialogTitleId) {
		if (progressDialog == null) {
			progressDialog = new PleaseWaitDialog(this, dialogTitleId);
		} else {
			progressDialog.setTitle(dialogTitleId);
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

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		CharSequence selectedCity = (CharSequence) adapterView.getItemAtPosition(position);
		storePreviousCitySelection(selectedCity);
	}

	private void storePreviousCitySelection(CharSequence selectedCity) {
		new PreviousCityDao(this).save(City.getByName(selectedCity.toString()));
	}

	private String getCityPrefix(CharSequence selectedCity) {
		return City.getPrefixByName(selectedCity.toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {
	}

	private class StopRequestNotifier implements OnHslRequestReady<Stop> {
		@Override
		public void notifyAboutResult(ArrayList<Stop> stops) {
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
		DialogFragment dialogFragment = new NewStopDialog();
		Bundle args = new Bundle();
		args.putParcelable(NewStopDialog.STOP, stopToBeSaved);
		dialogFragment.setArguments(args);
		dialogFragment.show(getSupportFragmentManager(), "newStop");
	}

	private class LinesRequestNotifier implements OnHslRequestReady<Line> {
		@Override
		public void notifyAboutResult(ArrayList<Line> lines) {
			hideProgressDialog();
			initializeRequests();
			if (lines == null || lines.isEmpty()) {
				ToastHelper.showToast(AddStopActivity.this,
						R.string.activity_add_stop_line_not_found);
			} else {
				Intent intent = new Intent(AddStopActivity.this,
						LinesActivity.class);
				intent.putParcelableArrayListExtra(LinesActivity.LINES_LIST,
						lines);
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
}
