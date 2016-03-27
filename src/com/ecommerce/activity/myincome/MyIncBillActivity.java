package com.ecommerce.activity.myincome;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.message.MessageActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVipWithdrawFlow;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipWithdrawFlowService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.NetWorkUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyIncBillActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {
	private static final String TAG = MyIncBillActivity.class.getSimpleName();
	private PullToRefreshListView pullist_inc_bill_view;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private int pageCount = 15;
	private int page = 1;
	private VipWithdrawFlowService vipWithdrawFlowService;
	private IncBillAdapter incBillAdapter;
	private TVipWithdrawFlow vipWithdrawFlow = new TVipWithdrawFlow();
	/**no network,no data notify**/
	private ViewGroup rl_no_network;
	private ViewGroup rl_loading_data;
	private ViewGroup rl_no_data;
	private ImageView iv_no_network;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_inc_bill);
		setTitle(R.string.title_activity_my_inc_bill);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		// pullist_productlistView
		pullist_inc_bill_view = (PullToRefreshListView) findViewById(R.id.pullist_inc_bill_view);
		pullist_inc_bill_view.setMode(Mode.BOTH);
		pullist_inc_bill_view.setOnItemClickListener(this);
		incBillAdapter = new IncBillAdapter(this);
		pullist_inc_bill_view.setAdapter(incBillAdapter);
		pullist_inc_bill_view
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e("TAG", "onPullDownToRefresh");
						incBillAdapter.clear();
						page = 1;
						getVipIncomeDetailList();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e("TAG", "onPullUpToRefresh");
						//这里写上拉加载更多的任务
						//						new GetDataTask().execute();
						getVipIncomeDetailList();
					}
				});

		//get vipWithdrawFlow list

		//rl_no_network
		rl_no_network = (ViewGroup) findViewById(R.id.rl_no_network);
		//rl_loading_data
		rl_loading_data = (ViewGroup) findViewById(R.id.rl_loading_data);

		//iv_no_network
		iv_no_network = (ImageView) findViewById(R.id.iv_no_network);
		iv_no_network.setOnClickListener(this);

		//rl_no_data
		rl_no_data = (ViewGroup) findViewById(R.id.rl_no_data);
		rl_no_data.setOnClickListener(this);
	}

	public void getVipIncomeDetailList() {
		/*if (!NetWorkUtil.isConnected(this)) {
			rl_no_network.setVisibility(View.VISIBLE);
			return;
		}*/
		rl_loading_data.setVisibility(View.VISIBLE);
		rl_no_data.setVisibility(View.GONE);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipWithdrawFlowService.getVipWithdrawFlowList(
							vipWithdrawFlow, page, pageCount, null, null);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetVipIncomeDetailList.sendMessage(msg);
			}

		});
	}

	private Handler handlerGetVipIncomeDetailList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			rl_loading_data.setVisibility(View.GONE);
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					LinkedList<TVipWithdrawFlow> dataList = (LinkedList<TVipWithdrawFlow>) jsonObj
							.getValue();
					if (dataList != null && !dataList.isEmpty()) {
						for (TVipWithdrawFlow item : dataList) {
							incBillAdapter.addItemLast(item);
						}
						page = page + 1;
					} else {
						if (page == 1) {
//							rl_no_data.setVisibility(View.VISIBLE);
						} else {
							Toast.makeText(
									MyIncBillActivity.this,
									getString(R.string.pull_to_refresh_no_more_date_label),
									Toast.LENGTH_SHORT).show();
						}
					}

					incBillAdapter.notifyDataSetChanged();
					rl_no_network.setVisibility(View.GONE);
				} else {
					Toast.makeText(MyIncBillActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				
				pullist_inc_bill_view.onRefreshComplete();
				if (incBillAdapter.getCount() == 0) {
					rl_no_data.setVisibility(View.VISIBLE);
				}
				
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MyIncBillActivity.this, errJsonObj.getMsg(),
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
		// TODO Auto-generated method stub
		vipWithdrawFlowService = ServiceUtils.getInstance(this)
				.getVipWithdrawFlowService();
		vipWithdrawFlow.setVipId(Constants.vipSession.getId());
		getVipIncomeDetailList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_no_network:
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			break;
		case R.id.rl_no_data:
			getVipIncomeDetailList();
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.pullist_inc_bill_view:
			//			TVipWithdrawFlow item = (TVipWithdrawFlow) incBillAdapter.getItem(position);
			//			productTypeWin.dismiss();
			//			Toast.makeText(getBaseContext(), pType.getName(), Toast.LENGTH_LONG)
			//					.show();
			break;
		default:
			break;
		}

	}

}
