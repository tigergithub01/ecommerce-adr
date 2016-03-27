package com.ecommerce.activity.personal;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.model.TVip;

public class VipAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<TVip> dataList;

	public VipAdapter(Context context) {
		this.context = context;
		dataList = new LinkedList<TVip>();
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
		public TextView tv_vip_no;
		public TextView tv_vip_direct_type;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = _LayoutInflater.inflate(R.layout.vip_adapter, null);
			holder.tv_vip_no = (TextView) convertView
					.findViewById(R.id.tv_vip_no);
			holder.tv_vip_direct_type = (TextView) convertView
					.findViewById(R.id.tv_vip_direct_type);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TVip item = dataList.get(position);
		holder.tv_vip_no.setText(item.getVip_no());
		holder.tv_vip_direct_type
				.setText((item.getDirect_flag() != null && item
						.getDirect_flag().intValue() == 1) ? "直接团员" : "间接团员");
		return convertView;
	}

	public LinkedList<TVip> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<TVip> dataList) {
		this.dataList = dataList;
	}

	public void addItemLast(TVip item) {
		dataList.addLast(item);
	}

	public void addItemFirst(TVip item) {
		dataList.addFirst(item);
	}

	public void removeItem(TVip item) {
		dataList.remove(item);
	}

	public void clear() {
		dataList.clear();
	}

}
