package com.ecommerce.activity.product;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.model.TProductType;
import com.ecommerce.utils.Constants;

public class ProductTypeActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	public static final String TAG = ProductTypeActivity.class.getSimpleName();
	private ListView lv_product_type;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private ProductTypeAdapter productTypeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_type);
		setTitle(R.string.title_activity_product_type);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		//lv_product_type
		lv_product_type = (ListView) findViewById(R.id.lv_product_type);
		lv_product_type.setOnItemClickListener(this);

		//productTypeAdapter
		productTypeAdapter = new ProductTypeAdapter(this);
		lv_product_type.setAdapter(productTypeAdapter);
	}

	@Override
	public void initParameters() {

	}

	@Override
	public void initData() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					LinkedList<TProductType> productTypeList = new LinkedList<TProductType>();
					
					TProductType pType = new TProductType();
					pType.setName("全部分类");
					productTypeList.add(pType);
					
					pType = new TProductType();
					pType.setName("移动POS机");
					productTypeList.add(pType);

					pType = new TProductType();
					pType.setName("服装");
					productTypeList.add(pType);

					msg.obj = productTypeList;
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					Bundle data = new Bundle();
					data.putString(Constants.NOTIFY_MSG, e.getMessage());
					msg.setData(data);
				}
				handler.sendMessage(msg);
			}
		});
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				LinkedList<TProductType> productTypeList = (LinkedList<TProductType>) msg.obj;
				if (productTypeList != null && !productTypeList.isEmpty()) {
					for (TProductType item : productTypeList) {
						productTypeAdapter.addItemLast(item);
					}
				}
				productTypeAdapter.notifyDataSetChanged();
				break;
			case Constants.MSG_WHAT_FAILED:
				Log.d(TAG,
						"--error messgage--"
								+ msg.getData().getString(Constants.NOTIFY_MSG));
				//				Toast.makeText(getBaseContext(),
				//						msg.getData().getString(Constants.NOTIFY_MSG),
				//						Toast.LENGTH_LONG).show();
				Toast.makeText(getBaseContext(),
						getString(R.string.err_get_data), Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TProductType pType = (TProductType) productTypeAdapter
				.getItem(position);
		Toast.makeText(getBaseContext(), pType.getName(), Toast.LENGTH_LONG)
				.show();
	}

}
