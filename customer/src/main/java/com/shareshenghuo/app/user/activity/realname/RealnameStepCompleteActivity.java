package com.shareshenghuo.app.user.activity.realname;

import android.os.Bundle;
import android.view.View;

import com.shareshenghuo.app.user.BaseTopActivity;
import com.shareshenghuo.app.user.R;

/**
 * 实名认证，验证手机号界面
 * Created by Snow on 2017/7/19.
 */

public class RealnameStepCompleteActivity extends BaseTopActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_realname_step_complete);
        initTopBar("实名认证");
        initData();
        initView();
        setEvent();
    }

    private void initData(){
    }

    private void initView(){
    }

    private void setEvent(){
        //下一步
        getView(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

