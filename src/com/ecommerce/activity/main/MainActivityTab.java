package com.ecommerce.activity.main;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import com.ecommerce.activity.message.MessageActivity;
import com.ecommerce.activity.personal.UpgradeVerDialog;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TAdInfo;
import com.ecommerce.model.TAppRelease;
import com.ecommerce.model.TProduct;
import com.ecommerce.model.TVip;
import com.ecommerce.service.AdInfoService;
import com.ecommerce.service.DownloadAppService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.Util;
import com.ecommerce.utils.VersionUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class MainActivityTab extends BaseActivity implements
		OnPageChangeListener, OnClickListener, OnItemClickListener {
	public static final String TAG = MainActivityTab.class.getSimpleName();
	private ViewPager vpager_gallery;
	/** 
	* 装点的ImageView数组
	*/
	private ImageView[] tips;

	/** 
	 * 装ImageView数组 
	 */
	private ImageView[] mImageViews;

	/** 
	 * 图片资源id 
	 */
	/*private int[] imgIdArray;*/

	//当前位置
	private int currentPosition;

	private ExecutorService executorServiceProduct = Executors
			.newFixedThreadPool(10);
	private ScheduledExecutorService executorService = Executors
			.newScheduledThreadPool(10);

	//Tencent share
	public static String mAppid = "222222";
	public static Tencent mTencent;

	//vip information service
	private VipService vipService;

	//adInfoService
	private AdInfoService adInfoService;
	private GalleryAdapter galleryAdapter;
	private ViewGroup ll_dots;

	// downloadAppService;
	private DownloadAppService downloadAppService;

	/***product list start**/
	private PullToRefreshListView plist_productlistView;
	private MainProductAdapter productAdapter;
	private int pageCount = 15;
	private int page = 1;
	private String orderColumn = "homepage_index";
	private String orderDirection = null;
	private ProductService productService;
	//share popupwindow
	private PopupWindow shareWin;

	//store product filter condition
	private TProduct product = new TProduct();

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	//to be shared product
	public TProduct shareProduct;

	//display features
	private ViewGroup rl_no_network;
	private ViewGroup rl_loading_data;
	private ViewGroup rl_no_data;
	private ImageView iv_no_network;

	/***product list end**/
	private View rootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//		setContentView(R.layout.activity_main_tab);
		rootView = getLayoutInflater()
				.inflate(R.layout.activity_main_tab, null);
		setContentView(rootView);
		setTitle(R.string.app_name);
		//		setContentView(R.layout.ad_head);

		initComponents();
		initParameters();
		initData();
	}

	public void initComponents() {
		//setLeftButton
		setLeftButtonVisibility(View.VISIBLE);
		setLeftButton(R.string.main_left_button_text, new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivityTab.this,
						MessageActivity.class));
			}
		});

		//setRightButton
		if (Constants.vipSession == null) {
			setRightButtonVisibility(View.VISIBLE);
		} else {
			setRightButtonVisibility(View.GONE);
		}
		setRightButton(R.string.personal_menu_login, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent intent = new Intent(MainActivityTab.this,
						LoginActivity.class);
				startActivity(intent);
			}
		});

		//set banner visibility
		setBarBannerVisibility(View.GONE);

		//set left button visibility
		setLeftBackButtonVisibility(View.GONE);

		//setBarTitleVisibility
		setBarTitleVisibility(View.VISIBLE);

		vpager_gallery = (ViewPager) findViewById(R.id.vpager_gallery);
		ll_dots = (ViewGroup) findViewById(R.id.ll_dots);

		/*//载入图片资源ID  
		imgIdArray = new int[] { R.drawable.ad_demo, R.drawable.ad_demo,
				R.drawable.ad_demo };

		//将点点加入到ViewGroup中  
		tips = new ImageView[imgIdArray.length];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(10, 10));
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.ad_gallery_on);
			} else {
				tips[i].setBackgroundResource(R.drawable.ad_gallery_off);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			ll_dots.addView(imageView, layoutParams);
		}

		//将图片装载到数组中 
		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}*/

		mImageViews = new ImageView[] {};
		galleryAdapter = new GalleryAdapter();
		//设置Adapter  
		vpager_gallery.setAdapter(galleryAdapter);
		//设置监听，主要是设置点点的背景  
		vpager_gallery.setOnPageChangeListener(this);
		//设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动  
		//		vpager_gallery.setCurrentItem((mImageViews.length) * 100);
		vpager_gallery.setCurrentItem(0);
		//		currentPosition = 300;

		// pullist_productlistView
		plist_productlistView = (PullToRefreshListView) findViewById(R.id.pullist_main_productlistView);
		plist_productlistView.setMode(Mode.PULL_FROM_START);
		plist_productlistView.setOnItemClickListener(this);
		productAdapter = new MainProductAdapter(this);
		plist_productlistView.setAdapter(productAdapter);
		plist_productlistView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e("TAG", "onPullDownToRefresh");
						//这里写下拉刷新的任务
						//						new GetDataTask().execute();
						productAdapter.clear();
						page = 1;
						getProductList();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e("TAG", "onPullUpToRefresh");
						//这里写上拉加载更多的任务
						//						new GetDataTask().execute();
						getProductList();
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
		//when remember me , the auto login
		vipService = ServiceUtils.getInstance(this).getVipService();
		if (vipService.getRememberMe()) {
			String[] vipPwd = vipService.getLocalVipPwd();
			TVip vip = new TVip();
			vip.setVip_no(vipPwd[0]);
			vip.setPassword(vipPwd[1]);
			if (Constants.vipSession == null) {
				autoLogin(vip);
			}
		}

		//get ad
		adInfoService = ServiceUtils.getInstance(this).getAdInfoService();
		getAdInfoList();

		downloadAppService = ServiceUtils.getInstance(this)
				.getDownloadAppService();
		//check new version
		getAppRelease();

		//get product list
		productService = ServiceUtils.getInstance(this).getProductService();
		getProductList();
	}

	public void getProductList() {
		/*if (!NetWorkUtil.isConnected(this)) {
			rl_no_network.setVisibility(View.VISIBLE);
			return;
		}*/
		rl_loading_data.setVisibility(View.VISIBLE);
		rl_no_data.setVisibility(View.GONE);
		executorServiceProduct.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					product.setShowInHomepage(1);
					jsonObj = productService.getProductList(product, page,
							pageCount, orderColumn, orderDirection);
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
					LinkedList<TProduct> dataList = (LinkedList<TProduct>) jsonObj
							.getValue();
					if (dataList != null && !dataList.isEmpty()) {
						for (TProduct item : dataList) {
							productAdapter.addItemLast(item);
						}
						page = page + 1;
					} else {
						if (page == 1) {
//							rl_no_data.setVisibility(View.VISIBLE);
						} else {
							Toast.makeText(
									MainActivityTab.this,
									getString(R.string.main_pull_to_refresh_no_more_date_label),
									Toast.LENGTH_SHORT).show();
						}
					}
					rl_no_network.setVisibility(View.GONE);
					productAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(MainActivityTab.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				
				plist_productlistView.onRefreshComplete();
				if (productAdapter.getCount() == 0) {
					rl_no_data.setVisibility(View.VISIBLE);
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MainActivityTab.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

	public void getAdInfoList() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = adInfoService.getAdInfoList();
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetAdInfoList.sendMessage(msg);
			}

		});
	}

	private Handler handlerGetAdInfoList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					LinkedList<TAdInfo> dataList = (LinkedList<TAdInfo>) jsonObj
							.getValue();
					createBanner(dataList);
					/*if (dataList != null && !dataList.isEmpty()) {
						for (TAdInfo item : dataList) {
							galleryAdapter.addItemLast(item);
						}
					}*/
					galleryAdapter.notifyDataSetChanged();
					galleryAutoPlay();
				} else {
					Toast.makeText(MainActivityTab.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MainActivityTab.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}

		}
	};

	private void createBanner(LinkedList<TAdInfo> dataList) {
		if (dataList == null || dataList.size() == 0) {
			return;
		}
		//将点点加入到ViewGroup中  
		tips = new ImageView[dataList.size()];

		//将图片装载到数组中 
		mImageViews = new ImageView[dataList.size()];
		for (int i = 0; i < dataList.size(); i++) {
			TAdInfo adInfo = dataList.get(i);

			//make tips
			ImageView imageViewTip = new ImageView(this);
			imageViewTip.setLayoutParams(new LayoutParams(10, 10));
			tips[i] = imageViewTip;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.ad_gallery_on);
			} else {
				tips[i].setBackgroundResource(R.drawable.ad_gallery_off);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			ll_dots.addView(imageViewTip, layoutParams);

			//make picture
			ImageView imageViewBanner = new ImageView(this);
			mImageViews[i] = imageViewBanner;
			imageViewBanner.setTag(adInfo);
			//			imageViewBanner.setImageBitmap(adInfo.getImage());
			imageViewBanner.setBackgroundDrawable(new BitmapDrawable(adInfo
					.getImage()));
			//			Drawable background = new Drawable();
			//			imageViewBanner.setba
			//			Drawable
			//			imageViewBanner.setBackground(background)
			//			imageViewBanner.setBackgroundResource(imgIdArray[i]);
			//			imageViewBanner.setb
		}

	}

	public class GalleryAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			//			return Integer.MAX_VALUE;
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			//						super.destroyItem(container, position, object);
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			try {
				final ImageView imageView = mImageViews[position
						% mImageViews.length];
				((ViewPager) container).addView(imageView, 0);
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							TAdInfo adinfo = (TAdInfo) imageView.getTag();
							Uri uri = Uri.parse(adinfo.getRedirectUrl());
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent);
						} catch (Exception e) {
							Toast.makeText(MainActivityTab.this,
									ExceptionHandler.getMessage(e).toString(),
									Toast.LENGTH_SHORT).show();
						}
						Log.d(TAG, "bind OnClickListener");
					}
				});

			} catch (Exception e) {
				//handler something  
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
			return mImageViews[position % mImageViews.length];
			//			return super.instantiateItem(container, position);
		}

		/** 
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键 
		 */

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		//		Log.d(TAG, "onPageScrolled:"+arg0);
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setImageBackground(arg0 % mImageViews.length);
		int position = arg0 % mImageViews.length;
		Log.d(TAG, "position is " + position);
	}

	/** 
	 * 设置选中的tip的背景 
	 * @param selectItems 
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.ad_gallery_on);
			} else {
				tips[i].setBackgroundResource(R.drawable.ad_gallery_off);
			}
		}
	}

	private void galleryAutoPlay() {
		executorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if(galleryAdapter.getCount()==0){
					return;
				}
				Message msg = new Message();
				msg.what = 1;
				if (currentPosition == mImageViews.length - 1) {
					currentPosition = 0;
				} else {
					currentPosition = currentPosition + 1;
				}
				handler.sendMessage(msg);
			}
		}, 2, 3, TimeUnit.SECONDS);

		/*executorService.submit(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what=1;
				currentPosition++;
				handler.sendMessage(msg);
			}
		});*/
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				//				int position = msg.getData().getInt("position");
				Log.d(TAG, "current position is :" + currentPosition);
				vpager_gallery.setCurrentItem(currentPosition);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause");
//		executorService.shutdown();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
				
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (mTencent != null) {
			mTencent.releaseResource();
		}

		if (api != null) {
			api.unregisterApp();
		}

		Log.d(TAG, "onDestroy");
	}

	@Override
	public void onClick(View v) {
		String url = null;
		String imageUrl = null;
		String content = null;
		if (shareProduct != null) {
			url = ConfigReader.getInstance(this)
					.getRemoteUrl(
							"/index.php?r=sale/product/view&id="
									+ shareProduct.getId());
			if (Constants.vipSession != null) {
				url = url + "&vip_no=" + Constants.vipSession.getVip_no();
			}
			imageUrl = ConfigReader.getInstance(this).getRemoteUrl(
					"/images/sale/app_logo.png");
			content = shareProduct.getName()
					+ CommonUtils.formatAmount(shareProduct.getPrice());
		}

		switch (v.getId()) {
		case R.id.llayout_qq:
			if (shareProduct == null) {
				break;
			}
			final Bundle params = new Bundle();
			params.putString(QQShare.SHARE_TO_QQ_TITLE,
					getString(R.string.vip_share_title));
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
			params.putString(QQShare.SHARE_TO_QQ_APP_NAME,
					getString(R.string.app_name));
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
					QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
			doShareToQQ(params);

			shareWin.dismiss();
			break;
		case R.id.llayout_weixin:
			if (shareProduct == null) {
				break;
			}
			/*shareWebchatText(SendMessageToWX.Req.WXSceneSession,
					"企业微营销平台,企业营销助手。");*/
			shareWebchaWebpage(SendMessageToWX.Req.WXSceneSession, url,
					getString(R.string.vip_share_title), content,
					R.drawable.app_logo);
			/*doShareToWX(SendMessageToWX.Req.WXSceneSession, url,
					getString(R.string.vip_share_title),
					shareProduct.getName(), R.drawable.app_logo);*/
			shareWin.dismiss();
			break;
		case R.id.llayout_weixin_friend:
			if (shareProduct == null) {
				break;
			}
			//			shareWebchatText(SendMessageToWX.Req.WXSceneTimeline, "企业微营销平台,企业营销助手。");
			shareWebchaWebpage(SendMessageToWX.Req.WXSceneTimeline, url,
					content, getString(R.string.vip_share_title),
					R.drawable.app_logo);
			shareWin.dismiss();
			break;
		case R.id.llayout_message:
			if (shareProduct == null) {
				break;
			}
			shareText(content + ":" + url);
			shareWin.dismiss();
			break;
		case R.id.iv_no_network:
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			break;
		case R.id.rl_no_data:
			getProductList();
			break;
		default:
			break;
		}
	}

	private void shareText(String smsBody) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}

	private void doShareToQQ(final Bundle params) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (null != MainActivityTab.mTencent) {
					MainActivityTab.mTencent.shareToQQ(MainActivityTab.this,
							params, qqShareListener);
				}
			}
		});
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {
			Toast.makeText(MainActivityTab.this, "取消分享 ", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			//        	Toast.makeText(ProductListActivity.this, "onComplete: " + response.toString(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivityTab.this, "分享出错：" + e.errorMessage,
					Toast.LENGTH_SHORT).show();
		}
	};

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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.pullist_productlistView:
			TProduct product = (TProduct) productAdapter.getItem(position);
			Toast.makeText(getBaseContext(), product.getName(),
					Toast.LENGTH_LONG).show();
			//			productTypeWin.dismiss();
			//			Toast.makeText(getBaseContext(), pType.getName(), Toast.LENGTH_LONG)
			//					.show();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && shareWin != null
				&& shareWin.isShowing()) {
			shareWin.dismiss();
			return false;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (rl_loading_data.getVisibility() == View.VISIBLE) {
				rl_loading_data.setVisibility(View.GONE);
				//				executorService.shutdown();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void createSharePopUpWin() {
		shareWin = new PopupWindow(getBaseContext());
		View shareView = getLayoutInflater().inflate(R.layout.sharepopwindow,
				null);
		shareWin.setContentView(shareView);
		shareWin.setWidth(LayoutParams.MATCH_PARENT);
		shareWin.setHeight(LayoutParams.WRAP_CONTENT);
		shareWin.setAnimationStyle(R.style.anim_popup_dir);
		//		win.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_bg));
		shareWin.setOutsideTouchable(false);
		shareWin.setTouchable(true);
		shareWin.setFocusable(true);
		shareWin.getBackground().setAlpha(140);

		shareWin.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
		//		shareWin.showAtLocation(rootView, Gravity.CENTER, 0, 0);
		// set background alpha
		//		setBackgroundAlpha(0.5f);
		CommonUtils.setBackgroundAlpha(getWindow(), 0.5f);

		// set background alpha when window dismiss
		shareWin.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				//				setBackgroundAlpha(1f);
				CommonUtils.setBackgroundAlpha(getWindow(), 1f);
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

	private void autoLogin(final TVip vip) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = vipService.login(vip);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerLogin.sendMessage(msg);
			}
		});
	}

	private Handler handlerLogin = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					/*Toast.makeText(MainActivityTab.this, "登录成功",
							Toast.LENGTH_SHORT).show();*/
					Log.d(TAG, "自动登录成功");
					setRightButtonVisibility(View.GONE);
				} else {
					Log.d(TAG, "--error messgage--" + jsonObj.getMsg());
					/*CommonUtils.showAlertDialog(MainActivityTab.this,
							jsonObj.getMsg());*/
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				/*Toast.makeText(MainActivityTab.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();*/
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}

		}
	};

	private void getAppRelease() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = downloadAppService
							.getAppRelease(Constants.ANDROID_IDENTITY);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetAppRelease.sendMessage(msg);
			}
		});
	}

	private Handler handlerGetAppRelease = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					TAppRelease appRelease = (TAppRelease) jsonObj.getValue();
					if (appRelease != null) {
						int versionCode = VersionUtils
								.getVerCode(MainActivityTab.this);
						if (appRelease.getVerNo() > versionCode) {
							//UpgradeVerDialog
							UpgradeVerDialog dialog = new UpgradeVerDialog();
							Bundle args = new Bundle();
							args.putSerializable("key", appRelease);
							dialog.setArguments(args);
							dialog.show(getSupportFragmentManager(),
									UpgradeVerDialog.class.getName());
						}
					}
				} else {
					/*Toast.makeText(MainActivityTab.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();*/
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				/*Toast.makeText(MainActivityTab.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();*/
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

}
