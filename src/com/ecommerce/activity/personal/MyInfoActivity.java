package com.ecommerce.activity.personal;

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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVip;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.StringUtils;

public class MyInfoActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = MyInfoActivity.class.getSimpleName();
	private ViewGroup rl_my_info_bank_card;
	private EditText et_myinfo_phone_number;
	private EditText et_myinfo_username;
	private EditText et_myinfo_email;
	private EditText et_myinfo_idcard_no;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private VipService vipService;
	private TVip vipDB =null;
	private ProgressDialog pd;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		setTitle(R.string.title_activity_my_info);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		rl_my_info_bank_card = (ViewGroup) findViewById(R.id.rl_my_info_bank_card);
		rl_my_info_bank_card.setOnClickListener(this);

		//et_myinfo_phone_number
		et_myinfo_phone_number = (EditText) findViewById(R.id.et_myinfo_phone_number);

		//et_myinfo_username
		et_myinfo_username = (EditText) findViewById(R.id.et_myinfo_username);

		//et_myinfo_email
		et_myinfo_email = (EditText) findViewById(R.id.et_myinfo_email);

		//et_myinfo_idcard_no
		et_myinfo_idcard_no = (EditText) findViewById(R.id.et_myinfo_idcard_no);

		//setRightButton
		setRightButtonVisibility(View.VISIBLE);
		setRightButton(R.string.my_info_submit, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*final Intent intent = new Intent(MainActivity.this,
						PersonalActivity.class);
				startActivity(intent);*/
				String name = et_myinfo_username.getText().toString();
				if (StringUtils.isEmpty(name)) {
					CommonUtils.showAlertDialog(MyInfoActivity.this, "姓名不能为空");
					et_myinfo_username.requestFocus();
					return;
				}

				String id_card = et_myinfo_idcard_no.getText().toString();
				if (StringUtils.isEmpty(id_card)) {
					CommonUtils.showAlertDialog(MyInfoActivity.this, "身份证不能为空");
					et_myinfo_idcard_no.requestFocus();
					return;
				}
				
				if (!Pattern.compile("^([0-9]{15}$|^[0-9]{18}$|^[0-9]{17}([0-9]|X|x))$").matcher(id_card)
						.matches()) {
					CommonUtils.showAlertDialog(MyInfoActivity.this, "请输入15位或18位身份证");
					et_myinfo_idcard_no.requestFocus();
					return;
				}
				
				if (StringUtils.isEmpty(id_card)) {
					CommonUtils.showAlertDialog(MyInfoActivity.this, "身份证不能为空");
					et_myinfo_idcard_no.requestFocus();
					return;
				}
				
				

				TVip vip = new TVip();
				if(vipDB!=null){
					vip.setId(vipDB.getId());
				}
				vip.setName(name);
				vip.setId_card(id_card);
				updateVip(vip);
			}
		});

	}

	private void updateVip(final TVip vip) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_processing_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipService.updateVip(vip);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerUpdateVip.sendMessage(msg);
			}
		});
	}

	private Handler handlerUpdateVip = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					/*Toast.makeText(MyInfoActivity.this, "保存成功",
							Toast.LENGTH_SHORT).show();*/
					CommonUtils.showAlertDialog(MyInfoActivity.this,
							"保存成功");
					/*final Intent intent = new Intent(MyInfoActivity.this,
							PersonalActivity.class);
					startActivity(intent);*/
				} else {
					CommonUtils.showAlertDialog(MyInfoActivity.this,
							jsonObj.getMsg());
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MyInfoActivity.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

	private void getVip(final Integer vipId) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_processing_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipService.getVip(vipId);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetVip.sendMessage(msg);
			}
		});
	}

	private Handler handlerGetVip = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					vipDB = (TVip) jsonObj.getValue();
					et_myinfo_phone_number.setText(vipDB.getVip_no());
					et_myinfo_username.setText(vipDB.getName());
					et_myinfo_idcard_no.setText(vipDB.getId_card());
				} else {
					Toast.makeText(MyInfoActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MyInfoActivity.this, errJsonObj.getMsg(),
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
		vipService = ServiceUtils.getInstance(this).getVipService();

		getVip(Constants.vipSession.getId());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_my_info_bank_card:
			startActivity(new Intent(this, MyInfoBankCardActivity.class));
			break;
		default:
			break;
		}

	}

}
