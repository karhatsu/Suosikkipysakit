package com.karhatsu.suosikkipysakit.datasource;

import android.content.Context;

public interface OnHslRequestReady<R> {

	void notifyAboutResult(R result);

	void notifyConnectionProblem();

	Context getContext();

}
