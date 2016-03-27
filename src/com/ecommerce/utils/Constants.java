package com.ecommerce.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Activity;

import com.ecommerce.model.PhoneInfo;
import com.ecommerce.model.TVip;

public class Constants {
//	public static final String JSESSIONID = "JSESSIONID";
	public static final String JSESSIONID = "PHPSESSID";
	
	public final static int MSG_WHAT_SUCCESS = 0;
	public final static int MSG_WHAT_FAILED = -1;
	public final static int MSG_WHAT_PROCESSING = 1;
	
	public final static int AJAX_STATUS_SUCESS = 1;
	public final static int AJAX_STATUS_FAILED = -1;
	//TODO:should define different error code.not only success & failed.
	

	public final static String NOTIFY_MSG = "msg";

	public final static int URL_REMOTE = 0;
	public final static int URL_LOCAL = 1;

	public final static int NOTITICATION_AUDIO_ID = 4564555;
	public final static int NOTITICATION_DOWNLOAD_ID = 4564556;

	public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	public final static String DATE_HOUR_FORMAT = "MM/dd HH:mm";
	
	public final static String DECIMAL_FORMAT = "0.00";
	

	public final static String FLAG_STR_YES = "1";
	public final static String FLAG_STR_NO = "0";
	
	public final static int FLAG_YES = 1;
	public final static int FLAG_NO = 0;

	public static String sessionId = null;
	public static Cookie sessionCookie = null;
	public static List<Cookie> sessionCookieList = null;
	public static TVip vipSession = null;
	
	public static final String FIRST_WORD_ETC = "...";
	
	//local parameters
	public static final String PARAM_VIP_NO = "VIP_NO";
	public static final String PARAM_VIP_PASSWORD = "VIP_PASSWORD";
	public static final String PARAM_REMEMBER_ME = "REMEMBER_ME";
	
	public static String PARAMETER_PAGE = "page";
	public static String PARAMETER_ROWS = "rows"; 
	public static String PARAMETER_RTN_NODE = "rows"; 
	public static String SORT_ASC = "asc"; 
	public static String SORT_DESC = "desc"; 
	
	
	
	
	public static int WIFI =1; 
	public static int WAP = 2; 
	public static int CMNET = 3; 
	
	public static int DEBUG_MODE = 0;
	public static String XDEBUG_SESSION_START = "ECLIPSE_DBGP";
	public static String XDEBUG_KEY = "14346197733872";
	/*public static String XDEBUG_SESSION_START = "ECLIPSE_DBGP";
	public static String XDEBUG_KEY = "14347556656671";*/
	
	
	//android application identity
	public static final int ANDROID_IDENTITY=1;
	
	//sms delay seconds
	public static int SMS_DELAY_SECONDS = 60;
	
	//微信开放平台申请的appID
	public static String WEBCHAT_APP_ID="wx1378c2f68caa332d";
//	public static String WEBCHAT_APP_ID_FORMAL="wx1378c2f68caa332d";
	public static String WEBCHAT_APP_ID_FORMAL="wx2bfbd48633b358a1";
	
	//微信支付
	public static final String APP_ID = "wx2bfbd48633b358a1";
//	public static final String APP_ID = "wx2bfbd48633b358a1";
	//商户号
	public static final String MCH_ID = "1266869501";//yum
	//  API密钥，在商户平台设置
	public static final  String API_KEY="zffXJL8978xjl4rHKlm47TYIddddT8Ik";//yum
	
	//sms
	/*public static String SMS_ACCOUNT = "cf_tiger_guo1";
	public static String SMS_PASSWORD = "e10adc3949ba59abbe56e057f20f883e";*/
	public static String SMS_ACCOUNT = "cf_zff";
	public static String SMS_PASSWORD = "79f882747fef15b3ddf87a6b20c9eaaf";
	
	public static String SMS_URL = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	
	//host url
//	public static String HOST_URL="http://172.16.33.252:8085";
	public static String HOST_URL="http://192.168.1.104:8085";
//	public static String HOST_URL="http://127.0.0.1:8085";
//	public static String HOST_URL="http://10.0.0.7:8085";
//	public static String HOST_URL_FORMAL="http://120.24.245.164";
	public static String HOST_URL_FORMAL="http://www.mantanghao.com";
//	public static String HOST_URL_FORMAL="http://app.mantanghao.com";
	
	public static List<Activity> activityList = new ArrayList<Activity>();
	
	public static PhoneInfo phoneInfo = null;
	
	
	

}
