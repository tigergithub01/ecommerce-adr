package com.ecommerce.activity.personal;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TBankInfo;
import com.ecommerce.model.TVipBankcard;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipBankcardService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.StringUtils;

public class MyInfoBankCardActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private static final String TAG = MyInfoBankCardActivity.class
			.getSimpleName();
	private View rootView;
	private EditText et_bankcard_no;
	private TextView tv_bank_name;
	private EditText et_bank_branch_name;
	private EditText et_bank_address;
	private TVipBankcard vipBankcardDB;
	private VipBankcardService vipBankcardService;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	//layout activity_product_type item
	private ListView lv_bank_info;
	private BankInfoAdapter bankInfoAdapter;
	private PopupWindow bankInfoWindow;
//	private LinkedList<TBankInfo> bankInfoList;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_my_info_bank_card);
		rootView = getLayoutInflater().inflate(R.layout.activity_my_info_bank_card,
				null);
		setContentView(rootView);
		setTitle(R.string.title_activity_my_info_bank_card);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_bank_name:
			createBankInfoPopUpWin(v);
			break;
		default:
			break;
		}
	}
	
	private void createBankInfoPopUpWin(View anchor) {
		bankInfoWindow = new PopupWindow(getBaseContext());
		View bankInfoView = getLayoutInflater().inflate(
				R.layout.activity_bank_info, null);
		bankInfoWindow.setContentView(bankInfoView);
		bankInfoWindow.setWidth(LayoutParams.MATCH_PARENT);
		bankInfoWindow.setHeight(LayoutParams.WRAP_CONTENT);
		bankInfoWindow.setOutsideTouchable(false);
		bankInfoWindow.setTouchable(true);
		bankInfoWindow.setFocusable(true);
		//		bankInfoWindow.getBackground().setAlpha(140);
//				bankInfoWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
		bankInfoWindow.showAsDropDown(anchor);
		// set background alpha
		//		CommonUtils.setBackgroundAlpha(getWindow(), 0.5f);
		//		rl_product_list_content.getBackground().setAlpha(100);
		// set background alpha when window dismiss
		bankInfoWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				//TODO:
			}
		});
		
		//initialize product type components
		initBankInfoComponents(bankInfoView);
	}
	
	public void initBankInfoComponents(View view) {
		//lv_product_type
		lv_bank_info = (ListView) view.findViewById(R.id.lv_bank_info);
		lv_bank_info.setOnItemClickListener(this);

		//productTypeAdapter
		lv_bank_info.setAdapter(bankInfoAdapter);
	}
	
	

	@Override
	public void initComponents() {
		//et_bankcard_no
		et_bankcard_no = (EditText) findViewById(R.id.et_bankcard_no);

		//tv_bank_name
		tv_bank_name = (TextView) findViewById(R.id.tv_bank_name);
		tv_bank_name.setOnClickListener(this);

		//et_bank_branch_name
		et_bank_branch_name = (EditText) findViewById(R.id.et_bank_branch_name);

		//et_bank_address
		et_bank_address = (EditText) findViewById(R.id.et_bank_address);

		//setRightButton
		setRightButtonVisibility(View.VISIBLE);
		setRightButton(R.string.my_info_submit, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*final Intent intent = new Intent(MainActivity.this,
						PersonalActivity.class);
				startActivity(intent);*/
				String cardNo = et_bankcard_no.getText().toString();
				if (StringUtils.isEmpty(cardNo)) {
					CommonUtils.showAlertDialog(MyInfoBankCardActivity.this,
							"银行卡号不能为空");
					et_bankcard_no.requestFocus();
					return;
				}
				
				if (!Pattern.compile("^([0-9]{16}|[0-9]{19})$").matcher(cardNo)
						.matches()) {
					CommonUtils.showAlertDialog(MyInfoBankCardActivity.this, "银行卡号是16位或者19位数字");
					et_bankcard_no.requestFocus();
					return;
				}

				Integer bankId = (Integer) tv_bank_name.getTag();
				if (bankId == null) {
					CommonUtils.showAlertDialog(MyInfoBankCardActivity.this,
							"所属银行不能为空");
					tv_bank_name.requestFocus();
					return;
				}

				String branchName = et_bank_branch_name.getText().toString();
				if (StringUtils.isEmpty(branchName)) {
					CommonUtils.showAlertDialog(MyInfoBankCardActivity.this,
							"支行不能为空");
					et_bank_branch_name.requestFocus();
					return;
				}

				String openAddr = et_bank_address.getText().toString();
				if (StringUtils.isEmpty(openAddr)) {
					CommonUtils.showAlertDialog(MyInfoBankCardActivity.this,
							"开户地不能为空");
					et_bank_address.requestFocus();
					return;
				}
				
				//update updateVipBankcard
				TVipBankcard bankcard = new TVipBankcard();
				bankcard.setVipId(Constants.vipSession.getId());
				bankcard.setCardNo(cardNo);
				bankcard.setBankId(bankId);
				bankcard.setBranchName(branchName);
				bankcard.setOpenAddr(openAddr);
				updateVipBankcard(bankcard);
			}
		});
	}

	private void updateVipBankcard(final TVipBankcard bankcard) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_processing_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					if (vipBankcardDB == null) {
						jsonObj = vipBankcardService
								.createVipBankcard(bankcard);
					} else {
						bankcard.setId(vipBankcardDB.getId());
						jsonObj = vipBankcardService
								.updateVipBankcard(bankcard);
					}
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerupdateVipBankcard.sendMessage(msg);
			}
		});
	}

	private Handler handlerupdateVipBankcard = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					vipBankcardDB = (TVipBankcard)jsonObj.getValue();
					/*Toast.makeText(MyInfoBankCardActivity.this, "保存成功",
							Toast.LENGTH_SHORT).show();*/
					CommonUtils.showAlertDialog(MyInfoBankCardActivity.this,
							"保存成功");
					/*final Intent intent = new Intent(MyInfoBankCardActivity.this,
							PersonalActivity.class);
							
					startActivity(intent);*/
				} else {
					CommonUtils.showAlertDialog(MyInfoBankCardActivity.this,
							jsonObj.getMsg());
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MyInfoBankCardActivity.this,
						errJsonObj.getMsg(), Toast.LENGTH_SHORT).show();
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

	private void getVipBankcard(final Integer vipId) {
		pd = ProgressDialog.show(this, null, getString(R.string.common_processing_data_text)); 
		pd.setCancelable(true);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipBankcardService.getVipBankcard(vipId);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetVipBankcard.sendMessage(msg);
			}
		});
	}
	
	

	private Handler handlerGetVipBankcard = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					vipBankcardDB = (TVipBankcard) jsonObj.getValue();
					et_bankcard_no.setText(vipBankcardDB.getCardNo());
					tv_bank_name.setTag(vipBankcardDB.getBankId());
					tv_bank_name.setText(vipBankcardDB.getBankName());
					et_bank_branch_name.setText(vipBankcardDB.getBranchName());
					et_bank_address.setText(vipBankcardDB.getOpenAddr());
				} else {
					Toast.makeText(MyInfoBankCardActivity.this,
							jsonObj.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MyInfoBankCardActivity.this,
						errJsonObj.getMsg(), Toast.LENGTH_SHORT).show();
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

	private void getBankInfoList() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipBankcardService.getBankList();
					/*if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
						bankInfoList = (LinkedList<TBankInfo>) jsonObj
								.getValue();
					}*/
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetBankInfoList.sendMessage(msg);
			}
		});
	}
	
	private Handler handlerGetBankInfoList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					LinkedList<TBankInfo> bankInfoList = (LinkedList<TBankInfo>) jsonObj
							.getValue();
					if (bankInfoList != null && !bankInfoList.isEmpty()) {
						for (TBankInfo item : bankInfoList) {
							bankInfoAdapter.addItem(item);
						}
					}
					bankInfoAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(MyInfoBankCardActivity.this,
							jsonObj.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MyInfoBankCardActivity.this,
						errJsonObj.getMsg(), Toast.LENGTH_SHORT).show();
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
		vipBankcardService = ServiceUtils.getInstance(this)
				.getVipBankcardService();
		getVipBankcard(Constants.vipSession.getId());
		
		//initialize BankInfoAdapter
		bankInfoAdapter = new BankInfoAdapter(this);
		//initialize bank info data list
		getBankInfoList();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.lv_bank_info:
			TBankInfo item = (TBankInfo) bankInfoAdapter
					.getItem(position);
			tv_bank_name.setText(item.getName());
			tv_bank_name.setTag(item.getId());
			bankInfoWindow.dismiss();
			//			Toast.makeText(getBaseContext(), pType.getName(), Toast.LENGTH_LONG)
			//					.show();
			break;
		default:
			break;
		}

	}

}
