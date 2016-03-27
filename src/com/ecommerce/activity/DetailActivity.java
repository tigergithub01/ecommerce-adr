package com.ecommerce.activity;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TSoSheet;
import com.ecommerce.model.WebviewObject;
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

@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class DetailActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = DetailActivity.class.getSimpleName();
	private WebView wbv_detail;
	private ProgressBar pgb_webview_loading;
	private WebviewObject webViewObject;

	//wx pay
	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
//	TextView show;
	Map<String, String> resultunifiedorder;
	StringBuffer sb;

	//order information
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private TSoSheet soSheet = null;
	private OrderService orderService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		setTitle(R.string.title_activity_detail);

		initParameters();
		initComponents();
		initData();
	}

	@Override
	public void initComponents() {

		setBarVisibility(View.GONE);

		//pgb_webview_loading
		pgb_webview_loading = (ProgressBar) findViewById(R.id.pgb_webview_loading);
		pgb_webview_loading.setMax(100);

		/*WebView wv = (WebView)findViewById(R.id.webview) ;

		String content = getUnicodeContent() ;

		wv.getSettings().setDefaultTextEncodingName(“UTF -8”) ;

		wv.loadData(content, “text/html”, “UTF-8”) ;*/

		//wbv_detail
		String url = webViewObject.getUrl();
		wbv_detail = (WebView) findViewById(R.id.wbv_detail);
		synSessionCookie(url, this);

		wbv_detail.setWebViewClient(new MyWebViewClient());
		wbv_detail.setWebChromeClient(new MyWebChromeClient());

		WebSettings webSettings = wbv_detail.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultTextEncodingName("utf-8");
		wbv_detail.addJavascriptInterface(new JsAndroidObject(this),
				"androidJs");

		if (StringUtils.isNotEmpty(url)) {
			wbv_detail.loadUrl(url);
		}

	}

	public void synSessionCookie(String url, Context context) {
		if (StringUtils.isEmpty(url)) {
			return;
		}
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		List<Cookie> sessionCookieList = Constants.sessionCookieList;
		if (sessionCookieList != null) {
			for (Cookie sessionCookie : sessionCookieList) {
				if (sessionCookie != null) {
					String cookieString = sessionCookie.getName() + "="
							+ sessionCookie.getValue() + "; domain="
							+ sessionCookie.getDomain() + ";path="
							+ sessionCookie.getPath() + ";expiry="
							+ sessionCookie.getExpiryDate();
					Log.i(TAG, "cookieString:" + cookieString);
					cookieManager.setCookie(url, cookieString);
					CookieSyncManager.getInstance().sync();
				}
			}
		}
	}

	@Override
	public void initParameters() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			webViewObject = (WebviewObject) extras.getSerializable("key");
			/*url = extras.getString("url");*/
			Log.i(TAG, "url:" + webViewObject.getUrl());
		}

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		//init wxpay
//		show = (TextView) findViewById(R.id.editText_prepay_id);
		req = new PayReq();
		sb = new StringBuffer();

		msgApi.registerApp(Constants.APP_ID);

		//init order service
		orderService = ServiceUtils.getInstance(this).getOrderService();
		/*if(StringUtils.isNotEmpty(orderId)){
			getSoSheet(Integer.valueOf(orderId));
		}*/
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			synSessionCookie(url, getBaseContext());
			Log.i(TAG, "shouldOverrideUrlLoading:" + url);
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i(TAG, "onPageStarted:" + url);
			//			etSheetDetailWebUrl.setText(url);
			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.i(TAG, "onPageFinished:" + url);
			view.loadUrl("javascript:setBrowseFlag('"
					+ Constants.ANDROID_IDENTITY + "')");
			super.onPageFinished(view, url);

		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onReceivedError--errorCode:" + errorCode
					+ "-description-" + description + "-failingUrl:"
					+ failingUrl);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();
			//			super.onReceivedSslError(view, handler, error);
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			Log.i(TAG, "shouldInterceptRequest:" + url);
			return null;
		}

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			Log.i(TAG, "shouldOverrideKeyEvent:");
			return super.shouldOverrideKeyEvent(view, event);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			Log.i(TAG, "onLoadResource:" + url);
			super.onLoadResource(view, url);
		}
	}

	class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			Log.i(TAG, "onProgressChanged:" + newProgress);
			pgb_webview_loading.setProgress(newProgress);
			if (newProgress == 100) {
				pgb_webview_loading.setProgress(0);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
		}

		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			super.onReceivedIcon(view, icon);
		}

		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			//			return super.onJsConfirm(view, url, message, result);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					DetailActivity.this)
					.setTitle(getText(R.string.dialog_tip))
					.setMessage(message)
					.setPositiveButton(R.string.dialog_ok,
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm();
								}
							})
					.setNegativeButton(R.string.dialog_cancel,
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									result.cancel();
								}
							});
			builder.setCancelable(false);
			builder.create();
			builder.show();
			return true;
		}

		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, final JsPromptResult result) {
			//			return super.onJsPrompt(view, url, message, defaultValue, result);
			final EditText et = new EditText(view.getContext());
			et.setSingleLine();
			et.setText(defaultValue);

			AlertDialog.Builder builder = new AlertDialog.Builder(
					DetailActivity.this)
					.setTitle(getText(R.string.dialog_tip))
					.setMessage(message)
					.setPositiveButton(R.string.dialog_ok,
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm(et.getText().toString());
								}
							})
					.setNegativeButton(R.string.dialog_cancel,
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									result.cancel();
								}
							});
			//			builder.setCancelable(false);  
			builder.create();
			builder.show();
			return true;
		}

		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					DetailActivity.this)
					.setTitle(getText(R.string.dialog_tip))
					.setMessage(message)
					.setPositiveButton(R.string.dialog_ok,
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									result.cancel();
								}
							});
			builder.setCancelable(false);
			builder.create();
			builder.show();
			return true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wbv_detail.canGoBack()) {
			wbv_detail.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class JsAndroidObject {
		Context context;

		public JsAndroidObject(Context context) {
			this.context = context;
		}

		@JavascriptInterface
		public void goBack() {
			if (wbv_detail.canGoBack()) {
				wbv_detail.goBack();
			} else {
				DetailActivity.this.finish();
			}
		}
		
		@JavascriptInterface
		public void finish() {
			DetailActivity.this.finish();
		}

		@JavascriptInterface
		public void wxPay(final String orderId) {
			/*Intent intent = new Intent(context, PayActivity.class);
			Bundle extras = new Bundle();
			extras.putString("orderId", orderId); 
			intent.putExtras(extras);
			//			getSoSheet(Integer.valueOf(orderId));
			startActivity(intent);*/
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
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetSoSheet.sendMessage(msg);
			}
		});
	}

	private Handler handlerGetSoSheet = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					soSheet = (TSoSheet) jsonObj.getValue();
					if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
						//execute resign
						if (soSheet != null) {
							GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
							getPrepayId.execute();
						}
					}
				} else {
					Toast.makeText(DetailActivity.this,
							jsonObj.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(DetailActivity.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

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
		try {  
			return new String(sb.toString().getBytes(), "ISO8859-1");
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
		return null;
		
//		return sb.toString();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(DetailActivity.this,
					getString(R.string.app_tip),
					getString(R.string.pay_processing));
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
//			show.setText(sb.toString());

			//			StringBuilder sbResult = new StringBuilder();
			/*for (java.util.Iterator<String> iter = result.keySet().iterator(); iter
					.hasNext();) {
				String key = iter.next();
				String value = result.get(key);
				sb.append(key + ":" + value + "\n\n");
//				show.setText(sb.toString());
			}*/

			resultunifiedorder = result;
			String prepayId = result.get("prepay_id");
			if(StringUtils.isNotEmpty(prepayId)){
				genPayReq();
			}else{
				//TODO: show error
			}
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

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String, String> xml = decodeXml(content);

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

	//TODO
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", soSheet.getProductName()));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", ConfigReader
					.getInstance(this).getRemoteUrl(
							"/notify/wx/sdk/wxNotify.php")));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					soSheet.getCode()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int)(soSheet.getOrderAmt().doubleValue()*100))));
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
		ProgressDialog dialog = ProgressDialog.show(DetailActivity.this,
				getString(R.string.app_tip),
				getString(R.string.pay_processing));
		dialog.setCancelable(true);
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

//		show.setText(sb.toString());

		Log.e("orion", signParams.toString());
		
		sendPayReq();
		if(dialog!=null){
			dialog.dismiss();
		}
	}

	private void sendPayReq() {

		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

}
