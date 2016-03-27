package com.ecommerce.test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ecommerce.utils.HttpUtils;

public class SmsTest extends AndroidTestCase {
	private static final String TAG = SmsTest.class.getSimpleName();

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	/**
	 * 
	 * 
	 * <?xml version="1.0" encoding="utf-8"?>
	<SubmitResult xmlns="http://106.ihuyi.cn/">
	<code>405</code>
	<msg>用户名或密码不正确(U:cf_tiger_guo1 P:e10adc3949ba59abbe56e057f20f883e)</msg>
	<smsid>0</smsid>
	</SubmitResult>
	 */
	public void test() {
		try {
			int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);

			//System.out.println(mobile);

			String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
			String content = new String("您的验证码是：" + mobile_code
					+ "。请不要把验证码泄露给其他人。");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("account", "cf_tiger_guo1");
			map.put("password", "e10adc3949ba59abbe56e057f20f883e");
			map.put("mobile", "13724346621");
			map.put("content", content);
			String result = HttpUtils.getResult(Url, map);
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
				} else if (nodeName.equals("msg")) {
					String msg = property.getNodeValue();
					Log.d(TAG, "msg" + msg);
				} else if (nodeName.equals("smsid")) {
					String smsid = property.getNodeValue();
					Log.d(TAG, "smsid" + smsid);
				}
			}
			Log.d(TAG, "result" + result);
		} catch (Exception e) {
			Log.d(TAG, "err:" + e);
		}

	}
}
