package com.ecommerce.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ecommerce.model.TVip;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipService;

public class VipRegisterTest extends AndroidTestCase {
	private static final String TAG = VipRegisterTest.class.getSimpleName();

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	public void test() {
		try {
			VipService vipService = ServiceUtils.getInstance(mContext).getVipService();
			TVip vip = new TVip();
			vip.setVip_no("13724346621");
			vip.setPassword("123456");
			vip.setVerifyCode("111111");
			vip.setParent_vip_no("22222222");
			vipService.register(vip);
		} catch (Exception e) {
			Log.d(TAG, "err:" + e);
		}

	}
}
