package com.bs.csm.util;

import android.app.ProgressDialog;
import android.content.Context;

public class Utils {

	private static ProgressDialog progressDialog;

	public static void showDialog(Context context) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context,
					ProgressDialog.THEME_HOLO_LIGHT);
		}
		progressDialog.show();
	}

	public static void dismissDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
