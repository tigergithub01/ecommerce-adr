package com.ecommerce.activity.myincome;

import java.text.DecimalFormat;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.model.TVipIncomeDetail;
import com.ecommerce.utils.Constants;

public class VipIncomeDetailAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<TVipIncomeDetail> dataList;

	public VipIncomeDetailAdapter(Context context) {
		this.context = context;
		dataList = new LinkedList<TVipIncomeDetail>();
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
		public TextView tv_inc_det_order_code;
		public TextView tv_inc_det_sub_vip_no;
		public TextView tv_inc_det_product_name;
		public TextView tv_inc_det_amount;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = _LayoutInflater.inflate(
					R.layout.vip_inc_detail_adapter, null);
			holder.tv_inc_det_order_code = (TextView) convertView
					.findViewById(R.id.tv_inc_det_order_code);
			holder.tv_inc_det_sub_vip_no = (TextView) convertView
					.findViewById(R.id.tv_inc_det_sub_vip_no);
			holder.tv_inc_det_product_name = (TextView) convertView
					.findViewById(R.id.tv_inc_det_product_name);
			holder.tv_inc_det_amount = (TextView) convertView
					.findViewById(R.id.tv_inc_det_amount);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TVipIncomeDetail item = dataList.get(position);
		holder.tv_inc_det_order_code.setText(item.getOrderCode());
		holder.tv_inc_det_sub_vip_no.setText(item.getSubVipNo());
		holder.tv_inc_det_product_name.setText(item.getProductName());
		DecimalFormat df = new DecimalFormat(Constants.DECIMAL_FORMAT);
		holder.tv_inc_det_amount.setText(df.format(item.getAmount()));

		return convertView;
	}

	public LinkedList<TVipIncomeDetail> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<TVipIncomeDetail> dataList) {
		this.dataList = dataList;
	}

	public void addItemLast(TVipIncomeDetail item) {
		dataList.addLast(item);
	}

	public void addItemFirst(TVipIncomeDetail item) {
		dataList.addFirst(item);
	}

	public void removeItem(TVipIncomeDetail item) {
		dataList.remove(item);
	}

	public void clear() {
		dataList.clear();
	}

}
