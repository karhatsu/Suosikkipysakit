package com.karhatsu.suosikkipysakit.datasource.parsers;

import org.json.JSONException;

import com.karhatsu.suosikkipysakit.datasource.DataNotFoundException;

import java.util.ArrayList;

public interface JSONParser<T> {

	ArrayList<T> parse(String json) throws DataNotFoundException, JSONException;

}
