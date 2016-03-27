package com.ecommerce.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TBankInfo;
import com.ecommerce.model.TSysFeedback;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class SysFeedbackService extends BaseService {
	private static final String TAG = SysFeedbackService.class.getSimpleName();

	public SysFeedbackService(Context context) {
		super(context);
	}

	public JsonObj createSysFeedback(TSysFeedback sysFeedback) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/sys-feedback/create");
		Map<String, Object> map = new HashMap<String, Object>();
		if (Constants.vipSession != null) {
			map.put("SysFeedback[vip_id]", Constants.vipSession.getId());
		}

		map.put("SysFeedback[content]", sysFeedback.getContent());
		map.put("SysFeedback[contact_method]", sysFeedback.getContactMethod());
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, null);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TSysFeedback rtnObj = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				rtnObj = new TSysFeedback();
				rtnObj.setId(value.getInt("id"));
				jsonObj.setValue(rtnObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			jsonObj.setStatus(Constants.AJAX_STATUS_FAILED);
			jsonObj.setMsg(ExceptionHandler.getMessage(e).toString());
		}
		return jsonObj;
	}

	public JsonObj getBankList() {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-bank/bank-list");
		try {
			//parase result
			String result = HttpUtils.getResult(url, null, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONArray jsonArray = new JSONArray(rtnValue);
				LinkedList<TBankInfo> datalist = new LinkedList<TBankInfo>();
				for (int i = 0; i < jsonArray.length(); i++) {
					TBankInfo item = new TBankInfo();
					JSONObject value = jsonArray.getJSONObject(i);
					item.setId(value.getInt("id"));
					item.setName(value.getString("name"));
					datalist.add(item);
				}
				jsonObj.setValue(datalist);
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
