package com.karhatsu.omatpysakit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class AddStopActivity extends Activity {

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

	public void saveStop(View button) {
		//
	}

}
