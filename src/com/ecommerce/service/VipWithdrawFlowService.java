package com.ecommerce.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVipWithdrawFlow;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class VipWithdrawFlowService extends BaseService {
	private static final String TAG = VipIncomeService.class.getSimpleName();

	public VipWithdrawFlowService(Context context) {
		super(context);
	}

	public JsonObj getVipWithdrawFlow(Integer id) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-withdraw-flow/view");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TVipWithdrawFlow item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item.setId(value.getInt("id"));
				item.setSheetTypeId(value.get("sheet_type_id") == JSONObject.NULL ? null
						: value.getInt("sheet_type_id"));
				item.setCode(value.get("code") == JSONObject.NULL ? null
						: value.getString("code"));
				item.setApplyDate(value.get("apply_date") == JSONObject.NULL ? null
						: value.getString("apply_date"));
				item.setVipId(value.get("product_id") == JSONObject.NULL ? null
						: value.getInt("product_id"));
				item.setVipId(value.get("vip_id") == JSONObject.NULL ? null
						: value.getInt("vip_id"));
				item.setAmount(value.get("amount") == JSONObject.NULL ? null
						: value.getDouble("amount"));
				item.setSettledAmt(value.get("settled_amt") == JSONObject.NULL ? null
						: value.getDouble("settled_amt"));
				item.setSettledDate(value.get("settled_date") == JSONObject.NULL ? null
						: value.getString("settled_date"));
				item.setStatus(value.get("status") == JSONObject.NULL ? null
						: value.getInt("status"));

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

	public JsonObj getVipWithdrawFlowList(TVipWithdrawFlow vipWithdrawFlow,
			int page, int pageCount, String orderColumn, String orderDirection) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-withdraw-flow/index");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("offset", (page - 1) * pageCount);
		map.put("page_count", pageCount);
		if (vipWithdrawFlow != null) {
			//TODO: do something
			map.put("vip_id", vipWithdrawFlow.getVipId());
		}
		map.put("order_column", orderColumn);
		map.put("order_direction", orderDirection);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONArray jsonArray = new JSONArray(rtnValue);
				LinkedList<TVipWithdrawFlow> datalist = new LinkedList<TVipWithdrawFlow>();
				for (int i = 0; i < jsonArray.length(); i++) {
					TVipWithdrawFlow item = new TVipWithdrawFlow();
					JSONObject value = jsonArray.getJSONObject(i);
					item.setId(value.getInt("id"));
					item.setSheetTypeId(value.get("sheet_type_id") == JSONObject.NULL ? null
							: value.getInt("sheet_type_id"));
					item.setCode(value.get("code") == JSONObject.NULL ? null
							: value.getString("code"));
					item.setApplyDate(value.get("apply_date") == JSONObject.NULL ? null
							: value.getString("apply_date"));
					item.setVipId(value.get("vip_id") == JSONObject.NULL ? null
							: value.getInt("vip_id"));
					item.setAmount(value.get("amount") == JSONObject.NULL ? null
							: value.getDouble("amount"));
					item.setSettledAmt(value.get("settled_amt") == JSONObject.NULL ? null
							: value.getDouble("settled_amt"));
					item.setSettledDate(value.get("settled_date") == JSONObject.NULL ? null
							: value.getString("settled_date"));
					item.setStatus(value.get("status") == JSONObject.NULL ? null
							: value.getInt("status"));

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

	public JsonObj createVipWithdrawFlow(TVipWithdrawFlow vipWithdrawFlow) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-withdraw-flow/create");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("VipWithdrawFlow[amount]", vipWithdrawFlow.getAmount());
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TVipWithdrawFlow item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item = new TVipWithdrawFlow();
				item.setId(value.getInt("id"));
				item.setSheetTypeId(value.get("sheet_type_id") == JSONObject.NULL ? null
						: value.getInt("sheet_type_id"));
				item.setCode(value.get("code") == JSONObject.NULL ? null
						: value.getString("code"));
				item.setApplyDate(value.get("apply_date") == JSONObject.NULL ? null
						: value.getString("apply_date"));
				item.setVipId(value.get("vip_id") == JSONObject.NULL ? null
						: value.getInt("vip_id"));
				item.setAmount(value.get("amount") == JSONObject.NULL ? null
						: value.getDouble("amount"));
				item.setSettledAmt(value.get("settled_amt") == JSONObject.NULL ? null
						: value.getDouble("settled_amt"));
				item.setSettledDate(value.get("settled_date") == JSONObject.NULL ? null
						: value.getString("settled_date"));
				item.setStatus(value.get("status") == JSONObject.NULL ? null
						: value.getInt("status"));
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
