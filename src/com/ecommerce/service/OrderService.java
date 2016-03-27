package com.ecommerce.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TSoSheet;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class OrderService extends BaseService {
	private static final String TAG = OrderService.class.getSimpleName();

	public OrderService(Context context) {
		super(context);
	}
	
	public JsonObj getOrder(Integer id) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-order/view");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TSoSheet item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item = new TSoSheet();
				item.setId(value.getInt("id"));
				item.setCode(value.get("code") == JSONObject.NULL ? null
						: value.getString("code"));
				item.setVipId(value.get("vip_id") == JSONObject.NULL ? null
						: value.getInt("vip_id"));
				item.setOrderAmt(value.get("order_amt") == JSONObject.NULL ? null
						: value.getDouble("order_amt"));
				item.setOrderQuantity(value.get("order_quantity") == JSONObject.NULL ? null
						: value.getInt("order_quantity"));
				item.setDeliverFee(value.get("deliver_fee") == JSONObject.NULL ? null
						: value.getDouble("deliver_fee"));
				item.setStatus(value.get("status") == JSONObject.NULL ? null
						: value.getInt("status"));
				item.setSettleFlag(value.get("settle_flag") == JSONObject.NULL ? null
						: value.getInt("settle_flag"));
				item.setOrderDate(value.get("order_date") == JSONObject.NULL ? null
						: value.getString("order_date"));
				item.setPayTypeId(value.get("pay_type_id") == JSONObject.NULL ? null
						: value.getInt("pay_type_id"));
				item.setPayAmt(value.get("pay_amt") == JSONObject.NULL ? null
						: value.getDouble("pay_amt"));
				item.setPayDate(value.get("pay_date") == JSONObject.NULL ? null
						: value.getString("pay_date"));
				item.setOrderStatusVal(value.get("order_status_val") == JSONObject.NULL ? null
						: value.getString("order_status_val"));
				item.setVipNo(value.get("vip_no") == JSONObject.NULL ? null
						: value.getString("vip_no"));
				item.setProductName(value.get("product_name") == JSONObject.NULL ? null
						: value.getString("product_name"));	
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

	public JsonObj getGroupOrderList(TSoSheet soSheet, int page, int pageCount,
			String orderColumn, String orderDirection) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip-order/group-index");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("offset", (page - 1) * pageCount);
		map.put("page_count", pageCount);
		if (soSheet != null) {
			map.put("vip_id", soSheet.getVipId());
			if(soSheet.getStatus()!=null){
				map.put("status", soSheet.getStatus());
			}
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
				LinkedList<TSoSheet> datalist = new LinkedList<TSoSheet>();
				for (int i = 0; i < jsonArray.length(); i++) {
					TSoSheet item = new TSoSheet();
					JSONObject value = jsonArray.getJSONObject(i);
					item.setId(value.getInt("id"));
					item.setCode(value.get("code") == JSONObject.NULL ? null
							: value.getString("code"));
					item.setVipId(value.get("vip_id") == JSONObject.NULL ? null
							: value.getInt("vip_id"));
					item.setOrderAmt(value.get("order_amt") == JSONObject.NULL ? null
							: value.getDouble("order_amt"));
					item.setOrderQuantity(value.get("order_quantity") == JSONObject.NULL ? null
							: value.getInt("order_quantity"));
					item.setDeliverFee(value.get("deliver_fee") == JSONObject.NULL ? null
							: value.getDouble("deliver_fee"));
					item.setStatus(value.get("status") == JSONObject.NULL ? null
							: value.getInt("status"));
					item.setSettleFlag(value.get("settle_flag") == JSONObject.NULL ? null
							: value.getInt("settle_flag"));
					item.setOrderDate(value.get("order_date") == JSONObject.NULL ? null
							: value.getString("order_date"));
					item.setPayTypeId(value.get("pay_type_id") == JSONObject.NULL ? null
							: value.getInt("pay_type_id"));
					item.setPayAmt(value.get("pay_amt") == JSONObject.NULL ? null
							: value.getDouble("pay_amt"));
					item.setPayDate(value.get("pay_date") == JSONObject.NULL ? null
							: value.getString("pay_date"));
					item.setOrderStatusVal(value.get("order_status_val") == JSONObject.NULL ? null
							: value.getString("order_status_val"));
					item.setVipNo(value.get("vip_no") == JSONObject.NULL ? null
							: value.getString("vip_no"));

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
