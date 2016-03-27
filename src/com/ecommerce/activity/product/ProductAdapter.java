package com.ecommerce.activity.product;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.activity.DetailActivity;
import com.ecommerce.model.TProduct;
import com.ecommerce.model.WebviewObject;
import com.ecommerce.utils.CommonUtils;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;

public class ProductAdapter extends BaseAdapter {
	private ProductListActivity context;
	private LinkedList<TProduct> dataList;

	public ProductAdapter(ProductListActivity context) {
		this.context = context;
		dataList = new LinkedList<TProduct>();
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
		private ViewGroup rl_product;
		private ImageView tv_product_img;
		public TextView tv_product_name;
		public TextView tv_product_price;
		public TextView tv_product_share_btn;
		public TextView tv_product_buy_btn;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = _LayoutInflater.inflate(R.layout.product_adapter,
					null);

			holder.rl_product = (ViewGroup) convertView
					.findViewById(R.id.rl_product);
			holder.tv_product_img = (ImageView) convertView
					.findViewById(R.id.tv_product_img);
			holder.tv_product_name = (TextView) convertView
					.findViewById(R.id.tv_product_name);
			holder.tv_product_price = (TextView) convertView
					.findViewById(R.id.tv_product_price);
			holder.tv_product_share_btn = (TextView) convertView
					.findViewById(R.id.tv_product_share_btn);
			holder.tv_product_buy_btn = (TextView) convertView
					.findViewById(R.id.tv_product_buy_btn);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TProduct item = dataList.get(position);

		if (item.getPrimaryPhotoBitmap() == null) {
			holder.tv_product_img.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.app_default_image));
		} else {
			holder.tv_product_img.setImageDrawable(new BitmapDrawable(item
					.getPrimaryPhotoBitmap()));
		}
		holder.tv_product_name.setText(CommonUtils.getFirstWords(item.getName(), 20, Constants.FIRST_WORD_ETC));
		holder.tv_product_price.setText(CommonUtils.formatAmount(item.getPrice()));
		holder.tv_product_share_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//				Toast.makeText(context, "share" + item.getName(),
				//						Toast.LENGTH_LONG).show();
				context.shareProduct = item;
				context.createSharePopUpWin();
			}
		});
		holder.tv_product_buy_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Map<String, Object> parameters  = new HashMap<String, Object>();
				parameters.put("{product_id}", item.getId());
				parameters.put("{quantity}", 1);
				parameters.put("{checked}", 1);
				String url = ConfigReader.getInstance(context).getRemoteUrl(
						"/index.php?r=sale/vip-order-confirm/create&detailList[0][product_id]={product_id}&detailList[0][quantity]={quantity}&detailList[0][checked]={checked}",parameters
								);
				WebviewObject obj = new WebviewObject();
				obj.setUrl(url);
				Intent intent = new Intent(context, DetailActivity.class);
				Bundle extras = new Bundle();
				extras.putSerializable("key", obj);
				intent.putExtras(extras);
				context.startActivity(intent);

				/*Toast.makeText(context, "buy" + item.getName(),
						Toast.LENGTH_LONG).show();*/
			}
		});
		holder.rl_product.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = ConfigReader.getInstance(context).getRemoteUrl(
						"/index.php?r=sale/product/view&id=" + item.getId());
				WebviewObject obj = new WebviewObject();
				obj.setUrl(url);
				Intent intent = new Intent(context, DetailActivity.class);
				Bundle extras = new Bundle();
				extras.putSerializable("key", obj);
				intent.putExtras(extras);
				context.startActivity(intent);
				/*context.startActivity(new Intent(context,
						ProductDetailActivity.class));*/
			}
		});

		return convertView;
	}

	public List<TProduct> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<TProduct> dataList) {
		this.dataList = dataList;
	}

	public void addItemLast(TProduct item) {
		dataList.addLast(item);
	}

	public void addItemFirst(TProduct item) {
		dataList.addFirst(item);
	}

	public void removeItem(TProduct item) {
		dataList.remove(item);
	}

	public void clear() {
		dataList.clear();
	}
	
	

}
