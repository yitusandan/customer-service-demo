package com.bs.csm.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bs.csm.Const;
import com.bs.csm.model.Service;

public class ServiceTable {
	private DBHelper mDBHelper;

	public static final String TABLE_NAME = "service";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USERID = "userid";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_DESC = "desc";

	public static final String CREATE_TABLE = "create table if not exists "
			+ TABLE_NAME + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_USERID
			+ " integer," + COLUMN_NAME + " text," + COLUMN_PRICE + " text,"
			+ COLUMN_DESC + " text" + ");";

	public static final String DROP_TABLE = "drop table if exists "
			+ TABLE_NAME;

	public ServiceTable(Context context) {
		mDBHelper = DBHelper.getInstance(context);
	}

	public long insert(Service service) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = getValues(service);
		return db.insert(TABLE_NAME, null, values);
	}

	public static ContentValues getValues(Service service) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_USERID, service.getUserid());
		values.put(COLUMN_NAME, service.getName());
		values.put(COLUMN_PRICE, service.getPrice());
		values.put(COLUMN_DESC, service.getDesc());
		return values;
	}

	public List<Service> queryAll() {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "
				+ COLUMN_USERID + " = ?",
				new String[] { String.valueOf(Const.userId) });
		List<Service> serviceList = new ArrayList<Service>();
		while (cursor.moveToNext()) {
			Service service = getService(cursor);
			serviceList.add(service);
		}
		cursor.close();
		return serviceList;
	}

	public List<Service> queryUnSelected(long customerId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		List<Service> list = new ArrayList<Service>();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "
				+ CSRelationTable.COLUMN_USERID + " = ? and " + COLUMN_ID
				+ " not in (select " + CSRelationTable.COLUMN_SERVICE_ID
				+ " from " + CSRelationTable.TABLE_NAME + " where "
				+ CSRelationTable.COLUMN_CUSTOMER_ID + " = ?)", new String[] {
				String.valueOf(Const.userId), String.valueOf(customerId) });
		while (cursor.moveToNext()) {
			Service service = getService(cursor);
			list.add(service);
		}
		cursor.close();
		return list;
	}

	public static Service getService(Cursor cursor) {
		Service service = new Service();
		service.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
		service.setUserid(cursor.getInt(cursor.getColumnIndex(COLUMN_USERID)));
		service.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
		service.setPrice(cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
		service.setDesc(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
		return service;
	}

	public long update(Service service) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = getValues(service);
		return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
				new String[] { String.valueOf(service.getId()) });
	}

	public int delete(Service service) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		return db.delete(TABLE_NAME, COLUMN_ID + " = ? ",
				new String[] { String.valueOf(service.getId()) });
	}

}
