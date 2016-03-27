package com.ecommerce.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.utils.VersionUtils;

public class AboutUsActivity  extends BaseActivity implements OnClickListener  {
	private TextView tv_about_us_ver_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		setTitle(R.string.title_activity_about_us);
		
		initComponents();
		initParameters();
		initData();
		
	}


	@Override
	public void initComponents() {
		// TODO Auto-generated method stub
		tv_about_us_ver_name = (TextView)findViewById(R.id.tv_about_us_ver_name);
		tv_about_us_ver_name.setText(VersionUtils.getVerName(this));
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
		// TODO Auto-generated method stub
		
	}

}
