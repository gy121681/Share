package com.td.qianhai.epay.oem.activity.lucre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.td.qianhai.epay.oem.BaseActivity;
import com.td.qianhai.epay.oem.R;

/**
 * Created by Snow on 2017/7/17.
 */

public class ExchangeMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_activity);
        initData();
        initView();
        setEvent();
    }

    private void initData(){

    }

    private void initView(){
        initTitle();
    }

    private void initTitle(){
        TextView title = getView(R.id.tv_title_contre);
        title.setText("兑换");
        TextView leftBack = getView(R.id.bt_title_left);
        leftBack.setText("");
        getView(R.id.bt_title_right).setVisibility(View.GONE);
        TextView rightBtn = getView(R.id.bt_title_right1);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText("兑换记录");
    }

    private void setEvent(){
        getView(R.id.bt_title_right1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ExchangeHistoryActivity.class));
            }
        });
        getView(R.id.bt_title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
