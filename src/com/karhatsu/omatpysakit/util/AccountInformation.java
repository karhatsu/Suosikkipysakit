package com.karhatsu.omatpysakit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

import com.karhatsu.omatpysakit.R;

public class AccountInformation {

	private static boolean inited;
	private static String userName;
	private static String password;

	public synchronized static void initialize(Context context) {
		if (inited) {
			return;
		}
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = context.getResources().openRawResource(R.raw.account);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));
			userName = bufferedReader.readLine();
			password = bufferedReader.readLine();
			inited = true;
		} catch (IOException e) {
			throw new RuntimeException(
					"Valid username/password file not found", e);
		} finally {
			closeFileResources(inputStream, bufferedReader);
		}
	}

	private static void closeFileResources(InputStream inputStream,
			BufferedReader bufferedReader) {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		} catch (IOException e) {
		}
	}

	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}
}
