package com.td.qianhai.epay.oem.activity.lucre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.BaseActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.QuickAdapter;
import com.td.qianhai.epay.oem.adapter.QuickAdapterHolder;
import com.td.qianhai.epay.oem.views.PullToRefreshListView;

import java.util.Arrays;

/**
 * Created by Snow on 2017/7/17.
 */

public class ExchangeHistoryActivity extends BaseActivity {

    private PullToRefreshListView mListView;
    private HistoryAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_history_list_activity);
        initData();
        initView();
        setEvent();
    }

    private void initData(){
        mAdapter = new HistoryAdapter();
        mAdapter.setDatas(Arrays.asList("", "", "", "", "", "", "", ""));
    }

    private void initView(){
        initTitle();
        mListView = getView(R.id.listview);
        mListView.setAdapter(mAdapter);
    }

    private void initTitle(){
        TextView title = getView(R.id.tv_title_contre);
        title.setText("兑换记录");
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ExchangeHistoryDetailActivity.class));
            }
        });
    }

    private class HistoryAdapter extends QuickAdapter<String> {

        @Override
        protected void fillView(QuickAdapterHolder holder, int pos, String data) {

        }

        @Override
        protected int getLayoutId(int type) {
            return R.layout.exchange_history_list_item;
        }
    }

}
