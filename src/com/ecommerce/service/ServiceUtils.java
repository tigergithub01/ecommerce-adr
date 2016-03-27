package com.ecommerce.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ecommerce.utils.FileUtil;
import com.ecommerce.utils.NetWorkUtil;
import com.ecommerce.utils.sqlite.SQLiteHelper;
import com.ecommerce.utils.sqlite.SQLiteUtils;

public class ServiceUtils {
	private boolean isConnected;
	private Context context;

	private static ServiceUtils dbBusiness = null;

	public static ServiceUtils getInstance(Context context) {
		if (dbBusiness == null) {
			dbBusiness = new ServiceUtils(context);
		}
		return dbBusiness;
	}

	public static SQLiteDatabase getLocalDB(Context context) {
		SQLiteDatabase db = null;
		if (FileUtil.isSdCardExist()) {
			db = SQLiteUtils.getInstance(context).getDb();
		} else {
			SQLiteHelper dbHelper = new SQLiteHelper(context);
			db = dbHelper.getReadableDatabase();
		}
		return db;
	}

	public ServiceUtils(Context context) {
		isConnected = NetWorkUtil.isConnected(context);
		this.context = context;
	}

	public SmsService getSmsService() {
		return new SmsService(context);
	}

	public VipService getVipService() {
		return new VipService(context);
	}

	public VipBankcardService getVipBankcardService() {
		return new VipBankcardService(context);
	}

	public SysFeedbackService getSysFeedbackService() {
		return new SysFeedbackService(context);
	}

	public ProductService getProductService() {
		return new ProductService(context);
	}

	public SaleAgreementService getSaleAgreementService() {
		return new SaleAgreementService(context);
	}

	public DownloadAppService getDownloadAppService() {
		return new DownloadAppService(context);
	}

	public AdInfoService getAdInfoService() {
		return new AdInfoService(context);
	}

	public OrderService getOrderService() {
		return new OrderService(context);
	}

	public VipIncomeService getVipIncomeService() {
		return new VipIncomeService(context);
	}

	public VipWithdrawFlowService getVipWithdrawFlowService() {
		return new VipWithdrawFlowService(context);
	}
	
	public NotificationService getNotificationService() {
		return new NotificationService(context);
	}

	public EarnGuildService getEarnGuildService() {
		return new EarnGuildService(context);
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	
}
