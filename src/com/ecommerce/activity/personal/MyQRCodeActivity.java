package com.ecommerce.activity.personal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.QRCodeUtils;
import com.google.zxing.WriterException;

public class MyQRCodeActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_my_qr_code;
	private TextView tv_my_qrcode_vipno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_qrcode);
		setTitle(R.string.title_activity_my_qrcode);

		initComponents();
		initParameters();
		initData();
	}

	@Override
	public void initComponents() {
		//tv_my_qrcode_vipno
		tv_my_qrcode_vipno = (TextView) findViewById(R.id.tv_my_qrcode_vipno);
		if (Constants.vipSession != null) {
			tv_my_qrcode_vipno.setText(Constants.vipSession.getVip_no());
		}

		//iv_my_qr_code
		iv_my_qr_code = (ImageView) findViewById(R.id.iv_my_qr_code);
		QRCodeUtils qrUtils = new QRCodeUtils();
		try {
			String url = ConfigReader.getInstance(this).getRemoteUrl(
					"/index.php?r=sale/vip-register/index&parent_vip_no="
							+ Constants.vipSession.getVip_no());
			Bitmap qrImage = qrUtils.createQRImage(url, 100, 100);
			iv_my_qr_code.setImageBitmap(qrImage);
		} catch (WriterException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initParameters() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
