package com.td.qianhai.epay.oem.activity.membermanager;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.share.app.entity.response.Constans;
import com.share.app.entity.response.UnActiveCodeResponse;
import com.share.app.network.CallbackList;
import com.share.app.network.Request;
import com.td.qianhai.epay.oem.BaseActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.QuickAdapter;
import com.td.qianhai.epay.oem.adapter.QuickAdapterHolder;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout;

import java.util.List;

/**
 * Created by Snow on 2017/7/25.
 */

public class UnActiveListActivity extends BaseActivity {

    private int start = 0;
    private int size = 20;
    private String mUserId;

    private ListView mListView;
    private CodeAdater mAdapter;
    private PullToRefreshLayout mPullToRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unactive_list);
        initData();
        initView();
        setEvent();
    }

    private void initData() {
        mUserId = MyCacheUtil.getshared(this).getString(Constans.Login.USERID, "");
        queryUnActiveCodeList(0);
        mAdapter = new CodeAdater();
    }

    /**
     * 查询未激活列表
     *
     * @param start  开始位置
     */
    private void queryUnActiveCodeList(final int start) {
        Request. getMemberQueryUnActivationdeList(mUserId,
                String.valueOf(start),
                String.valueOf(size),
                new CallbackList<List<UnActiveCodeResponse>>() {
                    @Override
                    public void onFailure(String msg) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }

                    @Override
                    public void onSuccess(List<UnActiveCodeResponse> data) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        if (start == 0) {
                            mAdapter.setDatas(data);
                        } else {
                            mAdapter.addDatas(data);
                        }
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);

                    }
                }
        );
    }

    private void initView(){
        mPullToRefreshLayout = getView(R.id.refresh_view);
        mListView = getView(R.id.listview);
        mListView.setAdapter(mAdapter);

        TextView tvTitle = getView(R.id.tvTopTitle);
        tvTitle.setText("未激活码");
    }

    private void setEvent(){
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                queryUnActiveCodeList(0);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                queryUnActiveCodeList(mAdapter.getCount());
            }
        });

        getView(R.id.llTopBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 复制
     */
    private void copy(String text) {
        ClipboardManager manager = (ClipboardManager) AppContext.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setText(text.trim());
    }

    private class CodeAdater extends QuickAdapter<UnActiveCodeResponse>{

        @Override
        protected void fillView(QuickAdapterHolder holder, int pos, final UnActiveCodeResponse data) {
            TextView tvCode = holder.findViewById(R.id.tv_code);
            tvCode.setText(data.getAct_id());
            holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    copy(data.getAct_id());
                    toast("复制成功");
                    return true;
                }
            });
        }

        @Override
        protected int getLayoutId(int type) {
            return R.layout.item_unactive_code;
        }
    }
}
