package com.ecommerce.activity.product;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;

public class ProductDetailActivity extends  BaseActivity implements
OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		setTitle(R.string.title_activity_product_detail);

		initComponents();
		initParameters();
		initData();
	}


	@Override
	public void initComponents() {
		// TODO Auto-generated method stub
		
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
