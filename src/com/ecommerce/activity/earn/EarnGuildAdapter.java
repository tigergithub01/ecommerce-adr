package com.ecommerce.activity.earn;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.model.TEarnGuild;

public class EarnGuildAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<TEarnGuild> dataList;

	public EarnGuildAdapter(Context context) {
		this.context = context;
		dataList = new LinkedList<TEarnGuild>();
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
		public TextView tv_earn_guild_title;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = _LayoutInflater.inflate(R.layout.earn_guild_adapter,
					null);
			holder.tv_earn_guild_title = (TextView) convertView
					.findViewById(R.id.tv_earn_guild_title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TEarnGuild item = dataList.get(position);
		holder.tv_earn_guild_title.setText(item.getTitle());

		return convertView;
	}

	public LinkedList<TEarnGuild> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<TEarnGuild> dataList) {
		this.dataList = dataList;
	}

	public void addItemLast(TEarnGuild item) {
		dataList.addLast(item);
	}

	public void addItemFirst(TEarnGuild item) {
		dataList.addFirst(item);
	}

	public void removeItem(TEarnGuild item) {
		dataList.remove(item);
	}

	public void clear() {
		dataList.clear();
	}

}
