package com.td.qianhai.epay.oem.activity.realname;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.td.qianhai.epay.oem.BaseActivity1;
import com.td.qianhai.epay.oem.R;


/**
 * 实名认证，验证手机号界面
 * Created by Snow on 2017/7/19.
 */

public class RealnameStepCompleteActivity extends BaseActivity1 {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_realname_step_complete);
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
        TextView mTtile = (TextView) findViewById(R.id.tvTopTitle);
        mTtile.setText("实名认证");
        //下一步
        getView(R.id.llTopBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setEvent(){
        getView(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

