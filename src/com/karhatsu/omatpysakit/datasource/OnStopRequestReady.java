package com.karhatsu.omatpysakit.datasource;

import com.karhatsu.omatpysakit.domain.Stop;

public interface OnStopRequestReady {

	void notifyStopRequested(Stop stop);

}
