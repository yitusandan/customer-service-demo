package com.bs.csm.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bs.csm.Const;
import com.bs.csm.model.CSRelation;
import com.bs.csm.model.Service;

public class CSRelationTable {

	public static final String TABLE_NAME = "cs_relation";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USERID = "userid";
	public static final String COLUMN_CUSTOMER_ID = "customer_id";
	public static final String COLUMN_SERVICE_ID = "service_id";

	public static final String CREATE_TABLE = "create table if not exists "
			+ TABLE_NAME + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_USERID
			+ " integer," + COLUMN_CUSTOMER_ID + " integer not null,"
			+ COLUMN_SERVICE_ID + " integer not null" + ");";

	public static final String DROP_TABLE = "drop table if exists "
			+ TABLE_NAME;

	private DBHelper mDBHelper;

	public CSRelationTable(Context context) {
		mDBHelper = DBHelper.getInstance(context);
	}

	public void insert(long customerId, List<Service> serviceList) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (Service service : serviceList) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_USERID, Const.userId);
				values.put(COLUMN_CUSTOMER_ID, customerId);
				values.put(COLUMN_SERVICE_ID, service.getId());
				db.insert(TABLE_NAME, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static ContentValues getValues(long customerId, long serviceId) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_CUSTOMER_ID, customerId);
		values.put(COLUMN_SERVICE_ID, serviceId);
		return values;
	}

	public void insert2(long customerId, List<Long> list) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (Long l : list) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_USERID, Const.userId);
				values.put(COLUMN_CUSTOMER_ID, customerId);
				values.put(COLUMN_SERVICE_ID, l);
				db.insert(TABLE_NAME, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void delete(long customerId, List<Long> list) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (Long l : list) {
				db.delete(
						TABLE_NAME,
						COLUMN_CUSTOMER_ID + " = ? and " + COLUMN_SERVICE_ID
								+ " = ?",
						new String[] { String.valueOf(customerId),
								String.valueOf(l) });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public List<CSRelation> queryByCustomerId(long customerId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CUSTOMER_ID + " = ?",
				new String[] { String.valueOf(customerId) }, null, null, null);
		List<CSRelation> relationList = new ArrayList<CSRelation>();
		while (cursor.moveToNext()) {
			CSRelation csRelation = getRelation(cursor);
			relationList.add(csRelation);
		}
		cursor.close();
		return relationList;
	}

	public static CSRelation getRelation(Cursor cursor) {
		CSRelation relation = new CSRelation();
		relation.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
		relation.setCustomerid(cursor.getLong(cursor
				.getColumnIndex(COLUMN_CUSTOMER_ID)));
		relation.setServiceid(cursor.getLong(cursor
				.getColumnIndex(COLUMN_SERVICE_ID)));
		return relation;
	}

	public int deleteByCustomerId(long customerId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		return db.delete(TABLE_NAME, COLUMN_CUSTOMER_ID + " = ? ",
				new String[] { String.valueOf(customerId) });
	}

	public int deleteByServiceId(long serviceId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		return db.delete(TABLE_NAME, COLUMN_SERVICE_ID + " = ? ",
				new String[] { String.valueOf(serviceId) });
	}
}
