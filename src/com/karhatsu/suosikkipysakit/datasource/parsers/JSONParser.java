package com.karhatsu.suosikkipysakit.datasource.parsers;

import org.json.JSONException;

import com.karhatsu.suosikkipysakit.datasource.DataNotFoundException;

public interface JSONParser<T> {

	T parse(String json) throws DataNotFoundException, JSONException;

}
