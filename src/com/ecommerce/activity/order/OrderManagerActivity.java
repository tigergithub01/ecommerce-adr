package com.ecommerce.activity.order;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.model.TParameter;
import com.viewpagerindicator.TabPageIndicator;

public class OrderManagerActivity extends BaseActivity {
	//	private static final String[] CONTENT = new String[] { "待付款", "待发货", "待收货", "待评价", "已完成"};
	private List<TParameter> statusList = new ArrayList<TParameter>();
	private List<OrderFragment> orderFragmentList = new ArrayList<OrderFragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_manager_new);
		setTitle(R.string.title_activity_order_list);

		//init statusList
		statusList.add(new TParameter(0, "全部订单"));
		statusList.add(new TParameter(3001, "待支付"));
		statusList.add(new TParameter(3002, "待发货"));
		statusList.add(new TParameter(3003, "待收货"));
		//		statusList.add(new TParameter(3004, "待评价"));
		statusList.add(new TParameter(3005, "已完成"));
		statusList.add(new TParameter(3006, "已关闭"));
		statusList.add(new TParameter(3007, "待退货"));
		statusList.add(new TParameter(3008, "待退款"));

		createOrderFragmentList();

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(
				getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager_order_list);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(8);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator_order_list);
		indicator.setViewPager(pager);

	}

	private void createOrderFragmentList() {
		for (int i = 0; i < statusList.size(); i++) {
			OrderFragment orderFragment = new OrderFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("status", statusList.get(i).getId());
			orderFragment.setArguments(bundle);
			orderFragmentList.add(orderFragment);
		}
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			/*OrderFragment orderFragment= new OrderFragment();
			Bundle bundle = new Bundle();  
			bundle.putInt("status", statusList.get(position).getId());
			orderFragment.setArguments(bundle);*/

			OrderFragment orderFragment = orderFragmentList.get(position % orderFragmentList.size());
			return orderFragment;
		}
		
		

		@Override
		public CharSequence getPageTitle(int position) {
			return statusList.get(position).getPaVal();
			//            return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			//          return CONTENT.length;
			return statusList.size();
		}
	}

	public void initComponents() {
		// TODO Auto-generated method stub

	}

	public void initParameters() {
		// TODO Auto-generated method stub

	}

	public void initData() {
		// TODO Auto-generated method stub

	}
}
