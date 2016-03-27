package com.ecommerce.activity.earn;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.LoginActivity;
import com.ecommerce.activity.main.MainActivity;
import com.ecommerce.activity.myincome.MyIncomeActivity;
import com.ecommerce.activity.order.OrderGuideActivity;
import com.ecommerce.activity.product.ProductListActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TEarnGuild;
import com.ecommerce.service.EarnGuildService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.StringUtils;
import com.ecommerce.utils.Util;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class VipShareActivity extends BaseActivity implements OnClickListener,
		IWXAPIEventHandler, OnItemClickListener {
	private static final String TAG = VipShareActivity.class.getSimpleName();
	private ViewGroup rlt_earn_vip_share;
	private ViewGroup rlt_earn_recommend_product;
	private ViewGroup rlt_my_income;
	private ViewGroup rlt_order_manager;
	private View rootView;
	private PopupWindow win;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	//ear guild
	private PullToRefreshListView pullist_earn_guild_list_view;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private int pageCount = 15;
	private int page = 1;
	private EarnGuildService earnGuildService;
	private EarnGuildAdapter earnGuildAdapter;
	private TEarnGuild earnGuild = new TEarnGuild();

	/**no network,no data notify**/
	private ViewGroup rl_no_network;
	private ViewGroup rl_loading_data;
	private ViewGroup rl_no_data;
	private ImageView iv_no_network;
	
	//Tencent share
	public static String mAppid = "222222";
	public static Tencent mTencent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = getLayoutInflater().inflate(R.layout.activity_vip_share,
				null);
		setContentView(rootView);
		setTitle(R.string.title_activity_vip_share);

		initComponents();
		initParameters();
		initData();

	}

	public void initComponents() {
		setLeftBackButtonVisibility(View.GONE);

		rlt_earn_vip_share = (ViewGroup) findViewById(R.id.rlt_earn_vip_share);
		rlt_earn_vip_share.setOnClickListener(this);

		rlt_earn_recommend_product = (ViewGroup) findViewById(R.id.rlt_earn_recommend_product);
		rlt_earn_recommend_product.setOnClickListener(this);

		//rlt_my_income
		rlt_my_income = (ViewGroup) findViewById(R.id.rlt_my_income);
		rlt_my_income.setOnClickListener(this);

		//rlt_order_manager
		rlt_order_manager = (ViewGroup) findViewById(R.id.rlt_order_manager);
		rlt_order_manager.setOnClickListener(this);

		//pullist_earn_guild_list_view
		pullist_earn_guild_list_view = (PullToRefreshListView) findViewById(R.id.pullist_earn_guild_list_view);
		pullist_earn_guild_list_view.setMode(Mode.DISABLED);
		pullist_earn_guild_list_view.setOnItemClickListener(this);
		earnGuildAdapter = new EarnGuildAdapter(this);
		pullist_earn_guild_list_view.setAdapter(earnGuildAdapter);
		pullist_earn_guild_list_view
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e("TAG", "onPullDownToRefresh");
						//这里写下拉刷新的任务
						//new GetDataTask().execute();
						earnGuildAdapter.clear();
						page = 1;
						getEarnGuildList();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e("TAG", "onPullUpToRefresh");
						//这里写上拉加载更多的任务
						//						new GetDataTask().execute();
						getEarnGuildList();
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

	public void initParameters() {
		if (mTencent == null) {
			mTencent = Tencent.createInstance(mAppid, this);
		}
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, CommonUtils.getWebchatAppId(this),
				false);
		// 将该app注册到微信
		api.registerApp(CommonUtils.getWebchatAppId(this));
	}

	public void initData() {
		earnGuildService = ServiceUtils.getInstance(this).getEarnGuildService();
		getEarnGuildList();
	}

	@Override
	public void onClick(View v) {
		String url = null;
		if (Constants.vipSession != null) {
			url = ConfigReader.getInstance(this).getRemoteUrl(
					"/index.php?r=sale/vip-register/index&parent_vip_no="
							+ Constants.vipSession.getVip_no());
		}
		String imageUrl = ConfigReader.getInstance(this).getRemoteUrl(
				"/images/sale/app_logo.png");
		switch (v.getId()) {
		case R.id.rlt_earn_vip_share:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			if (StringUtils.isEmpty(url)) {
				CommonUtils.showAlertDialog(this, "分享失败，请重试");
				break;
			}
			createPopUpWin();
			break;
		case R.id.rlt_earn_recommend_product:
			startActivity(new Intent(this, ProductListActivity.class));
			break;
		case R.id.rlt_my_income:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			final Intent intent = new Intent(this, MyIncomeActivity.class);
			startActivity(intent);
			break;
		case R.id.rlt_order_manager:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			/*final Intent intentOrderManager = new Intent(this, OrderManagerActivity.class);
			startActivity(intentOrderManager);*/
			startActivity(new Intent(this, OrderGuideActivity.class));
			break;
		case R.id.llayout_qq:
			if (StringUtils.isEmpty(url)) {
				CommonUtils.showAlertDialog(this, "分享失败，请重试");
				break;
			}
			final Bundle params = new Bundle();
			//分享的标题
			params.putString(QQShare.SHARE_TO_QQ_TITLE,
					getString(R.string.vip_share_title));
			//这条分享消息被好友点击后的跳转URL
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
			//分享的消息摘要，最长50个字
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
					getString(R.string.vip_share_content));
			//分享的图片URL
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);

			params.putString(QQShare.SHARE_TO_QQ_APP_NAME,
					getString(R.string.app_name));
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
					QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
			doShareToQQ(params);

			win.dismiss();
			break;
		case R.id.llayout_weixin:
			if (StringUtils.isEmpty(url)) {
				CommonUtils.showAlertDialog(this, "分享失败，请重试");
				break;
			}
			/*shareWebchatText(SendMessageToWX.Req.WXSceneSession,
					"企业微营销平台,企业营销助手。");*/
			shareWebchaWebpage(SendMessageToWX.Req.WXSceneSession, url,
					getString(R.string.vip_share_title),
					getString(R.string.vip_share_content), R.drawable.app_logo);
			/*doShareToWX(SendMessageToWX.Req.WXSceneSession, url,
					getString(R.string.vip_share_title),
					getString(R.string.vip_share_content), R.drawable.app_logo);*/
			win.dismiss();
			break;
		case R.id.llayout_weixin_friend:
			if (StringUtils.isEmpty(url)) {
				CommonUtils.showAlertDialog(this, "分享失败，请重试");
				break;
			}
			//			shareWebchatText(SendMessageToWX.Req.WXSceneTimeline, "企业微营销平台,企业营销助手。");
			shareWebchaWebpage(SendMessageToWX.Req.WXSceneTimeline, url,
					getString(R.string.vip_share_content),
					getString(R.string.vip_share_title), R.drawable.app_logo);
			win.dismiss();
			break;
		case R.id.llayout_message:
			if (StringUtils.isEmpty(url)) {
				CommonUtils.showAlertDialog(this, "分享失败，请重试");
				break;
			}
			shareText(getString(R.string.vip_share_title) + ":" + url);
			win.dismiss();
			break;
		case R.id.iv_no_network:
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			break;
		case R.id.rl_no_data:
			getEarnGuildList();
			break;
		default:
			break;
		}
	}

	private void shareWebchatText(int scene, String text) {
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		//					req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		req.scene = scene;
		// 调用api接口发送数据到微信
		api.sendReq(req);
	}

	/**
	 * 在主线程中分享
	 * @param scene
	 * @param webpageUrl
	 * @param title
	 * @param description
	 * @param drawableId
	 */
	public void doShareToWX(final int scene, final String webpageUrl,
			final String title, final String description, final int drawableId) {
		ThreadManager.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				shareWebchaWebpage(scene, webpageUrl, title, description,
						drawableId);
			}
		});
	}

	private void shareWebchaWebpage(int scene, String webpageUrl, String title,
			String description, int drawableId) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = webpageUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;
		Bitmap thumb = BitmapFactory.decodeResource(getResources(), drawableId);
		msg.thumbData = Util.bmpToByteArray(thumb, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = scene;
		api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	private void createPopUpWin() {
		win = new PopupWindow(getBaseContext());
		View shareView = getLayoutInflater().inflate(R.layout.sharepopwindow,
				null);
		win.setContentView(shareView);
		win.setWidth(LayoutParams.MATCH_PARENT);
		win.setHeight(LayoutParams.WRAP_CONTENT);
		win.setAnimationStyle(R.style.anim_popup_dir);
		//		win.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_bg));
		win.setOutsideTouchable(true);
		win.setTouchable(true);
		win.setFocusable(true);
		win.getBackground().setAlpha(140);

		win.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);

		// set background alpha
		setBackgroundAlpha(0.5f);

		// set background alpha when window dismiss
		win.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				setBackgroundAlpha(1f);
			}
		});

		//share qq
		LinearLayout llayout_qq = (LinearLayout) shareView
				.findViewById(R.id.llayout_qq);
		llayout_qq.setOnClickListener(this);

		//share weixin friends
		LinearLayout llayout_weixin = (LinearLayout) shareView
				.findViewById(R.id.llayout_weixin);
		llayout_weixin.setOnClickListener(this);

		LinearLayout llayout_weixin_friend = (LinearLayout) shareView
				.findViewById(R.id.llayout_weixin_friend);
		llayout_weixin_friend.setOnClickListener(this);

		LinearLayout llayout_message = (LinearLayout) shareView
				.findViewById(R.id.llayout_message);
		llayout_message.setOnClickListener(this);

	}

	private void doShareToQQ(final Bundle params) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (null != MainActivity.mTencent) {
					MainActivity.mTencent.shareToQQ(VipShareActivity.this,
							params, qqShareListener);
				}
			}
		});
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {
			Toast.makeText(VipShareActivity.this, "取消分享 ", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			//        	Toast.makeText(VipShareActivity.this, "onComplete: " + response.toString(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			Toast.makeText(VipShareActivity.this, "分享出错：" + e.errorMessage,
					Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (api != null) {
			api.unregisterApp();
		}
	}

	@Override
	public void onReq(BaseReq req) {
		// TODO Auto-generated method stub
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			Toast.makeText(this, "COMMAND_GETMESSAGE_FROM_WX",
					Toast.LENGTH_LONG).show();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			Toast.makeText(this, "COMMAND_SHOWMESSAGE_FROM_WX",
					Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		int result = 0;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

	private void sendSMS(String smsBody) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		startActivity(intent);
	}

	private void shareText(String smsBody) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//		return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK && win != null && win.isShowing()) {
			win.dismiss();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * set background alpha when window show
	 * @param bgAlpha
	 */
	public void setBackgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		getWindow().setAttributes(lp);
	}

	public void getEarnGuildList() {
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
					jsonObj = earnGuildService.getEarnGuildList(earnGuild,
							page, pageCount, null, null);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetEarnGuildList.sendMessage(msg);
			}

		});
	}

	private Handler handlerGetEarnGuildList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			rl_loading_data.setVisibility(View.GONE);
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					LinkedList<TEarnGuild> dataList = (LinkedList<TEarnGuild>) jsonObj
							.getValue();
					if (dataList != null && !dataList.isEmpty()) {
						for (TEarnGuild item : dataList) {
							earnGuildAdapter.addItemLast(item);
						}
						page = page + 1;
					} else {
						if (page == 1) {
//							rl_no_data.setVisibility(View.VISIBLE);
						} else {
							Toast.makeText(
									VipShareActivity.this,
									getString(R.string.pull_to_refresh_no_more_date_label),
									Toast.LENGTH_SHORT).show();
						}
					}
					rl_no_network.setVisibility(View.GONE);
					earnGuildAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(VipShareActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				
				pullist_earn_guild_list_view.onRefreshComplete();
				if (earnGuildAdapter.getCount() == 0) {
					rl_no_data.setVisibility(View.VISIBLE);
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(VipShareActivity.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();
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
		switch (view.getId()) {
		case R.id.rl_earn_guild_item:
			TEarnGuild item = (TEarnGuild) earnGuildAdapter.getItem(position);

			Intent intent = new Intent(this, EarnGuildDetailActivity.class);
			Bundle extras = new Bundle();
			extras.putInt("earnGuildId", item.getId());
			intent.putExtras(extras);
			startActivity(intent);

			break;
		default:
			break;
		}
	}

}
