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
import com.ecommerce.model.TVipBankcard;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class VipBankcardService extends BaseService {
	private static final String TAG = VipBankcardService.class.getSimpleName();

	public VipBankcardService(Context context) {
		super(context);
	}

	public JsonObj getVipBankcard(Integer vipId) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-bank/view");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vip_id", vipId);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TVipBankcard rtnObj = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				rtnObj = new TVipBankcard();
				rtnObj.setId(value.getInt("id"));
				rtnObj.setCardNo(value.get("card_no") == JSONObject.NULL ? null
						: value.getString("card_no"));
				rtnObj.setBankId(value.get("bank_id") == JSONObject.NULL ? null
						: value.getInt("bank_id"));
				rtnObj.setBranchName(value.get("branch_name") == JSONObject.NULL ? null
						: value.getString("branch_name"));
				rtnObj.setOpenAddr(value.get("open_addr") == JSONObject.NULL ? null
						: value.getString("open_addr"));
				rtnObj.setBankName(value.get("bank_name") == JSONObject.NULL ? null
						: value.getString("bank_name"));
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

	public JsonObj updateVipBankcard(TVipBankcard bankcard) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-bank/update");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", bankcard.getId());
		map.put("VipBankcard[vip_id]", bankcard.getVipId());
		map.put("VipBankcard[card_no]", bankcard.getCardNo());
		map.put("VipBankcard[bank_id]", bankcard.getBankId());
		map.put("VipBankcard[branch_name]", bankcard.getBranchName());
		map.put("VipBankcard[open_addr]", bankcard.getOpenAddr());
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TVipBankcard rtnObj = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				rtnObj = new TVipBankcard();
				rtnObj.setId(value.getInt("id"));
				rtnObj.setCardNo(value.get("card_no") == JSONObject.NULL ? null
						: value.getString("card_no"));
				rtnObj.setBankId(value.get("bank_id") == JSONObject.NULL ? null
						: value.getInt("bank_id"));
				rtnObj.setBranchName(value.get("branch_name") == JSONObject.NULL ? null
						: value.getString("branch_name"));
				rtnObj.setOpenAddr(value.get("open_addr") == JSONObject.NULL ? null
						: value.getString("open_addr"));
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

	public JsonObj createVipBankcard(TVipBankcard bankcard) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-bank/create");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("VipBankcard[vip_id]", bankcard.getVipId());
		map.put("VipBankcard[card_no]", bankcard.getCardNo());
		map.put("VipBankcard[bank_id]", bankcard.getBankId());
		map.put("VipBankcard[branch_name]", bankcard.getBranchName());
		map.put("VipBankcard[open_addr]", bankcard.getOpenAddr());
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TVipBankcard rtnObj = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				rtnObj = new TVipBankcard();
				rtnObj.setId(value.getInt("id"));
				rtnObj.setCardNo(value.get("card_no") == JSONObject.NULL ? null
						: value.getString("card_no"));
				rtnObj.setBankId(value.get("bank_id") == JSONObject.NULL ? null
						: value.getInt("bank_id"));
				rtnObj.setBranchName(value.get("branch_name") == JSONObject.NULL ? null
						: value.getString("branch_name"));
				rtnObj.setOpenAddr(value.get("open_addr") == JSONObject.NULL ? null
						: value.getString("open_addr"));
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
