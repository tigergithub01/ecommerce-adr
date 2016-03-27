package com.ecommerce.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetWorkUtil {


	public static String getTelephonyManager(Context cx) {
		String result = null;
		TelephonyManager tm = (TelephonyManager) cx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String str = "/n======/nTelephonyManager/n";
		str += "getDataState() = " + tm.getDataState() + "/n";
		str += "getDeviceId() = " + tm.getDeviceId() + "/n";
		str += "getLine1Number() = " + tm.getLine1Number() + "/n";
		str += "getSimSerialNumber() = " + tm.getSimSerialNumber() + "/n";
		str += "getSubscriberId() = " + tm.getSubscriberId() + "/n";
		str += "isNetworkRoaming() = " + tm.isNetworkRoaming() + "/n";
		result = str.toString();
		return result;
	}

	public static String getWifiInfo(Context mContext) {
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		String s1 = "/n======/nWifiState:/nWIFI_STATE_UNKNOWN/n";
		String s21 = "/n======/nWifiState:/nWIFI_STATE_KNOWN/n";
		StringBuilder stringbuilder = new StringBuilder("/nState:");
		if (wifiManager.getWifiState() != 3) {
			String s2 = s1;
			String s3 = stringbuilder.append(s2).toString();
			String s4 = s3;
			return s4;
		} else {
			return s21.toString();
		}
	}

	public String getActivityManager(Context mContext) {
		String result = null;
		StringBuffer str = new StringBuffer("/n======/nActivityManager/n");
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(mContext.ACTIVITY_SERVICE);
		List list1 = activityManager.getRunningAppProcesses();
		for (int i = 0; i < list1.size(); i++) {
			String b = list1.get(i).toString();
			str.append(b + "/n");
		}
		return str.toString();
	}

	private float getOldBrightness(Context mContext) {
		float brightness;
		try {
			brightness = Settings.System.getInt(mContext.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException snfe) {
			brightness = 255;
		}
		return brightness;
	}

	private String getCpu() {
		ProcessBuilder cmd;
		String result = "";
		try {
			String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) != -1) {
				System.out.println(new String(re));
				result = result + new String(re);
				return result.toString();
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result.toString();
	}

	/*public static String getNetWorkIP(Context mContext) {
		String result = "/n======/n";
		CMDExecute cmdexe = new CMDExecute();
		try {
			String[] args = { "/system/bin/netcfg" };
			result = cmdexe.run(args, "/system/bin/");
		} catch (IOException ex) {
			Log.i("fetch_process_info", "ex=" + ex.toString());
		}
		return result;
	}*/

	public static boolean isConnected(android.content.Context mContext) {
		ConnectivityManager mConnectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null) {
			return false;
		} 
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
				&& !mTelephony.isNetworkRoaming()) {
			return info.isConnected();
		} else {
			return false;
		}
	}
	
	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络  
	 * @param context
	 * @return
	 */
	public int getAPNType(Context context){ 
	int netType = -1; 
	ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
	NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); 
	if(networkInfo==null){ 
	return netType; 
	} 
	int nType = networkInfo.getType(); 
	if(nType==ConnectivityManager.TYPE_MOBILE){ 
	Log.e("networkInfo.getExtraInfo()", "networkInfo.getExtraInfo() is "+networkInfo.getExtraInfo()); 
	if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){ 
	netType = Constants.CMNET; 
	} 
	else{ 
	netType = Constants.WAP; 
	} 
	} 
	else if(nType==ConnectivityManager.TYPE_WIFI){ 
	netType = Constants.WIFI; 
	} 
	return netType; 
	}
}
