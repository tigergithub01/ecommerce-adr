package com.ecommerce.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TAdInfo;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class AdInfoService extends BaseService {
	private static final String TAG = AdInfoService.class.getSimpleName();

	public AdInfoService(Context context) {
		super(context);
	}

	/*public JsonObj getProduct(Integer productId) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/product/view");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", productId);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TProduct item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item = new TProduct();
				item.setId(value.getInt("id"));
				item.setCode(value.get("code") == JSONObject.NULL ? null
						: value.getString("code"));
				item.setName(value.get("name") == JSONObject.NULL ? null
						: value.getString("name"));
				item.setTypeId(value.get("type_id") == JSONObject.NULL ? null
						: value.getInt("type_id"));
				item.setPrice(value.get("price") == JSONObject.NULL ? null
						: value.getDouble("price"));
				item.setDescription(value.get("description") == JSONObject.NULL ? null
						: value.getString("description"));
				item.setStatus(value.get("status") == JSONObject.NULL ? null
						: value.getInt("status"));
				item.setStockQuantity(value.get("stock_quantity") == JSONObject.NULL ? null
						: value.getDouble("stock_quantity"));
				item.setSafetyQuantity(value.get("safety_quantity") == JSONObject.NULL ? null
						: value.getDouble("safety_quantity"));

				jsonObj.setValue(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			jsonObj.setStatus(Constants.AJAX_STATUS_FAILED);
			jsonObj.setMsg(ExceptionHandler.getMessage(e).toString());
		}
		return jsonObj;
	}*/

	public JsonObj getAdInfoList() {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/ad-info/index");
		Map<String, Object> map = new HashMap<String, Object>();
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
				LinkedList<TAdInfo> datalist = new LinkedList<TAdInfo>();
				for (int i = 0; i < jsonArray.length(); i++) {
					TAdInfo item = new TAdInfo();
					JSONObject value = jsonArray.getJSONObject(i);
					item.setId(value.getInt("id"));
					item.setImageUrl(value.get("image_url") == JSONObject.NULL ? null
							: value.getString("image_url"));
					item.setSequenceId(value.get("sequence_id") == JSONObject.NULL ? null
							: value.getInt("sequence_id"));
					item.setRedirectUrl(value.get("redirect_url") == JSONObject.NULL ? null
							: value.getString("redirect_url"));
					
					//get image
					String imageUrl =  ConfigReader.getInstance(context).getRemoteUrl(
							"/index.php?r=api/ad-info/ad-url");
					Map<String, Object> imageMap = new HashMap<String, Object>();
					imageMap.put("image_url", item.getImageUrl());
					Bitmap bitmap = HttpUtils.getBitmap(imageUrl, imageMap, Constants.sessionId);
					item.setImage(bitmap);
					
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
