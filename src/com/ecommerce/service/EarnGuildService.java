package com.ecommerce.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TEarnGuild;
import com.ecommerce.model.TNotification;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class EarnGuildService extends BaseService {
	private static final String TAG = NotificationService.class.getSimpleName();

	public EarnGuildService(Context context) {
		super(context);
	}

	public JsonObj getEarnGuild(Integer id) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/earn-guild/view");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TEarnGuild item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item = new TEarnGuild();
				item.setId(value.getInt("id"));
				item.setTitle(value.get("title") == JSONObject.NULL ? null
						: value.getString("title"));
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

	public JsonObj getEarnGuildList(TEarnGuild earnGuild, int page,
			int pageCount, String orderColumn, String orderDirection) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/earn-guild/index");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("offset", (page - 1) * pageCount);
		map.put("page_count", pageCount);
		if (earnGuild != null) {
			//TODO: do something
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
				LinkedList<TEarnGuild> datalist = new LinkedList<TEarnGuild>();
				for (int i = 0; i < jsonArray.length(); i++) {
					TEarnGuild item = new TEarnGuild();
					JSONObject value = jsonArray.getJSONObject(i);
					item = new TEarnGuild();
					item.setId(value.getInt("id"));
					item.setTitle(value.get("title") == JSONObject.NULL ? null
							: value.getString("title"));
					item.setContent(value.get("content") == JSONObject.NULL ? null
							: value.getString("content"));
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
