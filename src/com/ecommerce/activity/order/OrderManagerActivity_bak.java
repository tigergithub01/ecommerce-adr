package com.ecommerce.activity.order;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ecommerce.R;
import com.ecommerce.utils.PagerFragment;

public class OrderManagerActivity_bak extends FragmentActivity  {
	private ViewPager vPager_my_income;
	private List<PagerFragment> fragmentList;
	private PagerTabStrip pagetabs_order;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_manager);
		
		initComponents();
		initParameters();
		initData();
		
	}
	
	private void initComponents() {
		vPager_my_income = (ViewPager) findViewById(R.id.vPager_my_income);
		pagetabs_order = (PagerTabStrip) findViewById(R.id.pagetabs_order);
		pagetabs_order.setTextSpacing(2);
		
		initFragmentList();
		vPager_my_income.setOffscreenPageLimit(3);
		
		vPager_my_income.setOnPageChangeListener(new PageChangeListener());
		final HomeFragmentPagerAdapter pagerAdapter = new HomeFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList, getBaseContext());
		vPager_my_income.setAdapter(pagerAdapter);

	}
	
	private void initFragmentList() {
		fragmentList = new ArrayList<PagerFragment>();
		fragmentList.add(new PagerFragment(
				new OrderFragment(),"待付款"));
		fragmentList.add(new PagerFragment(
				new OrderFragment(),"待发货"));
		fragmentList.add(new PagerFragment(
				new OrderFragment(),"待收货"));
		fragmentList.add(new PagerFragment(
				new OrderFragment(),"待评价"));
		fragmentList.add(new PagerFragment(
				new OrderFragment(),"已完成"));

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
//			rg_my_income.check(getItemIdByPosition(position));

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
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
//			return super.getPageTitle(position);
			return (CharSequence)fragmentsList.get(position).getPageTitle();
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

	private void initParameters() {

	}

	private void initData() {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.my_income, menu);
		return true;
	}

}
