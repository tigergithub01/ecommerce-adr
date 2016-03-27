package com.ecommerce.service;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.HttpUtils;

public class SmsService extends BaseService {
	private static final String TAG = SmsService.class.getSimpleName();

	public SmsService(Context context) {
		super(context);
	}

	public String[] getSmsCode(String vipNo) {
		String[] rtnObj = new String[3];
		try {
			int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
			String Url = Constants.SMS_URL;
			String content = new String("您的验证码是：" + mobile_code
					+ "。请不要把验证码泄露给其他人。");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("account", Constants.SMS_ACCOUNT);
			map.put("password", Constants.SMS_PASSWORD);
			map.put("mobile", vipNo);
			map.put("content", content);
			String result = HttpUtils.getResult(Url, map);
			Log.d(TAG, "result" + result);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance(); //取得DocumentBuilderFactory实例  
			DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例  
			Document doc = builder.parse(new ByteArrayInputStream(result
					.getBytes("UTF-8"))); //解析输入流 得到Document实例  
			Element root = doc.getDocumentElement();
			NodeList properties = root.getChildNodes();
			for (int j = 0; j < properties.getLength(); j++) {
				Node property = properties.item(j);
				String nodeName = property.getNodeName();
				if (nodeName.equals("code")) {
					String code = property.getTextContent();
					Log.d(TAG, "code" + code);
					rtnObj[0] = code;

				} else if (nodeName.equals("msg")) {
					String msg = property.getTextContent();
					rtnObj[1] = msg;
					Log.d(TAG, "msg" + msg);
				} else if (nodeName.equals("smsid")) {
					String smsid = property.getTextContent();
					Log.d(TAG, "smsid" + smsid);
				}
			}
			rtnObj[2] = String.valueOf(mobile_code);
			
			//send successfully
			if (rtnObj[0].equals("2")) {
				//insert mobile_code into database 
				String smsCreateUrl =  ConfigReader.getInstance(context).getRemoteUrl("/index.php?r=/api/sms/create");
				Map<String, Object> mapSms = new HashMap<String, Object>();
				mapSms.put("PhoneVerifyCode[phone_number]", vipNo);
				mapSms.put("PhoneVerifyCode[verify_code]", rtnObj[2]);
				mapSms.put("PhoneVerifyCode[sms_content]", content);
				String smsResult = HttpUtils.getResult(smsCreateUrl, mapSms); 
				Log.d(TAG, "smsResult" + smsResult);
			}
		} catch (Exception e) {
			Log.d(TAG, "err:" + e);
			rtnObj[0] = "-100";
//			rtnObj[1] = e.getMessage();
			rtnObj[1] = ExceptionHandler.getMessage(e).toString();
		}
		return rtnObj;
	}
}
