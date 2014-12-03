package com.hck.book.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库
 * 
 * @author
 * 
 */
public class BookDB extends SQLiteOpenHelper {
	private static String DATABASE_NAME = "book.db";
	private static int DATABASE_VERSION = 1;
	private String PATH = "path";
	private String TYPE = "type";
	private String table = null;

	public BookDB(Context context, String table) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.table = table;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + table + " ( parent text not null, " + PATH
				+ " text not null, " + TYPE + " text not null"
				+ ", now  text not null, ready);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}

}
