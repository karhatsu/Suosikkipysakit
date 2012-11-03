package com.karhatsu.suosikkipysakit.ui;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastHelper {

	public static void showToast(Context context, int resourceId) {
		Toast toast = Toast.makeText(context, resourceId, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, 0, 200);
		toast.show();
	}

}
