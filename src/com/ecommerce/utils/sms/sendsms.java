package com.ecommerce.utils.sms;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ecommerce.utils.HttpUtils;

public class sendsms {
	
	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	
	
	
	public static void main(String [] args) {
		
//		HttpClient client = new HttpClient(); 
//		PostMethod method = new PostMethod(Url); 
			
		//client.getParams().setContentCharset("GBK");		
//		client.getParams().setContentCharset("UTF-8");
//		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		
		int mobile_code = (int)((Math.random()*9+1)*100000);

		//System.out.println(mobile);
		
	    String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。"); 
	    
	    Map<String, Object> map = new HashMap<String,Object>();
		map.put("account", "13724346621");
		map.put("password", "123456");
		map.put("mobile", "13724346621");
		map.put("content", content);
	    String result = HttpUtils.getResult(content, map);
	    
	    
	    
	    
//	    XmlPullParser parser = Xml.newPullParser();
//	    int eventType = parser.getEventType();  
//	    
	    
	    
	   /* DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例  
        DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例  
        Document doc = builder.parse(new ByteArrayInputStream(result.getBytes("UTF-8")));   //解析输入流 得到Document实例  
        Element root = doc.getDocumentElement();*/
        /*root.get
        root.getElementsByTagName("code")
        String code = root.getElementsByTagName(name) root.getNodeValue("code");	
		String msg = root.getNodeValue("msg");	
		String smsid = root.getNodeValue("smsid");	
		 if("2".equals(code)){
				System.out.println("短信提交成功");
			}*/
        /*
		 if("2".equals(code)){
			System.out.println("短信提交成功");
		}*/

		/*NameValuePair[] data = {//提交短信
			    new NameValuePair("account", "用户名"), 
			    new NameValuePair("password", "密码"), //密码可以使用明文密码或使用32位MD5加密
			    //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
			    new NameValuePair("mobile", "手机号码"), 
			    new NameValuePair("content", content),
		};
		
		method.setRequestBody(data);	*/	
		
		
		/*try {
			client.executeMethod(method);	
			
			String SubmitResult =method.getResponseBodyAsString();
					
			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult); 
			Element root = doc.getRootElement();


			String code = root.elementText("code");	
			String msg = root.elementText("msg");	
			String smsid = root.elementText("smsid");	
			
			
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
						
			 if("2".equals(code)){
				System.out.println("短信提交成功");
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/
		
	}
	
}