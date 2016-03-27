package com.ecommerce.utils.sqlite;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ecommerce.utils.FileUtil;
import com.ecommerce.utils.StringUtils;

public class SQLiteHelper extends SQLiteOpenHelper {
	private static final String TAG = "SQLiteHelper";
	private static final String name = "zff.db";
	private static final int version = 1;
	private Context context;

	public SQLiteHelper(Context context) {
		super(context, name, null, version);
		this.context = context;
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		/*//create table t_person
		String sql = "CREATE TABLE if not exists t_person(id INTEGER PRIMARY KEY AUTOINCREMENT,name text not null,birthday date,sex varchar(10),nation varchar(20),photo varchar(200),salary double,description text,other1 text,is_admin int,password text);";
		db.execSQL(sql);
		
		//create table t_nation
		sql = "CREATE TABLE if not exists t_nation(id INTEGER PRIMARY KEY AUTOINCREMENT,name text);";
		db.execSQL(sql);*/
		InputStream inputStream;
		try {
			inputStream = context.getAssets().open("create_db.sql");
			String sqlStr = FileUtil.readIsAsString(inputStream);
			String[] sqlList= sqlStr.split(";");
			for(String sql : sqlList){
				if(StringUtils.isNotBlank(sql)){
					db.execSQL(sql+";");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*String sql = null;
		String tableSQL = getTableSql(db,"t_person");
		Log.i(TAG, tableSQL);
		
		//add column t_person.other1
		if(StringUtils.isNotEmpty(tableSQL) && !tableSQL.contains("other1")){
			sql = "alter table t_person add column other1 text;";
			db.execSQL(sql);
		}
		
		//add column t_person.is_admin
		if(StringUtils.isNotEmpty(tableSQL) && !tableSQL.contains("is_admin")){
			sql = "alter table t_person add column is_admin int;";
			db.execSQL(sql);
		}
		
		//add column t_person.password
		if(StringUtils.isNotEmpty(tableSQL) && !tableSQL.contains("password")){
			sql = "alter table t_person add column password text;";
			db.execSQL(sql);
		}
		
		if(!tableExists(db, "t_nation")){
			sql = "CREATE TABLE t_nation(id INTEGER PRIMARY KEY AUTOINCREMENT,name text);";
			db.execSQL(sql);
		}
		Log.i(TAG, "onUpgrade");*/

	}

	private boolean tableExists(SQLiteDatabase db,String tableName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			String sql = "select count(*) c from sqlite_master where type =? and name =?";
			cursor = db.rawQuery(sql, new String[] { "table", tableName });
			if (cursor.moveToNext()) {
				int count = cursor.getInt(cursor.getColumnIndex("c"));
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return result;
	}
	
	/**
	 * get table schema
	 * @param tableName
	 * @return
	 */
	private String getTableSql(SQLiteDatabase db,String tableName) {
		String tableSQL = null;
		Cursor cursor = null;
		try {
			String sql = "select sql from sqlite_master where type =? and name =?";
			cursor = db.rawQuery(sql, new String[] { "table", tableName });
			if (cursor.moveToNext()) {
				return cursor.getString(cursor.getColumnIndex("sql"));
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return tableSQL;
	}

}
