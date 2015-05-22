package com.bs.csm.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.bs.csm.Const;
import com.bs.csm.db.provider.CustomerContract;
import com.bs.csm.model.Customer;

public class CustomerTable {
	public static final String TABLE_NAME = "customer";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USERID = "userid";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_MOBILE = "mobile";
	public static final String COLUMN_QQ = "qq";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_REMARK = "remark";
	public static final String COLUMN_PINYIN = "pinyin";
	public static final String COLUMN_INITIALS = "initials";

	public static final String CREATE_TABLE = "create table if not exists "
			+ TABLE_NAME + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_USERID
			+ " integer," + COLUMN_NAME + " text not null," + COLUMN_MOBILE
			+ " text not null," + COLUMN_QQ + " text," + COLUMN_ADDRESS
			+ " text," + COLUMN_REMARK + " text," + COLUMN_PINYIN + " text,"
			+ COLUMN_INITIALS + " text" + ");";

	public static final String CREATE_INDEX = "create index mobile_index on customer(mobile)";

	public static final String DROP_TABLE = "drop table if exists "
			+ TABLE_NAME;

	private final DBHelper mDBHelper;

	public CustomerTable(Context context) {
		mDBHelper = DBHelper.getInstance(context);
	}

	public long insert(Customer customer) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = getValues(customer);
		return db.insert(TABLE_NAME, null, values);
	}

	public static ContentValues getValues(Customer customer) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_USERID, customer.getUserid());
		values.put(COLUMN_NAME, customer.getName());
		values.put(COLUMN_MOBILE, customer.getMobile());
		values.put(COLUMN_QQ, customer.getQq());
		values.put(COLUMN_ADDRESS, customer.getAddress());
		values.put(COLUMN_REMARK, customer.getRemark());
		values.put(COLUMN_PINYIN, customer.getPinyin());
		values.put(COLUMN_INITIALS, customer.getInitials());
		return values;
	}

	/**
	 * 模糊查询
	 * 
	 * @param str
	 * @return
	 */
	public List<Customer> query(String str) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		Cursor cursor = null;
		if (TextUtils.isEmpty(str)) {
			cursor = db.query(TABLE_NAME, null, COLUMN_USERID + " = ?",
					new String[] { String.valueOf(Const.userId) }, null, null,
					null);
		} else {
			str = str + "%";
			cursor = db.query(TABLE_NAME, null, COLUMN_USERID + " = ? and"
					+ COLUMN_NAME + " like ? or " + COLUMN_PINYIN + " like ?",
					new String[] { String.valueOf(Const.userId), str, str },
					null, null, null);
		}
		List<Customer> list = new ArrayList<Customer>();
		while (cursor.moveToNext()) {
			Customer customer = getCustomer(cursor);
			list.add(customer);
		}
		cursor.close();
		return list;
	}

	/**
	 * 根据id查询
	 * 
	 * @param customerId
	 * @return
	 */
	public Customer getById(long customerId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "
				+ COLUMN_ID + " = ?",
				new String[] { String.valueOf(customerId) });
		Customer customer = null;
		if (cursor.moveToNext()) {
			customer = getCustomer(cursor);
		}
		cursor.close();
		return customer;
	}

	public static Customer getCustomer(Cursor cursor) {
		Customer customer = new Customer();
		customer.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
		customer.setUserid(cursor.getInt(cursor.getColumnIndex(COLUMN_USERID)));
		customer.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
		customer.setMobile(cursor.getString(cursor
				.getColumnIndex(COLUMN_MOBILE)));
		customer.setQq(cursor.getString(cursor.getColumnIndex(COLUMN_QQ)));
		customer.setAddress(cursor.getString(cursor
				.getColumnIndex(COLUMN_ADDRESS)));
		customer.setRemark(cursor.getString(cursor
				.getColumnIndex(COLUMN_REMARK)));
		customer.setPinyin(cursor.getString(cursor
				.getColumnIndex(COLUMN_PINYIN)));
		customer.setInitials(cursor.getString(cursor
				.getColumnIndex(COLUMN_INITIALS)));
		return customer;
	}

	/**
	 * 更新
	 */
	public int update(Customer customer) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = getValues(customer);
		return db.update(TABLE_NAME, values, COLUMN_ID + " = ? ",
				new String[] { String.valueOf(customer.getId()) });
	}

	/**
	 * 删除
	 * 
	 * @param customerId
	 * @return
	 */
	public int delete(long customerId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		return db.delete(TABLE_NAME, COLUMN_ID + " = ? ",
				new String[] { String.valueOf(customerId) });
	}

}
