package com.karhatsu.omatpysakit.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.karhatsu.omatpysakit.R;
import com.karhatsu.omatpysakit.datasource.Stops;
import com.karhatsu.omatpysakit.domain.Stop;

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
		String code = ((EditText) findViewById(R.id.add_stop_code)).getText()
				.toString();
		Stops.get().save(new Stop(code));
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
