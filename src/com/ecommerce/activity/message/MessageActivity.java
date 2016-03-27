package com.ecommerce.activity.message;

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
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TNotification;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.NetWorkUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MessageActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {
	private static final String TAG = MessageActivity.class.getSimpleName();
	private PullToRefreshListView pullist_message_list_view;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private int pageCount = 15;
	private int page = 1;
	private NotificationService notificationService;
	private MessageAdapter messageAdapter;
	private TNotification notification = new TNotification();

	/**no network,no data notify**/
	private ViewGroup rl_no_network;
	private ViewGroup rl_loading_data;
	private ViewGroup rl_no_data;
	private ImageView iv_no_network;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		setTitle(R.string.title_activity_message);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		// pullist_productlistView
		pullist_message_list_view = (PullToRefreshListView) findViewById(R.id.pullist_message_list_view);
		pullist_message_list_view.setMode(Mode.BOTH);
		pullist_message_list_view.setOnItemClickListener(this);
		messageAdapter = new MessageAdapter(this);
		pullist_message_list_view.setAdapter(messageAdapter);
		pullist_message_list_view
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						messageAdapter.clear();
						page = 1;
						getNotificationList();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getNotificationList();
					}
				});

		//get product list

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

	public void getNotificationList() {
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
					jsonObj = notificationService.getNotificationList(
							notification, page, pageCount, null, null);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetNotificationList.sendMessage(msg);
			}

		});
	}

	private Handler handlerGetNotificationList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			rl_loading_data.setVisibility(View.GONE);
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					LinkedList<TNotification> dataList = (LinkedList<TNotification>) jsonObj
							.getValue();
					if (dataList != null && !dataList.isEmpty()) {
						for (TNotification item : dataList) {
							messageAdapter.addItemLast(item);
						}
						page = page + 1;
					} else {
						if (page == 1) {
//							rl_no_data.setVisibility(View.VISIBLE);
						} else {
							Toast.makeText(
									MessageActivity.this,
									getString(R.string.pull_to_refresh_no_more_date_label),
									Toast.LENGTH_SHORT).show();
						}
					}
					messageAdapter.notifyDataSetChanged();
					
					rl_no_network.setVisibility(View.GONE);
				} else {
					Toast.makeText(MessageActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				
				pullist_message_list_view.onRefreshComplete();
				if (messageAdapter.getCount() == 0) {
					rl_no_data.setVisibility(View.VISIBLE);
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MessageActivity.this, errJsonObj.getMsg(),
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
		notificationService = ServiceUtils.getInstance(this)
				.getNotificationService();
		getNotificationList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_no_network:
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			break;
		case R.id.rl_no_data:
			getNotificationList();
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/*Log.d(TAG, "view.getId():"+view.getId());
		Log.d(TAG, "R.id.rl_notification:"+R.id.rl_notification);
		Log.d(TAG, "R.id.pullist_message_list_view:"+R.id.pullist_message_list_view);
		Log.d(TAG, "parent.getId():"+parent.getId());*/
		switch (view.getId()) {
		case R.id.rl_notification:
			TNotification item = (TNotification) messageAdapter
					.getItem(position);

			Intent intent = new Intent(this, MessageDetailActivity.class);
			Bundle extras = new Bundle();
			extras.putInt("notificationId", item.getId());
			intent.putExtras(extras);
			startActivity(intent);

			break;
		default:
			break;
		}

	}

}
