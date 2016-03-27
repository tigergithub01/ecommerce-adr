package com.ecommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVip;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;
import com.ecommerce.utils.StringUtils;
import com.ecommerce.utils.VersionUtils;

public class VipService extends BaseService {
	private static final String TAG = VipService.class.getSimpleName();

	public VipService(Context context) {
		super(context);
	}

	public JsonObj login(TVip vip) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=/api/vip-login/ajax-index");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("VipForm[vip_no]", vip.getVip_no());
		map.put("VipForm[password]", vip.getPassword());
		
		//		String result = HttpUtils.getResult(url, map);
		try {
			//parase result
			Object[] objs = HttpUtils.getHttpResponseAndHttpClient(url, map,
					null, false);
			HttpResponse resp = (HttpResponse) objs[0];
			String result = HttpUtils.getResult(resp);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			TVip rtnVip = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
				JSONObject value = new JSONObject(json.getString("value"));
				rtnVip = new TVip();
				rtnVip.setId(value.getInt("id"));
				rtnVip.setVip_no(value.getString("vip_no"));
				rtnVip.setPassword(value.getString("password"));
				rtnVip.setParent_id(value.get("parent_id") == JSONObject.NULL ? null
						: value.getInt("parent_id"));
				rtnVip.setParent_vip_no(value.get("parent_vip_no") == JSONObject.NULL ? null
						: value.getString("parent_vip_no"));
				jsonObj.setValue(rtnVip);
			}

			//store cookie
			List<Cookie> cookieList = new ArrayList<Cookie>();
			DefaultHttpClient httpclient = (DefaultHttpClient) objs[1];
			Cookie sessionCookie = HttpUtils.getSessionCookie(httpclient);
			String sessionId = null;
			if (sessionCookie != null) {
				cookieList.add(sessionCookie);
				sessionId = sessionCookie.getValue();
			}
			cookieList.add(sessionCookie);
			Log.d(TAG, "sessionCookie:" + sessionCookie);
			Log.d(TAG, "sessionId:" + sessionId);
			Constants.sessionId = sessionId;
			Constants.sessionCookieList = cookieList;
			Constants.vipSession = rtnVip;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			jsonObj.setStatus(Constants.AJAX_STATUS_FAILED);
			jsonObj.setMsg(ExceptionHandler.getMessage(e).toString());
		}

		return jsonObj;
	}

	public JsonObj register(TVip vip) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=/api/vip-register/ajax-index");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("VipForm[vip_no]", vip.getVip_no());
		map.put("VipForm[password]", vip.getPassword());
		map.put("VipForm[parent_vip_no]", vip.getParent_vip_no());
		map.put("VipForm[verifyCode]", vip.getVerifyCode());
		//		String result = HttpUtils.getResult(url, map);
		try {
			//parase result
			Object[] objs = HttpUtils.getHttpResponseAndHttpClient(url, map,
					null, false);
			HttpResponse resp = (HttpResponse) objs[0];
			String result = HttpUtils.getResult(resp);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			TVip rtnObj = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
				JSONObject value = new JSONObject(json.getString("value"));
				rtnObj = new TVip();
				rtnObj.setId(value.getInt("id"));
				rtnObj.setVip_no(value.getString("vip_no"));
				rtnObj.setParent_id(value.get("parent_id") == JSONObject.NULL ? null
						: value.getInt("parent_id"));
				rtnObj.setParent_vip_no(value.get("parent_vip_no") == JSONObject.NULL ? null
						: value.getString("parent_vip_no"));
				jsonObj.setValue(rtnObj);
			}

			//store cookie
			List<Cookie> cookieList = new ArrayList<Cookie>();
			DefaultHttpClient httpclient = (DefaultHttpClient) objs[1];
			Cookie sessionCookie = HttpUtils.getSessionCookie(httpclient);
			String sessionId = null;
			if (sessionCookie != null) {
				cookieList.add(sessionCookie);
				sessionId = sessionCookie.getValue();
			}
			cookieList.add(sessionCookie);
			Log.d(TAG, "sessionCookie:" + sessionCookie);
			Log.d(TAG, "sessionId:" + sessionId);
			Constants.sessionId = sessionId;
			Constants.sessionCookieList = cookieList;
			Constants.vipSession = rtnObj;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			jsonObj.setStatus(Constants.AJAX_STATUS_FAILED);
			jsonObj.setMsg(ExceptionHandler.getMessage(e).toString());
		}

		return jsonObj;
	}

	public JsonObj updatePwd(TVip vip) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=/api/vip-login/update-pwd");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("VipForm[vip_no]", vip.getVip_no());
		map.put("VipForm[password]", vip.getPassword());
		map.put("VipForm[verifyCode]", vip.getVerifyCode());
		try {
			//parase result
			String result = HttpUtils.getResult(url, map);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			//store cookie
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			jsonObj.setStatus(Constants.AJAX_STATUS_FAILED);
			jsonObj.setMsg(ExceptionHandler.getMessage(e).toString());
		}

		return jsonObj;
	}

	public JsonObj getVip(Integer vipId) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip/view");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", vipId);
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			String rtnValue = json.getString("value");
			TVip rtnObj = null;
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS
					&& StringUtils.isNotEmpty(rtnValue)) {
				JSONObject value = new JSONObject(rtnValue);
				rtnObj = new TVip();
				rtnObj.setId(value.getInt("id"));
				rtnObj.setName(value.get("name") == JSONObject.NULL ? null
						: value.getString("name"));
				rtnObj.setVip_no(value.getString("vip_no"));
				rtnObj.setId_card(value.get("id_card") == JSONObject.NULL ? null
						: value.getString("id_card"));
				rtnObj.setEmail(value.get("email") == JSONObject.NULL ? null
						: value.getString("email"));
				rtnObj.setParent_id(value.get("parent_id") == JSONObject.NULL ? null
						: value.getInt("parent_id"));
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

	public JsonObj updateVip(TVip vip) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip/update");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", vip.getId());
		map.put("Vip[name]", vip.getName());
		//		map.put("Vip[email]", vip.getEmail());
		map.put("Vip[id_card]", vip.getId_card());
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			jsonObj.setStatus(Constants.AJAX_STATUS_FAILED);
			jsonObj.setMsg(ExceptionHandler.getMessage(e).toString());
		}
		return jsonObj;
	}

	public JsonObj logout() {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=/api/vip-login/ajax-logout");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//parase result
			String result = HttpUtils.getResult(url, map, Constants.sessionId);
			JSONObject json = new JSONObject(result);
			jsonObj.setStatus(json.getInt("status"));
			jsonObj.setMsg(json.getString("msg"));
			if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
				Constants.sessionId = null;
				Constants.sessionCookieList = null;
				Constants.vipSession = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			jsonObj.setStatus(Constants.AJAX_STATUS_FAILED);
			jsonObj.setMsg(ExceptionHandler.getMessage(e).toString());
		}

		return jsonObj;
	}

	public JsonObj getChildren(Integer vipId) {
		JsonObj jsonObj = new JsonObj();
		String url = ConfigReader.getInstance(context).getRemoteUrl(
				"/index.php?r=api/vip/children");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vip_id", vipId);
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
				LinkedList<TVip> datalist = new LinkedList<TVip>();
				for (int i = 0; i < jsonArray.length(); i++) {
					TVip item = new TVip();
					JSONObject value = jsonArray.getJSONObject(i);
					item.setId(value.getInt("id"));
					item.setName(value.get("name") == JSONObject.NULL ? null
							: value.getString("name"));
					item.setVip_no(value.getString("vip_no"));
					item.setId_card(value.get("id_card") == JSONObject.NULL ? null
							: value.getString("id_card"));
					item.setEmail(value.get("email") == JSONObject.NULL ? null
							: value.getString("email"));
					item.setParent_id(value.get("parent_id") == JSONObject.NULL ? null
							: value.getInt("parent_id"));
					item.setParent_vip_no(value.get("parent_vip_no") == JSONObject.NULL ? null
							: value.getString("parent_vip_no"));
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

	public boolean getRememberMe() {
		Cursor cursor = db.rawQuery(
				"select * from T_PARAMETER where PARA_CODE = ?",
				new String[] { Constants.PARAM_REMEMBER_ME });
		if (cursor.moveToNext()) {
			String rememberMe = cursor.getString(cursor
					.getColumnIndex("PARA_VALUE"));
			return (Integer.valueOf(rememberMe).intValue() == 1) ? true : false;
		}
		cursor.close();
		return false;
	}

	public void rememberMe(boolean flag) {
		db.beginTransaction();
		try {
			Cursor cursor = db.rawQuery(
					"select * from T_PARAMETER where PARA_CODE = ?",
					new String[] { Constants.PARAM_REMEMBER_ME });
			if (cursor.moveToNext()) {
				//int id = cursor.getInt(cursor.getShort(cursor.getColumnIndex("ID")));
				//update T_PARAMETER
				ContentValues cv = new ContentValues();
				cv.put("PARA_VALUE", flag ? "1" : "0");
				db.update("T_PARAMETER", cv, "PARA_CODE=?",
						new String[] { Constants.PARAM_REMEMBER_ME });
			} else {
				//insert T_PARAMETER
				ContentValues cv = new ContentValues();
				cv.put("PARA_CODE", Constants.PARAM_REMEMBER_ME);
				cv.put("PARA_VALUE", flag ? "1" : "0");
				db.insert("T_PARAMETER", null, cv);
			}
			db.setTransactionSuccessful();
			cursor.close();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			throw new RuntimeException(e);
		} finally {
			db.endTransaction();
		}
	}

	public void deleteLocalVip() {
		db.beginTransaction();
		try {
			db.delete("T_PARAMETER", "PARA_CODE=?",
					new String[] { Constants.PARAM_VIP_NO });
			db.delete("T_PARAMETER", "PARA_CODE=?",
					new String[] { Constants.PARAM_VIP_PASSWORD });
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			throw new RuntimeException(e);
		} finally {
			db.endTransaction();
		}
	}

	public String[] getLocalVipPwd() {
		Cursor cursor = db.rawQuery(
				"select * from T_PARAMETER where PARA_CODE = ?",
				new String[] { Constants.PARAM_VIP_NO });
		//query vip_no
		String vip_no = null;
		if (cursor.moveToNext()) {
			vip_no = cursor.getString(cursor.getColumnIndex("PARA_VALUE"));
		}
		cursor.close();

		//query password
		cursor = db.rawQuery("select * from T_PARAMETER where PARA_CODE = ?",
				new String[] { Constants.PARAM_VIP_PASSWORD });
		String password = null;
		if (cursor.moveToNext()) {
			password = cursor.getString(cursor.getColumnIndex("PARA_VALUE"));
		}
		cursor.close();
		return new String[] { vip_no, password };
	}

	/**
	 * when register or login ,store values
	 * @param vip_no
	 * @param password
	 */
	public void saveLocalVipPwd(String vip_no, String password) {
		db.beginTransaction();
		try {
			Cursor cursor = db.rawQuery(
					"select * from T_PARAMETER where PARA_CODE = ?",
					new String[] { Constants.PARAM_VIP_NO });
			if (cursor.moveToNext()) {
				//				int id = cursor.getInt(cursor.getShort(cursor.getColumnIndex("ID")));
				//update vip_no
				ContentValues cv = new ContentValues();
				cv.put("PARA_VALUE", vip_no);
				db.update("T_PARAMETER", cv, "PARA_CODE=?",
						new String[] { Constants.PARAM_VIP_NO });

				//update password
				cv = new ContentValues();
				cv.put("PARA_VALUE", password);
				db.update("T_PARAMETER", cv, "PARA_CODE=?",
						new String[] { Constants.PARAM_VIP_PASSWORD });
			} else {
				//insert vip_no
				ContentValues cv = new ContentValues();
				cv.put("PARA_CODE", Constants.PARAM_VIP_NO);
				cv.put("PARA_VALUE", vip_no);
				db.insert("T_PARAMETER", null, cv);

				//insert password
				cv = new ContentValues();
				cv.put("PARA_CODE", Constants.PARAM_VIP_PASSWORD);
				cv.put("PARA_VALUE", password);
				db.insert("T_PARAMETER", null, cv);
			}

			db.setTransactionSuccessful();
			cursor.close();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			throw new RuntimeException(e);
		} finally {
			db.endTransaction();
		}
	}

}
