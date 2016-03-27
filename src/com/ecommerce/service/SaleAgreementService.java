package com.ecommerce.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TSaleAgreement;
import com.ecommerce.model.TUsageRights;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class SaleAgreementService extends BaseService {
	private static final String TAG = SaleAgreementService.class
			.getSimpleName();

	public SaleAgreementService(Context context) {
		super(context);
	}

	public JsonObj getSaleAgreement(Integer productId) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/sale-agreement/view");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TSaleAgreement item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item = new TSaleAgreement();
				item.setId(value.getInt("id"));
				item.setContent(value.get("content") == JSONObject.NULL ? null
						: value.getString("content"));

				jsonObj.setValue(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			jsonObj.setStatus(Constants.AJAX_STATUS_FAILED);
			jsonObj.setMsg(ExceptionHandler.getMessage(e).toString());
		}
		return jsonObj;
	}

	public JsonObj getUsageRights(Integer productId) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/sale-agreement/view");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TUsageRights item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item = new TUsageRights();
				item.setId(value.getInt("id"));
				item.setContent(value.get("content") == JSONObject.NULL ? null
						: value.getString("content"));

				jsonObj.setValue(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			jsonObj.setStatus(Constants.AJAX_STATUS_FAILED);
			jsonObj.setMsg(ExceptionHandler.getMessage(e).toString());
		}
		return jsonObj;
	}
}
