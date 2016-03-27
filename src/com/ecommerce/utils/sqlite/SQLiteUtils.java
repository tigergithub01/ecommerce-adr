package com.ecommerce.utils.sqlite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.FileUtil;
import com.ecommerce.utils.StringUtils;

public class SQLiteUtils {
	private static final String name = "zff.db";
	private SQLiteDatabase db;
	private Context context;
	private static SQLiteUtils sqliteUtils = null;
	
	public static SQLiteUtils getInstance(Context context) {
		if (sqliteUtils == null) {
			sqliteUtils = new SQLiteUtils(context);
		}
		return sqliteUtils;
	}
	
	public SQLiteUtils(Context context) {
		this.context = context;
		upgradeDataBase();
	}
	
	
	public boolean upgradeDataBase() {
		if (!FileUtil.isSdCardExist()) {
			return false;
		}
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ ConfigReader.getInstance(context).getProperties(
						"database_path");
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(path + name, null);
		InputStream inputStream;
		try {
			inputStream = context.getAssets().open("create_db.sql");
			String sqlStr = FileUtil.readIsAsString(inputStream);
			String[] sqlList = sqlStr.split(";");
			for (String sql : sqlList) {
				if (StringUtils.isNotBlank(sql)) {
					db.execSQL(sql + ";");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public SQLiteDatabase getDb() {
		return db;
	}
	
	/**
	 * 执行sql语句
	 * 
	 * @param sql
	 */
	public void executeSQL(String sql)

	{

		try {
			db.execSQL(sql);

		} catch (Exception e) {

			throw new RuntimeException("执行sql语句," + e.getMessage());

		}
	}

	/**
	 * 执行带占位符的sql
	 * 
	 * @param sql
	 * @param arr
	 * @return
	 */
	public Cursor executeSQLwithCursorReturn(String sql, String[] arr)

	{

		Cursor retCursor = null;
		try {
			retCursor = db.rawQuery(sql, arr);

		} catch (Exception e) {

			throw new RuntimeException("执行占位符sql," + e.getMessage());
		}
		return retCursor;

	}

	/**
	 * 查询是否相同
	 * 
	 * @param sql
	 * @return
	 */
	public boolean isExist(String sql)

	{

		boolean retBoolean = false;
		Cursor c = null;
		c = db.rawQuery(sql, null);
		if (c.getCount() == 0) {
			retBoolean = false;
		} else {
			retBoolean = true;
		}
		return retBoolean;

	}

	/**
	 * 查询执行表数据
	 * 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor select(String table, String[] columns, String selection,

	String[] selectionArgs, String groupBy, String having, String orderBy)

	{

		Cursor cursor = null;
		cursor = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);
		return cursor;

	}

	/**
	 * 添加执行表数据
	 * 
	 * @param table
	 * @param cv
	 * @return
	 */
	public long insert(String table, ContentValues cv)

	{
		return db.insert(table, null, cv);

	}

	/**
	 * 更新执行表数据
	 * 
	 * @param table
	 * @param cv
	 * @param whereCause
	 * @param whereArgs
	 * @return
	 */
	public int updateTableWithContentValues(String table, ContentValues cv,
			String whereCause, String[] whereArgs)

	{

		return db.update(table, cv, whereCause, whereArgs);

	}

	/**
	 * 删除执行表数据
	 * 
	 * @param table
	 * @param where
	 * @param whereArgs
	 * @return
	 */
	public int delete(String table, String where, String[] whereArgs)

	{
		return db.delete(table, where, whereArgs);
	}

	/**
	 * 多条数据添加
	 * 
	 * @param list
	 */
	public void insertBySqlList(ArrayList<String> list) {
		db.beginTransaction();
		try {
			for (int i = 0; i < list.size(); i++) {
				db.execSQL(list.get(i));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			throw new RuntimeException("多条添加数据" + e.getMessage());

		} finally {
			db.endTransaction();
		}
	}

	/**
	 * 执行表集合添加
	 * 
	 * @param table
	 * @param list
	 */
	public void insertBySqlList(String table, List<ContentValues> list) {
		db.beginTransaction();
		try {
			Iterator<ContentValues> it = list.iterator();
			while (it.hasNext()) {
				ContentValues cv = it.next();
				db.insertOrThrow(table, null, cv);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			throw new RuntimeException(e);

		} finally {
			db.endTransaction();

		}
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		// TODO Auto-generated method stub
		db.close();

	}

}
