package com.ecommerce.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVipIncome;
import com.ecommerce.model.TVipIncomeDetail;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class VipIncomeService extends BaseService {
	private static final String TAG = VipIncomeService.class.getSimpleName();

	public VipIncomeService(Context context) {
		super(context);
	}

	public JsonObj getVipIncome(Integer vipId) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-income/view");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vip_id", vipId);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TVipIncome item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item = new TVipIncome();
				item.setId(value.getInt("id"));
				item.setVipId(value.get("vip_id") == JSONObject.NULL ? null
						: value.getInt("vip_id"));
				item.setAmount(value.get("amount") == JSONObject.NULL ? null
						: value.getDouble("amount"));
				item.setCanSettleAmt(value.get("can_settle_amt") == JSONObject.NULL ? null
						: value.getDouble("can_settle_amt"));
				item.setSettledAmt(value.get("settled_amt") == JSONObject.NULL ? null
						: value.getDouble("settled_amt"));
				item.setCanWithdrawAmt(value.get("can_withdraw_amt") == JSONObject.NULL ? null
						: value.getDouble("can_withdraw_amt"));
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

	public JsonObj getVipIncomeDetailList(TVipIncomeDetail vipIncomeDetail, int page, int pageCount,
			String orderColumn, String orderDirection) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-income-detail/index");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("offset", (page - 1) * pageCount);
		map.put("page_count", pageCount);
		if (vipIncomeDetail != null) {
			//TODO: do something
			map.put("vip_id", vipIncomeDetail.getVipId());
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
				LinkedList<TVipIncomeDetail> datalist = new LinkedList<TVipIncomeDetail>();
				for (int i = 0; i < jsonArray.length(); i++) {
					TVipIncomeDetail item = new TVipIncomeDetail();
					JSONObject value = jsonArray.getJSONObject(i);
					item.setId(value.getInt("id"));
					item.setOrderId(value.get("order_id") == JSONObject.NULL ? null
							: value.getInt("order_id"));
					item.setProductId(value.get("product_id") == JSONObject.NULL ? null
							: value.getInt("product_id"));
					item.setVipId(value.get("vip_id") == JSONObject.NULL ? null
							: value.getInt("vip_id"));
					item.setSubVipId(value.get("sub_vip_id") == JSONObject.NULL ? null
							: value.getInt("sub_vip_id"));
					item.setAmount(value.get("amount") == JSONObject.NULL ? null
							: value.getDouble("amount"));
					item.setSubVipNo(value.get("sub_vip_no") == JSONObject.NULL ? null
							: value.getString("sub_vip_no"));
					item.setOrderCode(value.get("order_code") == JSONObject.NULL ? null
							: value.getString("order_code"));
					item.setProductName(value.get("product_name") == JSONObject.NULL ? null
							: value.getString("product_name"));		 			//get image

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
	
	public JsonObj getVipIncomeDetail(Integer vipId) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-income/view");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vip_id", vipId);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TVipIncome item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item = new TVipIncome();
				item.setId(value.getInt("id"));
				item.setVipId(value.get("vip_id") == JSONObject.NULL ? null
						: value.getInt("vip_id"));
				item.setAmount(value.get("amount") == JSONObject.NULL ? null
						: value.getDouble("amount"));
				item.setCanSettleAmt(value.get("can_settle_amt") == JSONObject.NULL ? null
						: value.getDouble("can_settle_amt"));
				item.setSettledAmt(value.get("settled_amt") == JSONObject.NULL ? null
						: value.getDouble("settled_amt"));

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
