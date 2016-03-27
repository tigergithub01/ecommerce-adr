package com.ecommerce.activity;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVip;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.StringUtils;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = LoginActivity.class.getSimpleName();
	private TextView tv_login;
	private TextView tv_forgot_pwd;
	private EditText et_phone_number;
	private EditText et_login_pwd;
	private TextView tv_login_register;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private VipService vipService;
	private CheckBox chkb_autoLogin;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle(R.string.title_activity_login);

		initComponents();
		initParameters();
		initData();
	}

	public void initComponents() {
		//setLeftBackButtonVisibility
		setLeftBackButtonVisibility(View.VISIBLE);

		//setRightButtonVisibility
		setRightButtonVisibility(View.VISIBLE);

		//setRightButton
		setRightButton(R.string.title_activity_register, new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});

		//tv_login
		tv_login = (TextView) findViewById(R.id.tv_login);
		tv_login.setOnClickListener(this);

		//tv_login_register
		tv_login_register = (TextView) findViewById(R.id.tv_login_register);
		tv_login_register.setOnClickListener(this);

		//tv_forgot_pwd
		tv_forgot_pwd = (TextView) findViewById(R.id.tv_forgot_pwd);
		tv_forgot_pwd.setOnClickListener(this);

		//et_phone_number
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);

		//et_login_pwd
		et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);

		//chkb_autoLogin
		chkb_autoLogin = (CheckBox) findViewById(R.id.chkb_autoLogin);
	}

	public void initParameters() {

	}

	public void initData() {
		vipService = ServiceUtils.getInstance(this).getVipService();

		String[] vipPwd = vipService.getLocalVipPwd();
		if (vipService.getRememberMe()) {
			chkb_autoLogin.setChecked(true);
			if (vipPwd != null) {
				et_phone_number.setText(vipPwd[0]);
				et_login_pwd.setText(vipPwd[1]);
			}
		} else {
			if (vipPwd != null) {
				et_phone_number.setText(vipPwd[0]);
			}
			chkb_autoLogin.setChecked(false);
		}
	}

	private void login(final TVip vip) {
		pd = ProgressDialog.show(this, null,
				getString(R.string.common_processing_data_text));
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipService.login(vip);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerLogin.sendMessage(msg);
			}
		});
	}

	private Handler handlerLogin = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					Toast.makeText(LoginActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
					if (chkb_autoLogin.isChecked()) {
						vipService.rememberMe(true);
						vipService.saveLocalVipPwd(et_phone_number.getText()
								.toString(), et_login_pwd.getText().toString());
					} else {
						vipService.rememberMe(false);
					}
					final Intent intent = new Intent(LoginActivity.this,
							HostActivity.class);
					startActivity(intent);
				} else {
					CommonUtils.showAlertDialog(LoginActivity.this,
							jsonObj.getMsg());
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(LoginActivity.this, errJsonObj.getMsg(),
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
		switch (v.getId()) {
		case R.id.tv_login:

			TVip vip = new TVip();
			String vip_No = et_phone_number.getText().toString();
			if (StringUtils.isEmpty(vip_No)) {
				CommonUtils.showAlertDialog(this, "手机号不能为空");
				et_phone_number.requestFocus();
				break;
			}

			String password = et_login_pwd.getText().toString();
			if (StringUtils.isEmpty(password)) {
				CommonUtils.showAlertDialog(this, "密码不能为空");
				et_login_pwd.requestFocus();
				break;
			}

			vip.setVip_no(vip_No);
			vip.setPassword(password);
			login(vip);
			break;
		case R.id.tv_login_register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		case R.id.tv_forgot_pwd:
			startActivity(new Intent(this, ModifyPasswordActivity.class));
			break;
		default:
			break;
		}
	}
}
