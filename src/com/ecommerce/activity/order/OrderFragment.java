package com.ecommerce.activity.order;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TSoSheet;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.NetWorkUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class OrderFragment extends Fragment implements OnItemClickListener,
		OnClickListener {
	private static final String TAG = OrderFragment.class.getSimpleName();
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private PullToRefreshListView plist_orderListView;
	private OrderAdapter orderAdapter;
	private int pageCount = 15;
	private int page = 1;
	private OrderService orderService;
	private TSoSheet soSheet = new TSoSheet();

	/**no network,no data notify**/
	private ViewGroup rl_no_network;
	private ViewGroup rl_loading_data;
	private ViewGroup rl_no_data;
	private ImageView iv_no_network;

	//	private static int refreshCnt = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.order_fragment, container, false);

		initParameters(view);
		initComponents(view);
		initData(view);
		return view;
	}

	public void initParameters(View view) {
		Bundle parameters = getArguments();
		if (parameters != null) {
			Integer status = parameters.getInt("status");
			if (status == 0) {
				soSheet.setStatus(null);
			} else {
				soSheet.setStatus(status);
			}

			Log.d(TAG, "--parameters--" + status);
		}
	}

	public void initData(View view) {
		/*service = ServiceUtils.getInstance(getActivity().getBaseContext())
				.getMyTaskService();*/
		page=1;
		orderService = ServiceUtils.getInstance(getActivity().getBaseContext())
				.getOrderService();
		soSheet.setVipId(Constants.vipSession.getId());
		getOrderList();
	}

	public void initComponents(View view) {
		// pullist_productlistView
		plist_orderListView = (PullToRefreshListView) view
				.findViewById(R.id.plist_orderListView);
		plist_orderListView.setMode(Mode.PULL_FROM_END);
		plist_orderListView.setOnItemClickListener(this);
		orderAdapter = new OrderAdapter(getActivity().getBaseContext());
		plist_orderListView.setAdapter(orderAdapter);
		plist_orderListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						orderAdapter.clear();
						page = 1;
						getOrderList();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.i(TAG, "onPullUpToRefresh");
						getOrderList();
					}
				});

		//get order list

		//rl_no_network
		rl_no_network = (ViewGroup) view.findViewById(R.id.rl_no_network);
		//rl_loading_data
		rl_loading_data = (ViewGroup) view.findViewById(R.id.rl_loading_data);

		//iv_no_network
		iv_no_network = (ImageView) view.findViewById(R.id.iv_no_network);
		iv_no_network.setOnClickListener(this);

		//rl_no_data
		rl_no_data = (ViewGroup) view.findViewById(R.id.rl_no_data);
		rl_no_data.setOnClickListener(this);

	}

	public void getOrderList() {
		/*if (!NetWorkUtil.isConnected(getActivity().getApplicationContext())) {
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
					jsonObj = orderService.getGroupOrderList(soSheet, page,
							pageCount, null, null);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetProductList.sendMessage(msg);
			}

		});
	}

	private Handler handlerGetProductList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			rl_loading_data.setVisibility(View.GONE);
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					LinkedList<TSoSheet> dataList = (LinkedList<TSoSheet>) jsonObj
							.getValue();
					if (dataList != null && !dataList.isEmpty()) {
						for (TSoSheet item : dataList) {
							orderAdapter.addItemLast(item);
						}
						page = page + 1;
					} else {
						if (page == 1) {
//							rl_no_data.setVisibility(View.VISIBLE);
						} else {
							Toast.makeText(
									getActivity().getApplicationContext(),
									//									OrderManagerActivity.this,
									getString(R.string.pull_to_refresh_no_more_date_label),
									Toast.LENGTH_SHORT).show();
						}
					}

					orderAdapter.notifyDataSetChanged();
					rl_no_network.setVisibility(View.GONE);
				} else {
					Toast.makeText(getActivity().getBaseContext(),
							jsonObj.getMsg(), Toast.LENGTH_SHORT).show();
				}
				
				plist_orderListView.onRefreshComplete();
				if (orderAdapter.getCount() == 0) {
					rl_no_data.setVisibility(View.VISIBLE);
				}
				
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(getActivity().getBaseContext(),
						errJsonObj.getMsg(), Toast.LENGTH_SHORT).show();
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}

		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.plist_orderListView:
			TSoSheet item = (TSoSheet) orderAdapter.getItem(position);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_no_network:
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			break;
		case R.id.rl_no_data:
			getOrderList();
			break;
		default:
			break;
		}
	}

}
