package com.td.qianhai.epay.oem.activity.business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.share.app.entity.response.AreaInfoBean;
import com.share.app.entity.response.ManagerInfoResponse;
import com.share.app.entity.response.ManagerTradeResponse;
import com.share.app.network.CallbackList;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.QuickAdapter;
import com.td.qianhai.epay.oem.adapter.QuickAdapterHolder;
import com.td.qianhai.epay.oem.mail.utils.NumUtil;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout;
import com.td.qianhai.epay.oem.views.PullToRefreshListView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Snow on 2017/7/17.
 */

public class AreaAchievementFragment extends Fragment {

    private ListView mListView;
    private AchievementAdapter mAdapter;

    private ManagerInfoResponse mManagerInfo;

    private String mAreaCode;

    private int mPageNo = 1;//分页第几页，从1开始
    private int mPageSize = 10;//

    private PullToRefreshLayout mRefreshLayout;

    public void setManagerInfo(ManagerInfoResponse managerInfo) {
        mManagerInfo = managerInfo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.turnover_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        setEvent();
    }

    private void initData(){
        mAdapter = new AchievementAdapter();
    }

    private void initView(){
        mListView = (ListView) getView().findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        mRefreshLayout = (PullToRefreshLayout) getView().findViewById(R.id.refresh_view);
    }

    private void setEvent(){
        mRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                resetPageNo();
                requestManagerTradeInfo();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                requestManagerTradeInfo();
            }
        });
    }

    private class AchievementAdapter extends QuickAdapter<ManagerTradeResponse> {

        @Override
        protected void fillView(QuickAdapterHolder holder, int pos, ManagerTradeResponse data) {
            TextView mTvDate = holder.findViewById(R.id.tv_date);
            TextView mTvTotalAmount = holder.findViewById(R.id.total_amount);
            TextView mTvAmount100p = holder.findViewById(R.id.trade_amount_type3);
            TextView mTvAmount50p = holder.findViewById(R.id.trade_amount_type2);
            TextView mTvAmount25p = holder.findViewById(R.id.trade_amount_type1);
            mTvDate.setText(NumUtil.getStrTime(data.getTradeDate()));
            mTvTotalAmount.setText("¥"+NumUtil.getfotmatnum(data.getTotalTradeAmount(), true, 1));
            mTvAmount100p.setText("¥"+NumUtil.getfotmatnum(data.getTradeAmountType3(), true, 1));
            mTvAmount50p.setText("¥"+NumUtil.getfotmatnum(data.getTradeAmountType2(), true, 1));
            mTvAmount25p.setText("¥"+NumUtil.getfotmatnum(data.getTradeAmountType1(), true, 1));
        }

        @Override
        protected int getLayoutId(int type) {
            return R.layout.turnover_item;
        }
    }


    /**
     * 获取区域营业数据
     * 默认获取所有区域数据，
     * @param areas 区域
     */
    public void queryManagerTradeInfo(AreaInfoBean... areas){
        mAreaCode = getAreaCode(areas);
        resetPageNo();
        requestManagerTradeInfo();
    }

    /**
     * 重置页号
     */
    private void resetPageNo(){
        mPageNo = 1;
    }

    private void requestManagerTradeInfo() {
        if (TextUtils.isEmpty(mAreaCode)) {
            return;
        }
        Request.getBusinessZoneFindRegionalManagerTrade("0", "", "", mManagerInfo.getProvince_code(), mManagerInfo.getCity_code(),
                mAreaCode, String.valueOf(mPageNo), String.valueOf(mPageSize), new CallbackList<List<ManagerTradeResponse>>() {
                    @Override
                    public void onFailure(String msg) {
                        mRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }

                    @Override
                    public void onSuccess(List<ManagerTradeResponse> data) {
                        ProgressDialogUtil.dismissProgressDlg();
                        mRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        if (mPageNo == 1) {
                            mAdapter.setDatas(data);
                        } else {
                            mAdapter.addDatas(data);
                        }
                        if (data != null && !data.isEmpty()) {
                            mPageNo++;
                        }
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        mRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);

                    }
                });
    }

    /**
     * 获取区域编码
     * @param areas
     * @return
     */
    private String getAreaCode(AreaInfoBean... areas) {
        if (areas == null || areas.length == 0) {
            return "";
        }
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0, count = areas.length; i < count; i++) {
            sBuilder.append(areas[i].getArea_code());
            if (i < count - 1) {
                sBuilder.append(",");
            }
        }
        return sBuilder.toString();
    }
}


