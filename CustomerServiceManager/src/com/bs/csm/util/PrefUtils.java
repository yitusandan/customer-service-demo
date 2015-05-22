package com.bs.csm.util;

import com.bs.csm.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

	private static final String PREF_NAME = "bs_pref";

	private static final String KEY_USER_ID = "userId";

	public static SharedPreferences getPref() {
		return MyApplication.getInstance().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
	}

	public static void setUserId(int userId) {
		getPref().edit().putInt(KEY_USER_ID, userId).commit();
	}

	public static int getUserId() {
		return getPref().getInt(KEY_USER_ID, -1);
	}

}
