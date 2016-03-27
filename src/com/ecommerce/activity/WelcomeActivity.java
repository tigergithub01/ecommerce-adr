package com.ecommerce.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.model.PhoneInfo;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.VersionUtils;

public class WelcomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		/*View view = ((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
		view.setVisibility(View.GONE);
		
		Toast.makeText(this, "hide ok1?", Toast.LENGTH_LONG).show();
		
		((RelativeLayout)findViewById(R.id.loading)).setVisibility(View.GONE); 		
		Toast.makeText(this, "hide ok?", Toast.LENGTH_LONG).show();*/
		
		initComponents();
		initParameters();
		initData();
		 
		
	}

	public void initComponents() {
		setBarVisibility(View.GONE);
	}

	public void initParameters() {

	}

	public void initData() {
		initPhoneInfo();
		jumpToIntent();
	}

	private void jumpToIntent() {
//		final Intent intent = new Intent(this, MainActivity.class);
		final Intent intent = new Intent(this, HostActivity.class);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startActivity(intent); 
			}
		};
		timer.schedule(task, 1000 * 1); 
	}
	
	public void initPhoneInfo(){
		PhoneInfo phoneInfo = new PhoneInfo();
		phoneInfo.setOsRelease(CommonUtils.getOsRelease());
		phoneInfo.setOsSdk(CommonUtils.getOsSdk());
		phoneInfo.setPhoneBrand(CommonUtils.getPhoneBrand());
		phoneInfo.setAppVerName(VersionUtils.getVerName(this));
		Constants.phoneInfo = phoneInfo;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
