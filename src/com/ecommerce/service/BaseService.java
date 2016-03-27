package com.ecommerce.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BaseService {
	protected Context context;
	protected SQLiteDatabase db;
	
	public BaseService(Context context) {
		this.context = context;
		db=ServiceUtils.getLocalDB(context);
	}
}
