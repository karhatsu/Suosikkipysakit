package com.karhatsu.omatpysakit.ui;

import android.app.ProgressDialog;
import android.content.Context;

import com.karhatsu.omatpysakit.R;

public class PleaseWaitDialog extends ProgressDialog {

	public PleaseWaitDialog(Context context) {
		super(context);
		setCancelable(false);
		setTitle(R.string.please_wait);
	}

}
