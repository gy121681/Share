package com.shareshenghuo.app.shop;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shareshenghuo.app.shop.fragment.ConsumptionFragment;

public class ConsumptionFragmentActivity extends BaseTopActivity{
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.consumption_fragment_activity);
		initTopBar("消费系列");
        Button btn1 = (Button) findViewById(R.id.btnTopRight1);
        btn1.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btn1.setLayoutParams(lp);
        lp.setMargins(0, 0, 10, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btn1.setBackground(getResources().getDrawable(R.drawable.share_b_public_icon_date));
        } else {
            btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.share_b_public_icon_date));
        }
        final ConsumptionFragment fragment = (ConsumptionFragment)(getSupportFragmentManager().findFragmentById(R.id.fragment));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment != null) {
                    fragment.setdate();
                }
            }
        });
    }

}
