package com.ecommerce.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.DetailActivity;
import com.ecommerce.activity.LoginActivity;
import com.ecommerce.model.WebviewObject;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;

public class OrderGuideActivity extends BaseActivity implements OnClickListener {
	private ViewGroup rl_personal_order;
	private ViewGroup rl_group_order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_guide);
		setTitle(R.string.title_activity_order_guide);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		//rl_group_order
		rl_personal_order = (ViewGroup) findViewById(R.id.rl_personal_order);
		rl_personal_order.setOnClickListener(this);

		//rl_group_order
		rl_group_order = (ViewGroup) findViewById(R.id.rl_group_order);
		rl_group_order.setOnClickListener(this);

	}

	@Override
	public void initParameters() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_personal_order:
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			String url = ConfigReader.getInstance(this).getRemoteUrl(
					"/index.php?r=sale/vip-center/index");
			WebviewObject obj = new WebviewObject();
			obj.setUrl(url);
			Intent intent = new Intent(this, DetailActivity.class);
			Bundle extras = new Bundle();
			extras.putSerializable("key", obj);
			intent.putExtras(extras);
			startActivity(intent);
			break;
		case R.id.rl_group_order:
			startActivity(new Intent(this,
					OrderManagerActivity.class));
			if (Constants.vipSession == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			}
			break;
		default:
			break;
		}

	}

}
