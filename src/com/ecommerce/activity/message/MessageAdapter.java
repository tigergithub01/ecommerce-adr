package com.ecommerce.activity.message;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.model.TNotification;

public class MessageAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<TNotification> dataList;

	public MessageAdapter(Context context) {
		this.context = context;
		dataList = new LinkedList<TNotification>();
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
		public TextView tv_notification_title;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = _LayoutInflater.inflate(R.layout.message_adapter,
					null);
			holder.tv_notification_title = (TextView) convertView
					.findViewById(R.id.tv_notification_title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TNotification item = dataList.get(position);
		holder.tv_notification_title.setText(item.getTitle());

		return convertView;
	}

	public LinkedList<TNotification> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<TNotification> dataList) {
		this.dataList = dataList;
	}

	public void addItemLast(TNotification item) {
		dataList.addLast(item);
	}

	public void addItemFirst(TNotification item) {
		dataList.addFirst(item);
	}

	public void removeItem(TNotification item) {
		dataList.remove(item);
	}

	public void clear() {
		dataList.clear();
	}

}
