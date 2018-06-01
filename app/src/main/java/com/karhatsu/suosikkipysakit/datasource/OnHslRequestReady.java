package com.karhatsu.suosikkipysakit.datasource;

import android.content.Context;

import java.util.ArrayList;

public interface OnHslRequestReady<R> {

	void notifyAboutResult(ArrayList<R> result);

	void notifyConnectionProblem();

	Context getContext();

}
