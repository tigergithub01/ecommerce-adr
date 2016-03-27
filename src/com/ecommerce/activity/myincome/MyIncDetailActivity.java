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
import com.ecommerce.model.TVipIncomeDetail;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipIncomeService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.NetWorkUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyIncDetailActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private static final String TAG = MyIncDetailActivity.class.getSimpleName();
	private PullToRefreshListView pullist_inc_detail_view;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private int pageCount = 15;
	private int page = 1;
	private VipIncomeService vipIncomeService;
	private VipIncomeDetailAdapter vipIncomeDetailAdapter;
	private TVipIncomeDetail vipIncomeDetail = new TVipIncomeDetail();

	/**no network,no data notify**/
	private ViewGroup rl_no_network;
	private ViewGroup rl_loading_data;
	private ViewGroup rl_no_data;
	private ImageView iv_no_network;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_inc_detail);
		setTitle(R.string.title_activity_my_inc_detail);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		// pullist_productlistView
		pullist_inc_detail_view = (PullToRefreshListView) findViewById(R.id.pullist_inc_detail_view);
		pullist_inc_detail_view.setMode(Mode.BOTH);
		pullist_inc_detail_view.setOnItemClickListener(this);
		vipIncomeDetailAdapter = new VipIncomeDetailAdapter(this);
		pullist_inc_detail_view.setAdapter(vipIncomeDetailAdapter);
		pullist_inc_detail_view
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						vipIncomeDetailAdapter.clear();
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
					jsonObj = vipIncomeService.getVipIncomeDetailList(
							vipIncomeDetail, page, pageCount, null, null);
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
					LinkedList<TVipIncomeDetail> dataList = (LinkedList<TVipIncomeDetail>) jsonObj
							.getValue();
					if (dataList != null && !dataList.isEmpty()) {
						for (TVipIncomeDetail item : dataList) {
							vipIncomeDetailAdapter.addItemLast(item);
						}
						page = page + 1;
					} else {
						if (page == 1) {
//							rl_no_data.setVisibility(View.VISIBLE);
						} else {
							Toast.makeText(
									MyIncDetailActivity.this,
									getString(R.string.pull_to_refresh_no_more_date_label),
									Toast.LENGTH_SHORT).show();
						}
					}

					vipIncomeDetailAdapter.notifyDataSetChanged();
					rl_no_network.setVisibility(View.GONE);
				} else {
					Toast.makeText(MyIncDetailActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				
				pullist_inc_detail_view.onRefreshComplete();
				if (vipIncomeDetailAdapter.getCount() == 0) {
					rl_no_data.setVisibility(View.VISIBLE);
				}
				
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MyIncDetailActivity.this, errJsonObj.getMsg(),
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
		vipIncomeService = ServiceUtils.getInstance(this).getVipIncomeService();
		vipIncomeDetail.setVipId(Constants.vipSession.getId());
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
		case R.id.pullist_inc_detail_view:
			//			TVipIncomeDetail item = (TVipIncomeDetail) vipIncomeDetailAdapter.getItem(position);
			//			productTypeWin.dismiss();
			//			Toast.makeText(getBaseContext(), pType.getName(), Toast.LENGTH_LONG)
			//					.show();
			break;
		default:
			break;
		}

	}

}
