package com.ecommerce.activity;

import android.support.v4.app.FragmentActivity;

public abstract class AbstractActivity extends FragmentActivity {
	public abstract void initComponents();
	public abstract void initParameters();
	public abstract void initData(); 
}
