package com.ecommerce.activity.personal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.DetailActivity;
import com.ecommerce.activity.LoginActivity;
import com.ecommerce.activity.ModifyPasswordActivity;
import com.ecommerce.activity.RegisterActivity;
import com.ecommerce.activity.myincome.MyIncomeActivity;
import com.ecommerce.activity.order.OrderGuideActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TAppRelease;
import com.ecommerce.model.WebviewObject;
import com.ecommerce.service.DownloadAppService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.VersionUtils;

public class PersonalActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = PersonalActivity.class.getSimpleName();
	private ViewGroup rl_my_info;
	private ViewGroup rl_my_group;
	private ViewGroup rl_my_qrcode;
	private ViewGroup rl_feedback;
	private ViewGroup rl_contact_us;
	private ViewGroup rl_change_pwd;
	private ViewGroup rl_upgrade_version;
	private ViewGroup ll_exit;
	private TextView tv_personal_login;
	private TextView tv_personal_register;
	private ViewGroup rl_personal_myinfo;
	private TextView tv_personal_myinfo_vip_no;
	private ViewGroup rl_sale_agreement;
	private ViewGroup rl_register_agreement;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private VipService vipService;
	private DownloadAppService downloadAppService;
	private TextView tv_new_version_flag;
	private TAppRelease appRelease;
	private ViewGroup rl_my_income;
	private ViewGroup rl_my_order;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		setTitle(R.string.title_activity_personal);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		setLeftBackButtonVisibility(View.GONE);
		
		ll_exit = (ViewGroup) findViewById(R.id.ll_exit);
		ll_exit.setOnClickListener(this);

		rl_change_pwd = (ViewGroup) findViewById(R.id.rl_change_pwd);
		rl_change_pwd.setOnClickListener(this);

		rl_my_qrcode = (ViewGroup) findViewById(R.id.rl_my_qrcode);
		rl_my_qrcode.setOnClickListener(this);

		rl_my_info = (ViewGroup) findViewById(R.id.rl_my_info);
		rl_my_info.setOnClickListener(this);

		rl_my_group = (ViewGroup) findViewById(R.id.rl_my_group);
		rl_my_group.setOnClickListener(this);
		
		rl_my_income = (ViewGroup) findViewById(R.id.rl_my_income);
		rl_my_income.setOnClickListener(this);
		
		rl_my_order = (ViewGroup) findViewById(R.id.rl_my_order);
		rl_my_order.setOnClickListener(this);

		rl_feedback = (ViewGroup) findViewById(R.id.rl_feedback);
		rl_feedback.setOnClickListener(this);

		rl_contact_us = (ViewGroup) findViewById(R.id.rl_contact_us);
		rl_contact_us.setOnClickListener(this);

		rl_upgrade_version = (ViewGroup) findViewById(R.id.rl_upgrade_version);
		rl_upgrade_version.setOnClickListener(this);

		tv_new_version_flag = (TextView) findViewById(R.id.tv_new_version_flag);

		tv_personal_login = (TextView) findViewById(R.id.tv_personal_login);
		tv_personal_login.setOnClickListener(this);

		tv_personal_register = (TextView) findViewById(R.id.tv_personal_register);
		tv_personal_register.setOnClickListener(this);
		

		//set visibility of components
		rl_personal_myinfo = (ViewGroup) findViewById(R.id.rl_personal_myinfo);
		tv_personal_myinfo_vip_no = (TextView) findViewById(R.id.tv_personal_myinfo_vip_no);
		if (Constants.vipSession != null) {
			rl_personal_myinfo.setVisibility(View.VISIBLE);
			tv_personal_login.setVisibility(View.GONE);
			tv_personal_register.setVisibility(View.GONE);
			tv_personal_myinfo_vip_no.setText(Constants.vipSession.getVip_no());
		}

		//rl_sale_agreement
		rl_sale_agreement = (ViewGroup) findViewById(R.id.rl_sale_agreement);
		rl_sale_agreement.setOnClickListener(this);

		//rl_register_agreement
		rl_register_agreement = (ViewGroup) findViewById(R.id.rl_register_agreement);
		rl_register_agreement.setOnClickListener(this);
	}

	@Override
	public void initParameters() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		vipService = ServiceUtils.getInstance(this).getVipService();
		downloadAppService = ServiceUtils.getInstance(this)
				.getDownloadAppService();
		//check new version
		getAppRelease();
	}

	private void getAppRelease() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = downloadAppService
							.getAppRelease(Constants.ANDROID_IDENTITY);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetAppRelease.sendMessage(msg);
			}
		});
	}

	private Handler handlerGetAppRelease = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					appRelease = (TAppRelease) jsonObj.getValue();
					if (appRelease != null) {
						int versionCode = VersionUtils
								.getVerCode(PersonalActivity.this);
						if (appRelease.getVerNo() > versionCode) {
							tv_new_version_flag.setVisibility(View.VISIBLE);
						} else {
							tv_new_version_flag.setVisibility(View.GONE);
						}
					}
				} else {
					/*Toast.makeText(PersonalActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();*/
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				/*Toast.makeText(PersonalActivity.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();*/
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

	private void loginOut() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipService.logout();
					vipService.rememberMe(false);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerLoginOut.sendMessage(msg);
			}
		});
	}

	private Handler handlerLoginOut = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					rl_personal_myinfo.setVisibility(View.GONE);
					tv_personal_login.setVisibility(View.VISIBLE);
					tv_personal_register.setVisibility(View.VISIBLE);
					Toast.makeText(PersonalActivity.this, "退出登录成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(PersonalActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(PersonalActivity.this, errJsonObj.getMsg(),
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
		case R.id.ll_exit:
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("是否退出账号?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									loginOut();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).create().show();
			break;
		case R.id.rl_change_pwd:
			final Intent forgotPwdIntent = new Intent(this,
					ModifyPasswordActivity.class);
			startActivity(forgotPwdIntent);
			break;
		case R.id.rl_my_info:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			startActivity(new Intent(this, MyInfoActivity.class));
			break;
		case R.id.rl_my_group:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			startActivity(new Intent(this, MyGroupActivity.class));
			break;
		case R.id.rl_my_order:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			startActivity(new Intent(this, OrderGuideActivity.class));
			break;
		case R.id.rl_my_income:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			startActivity(new Intent(this, MyIncomeActivity.class));
			break;
		case R.id.rl_my_qrcode:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			startActivity(new Intent(this, MyQRCodeActivity.class));
			break;
		case R.id.rl_feedback:
			startActivity(new Intent(this, FeedbackActivity.class));
			break;
		case R.id.rl_contact_us:
			startActivity(new Intent(this, AboutUsActivity.class));
			break;
		case R.id.rl_upgrade_version:
			int versionCode = VersionUtils.getVerCode(PersonalActivity.this);
			if (appRelease == null || appRelease.getVerNo() <= versionCode) {
				Toast.makeText(this, "当前版本已经是最新版本.", Toast.LENGTH_SHORT).show();
				break;
			}
			
			//UpgradeVerDialog
			UpgradeVerDialog dialog = new UpgradeVerDialog();
			Bundle args = new Bundle();
			args.putSerializable("key", appRelease);
			dialog.setArguments(args);
			dialog.show(getSupportFragmentManager(),
					UpgradeVerDialog.class.getName());
			break;
		case R.id.tv_personal_login:
			startActivity(new Intent(this, LoginActivity.class));
			break;
		case R.id.tv_personal_register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		case R.id.rl_sale_agreement:
			String url = ConfigReader.getInstance(this).getRemoteUrl(
					"/index.php?r=sale/sale-agreement/view");
			WebviewObject obj = new WebviewObject();
			obj.setUrl(url);
			Intent intent = new Intent(this, DetailActivity.class);
			Bundle extras = new Bundle();
			extras.putSerializable("key", obj);
			intent.putExtras(extras);
			startActivity(intent);
			break;
		case R.id.rl_register_agreement:
			String url_usage_rights = ConfigReader.getInstance(this)
					.getRemoteUrl(
							"/index.php?r=sale/sale-agreement/view-rights");
			WebviewObject obj_usage_rights = new WebviewObject();
			obj_usage_rights.setUrl(url_usage_rights);
			Intent intent_usage_rights = new Intent(this, DetailActivity.class);
			Bundle extras_usage_rights = new Bundle();
			extras_usage_rights.putSerializable("key", obj_usage_rights);
			intent_usage_rights.putExtras(extras_usage_rights);
			startActivity(intent_usage_rights);
			break;
		default:
			break;
		}

	}

}
