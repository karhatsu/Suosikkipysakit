package com.karhatsu.suosikkipysakit.datasource;

import android.content.Context;

import com.karhatsu.suosikkipysakit.domain.Stop;

public interface OnStopRequestReady {

	void notifyStopRequested(Stop stop);

	void notifyConnectionProblem();

	Context getContext();

}
