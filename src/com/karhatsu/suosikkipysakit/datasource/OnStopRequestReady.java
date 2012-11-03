package com.karhatsu.suosikkipysakit.datasource;

import com.karhatsu.suosikkipysakit.domain.Stop;

public interface OnStopRequestReady {

	void notifyStopRequested(Stop stop);

}
