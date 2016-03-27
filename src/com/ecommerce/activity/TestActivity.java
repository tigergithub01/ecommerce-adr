package com.ecommerce.activity;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ecommerce.R;

public class TestActivity extends Activity implements OnClickListener {
	private Button btn_popup;
	private LinearLayout ll_test_menu;
	private View rootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = getLayoutInflater().inflate(R.layout.activity_test, null);
		setContentView(rootView);
		
//		setContentView(R.layout.activity_test);
		
		initComponents();
		initParameters();
		initData();
	}
	
	private void initComponents() {
		btn_popup = (Button)findViewById(R.id.btn_test_popup);
		btn_popup.setOnClickListener(this);
		
		ll_test_menu = (LinearLayout)findViewById(R.id.ll_test_menu);
		
//		 FragmentManager fm = getFragmentManager();  
//	     FragmentTransaction tx = fm.beginTransaction();  
	        
//		getFragmentManager().beginTransaction().replace(R.id.frag_test_detail, new Fragment()).commit();
	}
	
	private void initParameters() {
		
	}
	
	private void initData(){
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_test_popup:
//			openOptionsMenu();
//			ll_test_menu.setVisibility(View.VISIBLE);
			Context ctx = getBaseContext();
			PopupWindow win = new PopupWindow(getBaseContext());
			final Resources res = ctx.getResources();
			View shareView = getLayoutInflater().inflate(R.layout.sharepopwindow, null);
			win.setContentView(shareView);
			win.setWidth(LayoutParams.MATCH_PARENT);
			win.setHeight(LayoutParams.WRAP_CONTENT);
			win.setOutsideTouchable(true);
			win.showAsDropDown(btn_popup, 0, 0);
			win.setTouchable(true);
//			ImageView img_qq_share = (ImageView)shareView.findViewById(R.id.img_share_qq);
			LinearLayout layout_qq = (LinearLayout)shareView.findViewById(R.id.layout_qq);
			
//			img_qq_share.setOnClickListener(this);
			layout_qq.setOnClickListener(this);
//			win.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
			break;
		
		case R.id.layout_qq:
			Toast.makeText(this, "qq share", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
