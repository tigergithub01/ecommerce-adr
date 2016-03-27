package com.ecommerce.activity.earn;

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
import com.ecommerce.model.TEarnGuild;
import com.ecommerce.service.EarnGuildService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.Constants;

public class EarnGuildDetailActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = EarnGuildDetailActivity.class
			.getSimpleName();
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private EarnGuildService earnGuildService;
	private Integer earnGuildId = null;
	private TextView tv_earn_guild_title;
	private WebView wv_earn_guild_content;
	private ProgressDialog pd;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_earn_guild_detail);
		setTitle(R.string.title_activity_earn_guild_detail);

		initParameters();
		initComponents();
		initData();
	}

	@Override
	public void initComponents() {
		//tv_earn_guild_title
		tv_earn_guild_title = (TextView) findViewById(R.id.tv_earn_guild_title);

		//wv_earn_guild_content
		wv_earn_guild_content = (WebView) findViewById(R.id.wv_earn_guild_content);

	}

	@Override
	public void initParameters() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			earnGuildId = extras.getInt("earnGuildId");
			Log.i(TAG, "earnGuildId:" + earnGuildId);
		}

	}

	@Override
	public void initData() {
		earnGuildService = ServiceUtils.getInstance(this)
				.getEarnGuildService();
		if (earnGuildId != null) {
			getEarnGuild(earnGuildId);
		}
	}

	private void getEarnGuild(final Integer id) {
//		pd = ProgressDialog.show(this, getString(R.string.common_dialog_title), getString(R.string.common_loading_data_text)); 
		pd = ProgressDialog.show(this, null, getString(R.string.common_loading_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = earnGuildService.getEarnGuild(id);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetEarnGuild.sendMessage(msg);
			}
		});
	}

	private Handler handlerGetEarnGuild = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					TEarnGuild notification = (TEarnGuild) jsonObj
							.getValue();
					tv_earn_guild_title.setText(notification.getTitle());
					wv_earn_guild_content.getSettings()
							.setDefaultTextEncodingName("UTF-8");
					wv_earn_guild_content.loadData(notification.getContent(),
							"text/html;charset=UTF-8", "UTF-8");
//					wv_message_content.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl)
				} else {
					Toast.makeText(EarnGuildDetailActivity.this,
							jsonObj.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(EarnGuildDetailActivity.this, errJsonObj.getMsg(),
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
