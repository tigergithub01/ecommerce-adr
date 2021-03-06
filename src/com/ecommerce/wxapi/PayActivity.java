package com.ecommerce.wxapi;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TSoSheet;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.MD5;
import com.ecommerce.utils.StringUtils;
import com.ecommerce.utils.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PayActivity extends BaseActivity {
	private static final String TAG = PayActivity.class.getSimpleName();
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private TSoSheet soSheet = null;
	private String orderId = null;
	private OrderService orderService;

	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	TextView show;
	Map<String, String> resultunifiedorder;
	StringBuffer sb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		setTitle(R.string.title_activity_pay_wx);

		initParameters();
		initComponents();
		initData();

	}

	@Override
	public void initComponents() {
		//生成prepay_id
		Button payBtn = (Button) findViewById(R.id.unifiedorder_btn);
		payBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
				getPrepayId.execute();
			}
		});
		Button appayBtn = (Button) findViewById(R.id.appay_btn);
		appayBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendPayReq();
			}
		});

		//生成签名参数
		Button appay_pre_btn = (Button) findViewById(R.id.appay_pre_btn);
		appay_pre_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				genPayReq();
			}
		});

	}

	@Override
	public void initParameters() {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			orderId = extras.getString("orderId");
			/*url = extras.getString("url");*/
			//					CommonUtils.showAlertDialog(this, orderId);
		}

		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
		//init wxpay
		show = (TextView) findViewById(R.id.editText_prepay_id);
		req = new PayReq();
		sb = new StringBuffer();

		msgApi.registerApp(Constants.APP_ID);
		
		//init order service
		orderService = ServiceUtils.getInstance(this)
				.getOrderService();
		if(StringUtils.isNotEmpty(orderId)){
			getSoSheet(Integer.valueOf(orderId));
		}
	}

	private void getSoSheet(final Integer id) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = orderService.getOrder(id);
					if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
						soSheet = (TSoSheet) jsonObj
								.getValue();
					}
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
			}
		});
	}

	/**
	 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes());
		Log.e("orion", appSign);
		return appSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PayActivity.this,
					getString(R.string.app_tip),
					getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
			show.setText(sb.toString());

			//			StringBuilder sbResult = new StringBuilder();
			for (java.util.Iterator<String> iter = result.keySet().iterator(); iter
					.hasNext();) {
				String key = iter.next();
				String value = result.get(key);
				sb.append(key + ":" + value + "\n\n");
				show.setText(sb.toString());
			}

			resultunifiedorder = result;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", entity);
			Map<String, String> xml = decodeXml(entity);
			/*byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String, String> xml = decodeXml(content);*/

			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						//实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	//
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
//			packageParams.add(new BasicNameValuePair("body", soSheet.getProductName()));
			packageParams.add(new BasicNameValuePair("body", "衣服"));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", ConfigReader
					.getInstance(this).getRemoteUrl(
							"/notify/wx/jsapi/wxNotify.php")));
//			packageParams.add(new BasicNameValuePair("out_trade_no",
//					soSheet.getCode()));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					"SO-20150828-00198"));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
//			packageParams.add(new BasicNameValuePair("total_fee", String.valueOf(soSheet.getOrderAmt()*100)));
			packageParams.add(new BasicNameValuePair("total_fee", "1"));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}

	}

	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n" + req.sign + "\n\n");

		show.setText(sb.toString());

		Log.e("orion", signParams.toString());

	}

	private void sendPayReq() {

		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

}
