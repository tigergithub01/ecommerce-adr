package com.ecommerce.activity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVip;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.SmsService;
import com.ecommerce.service.VipService;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.StringUtils;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private TextView tv_register_get_vfcode;
	private TextView tv_register;
	private SmsService smsService;
	private VipService vipService;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private EditText et_register_phone_number;
	private EditText et_register_verify_code;
	private EditText et_register_pwd;
	private EditText et_register_recommend_phone;
	private int delaySeconds = Constants.SMS_DELAY_SECONDS;
	private final static int MSG_WHAT_SMS = 100;
	private ProgressDialog pd; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setTitle(R.string.title_activity_register);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		//tv_register_get_vfcode
		tv_register_get_vfcode = (TextView) findViewById(R.id.tv_register_get_vfcode);
		tv_register_get_vfcode.setOnClickListener(this);

		//tv_register
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_register.setOnClickListener(this);

		//et_register_phone_number
		et_register_phone_number = (EditText) findViewById(R.id.et_register_phone_number);

		//et_register_phone_number
		et_register_verify_code = (EditText) findViewById(R.id.et_register_verify_code);

		//et_register_phone_number
		et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);

		//et_register_phone_number
		et_register_recommend_phone = (EditText) findViewById(R.id.et_register_recommend_phone);
	}

	@Override
	public void initParameters() {

	}

	@Override
	public void initData() {
		smsService = ServiceUtils.getInstance(this).getSmsService();
		vipService = ServiceUtils.getInstance(this).getVipService();
	}

	private void getSms(final String vipNo) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					String[] rtnObj = smsService.getSmsCode(vipNo);
					msg.obj = rtnObj;
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					Bundle data = new Bundle();
					data.putString(Constants.NOTIFY_MSG, e.getMessage());
					msg.setData(data);
				}
				handlerSms.sendMessage(msg);
			}
		});
	}

	private void disableVerifyBtn() {
		//start timer for 1 minite
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				delaySeconds--;
				handlerSms.sendEmptyMessage(MSG_WHAT_SMS);
				if (delaySeconds == 0) {
					cancel();
				}

			}
		}, new java.util.Date(), 1000);
	}

	private Handler handlerSms = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				String[] rtnObj = (String[]) msg.obj;
				if (rtnObj[0].equals("2")) {
					CommonUtils.showAlertDialog(RegisterActivity.this,
							"短信验证码已经发送");
					/*tv_register_get_vfcode.setEnabled(false);
					disableVerifyBtn();*/
				} else {
					CommonUtils.showAlertDialog(RegisterActivity.this,
							rtnObj[1]);
				}
				break;
			case MSG_WHAT_SMS:
				tv_register_get_vfcode.setText("(" + (delaySeconds) + "s)"
						+ getString(R.string.register_vfcode_get_text));
				if (delaySeconds == 0) {
					tv_register_get_vfcode
							.setText(getString(R.string.register_vfcode_get_text));
					tv_register_get_vfcode.setEnabled(true);
					delaySeconds = Constants.SMS_DELAY_SECONDS;
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				Log.d(TAG,
						"--error messgage--"
								+ msg.getData().getString(Constants.NOTIFY_MSG));
				break;
			default:
				break;
			}

		}
	};

	private void register(final TVip vip) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_processing_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					JsonObj jsonObj = vipService.register(vip);
					msg.obj = jsonObj;
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					Bundle data = new Bundle();
					data.putString(Constants.NOTIFY_MSG, e.getMessage());
					msg.setData(data);
				}
				handlerRegister.sendMessage(msg);
			}
		});
	}

	private Handler handlerRegister = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT)
							.show();
					final Intent intent = new Intent(RegisterActivity.this, HostActivity.class);
					startActivity(intent);
				} else {
					CommonUtils.showAlertDialog(RegisterActivity.this, jsonObj.getMsg());
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				Log.d(TAG,
						"--error messgage--"
								+ msg.getData().getString(Constants.NOTIFY_MSG));
				break;
			default:
				break;
			}

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_register_get_vfcode:
			String vipNo = et_register_phone_number.getText().toString();
			if (StringUtils.isEmpty(vipNo)) {
				CommonUtils.showAlertDialog(RegisterActivity.this, "手机号不能为空");
				et_register_phone_number.requestFocus();
			} else {
				tv_register_get_vfcode.setEnabled(false);
				disableVerifyBtn();
				getSms(vipNo);
			}

			break;
		case R.id.tv_register:
			TVip vip = new TVip();
			String vip_No = et_register_phone_number.getText().toString();
			if (StringUtils.isEmpty(vip_No)) {
				CommonUtils.showAlertDialog(this, "手机号不能为空");
				et_register_phone_number.requestFocus();
				//				Toast.makeText(getBaseContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
				break;
			}

			if (!Pattern.compile("^1[0-9]{10}$").matcher(vip_No).matches()) {
				CommonUtils.showAlertDialog(this, "手机号必须为1开头的11位纯数字");
				et_register_phone_number.requestFocus();
				break;
			}

			String verifyCode = et_register_verify_code.getText().toString();
			if (StringUtils.isEmpty(verifyCode)) {
				CommonUtils.showAlertDialog(this, "验证码不能为空");
				et_register_verify_code.requestFocus();
				break;
			}

			String password = et_register_pwd.getText().toString();
			if (StringUtils.isEmpty(password)) {
				CommonUtils.showAlertDialog(this, "密码不能为空");
				et_register_pwd.requestFocus();
				break;
			}

			if (password.length() < 6 || password.length() > 16) {
				CommonUtils.showAlertDialog(this, "请输入6-16位密码");
				et_register_pwd.requestFocus();
				break;
			}

			String parent_vip_no = et_register_recommend_phone.getText()
					.toString();
			if (StringUtils.isEmpty(parent_vip_no)) {
				CommonUtils.showAlertDialog(this, "推荐人手机号不能为空");
				et_register_recommend_phone.requestFocus();
				break;
			}

			if (!Pattern.compile("^1[0-9]{10}$").matcher(parent_vip_no)
					.matches()) {
				CommonUtils.showAlertDialog(this, "推荐人手机号必须为1开头的11位纯数字");
				et_register_recommend_phone.requestFocus();
				break;
			}

			vip.setVip_no(vip_No);
			vip.setVerifyCode(verifyCode);
			vip.setPassword(password);
			vip.setParent_vip_no(parent_vip_no);
			register(vip);
			break;
		default:
			break;
		}
	}

}
