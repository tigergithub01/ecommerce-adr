package com.ecommerce.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.earn.VipShareActivity;
import com.ecommerce.activity.main.MainActivityTab;
import com.ecommerce.activity.personal.PersonalActivity;
import com.ecommerce.activity.product.ProductListActivity;
import com.ecommerce.utils.CommonUtils;

public class HostActivity extends TabActivity {
	private long firstTime = 0;

	private TabHost tabHost;

	private View rootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host);
		/*rootView = getLayoutInflater().inflate(R.layout.activity_vip_share,
				null);*/
		//		setContentView(rootView);
		tabHost = getTabHost();

		CommonUtils.addActivity(this);

		initComponents();
		initParameters();
		initData();
	}

	public void initComponents() {
		Intent homepageIntent = new Intent(this, MainActivityTab.class);
		tabHost.addTab(this.buildTagSpec("homepage",
				R.string.title_activity_host_homepage,
				R.drawable.tab_item_img_selector_home, homepageIntent));
		Intent productIntent = new Intent(this, ProductListActivity.class);
		tabHost.addTab(this.buildTagSpec("product",
				R.string.title_activity_host_product,
				R.drawable.tab_item_img_selector_product, productIntent));
		Intent shareIntent = new Intent(this, VipShareActivity.class);
		tabHost.addTab(this.buildTagSpec("share",
				R.string.title_activity_host_share,
				R.drawable.tab_item_img_selector_share, shareIntent));
		Intent personalIntent = new Intent(this, PersonalActivity.class);
		tabHost.addTab(this.buildTagSpec("personal",
				R.string.title_activity_host_personal,
				R.drawable.tab_item_img_selector_personal, personalIntent));

	}

	public void initParameters() {

	}

	public void initData() {

	}

	private TabHost.TabSpec buildTagSpec(String tagName, int tagLable,
			int icon, Intent content) {
		View localView = LayoutInflater.from(this.tabHost.getContext())
				.inflate(R.layout.host_tab_item, null);
		ImageView tab_item_image = ((ImageView) localView
				.findViewById(R.id.tab_item_image));
		tab_item_image.setBackgroundResource(icon);
		TextView tab_item_text = ((TextView) localView
				.findViewById(R.id.tab_item_text));
		tab_item_text.setText(tagLable);

		return tabHost.newTabSpec(tagName).setIndicator(localView)
				.setContent(content);

		/*return tabHost
				.newTabSpec(tagName)
				.setIndicator(getResources().getString(tagLable),
						getResources().getDrawable(icon)).setContent(content);*/
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			long secondTime = System.currentTimeMillis();
			/*CommonUtils.showAlertDialog(this, "xx:"+(secondTime - firstTime));*/
			if (secondTime - firstTime > 2000) {
				Toast.makeText(this,
						"再按一次返回键退出" + getString(R.string.app_name),
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;
				return true;
			} else {
				Intent exit = new Intent();
				exit.setAction("Exit_APP");
				sendBroadcast(exit);

				//finish all activity
				CommonUtils.exitSystem(this);
			}
		}
		return super.dispatchKeyEvent(event);
	}

}
