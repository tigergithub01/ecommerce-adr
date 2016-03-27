package com.ecommerce.activity.message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TNotification;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.Constants;

public class MessageDetailActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = MessageDetailActivity.class
			.getSimpleName();
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private NotificationService notificationService;
	private Integer notificationId = null;
	private TextView tv_message_title;
	private WebView wv_message_content;
	private ProgressDialog pd;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_detail);
		setTitle(R.string.title_activity_message_detail);

		initParameters();
		initComponents();
		initData();
	}

	@Override
	public void initComponents() {
		//tv_message_title
		tv_message_title = (TextView) findViewById(R.id.tv_message_title);

		//wv_message_content
		wv_message_content = (WebView) findViewById(R.id.wv_message_content);

	}

	@Override
	public void initParameters() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			notificationId = extras.getInt("notificationId");
			Log.i(TAG, "notificationId:" + notificationId);
		}

	}

	@Override
	public void initData() {
		notificationService = ServiceUtils.getInstance(this)
				.getNotificationService();
		if (notificationId != null) {
			getNotification(notificationId);
		}
	}

	private void getNotification(final Integer id) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_loading_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = notificationService.getNotification(id);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetNotification.sendMessage(msg);
			}
		});
	}

	private Handler handlerGetNotification = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					TNotification notification = (TNotification) jsonObj
							.getValue();
					tv_message_title.setText(notification.getTitle());
					wv_message_content.getSettings()
							.setDefaultTextEncodingName("UTF-8");
					wv_message_content.loadData(notification.getContent(),
							"text/html;charset=UTF-8", "UTF-8");
//					wv_message_content.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl)
				} else {
					Toast.makeText(MessageDetailActivity.this,
							jsonObj.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MessageDetailActivity.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
