package com.ecommerce.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ecommerce.model.JsonObj;
import com.ecommerce.model.StreamEntity;
import com.ecommerce.model.TAppRelease;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;

public class DownloadAppService extends BaseService {
	private static final String TAG = ProductService.class.getSimpleName();

	public DownloadAppService(Context context) {
		super(context);
	}

	public JsonObj getAppRelease(Integer productId) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/download-app/ajax-view");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TAppRelease item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				item = new TAppRelease();
				item.setId(value.getInt("id"));
				item.setName(value.get("name") == JSONObject.NULL ? null
						: value.getString("name"));
				item.setUpgradeDesc(value.get("upgrade_desc") == JSONObject.NULL ? null
						: value.getString("upgrade_desc"));
				item.setVerNo(value.get("ver_no") == JSONObject.NULL ? null
						: value.getInt("ver_no"));
				item.setForceUpgrade(value.get("force_upgrade") == JSONObject.NULL ? null
						: value.getInt("force_upgrade"));
				item.setAppPath(value.get("app_path") == JSONObject.NULL ? null
						: value.getString("app_path"));
				
				//get contentLength for display
				StreamEntity streamEntity = getStreamEntity(item.getAppPath());
				if(streamEntity!=null){
					item.setContentLength(streamEntity.getContentLength());
				}
				
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

	public JsonObj getAppRelease(String appPath) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/download-app/view-app");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app_path", appPath);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TAppRelease item = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
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

	public StreamEntity getStreamEntity(String appPath) {
		StreamEntity streamEntity = new StreamEntity();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/download-app/view-app");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app_path", appPath);
		try {
			HttpResponse response = HttpUtils.getHttpResponse(url, map);
			InputStream inputStream = response.getEntity().getContent();
			long contentLength = response.getEntity().getContentLength();
			streamEntity.setInputStream(inputStream);
			streamEntity.setContentLength(contentLength);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return streamEntity;
	}

}
