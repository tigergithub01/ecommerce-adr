package com.ecommerce.activity.order;

import java.text.DecimalFormat;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.model.TSoSheet;
import com.ecommerce.utils.Constants;

public class OrderAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<TSoSheet> dataList;

	public OrderAdapter(Context context) {
		this.context = context;
		dataList = new LinkedList<TSoSheet>();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		public TextView tv_order_code;
		public TextView tv_order_status;
		public TextView tv_order_date;
		public TextView tv_order_amt;
		public TextView tv_order_vip_no;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = _LayoutInflater.inflate(R.layout.order_adapter, null);
			holder.tv_order_code = (TextView) convertView
					.findViewById(R.id.tv_order_code);
			holder.tv_order_status = (TextView) convertView
					.findViewById(R.id.tv_order_status);
			holder.tv_order_date = (TextView) convertView
					.findViewById(R.id.tv_order_date);
			holder.tv_order_amt = (TextView) convertView
					.findViewById(R.id.tv_order_amt);
			holder.tv_order_vip_no = (TextView) convertView
					.findViewById(R.id.tv_order_vip_no);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TSoSheet item = dataList.get(position);
		holder.tv_order_code.setText(item.getCode());
		holder.tv_order_status.setText(item.getOrderStatusVal());
		holder.tv_order_date.setText(item.getOrderDate());
		DecimalFormat df = new DecimalFormat(Constants.DECIMAL_FORMAT);
		holder.tv_order_amt.setText(df.format(item.getOrderAmt()));
		holder.tv_order_vip_no.setText(item.getVipNo());

		return convertView;
	}

	public LinkedList<TSoSheet> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<TSoSheet> dataList) {
		this.dataList = dataList;
	}

	public void addItemLast(TSoSheet item) {
		dataList.addLast(item);
	}

	public void addItemFirst(TSoSheet item) {
		dataList.addFirst(item);
	}

	public void removeItem(TSoSheet item) {
		dataList.remove(item);
	}

	public void clear() {
		dataList.clear();
	}

}
