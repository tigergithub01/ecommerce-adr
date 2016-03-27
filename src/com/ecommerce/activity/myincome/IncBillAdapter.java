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
import com.ecommerce.model.TVipWithdrawFlow;
import com.ecommerce.utils.Constants;

public class IncBillAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<TVipWithdrawFlow> dataList;

	public IncBillAdapter(Context context) {
		this.context = context;
		dataList = new LinkedList<TVipWithdrawFlow>();
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
		public TextView tv_inc_withdraw_code;
		public TextView tv_inc_withdraw_status;
		public TextView tv_inc_withdraw_apply_date;
		public TextView tv_inc_withdraw_amount;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = _LayoutInflater.inflate(
					R.layout.vip_inc_bill_adapter, null);
			holder.tv_inc_withdraw_code = (TextView) convertView
					.findViewById(R.id.tv_inc_withdraw_code);
			holder.tv_inc_withdraw_status = (TextView) convertView
					.findViewById(R.id.tv_inc_withdraw_status);
			holder.tv_inc_withdraw_apply_date = (TextView) convertView
					.findViewById(R.id.tv_inc_withdraw_apply_date);
			holder.tv_inc_withdraw_amount = (TextView) convertView
					.findViewById(R.id.tv_inc_withdraw_amount);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TVipWithdrawFlow item = dataList.get(position);
		holder.tv_inc_withdraw_code.setText(item.getCode());
		String statusName = null;
		if(item.getStatus()==1){
			statusName="已结算";
		}else if(item.getStatus()==2){
			statusName="结算中";
		}else{
			statusName="未结算";
		}
//		holder.tv_inc_withdraw_status.setText((item.getStatus()==1)?"已结算":"未结算");
		holder.tv_inc_withdraw_status.setText(statusName);
		holder.tv_inc_withdraw_apply_date.setText(item.getApplyDate());
		DecimalFormat df = new DecimalFormat(Constants.DECIMAL_FORMAT);
		holder.tv_inc_withdraw_amount.setText(df.format(item.getAmount()));

		return convertView;
	}

	public LinkedList<TVipWithdrawFlow> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<TVipWithdrawFlow> dataList) {
		this.dataList = dataList;
	}

	public void addItemLast(TVipWithdrawFlow item) {
		dataList.addLast(item);
	}

	public void addItemFirst(TVipWithdrawFlow item) {
		dataList.addFirst(item);
	}

	public void removeItem(TVipWithdrawFlow item) {
		dataList.remove(item);
	}

	public void clear() {
		dataList.clear();
	}

}
