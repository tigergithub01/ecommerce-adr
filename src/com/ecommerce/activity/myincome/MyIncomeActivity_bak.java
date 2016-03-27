package com.ecommerce.activity.myincome;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.order.OrderFragment;
import com.ecommerce.utils.PagerFragment;

public class MyIncomeActivity_bak extends BaseActivity  {
	private RadioGroup rg_my_income;
	private ViewPager vPager_my_income;
	private List<PagerFragment> fragmentList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_income_bak);
		setTitle(R.string.title_activity_my_income);
		
		initComponents();
		initParameters();
		initData();
		
	}
	
	public void initComponents() {
		rg_my_income = (RadioGroup) findViewById(R.id.rg_my_income);
		vPager_my_income = (ViewPager) findViewById(R.id.vPager_my_income);
		
		initFragmentList();
		vPager_my_income.setOffscreenPageLimit(3);
		
		vPager_my_income.setOnPageChangeListener(new PageChangeListener());
		final HomeFragmentPagerAdapter pagerAdapter = new HomeFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList, getBaseContext());
		vPager_my_income.setAdapter(pagerAdapter);

		rg_my_income.check(R.id.rb_income_person);
		rg_my_income.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				Log.d(TAG, "onCheckedChanged checkedId:"+checkedId);
				vPager_my_income.setCurrentItem(getPositionById(checkedId));

			}
		});
	}
	
	private void initFragmentList() {
		fragmentList = new ArrayList<PagerFragment>();
		fragmentList.add(new PagerFragment(R.id.rb_income_person,
				new OrderFragment()));
		fragmentList.add(new PagerFragment(R.id.rb_income_group,
				new OrderFragment()));
		fragmentList.add(new PagerFragment(R.id.rb_income_feelist,
				new OrderFragment()));

	}
	
	private int getItemIdByPosition(int position) {
		return fragmentList.get(position).getId();
	}

	private int getPositionById(int id) {
		for (int i = 0; i < fragmentList.size(); i++) {
			if (fragmentList.get(i).getId() == id) {
				return i;
			}
		}
		return 0;
	}
	
	class PageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			rg_my_income.check(getItemIdByPosition(position));

		}

	}
	
	class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
		private List<PagerFragment> fragmentsList;
		private Context ctx;

		public HomeFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public HomeFragmentPagerAdapter(FragmentManager fm,
				List<PagerFragment> fragmentsList, Context ctx) {
			super(fm);
			this.fragmentsList = fragmentsList;
			this.ctx = ctx;
		}

		@Override
		public Fragment getItem(int position) {
			PagerFragment fragment = fragmentsList.get(position);
			Bundle param = fragment.getParameters();
			return Fragment.instantiate(ctx, fragmentsList.get(position)
					.getFragment().getClass().getName(), param);
		}

		@Override
		public int getCount() {
			if (fragmentsList != null) {
				return fragmentsList.size();
			} else {
				return 0;
			}

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return super.isViewFromObject(view, object);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}

	public void initParameters() {

	}

	public void initData() {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.my_income, menu);
		return true;
	}

}
