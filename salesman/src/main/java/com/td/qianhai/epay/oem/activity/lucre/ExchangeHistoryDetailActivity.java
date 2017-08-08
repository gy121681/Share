package com.td.qianhai.epay.oem.activity.lucre;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.BaseActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.QuickAdapter;
import com.td.qianhai.epay.oem.adapter.QuickAdapterHolder;
import com.td.qianhai.epay.oem.views.DetailItemView;
import com.td.qianhai.epay.oem.views.PullToRefreshListView;

import java.util.Arrays;

/**
 * Created by Snow on 2017/7/17.
 */

public class ExchangeHistoryDetailActivity extends BaseActivity {

    private LinearLayout mLayoutDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_history_detail_activity);
        initData();
        initView();
        setEvent();
    }

    private void initData(){

    }

    private void initView(){
        initTitle();
        mLayoutDetails = (LinearLayout) findViewById(R.id.layout_detail);
        mLayoutDetails.addView(new DetailItemView(getApplicationContext(), " 申请人：", "XXXX"));
        mLayoutDetails.addView(new DetailItemView(getApplicationContext(), " 兑换时间：", "XXXX"));
        mLayoutDetails.addView(new DetailItemView(getApplicationContext(), " 开户行：", "XXXX"));
        mLayoutDetails.addView(new DetailItemView(getApplicationContext(), " 银行账号：", "XXXX"));
        mLayoutDetails.addView(new DetailItemView(getApplicationContext(), " 回购秀点数：", "XXXX"));
        mLayoutDetails.addView(new DetailItemView(getApplicationContext(), " 手续费：", "XXXX"));
        mLayoutDetails.addView(new DetailItemView(getApplicationContext(), " 兑换类型：", "XXXX"));
        mLayoutDetails.addView(new DetailItemView(getApplicationContext(), " 实际到账秀点：", "XXXX"));
        mLayoutDetails.addView(new DetailItemView(getApplicationContext(), " 状态：", "XXXX"));
    }

    private void initTitle(){
        TextView title = getView(R.id.tv_title_contre);
        title.setText("兑换详情");
        TextView leftBack = getView(R.id.bt_title_left);
        leftBack.setText("");
    }

    private void setEvent(){
        getView(R.id.bt_title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
