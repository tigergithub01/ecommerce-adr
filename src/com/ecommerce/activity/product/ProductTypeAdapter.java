package com.ecommerce.activity.product;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.model.TProduct;
import com.ecommerce.model.TProductType;

public class ProductTypeAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<TProductType> dataList;

	public ProductTypeAdapter(Context context) {
		this.context = context;
		dataList = new LinkedList<TProductType>();
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
		public TextView tv_product_type_name;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = _LayoutInflater.inflate(
					R.layout.product_type_adapter, null);
			holder.tv_product_type_name = (TextView) convertView
					.findViewById(R.id.tv_product_type_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TProductType item = dataList.get(position);
		holder.tv_product_type_name.setText(item.getName());
		return convertView;
	}

	public LinkedList<TProductType> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<TProductType> dataList) {
		this.dataList = dataList;
	}

	public void addItemLast(TProductType item) {
		dataList.addLast(item);
	}

	public void addItemFirst(TProductType item) {
		dataList.addFirst(item);
	}

	public void removeItem(TProductType item) {
		dataList.remove(item);
	}

	public void clear() {
		dataList.clear();
	}

}
