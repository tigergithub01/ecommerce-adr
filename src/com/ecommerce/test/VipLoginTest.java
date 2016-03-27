package com.ecommerce.test;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ecommerce.utils.HttpUtils;

public class VipLoginTest extends AndroidTestCase {
	private static final String TAG = VipLoginTest.class.getSimpleName();

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	public void test() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("VipForm[vip_no]", "13724346621");
			map.put("VipForm[password]", "123456");
			String result = HttpUtils
					.getResult(
							"http://localhost:8085/index.php?r=api/vip-login/ajax-index",
							map);
			JSONObject obj = new JSONObject(result);
			Log.d(TAG, "obj:" + obj);
			//			obj.getInt("status");
			//			obj.getString("msg");
		} catch (Exception e) {
			Log.d(TAG, "err:" + e);
		}

	}
}
