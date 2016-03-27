package com.ecommerce.activity.personal;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.model.TBankInfo;

public class BankInfoAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<TBankInfo> dataList;

	public BankInfoAdapter(Context context) {
		this.context = context;
		dataList = new LinkedList<TBankInfo>();
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
		public TextView tv_bank_info_name;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = _LayoutInflater.inflate(
					R.layout.bank_info_adapter, null);
			holder.tv_bank_info_name = (TextView) convertView
					.findViewById(R.id.tv_bank_info_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TBankInfo item = dataList.get(position);
		holder.tv_bank_info_name.setText(item.getName());
		return convertView;
	}

	public LinkedList<TBankInfo> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<TBankInfo> dataList) {
		this.dataList = dataList;
	}

	public void addItem(TBankInfo item) {
		dataList.add(item);
	}

	public void removeItem(TBankInfo item) {
		dataList.remove(item);
	}

}
