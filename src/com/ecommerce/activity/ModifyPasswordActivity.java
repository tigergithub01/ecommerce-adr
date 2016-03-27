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

public class ModifyPasswordActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = ModifyPasswordActivity.class
			.getSimpleName();
	private EditText et_modify_pwd_phone;
	private EditText et_modify_pwd_vf_code;
	private EditText et_modify_pwd_password;
	private TextView tv_modify_pwd_confirm;
	private TextView tv_modify_pwd_vf_code_get;
	private SmsService smsService;
	private VipService vipService;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private int delaySeconds = Constants.SMS_DELAY_SECONDS;
	private final static int MSG_WHAT_SMS = 100;
	private ProgressDialog pd; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_password);
		setTitle(R.string.title_activity_modify_password);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		//et_register_phone_number
		et_modify_pwd_phone = (EditText) findViewById(R.id.et_modify_pwd_phone);
//		et_modify_pwd_phone.setEnabled(false);
		if(Constants.vipSession!=null){
			et_modify_pwd_phone.setText(Constants.vipSession.getVip_no());
		}

		//et_register_phone_number
		et_modify_pwd_vf_code = (EditText) findViewById(R.id.et_modify_pwd_vf_code);

		//et_register_phone_number
		et_modify_pwd_password = (EditText) findViewById(R.id.et_modify_pwd_password);

		//tv_modify_pwd_confirm
		tv_modify_pwd_confirm = (TextView) findViewById(R.id.tv_modify_pwd_confirm);
		tv_modify_pwd_confirm.setOnClickListener(this);

		//tv_modify_pwd_vf_code_get
		tv_modify_pwd_vf_code_get = (TextView) findViewById(R.id.tv_modify_pwd_vf_code_get);
		tv_modify_pwd_vf_code_get.setOnClickListener(this);

	}

	@Override
	public void initParameters() {
		// TODO Auto-generated method stub

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
					CommonUtils.showAlertDialog(ModifyPasswordActivity.this,
							"短信验证码已经发送");
					/*tv_register_get_vfcode.setEnabled(false);
					disableVerifyBtn();*/
				} else {
					CommonUtils.showAlertDialog(ModifyPasswordActivity.this,
							rtnObj[1]);
				}
				break;
			case MSG_WHAT_SMS:
				tv_modify_pwd_vf_code_get.setText("(" + (delaySeconds) + "s)"
						+ getString(R.string.register_vfcode_get_text));
				if (delaySeconds == 0) {
					tv_modify_pwd_vf_code_get
							.setText(getString(R.string.register_vfcode_get_text));
					tv_modify_pwd_vf_code_get.setEnabled(true);
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

	private void updatePwd(final TVip vip) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_processing_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					JsonObj jsonObj = vipService.updatePwd(vip);
					msg.obj = jsonObj;
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					Bundle data = new Bundle();
					data.putString(Constants.NOTIFY_MSG, e.getMessage());
					msg.setData(data);
				}
				handlerUpdatePwd.sendMessage(msg);
			}
		});
	}

	private Handler handlerUpdatePwd = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					Toast.makeText(ModifyPasswordActivity.this, "密码修改成功",
							Toast.LENGTH_SHORT).show();
					final Intent intent = new Intent(
							ModifyPasswordActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					CommonUtils.showAlertDialog(ModifyPasswordActivity.this,
							jsonObj.getMsg());
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
		case R.id.tv_modify_pwd_vf_code_get:
			String vipNo = et_modify_pwd_phone.getText().toString();
			if (StringUtils.isEmpty(vipNo)) {
				CommonUtils.showAlertDialog(ModifyPasswordActivity.this,
						"手机号不能为空");
				et_modify_pwd_phone.requestFocus();
			} else {
				tv_modify_pwd_vf_code_get.setEnabled(false);
				disableVerifyBtn();
				getSms(vipNo);
			}

			break;
		case R.id.tv_modify_pwd_confirm:
			TVip vip = new TVip();
			String vip_No = et_modify_pwd_phone.getText().toString();
			if (StringUtils.isEmpty(vip_No)) {
				CommonUtils.showAlertDialog(this, "手机号不能为空");
				et_modify_pwd_phone.requestFocus();
				break;
			}

			if (!Pattern.compile("^1[0-9]{10}$").matcher(vip_No).matches()) {
				CommonUtils.showAlertDialog(this, "手机号必须为1开头的11位纯数字");
				et_modify_pwd_phone.requestFocus();
				break;
			}

			String verifyCode = et_modify_pwd_vf_code.getText().toString();
			if (StringUtils.isEmpty(verifyCode)) {
				CommonUtils.showAlertDialog(this, "验证码不能为空");
				et_modify_pwd_vf_code.requestFocus();
				break;
			}

			String password = et_modify_pwd_password.getText().toString();
			if (StringUtils.isEmpty(password)) {
				CommonUtils.showAlertDialog(this, "密码不能为空");
				et_modify_pwd_password.requestFocus();
				break;
			}

			if (password.length() < 6 || password.length() > 16) {
				CommonUtils.showAlertDialog(this, "请输入6-16位密码");
				et_modify_pwd_password.requestFocus();
				break;
			}

			vip.setVip_no(vip_No);
			vip.setVerifyCode(verifyCode);
			vip.setPassword(password);
			updatePwd(vip);
			break;
		default:
			break;
		}
	}

}
