package com.bs.csm.net;

import java.net.CookieManager;
import java.net.CookiePolicy;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

public class OkHttp {

	private static OkHttpClient client = new OkHttpClient();

	public static OkHttpClient init(Context context) {
		client.setCookieHandler(new CookieManager(new PersistentCookieStore(
				context), CookiePolicy.ACCEPT_ALL));
		return client;
	}

	public static OkHttpClient getClient() {
		return client;
	}

}
