package com.bs.csm;

import android.app.Application;

import com.bs.csm.net.OkHttp;
import com.bs.csm.util.PrefUtils;

public class MyApplication extends Application {

	private static MyApplication instance;

	public static MyApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		Const.userId = PrefUtils.getUserId();
		OkHttp.init(getApplicationContext());
	}

}
