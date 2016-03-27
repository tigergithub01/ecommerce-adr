package com.ecommerce.activity.myincome;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.ModifyPasswordActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVipIncome;
import com.ecommerce.model.TVipWithdrawFlow;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipWithdrawFlowService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.StringUtils;

public class WithdrawCashActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = WithdrawCashActivity.class
			.getSimpleName();
	private EditText et_withdraw_cash_amount;
	private VipWithdrawFlowService vipWithdrawFlowService;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private TextView tv_withdraw_cash_submit;
	private TVipIncome vipIncome;
	private TextView tv_withdraw_cash_max_amount;
	private ProgressDialog pd; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_cash);
		setTitle(R.string.title_activity_withdraw_cash);

		initParameters();
		initComponents();
		initData();
	}

	@Override
	public void initComponents() {
		//et_withdraw_cash_amount
		et_withdraw_cash_amount = (EditText) findViewById(R.id.et_withdraw_cash_amount);

		//tv_withdraw_cash_max_amount
		tv_withdraw_cash_max_amount = (TextView) findViewById(R.id.tv_withdraw_cash_max_amount);
		if (vipIncome != null && vipIncome.getCanSettleAmt() != null) {
			DecimalFormat df = new DecimalFormat(Constants.DECIMAL_FORMAT);
			tv_withdraw_cash_max_amount.setText(df.format(vipIncome
					.getCanWithdrawAmt()));
		}

		//tv_withdraw_cash_submit
		tv_withdraw_cash_submit = (TextView) findViewById(R.id.tv_withdraw_cash_submit);
		tv_withdraw_cash_submit.setOnClickListener(this);
	}

	@Override
	public void initParameters() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			vipIncome = (TVipIncome) extras.getSerializable("key");
		}
	}

	@Override
	public void initData() {
		vipWithdrawFlowService = ServiceUtils.getInstance(this)
				.getVipWithdrawFlowService();
	}

	private void createVipWithdrawFlow(final TVipWithdrawFlow vipWithdrawFlow) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_processing_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipWithdrawFlowService
							.createVipWithdrawFlow(vipWithdrawFlow);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerCreateVipWithdrawFlow.sendMessage(msg);
			}
		});
	}

	private Handler handlerCreateVipWithdrawFlow = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					/*CommonUtils.showAlertDialog(WithdrawCashActivity.this,
							"提现申请成功");*/
					new AlertDialog.Builder(WithdrawCashActivity.this)
					.setTitle(R.string.common_dialog_title)
					.setMessage("提现申请成功")
					.setPositiveButton(R.string.common_dialog_confirm,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									final Intent intent = new Intent(WithdrawCashActivity.this,
											MyIncomeActivity.class);
									startActivity(intent);
									finish();
								}
							}).create().show();
					
					/*Toast.makeText(WithdrawCashActivity.this, "提现申请成功",
							Toast.LENGTH_SHORT).show();*/
					/*final Intent intent = new Intent(WithdrawCashActivity.this,
							MyIncomeActivity.class);
					startActivity(intent);
					finish();*/
				} else {
					CommonUtils.showAlertDialog(WithdrawCashActivity.this,
							jsonObj.getMsg());
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(WithdrawCashActivity.this, errJsonObj.getMsg(),
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
		case R.id.tv_withdraw_cash_submit:

			TVipWithdrawFlow vipWithdrawFlow = new TVipWithdrawFlow();
			String amount = et_withdraw_cash_amount.getText().toString();
			if (StringUtils.isEmpty(amount)) {
				CommonUtils.showAlertDialog(this, "提现金额不能为空");
				et_withdraw_cash_amount.requestFocus();
				break;
			}

			if (Double.valueOf(amount) < 50) {
				CommonUtils.showAlertDialog(this, "提现金额必须大于等于50元");
				et_withdraw_cash_amount.requestFocus();
				break;
			}

			vipWithdrawFlow.setAmount(Double.valueOf(amount));
			createVipWithdrawFlow(vipWithdrawFlow);

			break;
		case R.id.tv_forgot_pwd:
			final Intent forgotPwdIntent = new Intent(this,
					ModifyPasswordActivity.class);
			startActivity(forgotPwdIntent);
			break;
		default:
			break;
		}

	}

}
