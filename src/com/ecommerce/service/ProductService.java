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
import com.ecommerce.model.TProduct;
import com.ecommerce.model.TProductType;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class ProductService extends BaseService {
	private static final String TAG = ProductService.class.getSimpleName();

	public ProductService(Context context) {
		super(context);
	}

	public JsonObj getProduct(Integer productId) {
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
	}

	public JsonObj getProductList(TProduct product, int page, int pageCount,
			String orderColumn, String orderDirection) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/product/index");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("offset", (page - 1) * pageCount);
		map.put("page_count", pageCount);
		if (product != null) {
			if (StringUtils.isNotEmpty(product.getName())) {
				map.put("product_name", product.getName());
			}

			if (product.getTypeId() != null) {
				map.put("product_type_id", product.getTypeId());
			}
			
			if (product.getShowInHomepage()!= null) {
				map.put("show_in_homepage", product.getShowInHomepage());
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
				LinkedList<TProduct> datalist = new LinkedList<TProduct>();
				for (int i = 0; i < jsonArray.length(); i++) {
					TProduct item = new TProduct();
					JSONObject value = jsonArray.getJSONObject(i);
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
					item.setPrimaryPhotoUrl(value.get("primaryPhoto_url") == JSONObject.NULL ? null
							: value.getString("primaryPhoto_url"));	
					
					//get image
					if (StringUtils.isNotEmpty(item.getPrimaryPhotoUrl())) {
						String imageUrl = ConfigReader.getInstance(context)
								.getRemoteUrl(
										"/index.php?r=api/product/photo-view");
						Map<String, Object> imageMap = new HashMap<String, Object>();
						imageMap.put("url", item.getPrimaryPhotoUrl());
						Bitmap bitmap = HttpUtils.getBitmap(imageUrl, imageMap,
								Constants.sessionId);
						item.setPrimaryPhotoBitmap(bitmap);
					}

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

	public JsonObj getProductTypeList() {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/product-type/index");
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
				LinkedList<TProductType> datalist = new LinkedList<TProductType>();
				for (int i = 0; i < jsonArray.length(); i++) {
					TProductType item = new TProductType();
					JSONObject value = jsonArray.getJSONObject(i);
					item.setId(value.getInt("id"));
					item.setName(value.get("name") == JSONObject.NULL ? null
							: value.getString("name"));
					item.setParentId(value.get("parent_id") == JSONObject.NULL ? null
							: value.getInt("parent_id"));
					item.setDescription(value.get("description") == JSONObject.NULL ? null
							: value.getString("description"));
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
