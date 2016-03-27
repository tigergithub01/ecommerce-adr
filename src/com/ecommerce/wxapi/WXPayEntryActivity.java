package com.ecommerce.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ecommerce.R;
import com.ecommerce.activity.BaseActivity;
import com.ecommerce.activity.DetailActivity;
import com.ecommerce.model.WebviewObject;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private static final String TAG = WXPayEntryActivity.class.getSimpleName();
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        setTitle(R.string.pay_wx_result);
        
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        
        initComponents();
		initParameters();
		initData();
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		int result = 0;
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode==0){
				result=R.string.pay_wx_result_success;
				String url = ConfigReader.getInstance(this).getRemoteUrl(
						"/index.php?r=sale/vip-center/index");
				WebviewObject obj = new WebviewObject();
				obj.setUrl(url);
				Intent intent = new Intent(this, DetailActivity.class);
				Bundle extras = new Bundle();
				extras.putSerializable("key", obj);
				intent.putExtras(extras);
				startActivity(intent);
				
			}else if(resp.errCode==-1){
				result=R.string.pay_wx_result_failed;
			}else if(resp.errCode==-2){
				result=R.string.pay_wx_result_cancel;
			}
			
			/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errStr +";code=" + String.valueOf(resp.errCode)));
			builder.show();*/
			
			//TODO:			
			finish();
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			
		}
	}

	@Override
	public void initComponents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initParameters() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
}