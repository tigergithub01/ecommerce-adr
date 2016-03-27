package com.ecommerce.activity.main;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.LoginActivity;
import com.ecommerce.activity.earn.VipShareActivity;
import com.ecommerce.activity.message.MessageActivity;
import com.ecommerce.activity.myincome.MyIncomeActivity;
import com.ecommerce.activity.order.OrderGuideActivity;
import com.ecommerce.activity.personal.PersonalActivity;
import com.ecommerce.activity.personal.UpgradeVerDialog;
import com.ecommerce.activity.product.ProductListActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TAdInfo;
import com.ecommerce.model.TAppRelease;
import com.ecommerce.model.TVip;
import com.ecommerce.service.AdInfoService;
import com.ecommerce.service.DownloadAppService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.VipService;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.VersionUtils;
import com.tencent.tauth.Tencent;

public class MainActivity extends BaseActivity implements OnPageChangeListener,
		OnClickListener {
	public static final String TAG = MainActivity.class.getSimpleName();
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

	private ViewGroup rlt_my_income;
	private ViewGroup rlt_vip_share;
	private ViewGroup rlt_order_manager;
	private ViewGroup rlt_goods_list;
	private ScheduledExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor();

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		setTitle(R.string.app_name);
		//		setContentView(R.layout.ad_head);
//setVisible(false);
//		initComponents();
//		initParameters();
//		initData();
	}

	public void initComponents() {

		//setLeftButton
		setLeftButtonVisibility(View.VISIBLE);
		setLeftButton(R.string.main_left_button_text, new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						MessageActivity.class));
			}
		});

		//setRightButton
		setRightButtonVisibility(View.VISIBLE);
		setRightButton(R.string.title_activity_personal, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent intent = new Intent(MainActivity.this,
						PersonalActivity.class);
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

		//rlt_my_income
		rlt_my_income = (ViewGroup) findViewById(R.id.rlt_my_income);
		rlt_my_income.setOnClickListener(this);

		//rlt_my_income
		rlt_vip_share = (ViewGroup) findViewById(R.id.rlt_vip_share);
		rlt_vip_share.setOnClickListener(this);

		//rlt_order_manager
		rlt_order_manager = (ViewGroup) findViewById(R.id.rlt_order_manager);
		rlt_order_manager.setOnClickListener(this);

		//rlt_goods_list
		rlt_goods_list = (ViewGroup) findViewById(R.id.rlt_goods_list);
		rlt_goods_list.setOnClickListener(this);
	}

	public void initParameters() {
		if (mTencent == null) {
			mTencent = Tencent.createInstance(mAppid, this);
		}
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
	}

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
				} else {
					Toast.makeText(MainActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(MainActivity.this, errJsonObj.getMsg(),
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
							Toast.makeText(MainActivity.this,
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
				Message msg = new Message();
				msg.what = 1;
				//				Bundle data = new Bundle();
				//				data.putInt("position", currentPosition);
				//				msg.setData(data);
				//				mImageViews.length

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

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause");
		executorService.shutdown();
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
		//		galleryAutoPlay();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if (mTencent != null) {
			mTencent.releaseResource();
		}
		
		Log.d(TAG, "onDestroy");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlt_my_income:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			final Intent intent = new Intent(this, MyIncomeActivity.class);
			startActivity(intent);
			break;
		case R.id.rlt_vip_share:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			final Intent intentVipShareActivity = new Intent(this,
					VipShareActivity.class);
			startActivity(intentVipShareActivity);
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
		case R.id.rlt_goods_list:
			startActivity(new Intent(this, ProductListActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//		return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*ExitSystemDialog exitDialog = new ExitSystemDialog();
			exitDialog.show(getSupportFragmentManager(),
					UpgradeVerDialog.class.getName());*/
			AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("是否退出系统?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent exit = new Intent();
									exit.setAction("Exit_APP");
									sendBroadcast(exit);
									
									//finish all activity
									CommonUtils.exitSystem(MainActivity.this);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									return;
								}
							}).create();
			dialog.show();
			return false;
		}
		return super.onKeyDown(keyCode, event);
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
					/*Toast.makeText(MainActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();*/
					Log.d(TAG, "自动登录成功");
				} else {
					Log.d(TAG, "--error messgage--" + jsonObj.getMsg());
					/*CommonUtils.showAlertDialog(MainActivity.this,
							jsonObj.getMsg());*/
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				/*Toast.makeText(MainActivity.this, errJsonObj.getMsg(),
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
								.getVerCode(MainActivity.this);
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
					/*Toast.makeText(MainActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();*/
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				/*Toast.makeText(MainActivity.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();*/
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};
	

}
