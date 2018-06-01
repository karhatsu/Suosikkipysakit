package com.karhatsu.suosikkipysakit.ui;

import android.app.ProgressDialog;
import android.content.Context;

import com.karhatsu.suosikkipysakit.R;

public class PleaseWaitDialog extends ProgressDialog {

	public PleaseWaitDialog(Context context, int titleTextId) {
		super(context);
		setCancelable(false);
		setTitle(titleTextId);
		setMessage(context.getText(R.string.please_wait));
	}

}
