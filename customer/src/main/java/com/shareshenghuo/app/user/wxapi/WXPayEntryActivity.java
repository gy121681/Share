package com.shareshenghuo.app.user.wxapi;


import net.sourceforge.simcpux.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.shareshenghuo.app.user.receiver.ExitActivityWatcher;
import com.shareshenghuo.app.user.receiver.PayWatcher;
import com.shareshenghuo.app.user.util.T;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

	private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}
	
	@Override
	public void onReq(BaseReq arg0) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			Intent it1 = new Intent(PayWatcher.ACT_PAY);
			it1.putExtra("code", resp.errCode);
			it1.putExtra("str", resp.errStr);
			sendBroadcast(it1);
			
			if(resp.errCode == 0) {
				// 成功
				T.showShort(this, "支付成功");
				sendBroadcast(new Intent(ExitActivityWatcher.ACT_EXIT));
			} else {
				// 失败
				T.showShort(this, "支付失败："+resp.errCode);
			}
		 }
		finish();
	}
}