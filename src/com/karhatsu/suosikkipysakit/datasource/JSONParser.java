package com.karhatsu.suosikkipysakit.datasource;

import org.json.JSONException;

public interface JSONParser<T> {

	T parse(String json) throws DataNotFoundException, JSONException;

}
