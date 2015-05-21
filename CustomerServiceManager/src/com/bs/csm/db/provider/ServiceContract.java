package com.bs.csm.db.provider;

import android.net.Uri;

import com.bs.csm.Const;

public class ServiceContract {

	public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.csm.service";
	public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.csm.service";

	public static final String AUTHORITY = Const.CONTENT_AUTHORITY;

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/service");

}
