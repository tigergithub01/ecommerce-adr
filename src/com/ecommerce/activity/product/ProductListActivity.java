package com.ecommerce.activity.product;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.main.MainActivity;
import com.ecommerce.model.JsonObj;
import com.ecommerce.model.TProduct;
import com.ecommerce.model.TProductType;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.service.exception.ExceptionHandler;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.Util;
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

public class ProductListActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, OnTouchListener, TextWatcher {
	public static final String TAG = ProductListActivity.class.getSimpleName();

	private View rootView;
	private ImageView iv_productlist_back;
	//	private ViewGroup rl_product_list_content;

	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	/**
	 * 产品列表上拉刷新的控件
	 */
	private PullToRefreshListView plist_productlistView;
	private ProductAdapter productAdapter;
	private int pageCount = 15;
	private int page = 1;
	private String orderColumn = null;
	private String orderDirection = null;
	private ProductService productService;

	//tv_product_list_category,product type window
	private TextView tv_product_list_category;
	private ListView lv_product_type;
	private ProductTypeAdapter productTypeAdapter;
	private PopupWindow productTypeWin;

	//sort type window
	private TextView tv_product_condition_filter;
	private PopupWindow conditionWin;
	private ListView lv_product_condition_type;
	private LinkedList<String> sortTypeList;
	private ArrayAdapter<String> conditionAdapter;

	//share popupwindow
	private PopupWindow shareWin;

	//store product filter condition
	private TProduct product = new TProduct();

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	//to be shared product
	public TProduct shareProduct;

	//product_item_search
	private EditText product_item_search;

	//sort type 
	private final String PRICE_SORT_DEFAULT = "默认排序";
	private final String PRICE_SORT_ASC = "价格升序";
	private final String PRICE_SORT_DESC = "价格降序";

	//display features
	private ViewGroup rl_no_network;
	private ViewGroup rl_loading_data;
	private ViewGroup rl_no_data;
	private ImageView iv_no_network;
	private TextView tv_no_data;
	
	//Tencent share
	public static String mAppid = "222222";
	public static Tencent mTencent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		setContentView(R.layout.activity_product_list);
		rootView = getLayoutInflater().inflate(R.layout.activity_product_list,
				null);
		setContentView(rootView);
		setTitle(R.string.title_activity_product_list);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		// TODO Auto-generated method stub
		setBarVisibility(View.GONE);

		// 设置适配器
		sortTypeList = new LinkedList<String>();
		sortTypeList.add(PRICE_SORT_DEFAULT);
		sortTypeList.add(PRICE_SORT_ASC);
		sortTypeList.add(PRICE_SORT_DESC);
		conditionAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, sortTypeList);
		//		lv_product_condition_type.setAdapter(conditionAdapter);

		//iv_productlist_back
		iv_productlist_back = (ImageView) findViewById(R.id.iv_productlist_back);
		iv_productlist_back.setOnClickListener(this);

		// pullist_productlistView
		plist_productlistView = (PullToRefreshListView) findViewById(R.id.pullist_productlistView);
		//		plist_productlistView.setMode(Mode.PULL_FROM_END);
		plist_productlistView.setMode(Mode.BOTH);

		plist_productlistView.setOnItemClickListener(this);
		productAdapter = new ProductAdapter(this);
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

		//		plist_productlistView.setRefreshing(true);

		//		rl_product_list_content = (ViewGroup) findViewById(R.id.rl_product_list_content);

		//tv_product_list_category
		tv_product_list_category = (TextView) findViewById(R.id.tv_product_list_category);
		tv_product_list_category.setOnTouchListener(this);

		//tv_product_condition_filter
		tv_product_condition_filter = (TextView) findViewById(R.id.tv_product_condition_filter);
		tv_product_condition_filter.setOnTouchListener(this);

		//product_item_search
		product_item_search = (EditText) findViewById(R.id.product_item_search);
		product_item_search.addTextChangedListener(this);
		product_item_search
				.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub
						//				event.getKeyCode()==KeyEvent.KEYCODE_ENTER
						Log.d(TAG, "onEditorAction Text:"
								+ v.getText().toString());
						Log.d(TAG, "onEditorAction actionId:" + actionId);
						if (actionId == EditorInfo.IME_ACTION_GO
								|| actionId == EditorInfo.IME_ACTION_DONE
								|| actionId == EditorInfo.IME_ACTION_SEARCH) {
							product.setName(v.getText().toString());
							productAdapter.clear();
							page = 1;
							getProductList();
						}
						return false;
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
		
		//tv_no_data
		tv_no_data = (TextView) findViewById(R.id.tv_no_data);
	}

	private void createProductTypePopUpWin(View anchor) {
		productTypeWin = new PopupWindow(getBaseContext());
		View productTypeView = getLayoutInflater().inflate(
				R.layout.activity_product_type, null);
		productTypeWin.setContentView(productTypeView);
		productTypeWin.setWidth(LayoutParams.MATCH_PARENT);
		productTypeWin.setHeight(LayoutParams.WRAP_CONTENT);
		//		productTypeWin.setAnimationStyle(R.style.anim_popup_dir);
		//		win.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_bg));
		productTypeWin.setOutsideTouchable(false);
		productTypeWin.setTouchable(true);
		productTypeWin.setFocusable(true);
		//		productTypeWin.getBackground().setAlpha(140);
		//		productTypeWin.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
		productTypeWin.showAsDropDown(anchor);
		// set background alpha
		//		CommonUtils.setBackgroundAlpha(getWindow(), 0.5f);
		//		rl_product_list_content.getBackground().setAlpha(100);
		// set background alpha when window dismiss
		productTypeWin.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				//				rl_product_list_content.getBackground().setAlpha(255);
				//				CommonUtils.setBackgroundAlpha(plist_productlistView, 1f);
				Drawable drawableDown = getResources().getDrawable(
						R.drawable.filter_icon_down);
				drawableDown.setBounds(0, 0, drawableDown.getMinimumWidth(),
						drawableDown.getMinimumHeight());
				tv_product_list_category.setCompoundDrawables(null, null,
						drawableDown, null);
			}
		});

		//initialize product type components
		initProductTypeComponents(productTypeView);

		//initialize product type data list
		//		productTypeAdapter.clear();
		//		getProductTypeList();
	}

	private void createConditionPopUpWin(View anchor) {
		conditionWin = new PopupWindow(getBaseContext());
		View conditionView = getLayoutInflater().inflate(
				R.layout.activity_product_sort_type, null);
		conditionWin.setContentView(conditionView);
		conditionWin.setWidth(LayoutParams.MATCH_PARENT);
		conditionWin.setHeight(LayoutParams.WRAP_CONTENT);
		//		productTypeWin.setAnimationStyle(R.style.anim_popup_dir);
		//		win.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_bg));
		conditionWin.setOutsideTouchable(false);
		conditionWin.setTouchable(true);
		conditionWin.setFocusable(true);
		//		productTypeWin.getBackground().setAlpha(140);
		//		productTypeWin.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
		conditionWin.showAsDropDown(anchor);
		// set background alpha
		//		CommonUtils.setBackgroundAlpha(getWindow(), 0.5f);
		//		rl_product_list_content.getBackground().setAlpha(100);
		// set background alpha when window dismiss
		conditionWin.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				//				rl_product_list_content.getBackground().setAlpha(255);
				//				CommonUtils.setBackgroundAlpha(plist_productlistView, 1f);
				Drawable drawableDown = getResources().getDrawable(
						R.drawable.filter_icon_down);
				drawableDown.setBounds(0, 0, drawableDown.getMinimumWidth(),
						drawableDown.getMinimumHeight());
				tv_product_condition_filter.setCompoundDrawables(null, null,
						drawableDown, null);
			}
		});

		//initialize product type components
		initConditionComponents(conditionView);

		//initialize product type data list
		//		productTypeAdapter.clear();
		//		getProductTypeList();
	}

	@Override
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

	@Override
	public void initData() {

		//get product list
		productService = ServiceUtils.getInstance(this).getProductService();
		getProductList();

		// 初始化数据和数据源
		productTypeAdapter = new ProductTypeAdapter(this);
		getProductTypeList();
		//
	}

	public void getProductList() {
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
									ProductListActivity.this,
									getString(R.string.pull_to_refresh_no_more_date_label),
									Toast.LENGTH_SHORT).show();
						}
					}
					rl_no_network.setVisibility(View.GONE);
					productAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(ProductListActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				
				plist_productlistView.onRefreshComplete();
				if (productAdapter.getCount() == 0) {
					rl_no_data.setVisibility(View.VISIBLE);
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				Toast.makeText(ProductListActivity.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();
				
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}
		}
	};

	public void initProductTypeComponents(View view) {
		//lv_product_type
		lv_product_type = (ListView) view.findViewById(R.id.lv_product_type);
		lv_product_type.setOnItemClickListener(this);

		//productTypeAdapter
		//		productTypeAdapter = new ProductTypeAdapter(this);
		lv_product_type.setAdapter(productTypeAdapter);
	}

	public void initConditionComponents(View view) {
		//lv_product_type
		lv_product_condition_type = (ListView) view
				.findViewById(R.id.lv_product_condition_type);
		lv_product_condition_type.setOnItemClickListener(this);

		//productTypeAdapter
		//		productTypeAdapter = new ProductTypeAdapter(this);
		lv_product_condition_type.setAdapter(conditionAdapter);
	}

	public void getProductTypeList() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = productService.getProductTypeList();
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
				handlerGetProductTypeList.sendMessage(msg);
			}

		});
	}

	private Handler handlerGetProductTypeList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				JsonObj jsonObj = (JsonObj) msg.obj;
				if (jsonObj.getStatus() == Constants.AJAX_STATUS_SUCESS) {
					LinkedList<TProductType> dataList = (LinkedList<TProductType>) jsonObj
							.getValue();
					productTypeAdapter.addItemLast(new TProductType(null,
							"全部分类"));
					if (dataList != null && !dataList.isEmpty()) {
						for (TProductType item : dataList) {
							productTypeAdapter.addItemLast(item);
						}
					}
					productTypeAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(ProductListActivity.this, jsonObj.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MSG_WHAT_FAILED:
				JsonObj errJsonObj = (JsonObj) msg.obj;
				/*Toast.makeText(ProductListActivity.this, errJsonObj.getMsg(),
						Toast.LENGTH_SHORT).show();*/
				Log.d(TAG, "--error messgage--" + errJsonObj.getMsg());
				break;
			default:
				break;
			}

		}

	};

	private class GetDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return "" + (pageCount++);
		}

		@Override
		protected void onPostExecute(String result) {
			//			mListItems.add(result);
			productAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			plist_productlistView.onRefreshComplete();
		}
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
		case R.id.iv_productlist_back:
			finish();
			break;
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

	private void doShareToQQ(final Bundle params) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (null != MainActivity.mTencent) {
					MainActivity.mTencent.shareToQQ(ProductListActivity.this,
							params, qqShareListener);
				}
			}
		});
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {
			Toast.makeText(ProductListActivity.this, "取消分享 ",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			//        	Toast.makeText(ProductListActivity.this, "onComplete: " + response.toString(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			Toast.makeText(ProductListActivity.this, "分享出错：" + e.errorMessage,
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
		case R.id.lv_product_type:
			TProductType pType = (TProductType) productTypeAdapter
					.getItem(position);
			tv_product_list_category.setText(pType.getName());
			product.setTypeId(pType.getId());
			productTypeWin.dismiss();
			productAdapter.clear();
			page = 1;
			getProductList();
			break;
		case R.id.lv_product_condition_type:
			String condition = (String) conditionAdapter.getItem(position);
			tv_product_condition_filter.setText(condition);
			//			product.setTypeId(pType.getId());
			conditionWin.dismiss();
			productAdapter.clear();
			page = 1;
			if (PRICE_SORT_DEFAULT.equals(condition)) {
				orderColumn = null;
				orderDirection = null;
			} else if (PRICE_SORT_ASC.equals(condition)) {
				orderColumn = "price";
				orderDirection = Constants.SORT_ASC;
			} else if (PRICE_SORT_DESC.equals(condition)) {
				orderColumn = "price";
				orderDirection = Constants.SORT_DESC;
			}

			getProductList();
			break;
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
		// TODO Auto-generated method stub
		//		return super.onKeyDown(keyCode, event);

		if (keyCode == KeyEvent.KEYCODE_BACK && productTypeWin != null
				&& productTypeWin.isShowing()) {
			productTypeWin.dismiss();
			return false;
		}

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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.tv_product_list_category) {
			if (productTypeWin != null && productTypeWin.isShowing()) {
				/*Drawable drawableDown = getResources().getDrawable(
						R.drawable.filter_icon_down);
				drawableDown.setBounds(0, 0, drawableDown.getMinimumWidth(),
						drawableDown.getMinimumHeight());
				tv_product_list_category.setCompoundDrawables(null, null,
						drawableDown, null);*/
				productTypeWin.dismiss();
			} else {
				Drawable drawableUp = getResources().getDrawable(
						R.drawable.filter_icon_up);
				drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(),
						drawableUp.getMinimumHeight());
				tv_product_list_category.setCompoundDrawables(null, null,
						drawableUp, null);
				//popup window
				createProductTypePopUpWin(v);
			}
			return false;
		} else if (v.getId() == R.id.tv_product_condition_filter) {
			if (conditionWin != null && conditionWin.isShowing()) {
				/*Drawable drawableDown = getResources().getDrawable(
						R.drawable.filter_icon_down);
				drawableDown.setBounds(0, 0, drawableDown.getMinimumWidth(),
						drawableDown.getMinimumHeight());
				tv_product_list_category.setCompoundDrawables(null, null,
						drawableDown, null);*/
				conditionWin.dismiss();
			} else {
				Drawable drawableUp = getResources().getDrawable(
						R.drawable.filter_icon_up);
				drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(),
						drawableUp.getMinimumHeight());
				tv_product_condition_filter.setCompoundDrawables(null, null,
						drawableUp, null);
				//popup window
				createConditionPopUpWin(v);
			}
			return false;
		}
		return true;
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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		Log.d(TAG, "beforeTextChanged");
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.d(TAG, "onTextChanged");
	}

	@Override
	public void afterTextChanged(Editable s) {
		product.setName(product_item_search.getText().toString());
		productAdapter.clear();
		page = 1;
		getProductList();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (api != null) {
			api.unregisterApp();
		}
	}
}
