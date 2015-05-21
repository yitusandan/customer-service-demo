package com.bs.csm.db.provider;

import com.bs.csm.Const;
import com.bs.csm.db.CSRelationTable;
import com.bs.csm.db.CustomerTable;
import com.bs.csm.db.DBHelper;
import com.bs.csm.db.SelectionBuilder;
import com.bs.csm.db.ServiceTable;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class CSContentProvider extends ContentProvider {

	private static final String TAG = "CSContentProvider";

	public static final UriMatcher mUriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	private static final String CUSTOMER_PATH = "customer";
	private static final int CUSTOMER_CODE = 100;

	private static final String CUSTOMER_PATH_ITEM = "customer/*";
	private static final int CUSTOMER_CODE_ITEM = 101;

	private static final String SERVICE_PATH = "service";
	private static final int SERVICE_CODE = 200;

	private static final String SERVICE_PATH_ITEM = "service/*";
	private static final int SERVICE_CODE_ITEM = 201;

	private static final String CSRELATION_PATH = "csrelation";
	private static final int CSRELATION_CODE = 300;
	private static final String CSRELATION_PATH_ITEM = "csrelation/*";
	private static final int CSRELATION_CODE_ITEM = 301;

	private static final String AUTHORITY = Const.CONTENT_AUTHORITY;
	static {
		mUriMatcher.addURI(AUTHORITY, CUSTOMER_PATH, CUSTOMER_CODE);
		mUriMatcher.addURI(AUTHORITY, CUSTOMER_PATH_ITEM, CUSTOMER_CODE_ITEM);

		mUriMatcher.addURI(AUTHORITY, SERVICE_PATH, SERVICE_CODE);
		mUriMatcher.addURI(AUTHORITY, SERVICE_PATH_ITEM, SERVICE_CODE_ITEM);

		mUriMatcher.addURI(AUTHORITY, CSRELATION_PATH, CSRELATION_CODE);
		mUriMatcher.addURI(AUTHORITY, CSRELATION_PATH_ITEM,
				CSRELATION_CODE_ITEM);
	}

	private DBHelper mDBHelper;

	@Override
	public boolean onCreate() {
		mDBHelper = DBHelper.getInstance(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		int code = mUriMatcher.match(uri);
		switch (code) {
		case CUSTOMER_CODE:
			return CustomerContract.CONTENT_TYPE_DIR;
		case CUSTOMER_CODE_ITEM:
			return CustomerContract.CONTENT_TYPE_ITEM;
		case SERVICE_CODE:
			return ServiceContract.CONTENT_TYPE_DIR;
		case SERVICE_CODE_ITEM:
			return ServiceContract.CONTENT_TYPE_ITEM;
		case CSRELATION_CODE:
			return CSRelationContract.CONTENT_TYPE_DIR;
		case CSRELATION_CODE_ITEM:
			return CSRelationContract.CONTENT_TYPE_ITEM;
		default:
			break;
		}

		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		SelectionBuilder builder = new SelectionBuilder();
		int code = mUriMatcher.match(uri);
		Cursor cursor;
		String id;
		switch (code) {
		case CUSTOMER_CODE:
			cursor = builder.table(CustomerTable.TABLE_NAME)
					.where(selection, selectionArgs).query(db, null, null);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			return cursor;
		case SERVICE_CODE:
			cursor = builder.table(ServiceTable.TABLE_NAME)
					.where(selection, selectionArgs).query(db, null, null);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			return cursor;
		case CSRELATION_CODE:
			cursor = builder.table(CSRelationTable.TABLE_NAME)
					.where(selection, selectionArgs).query(db, null, null);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			return cursor;

		case CUSTOMER_CODE_ITEM:
			id = uri.getLastPathSegment();
			return builder.table(CustomerTable.TABLE_NAME)
					.where(CustomerTable.COLUMN_ID + "=?", id)
					.query(db, null, null);
		case SERVICE_CODE_ITEM:
			id = uri.getLastPathSegment();
			return builder.table(ServiceTable.TABLE_NAME)
					.where(ServiceTable.COLUMN_ID + "=?", id)
					.query(db, null, null);
		case CSRELATION_CODE_ITEM:
			id = uri.getLastPathSegment();
			return builder.table(CSRelationTable.TABLE_NAME)
					.where(CSRelationTable.COLUMN_ID + "=?", id)
					.query(db, null, null);
		default:
			break;
		}

		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int code = mUriMatcher.match(uri);
		Uri result = null;
		long id = -1;
		switch (code) {
		case CUSTOMER_CODE:
			id = db.insert(CustomerTable.TABLE_NAME, null, values);
			if (id != -1) {
				result = Uri.parse(CustomerContract.CONTENT_URI + "/" + id);
			}
			break;
		case SERVICE_CODE:
			id = db.insert(ServiceTable.TABLE_NAME, null, values);
			if (id != -1) {
				result = Uri.parse(ServiceContract.CONTENT_URI + "/" + id);
			}
			break;
		case CSRELATION_CODE:
			id = db.insert(CSRelationTable.TABLE_NAME, null, values);
			if (id != -1) {
				result = Uri.parse(CSRelationContract.CONTENT_URI + "/" + id);
			}
			break;
		default:
			Log.d("insert", "default");
			return null;
		}
		if (uri != null) {
			getContext().getContentResolver().notifyChange(uri, null, false);
		}
		return result;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
