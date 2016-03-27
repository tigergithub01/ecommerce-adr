package com.ecommerce.activity.myincome;

import java.text.DecimalFormat;
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
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.DetailActivity;
import com.ecommerce.activity.personal.MyInfoActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVipIncome;
import com.ecommerce.model.WebviewObject;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipIncomeService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;

public class MyIncomeActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = MyIncomeActivity.class.getSimpleName();
	private ViewGroup rl_inc_total_detail;
	private TextView tv_inc_settle;
	private TextView tv_inc_bill_list;
	private TextView tv_inc_balance;
	private TextView tv_inc_total_income;
	private TextView tv_my_inc_all_income;
	private TextView tv_my_inc_balance_detail;
	private TextView tv_my_inc_can_withdraw;
	private VipIncomeService vipIncomeService;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private TVipIncome vipIncome;
	private ProgressDialog pd;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_income);
		setTitle(R.string.title_activity_my_income);

		initComponents();
		initParameters();
		initData();
	}

	public void initComponents() {
		//rl_inc_total_detail
		rl_inc_total_detail = (ViewGroup) findViewById(R.id.rl_inc_total_detail);
		rl_inc_total_detail.setOnClickListener(this);

		//tv_inc_settle
		tv_inc_settle = (TextView) findViewById(R.id.tv_inc_settle);
		tv_inc_settle.setOnClickListener(this);

		//tv_inc_bill_list
		tv_inc_bill_list = (TextView) findViewById(R.id.tv_inc_bill_list);
		tv_inc_bill_list.setOnClickListener(this);

		//tv_inc_balance
		tv_inc_balance = (TextView) findViewById(R.id.tv_inc_balance);

		//tv_inc_total_income
		tv_inc_total_income = (TextView) findViewById(R.id.tv_inc_total_income);

		//tv_my_inc_all_income
		tv_my_inc_all_income = (TextView) findViewById(R.id.tv_my_inc_all_income);

		//tv_inc_balance_detail
		tv_my_inc_balance_detail = (TextView) findViewById(R.id.tv_my_inc_balance_detail);
		
		//tv_my_inc_can_withdraw
		tv_my_inc_can_withdraw= (TextView) findViewById(R.id.tv_my_inc_can_withdraw);

	}

	public void initParameters() {

	}

	public void initData() {
		vipIncomeService = ServiceUtils.getInstance(this).getVipIncomeService();
		getVipIncome(Constants.vipSession.getId());
	}

	private void getVipIncome(final Integer vipId) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_loading_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipIncomeService.getVipIncome(vipId);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerVipIncome.sendMessage(msg);
			}
		});
	}

	private Handler handlerVipIncome = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					vipIncome = (TVipIncome) jsonObj.getValue();

					DecimalFormat df = new DecimalFormat(
							Constants.DECIMAL_FORMAT);
					tv_inc_balance.setText(df.format(vipIncome
							.getCanSettleAmt()));
					tv_inc_total_income
							.setText(df.format(vipIncome.getAmount()));
					tv_my_inc_all_income.setText(df.format(vipIncome
							.getAmount()));
					tv_my_inc_balance_detail.setText(df.format(vipIncome
							.getCanSettleAmt()));
					tv_my_inc_can_withdraw.setText(df.format(vipIncome
							.getCanWithdrawAmt()));
				} else {
					Toast.makeText(MyIncomeActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MyIncomeActivity.this, errJsonObj.getMsg(),
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
		case R.id.rl_inc_total_detail:
			/*final Intent intent = new Intent(this, MyIncomeActivity.class);
			startActivity(intent);*/
			startActivity(new Intent(this, MyIncDetailActivity.class));
			break;
		case R.id.tv_inc_settle:
			if(vipIncome==null || vipIncome.getCanWithdrawAmt()==null || vipIncome.getCanWithdrawAmt().doubleValue()<=0.0d){
				CommonUtils.showAlertDialog(this,
						"您的可提现金额为零，不能提现。");
				return;
			}
			Intent intent = new Intent(this, WithdrawCashActivity.class);
			Bundle extras = new Bundle();
			extras.putSerializable("key", vipIncome);
			intent.putExtras(extras);
			startActivity(intent);
			
			/*startActivity(new Intent(this, WithdrawCashActivity.class));*/
			break;
		case R.id.tv_inc_bill_list:
			startActivity(new Intent(this, MyIncBillActivity.class));
			break;
		default:
			break;
		}

	}
}
