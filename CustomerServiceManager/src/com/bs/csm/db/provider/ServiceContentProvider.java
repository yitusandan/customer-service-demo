package com.bs.csm.db.provider;

import com.bs.csm.Const;
import com.bs.csm.db.ServiceTable;
import com.bs.csm.db.DBHelper;
import com.bs.csm.db.SelectionBuilder;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ServiceContentProvider extends ContentProvider {

	public static final String TAG = "ServiceContentProvider";
	
	public static final UriMatcher URI_MATCHER = buildUriMatcher();
	public static final String PATH = "service";
	public static final int PATH_TOKEN = 101;
	public static final String PATH_FOR_ID = "service/*";
	public static final int PATH_FOR_ID_TOKEN = 201;

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
//		final String authority = Const.CONTENT_AUTHORITY;
		final String authority = ServiceContract.AUTHORITY;
		matcher.addURI(authority, PATH, PATH_TOKEN);
		matcher.addURI(authority, PATH_FOR_ID, PATH_FOR_ID_TOKEN);
		return matcher;
	}

	private DBHelper mDBHelper;

	@Override
	public boolean onCreate() {
		mDBHelper = DBHelper.getInstance(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "uri = " + uri.toString());
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		SelectionBuilder builder = new SelectionBuilder();
		final int match = URI_MATCHER.match(uri);
		switch (match) {
		case PATH_TOKEN: {
			Cursor c = builder.table(ServiceTable.TABLE_NAME)
					.where(selection, selectionArgs).query(db, null, null);
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		}
		case PATH_FOR_ID_TOKEN: {
			String id = uri.getLastPathSegment();
			return builder.table(ServiceTable.TABLE_NAME)
					.where(ServiceTable.COLUMN_ID + "=?", id)
					.query(db, null, null);
		}
		default:
			return null;
		}
	}

	@Override
	public String getType(Uri uri) {
		final int match = URI_MATCHER.match(uri);
		switch (match) {
		case PATH_TOKEN:
			return ServiceContract.CONTENT_TYPE_DIR;
		case PATH_FOR_ID_TOKEN:
			return ServiceContract.CONTENT_TYPE_ITEM;
		default:
			throw new UnsupportedOperationException("URI " + uri
					+ " is not supported.");
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int token = URI_MATCHER.match(uri);
		switch (token) {
		case PATH_TOKEN: {
			long id = db.insert(ServiceTable.TABLE_NAME, null, values);
			if (id != -1)
				getContext().getContentResolver().notifyChange(uri, null);
			Uri result = Uri.parse(ServiceContract.CONTENT_URI + "/" + id);
			getContext().getContentResolver().notifyChange(uri, null, false);
			return result;
		}
		default: {
			throw new UnsupportedOperationException("URI: " + uri
					+ " not supported.");
		}
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int token = URI_MATCHER.match(uri);
		SelectionBuilder builder = new SelectionBuilder();
		int count = -1;
		switch (token) {
		case (PATH_TOKEN):
			count = builder.table(ServiceTable.TABLE_NAME)
					.where(selection, selectionArgs).delete(db);
			break;
		case (PATH_FOR_ID_TOKEN):
			String id = uri.getLastPathSegment();
			count = builder.table(ServiceTable.TABLE_NAME)
					.where(ServiceTable.COLUMN_ID + "=?", id)
					.where(selection, selectionArgs).delete(db);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		// Notifying the changes, if there are any
		if (count != -1)
			getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SelectionBuilder builder = new SelectionBuilder();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int uriMatch = URI_MATCHER.match(uri);
		int count;
		switch (uriMatch) {
		case PATH_TOKEN:
			count = builder.table(ServiceTable.TABLE_NAME)
					.where(selection, selectionArgs).update(db, values);
			break;
		case PATH_FOR_ID_TOKEN:
			String id = uri.getLastPathSegment();
			count = builder.table(ServiceTable.TABLE_NAME)
					.where(ServiceTable.COLUMN_ID + "=?", id)
					.where(selection, selectionArgs).update(db, values);
			break;
		default:
			throw new UnsupportedOperationException("Unknow uri:" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null, false);
		return count;
	}

}
