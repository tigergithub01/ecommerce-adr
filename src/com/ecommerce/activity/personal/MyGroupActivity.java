package com.ecommerce.activity.personal;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TVip;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.NetWorkUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyGroupActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {
	private static final String TAG = MyGroupActivity.class.getSimpleName();
	private PullToRefreshListView plist_grouplistView;
	private TextView tv_group_direct_vip_count;
	private TextView tv_group_indirect_vip_count;
	private TextView tv_group_total_vip_count;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private VipService vipService;
	private VipAdapter vipAdapter;

	/**no network,no data notify**/
	private ViewGroup rl_no_network;
	private ViewGroup rl_loading_data;
	private ViewGroup rl_no_data;
	private ImageView iv_no_network;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_group);
		setTitle(R.string.title_activity_my_group);

		initComponents();
		initParameters();
				initData();
	}

	@Override
	public void initComponents() {
		//tv_group_direct_vip_count
		tv_group_direct_vip_count = (TextView) findViewById(R.id.tv_group_direct_vip_count);

		//tv_group_direct_vip_count
		tv_group_indirect_vip_count = (TextView) findViewById(R.id.tv_group_indirect_vip_count);

		//tv_group_total_vip_count
		tv_group_total_vip_count = (TextView) findViewById(R.id.tv_group_total_vip_count);

		//plist_grouplistView
		plist_grouplistView = (PullToRefreshListView) findViewById(R.id.plist_grouplistView);
		plist_grouplistView.setMode(Mode.PULL_FROM_START);
		plist_grouplistView.setOnItemClickListener(this);
		vipAdapter = new VipAdapter(this);
		plist_grouplistView.setAdapter(vipAdapter);
		plist_grouplistView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e("TAG", "onPullDownToRefresh");
						//这里写下拉刷新的任务
						vipAdapter.clear();
						getVipChildren(Constants.vipSession.getId());
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e("TAG", "onPullUpToRefresh");
						//这里写上拉加载更多的任务
//										getVipChildren(Constants.vipSession.getId());
					}
				});
		
		
		//get vip childern
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

	@Override
	public void initParameters() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		vipService = ServiceUtils.getInstance(this).getVipService();

		//get vip childern
		getVipChildren(Constants.vipSession.getId());

	}

	private void getVipChildren(final Integer vipId) {
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
					jsonObj = vipService.getChildren(vipId);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				Bundle data = new Bundle();
				data.putInt("vipId", vipId);
				msg.setData(data);
				handlerGetVipChildren.sendMessage(msg);
			}
		});
	}

	private Handler handlerGetVipChildren = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			rl_loading_data.setVisibility(View.GONE);
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				Integer vipId = msg.getData().getInt("vipId");
				Integer direct_vip_count = 0;
				Integer indirect_vip_count = 0;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					LinkedList<TVip> dataList = (LinkedList<TVip>) jsonObj
							.getValue();
					if (dataList != null && !dataList.isEmpty()) {
						for (TVip item : dataList) {
							vipAdapter.addItemLast(item);
							if (item.getParent_id() != null
									&& item.getParent_id().intValue() == vipId) {
								direct_vip_count++;
								item.setDirect_flag(1);
							} else {
								indirect_vip_count++;
								item.setDirect_flag(0);
							}
						}
					}
					vipAdapter.notifyDataSetChanged();
					
					//count
					tv_group_direct_vip_count.setText(String
							.valueOf(direct_vip_count));
					tv_group_indirect_vip_count.setText(String
							.valueOf(indirect_vip_count));
					tv_group_total_vip_count.setText(String
							.valueOf(dataList == null ? 0 : dataList.size()));
				} else {
					Toast.makeText(MyGroupActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				
				plist_grouplistView.onRefreshComplete();
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MyGroupActivity.this, errJsonObj.getMsg(),
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
		case R.id.iv_no_network:
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			break;
		case R.id.rl_no_data:
			getVipChildren(Constants.vipSession.getId());
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.plist_grouplistView:
			TVip item = (TVip) vipAdapter.getItem(position);
			break;
		default:
			break;
		}

	}

}
