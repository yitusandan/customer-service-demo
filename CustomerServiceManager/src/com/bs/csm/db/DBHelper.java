package com.bs.csm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "com_bs_csm";
	private static final int DB_VERSION = 3;

	private static DBHelper mDBHelper;

	public static DBHelper getInstance(Context context) {
		if (mDBHelper == null) {
			mDBHelper = new DBHelper(context);
		}
		return mDBHelper;
	}

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CustomerTable.CREATE_TABLE);
		db.execSQL(CustomerTable.CREATE_INDEX);
		db.execSQL(ServiceTable.CREATE_TABLE);
		db.execSQL(CSRelationTable.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(CustomerTable.DROP_TABLE);
		db.execSQL(ServiceTable.DROP_TABLE);
		db.execSQL(CSRelationTable.DROP_TABLE);
		db.execSQL(CustomerTable.CREATE_TABLE);
		db.execSQL(CustomerTable.CREATE_INDEX);
		db.execSQL(ServiceTable.CREATE_TABLE);
		db.execSQL(CSRelationTable.CREATE_TABLE);
	}

}
