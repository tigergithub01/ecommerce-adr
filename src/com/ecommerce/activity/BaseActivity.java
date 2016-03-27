package com.ecommerce.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.utils.CommonUtils;

public abstract class BaseActivity extends FragmentActivity implements ActivityEventListener {
	
	/*public static List<Activity> activityList = new ArrayList<Activity>();*/
	
	private ImageView iv_bar_back;

	private TextView tv_bar_title;

	private TextView tv_bar_right;
	
	private TextView tv_bar_left;
	
	private ViewGroup ll_top_bar;
	
	 BroadcastReceiver mybroad=new BroadcastReceiver() { 

		@Override
		public void onReceive(Context context, Intent intent) {
			finish();  
		}  
		 
	 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.activity_base);
		init();
		
		 IntentFilter filter=new IntentFilter();  
	     filter.addAction("Exit_APP");  
	     this.registerReceiver(mybroad, filter);
	     
	     CommonUtils.addActivity(this);

		//		//		setTitle(null);
		//		setLeftButton("返回", new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//				finish();
		//			}
		//		});
	}

//	public abstract void initParameters();
	
	private void init() {
		iv_bar_back = (ImageView) findViewById(R.id.iv_bar_back);
		iv_bar_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tv_bar_title = (TextView) findViewById(R.id.tv_bar_title);

		tv_bar_right = (TextView) findViewById(R.id.tv_bar_right);
		
		tv_bar_left = (TextView) findViewById(R.id.tv_bar_left);
		
		ll_top_bar = (ViewGroup) findViewById(R.id.ll_top_bar);
	}

	/*private void initParameters() {

	}*/

//	private void initData() {
//
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mybroad);  
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	protected void setLeftBackButtonVisibility(int visibility) {
		iv_bar_back.setVisibility(visibility);
	}
	
	protected void setLeftButtonVisibility(int visibility) {
		tv_bar_left.setVisibility(visibility);
	}
	
	protected void setRightButtonVisibility(int visibility) {
		tv_bar_right.setVisibility(visibility);
	}
	
	protected void setBarTitleVisibility(int visibility) {
		tv_bar_title.setVisibility(visibility);
	}

	protected void setBarVisibility(int visibility) {
		ll_top_bar.setVisibility(visibility);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		LayoutInflater inflater = LayoutInflater.from(this);
		((LinearLayout) findViewById(R.id.layout_content)).addView(inflater
				.inflate(layoutResID, null));
	}
	
	@Override
	public void setContentView(View view) {
		((LinearLayout) findViewById(R.id.layout_content)).addView(view);
	}

	protected void setRightButton(String title, OnClickListener listener) {
		//		Button leftButton = (Button) findViewById(R.id.left_button);
		tv_bar_right.setText(title);
		tv_bar_right.setOnClickListener(listener);
	}

	protected void setRightButton(int strId, OnClickListener listener) {
		//		Button leftButton = (Button) findViewById(R.id.left_button);
		tv_bar_right.setText(getResources().getString(strId));
		tv_bar_right.setOnClickListener(listener);
	}
	
	protected void setLeftButton(String title, OnClickListener listener) {
		//		Button leftButton = (Button) findViewById(R.id.left_button);
		tv_bar_left.setText(title);
		tv_bar_left.setOnClickListener(listener);
	}
	
	protected void setLeftButton(int strId, OnClickListener listener) {
		//		Button leftButton = (Button) findViewById(R.id.left_button);
		tv_bar_left.setText(getResources().getString(strId));
		tv_bar_left.setOnClickListener(listener);
	}
	
	
	public void setTitle(int strId) {
		tv_bar_title.setText(getString(strId));
	}
	
	public void setTitle(CharSequence title) {
		tv_bar_title.setText(title);
	}
	
	public void setBarBannerVisibility(int visibility){
		 ((ImageView) findViewById(R.id.iv_bar_head)).setVisibility(visibility);
	}

	

}
