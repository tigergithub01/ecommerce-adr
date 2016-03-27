package com.ecommerce.activity.personal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TSysFeedback;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.SysFeedbackService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.StringUtils;

public class FeedbackActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = FeedbackActivity.class.getSimpleName();
	private EditText et_feedback_content;
	private EditText et_feedback_contact_method;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private SysFeedbackService sysFeedbackService;
	private ProgressDialog pd; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		setTitle(R.string.title_activity_feedback);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		//et_feedback_content
		et_feedback_content = (EditText) findViewById(R.id.et_feedback_content);

		//et_feedback_contact_method
		et_feedback_contact_method = (EditText) findViewById(R.id.et_feedback_contact_method);

		//setRightButton
		setRightButtonVisibility(View.VISIBLE);
		setRightButton(R.string.my_info_submit, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*final Intent intent = new Intent(MainActivity.this,
						PersonalActivity.class);
				startActivity(intent);*/
				String content = et_feedback_content.getText().toString();
				if (StringUtils.isEmpty(content)) {
					CommonUtils.showAlertDialog(FeedbackActivity.this,
							"反馈内容不能为空");
					et_feedback_content.requestFocus();
					return;
				}

				String contactMethod = et_feedback_contact_method.getText()
						.toString();
				if (StringUtils.isEmpty(contactMethod)) {
					CommonUtils.showAlertDialog(FeedbackActivity.this,
							"反馈联系方式不能为空");
					et_feedback_contact_method.requestFocus();
					return;
				}

				TSysFeedback sysFeedback = new TSysFeedback();
				sysFeedback.setContent(content);
				sysFeedback.setContactMethod(contactMethod);
				createSysFeedback(sysFeedback);
			}
		});
	}

	private void createSysFeedback(final TSysFeedback sysFeedback) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_processing_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = sysFeedbackService.createSysFeedback(sysFeedback);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerCreateSysFeedback.sendMessage(msg);
			}
		});
	}

	private Handler handlerCreateSysFeedback = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					Toast.makeText(FeedbackActivity.this, "提交成功",
							Toast.LENGTH_SHORT).show();
					final Intent intent = new Intent(FeedbackActivity.this,
							PersonalActivity.class);
					startActivity(intent);
					finish();
				} else {
					CommonUtils.showAlertDialog(FeedbackActivity.this,
							jsonObj.getMsg());
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(FeedbackActivity.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void initParameters() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		sysFeedbackService = ServiceUtils.getInstance(this)
				.getSysFeedbackService();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
