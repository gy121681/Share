package com.td.qianhai.epay.oem.activity.business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.share.app.entity.response.AreaInfoBean;
import com.share.app.entity.response.ManagerInfoResponse;
import com.share.app.entity.response.ManagerShopNumBean;
import com.share.app.entity.response.ManagerShopResponse;
import com.share.app.network.CallbackObject;
import com.share.app.network.Request;
import com.share.app.utils.ParseUtil;
import com.share.app.utils.ProgressDialogUtil;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.QuickAdapter;
import com.td.qianhai.epay.oem.adapter.QuickAdapterHolder;

import java.util.Arrays;

/**
 * Created by Snow on 2017/7/17.
 */

public class AreaBusinessFragment extends Fragment {

    private TextView mTvShopCount;
    private ListView mListView;
    private BusinessAdapter mAdapter;

    private ManagerInfoResponse mManagerInfo;
    private String mAreaCode;

    public void setManagerInfo(ManagerInfoResponse managerInfo) {
        mManagerInfo = managerInfo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.business_item, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        setEvent();
    }

    private void initData(){
        mAdapter = new BusinessAdapter();
    }

    private void initView(){
        mListView = (ListView) getView().findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        mTvShopCount = (TextView) getView().findViewById(R.id.tv_num);
    }

    private void setEvent(){

    }

    /**
     * 获取区域商家信息
     * @param areas 区域
     */
    public void queryAreaBusiness(AreaInfoBean... areas){
        mAreaCode = getAreaCode(areas);
        requestShopNumber();
    }

    private void requestShopNumber() {
        ProgressDialogUtil.showProgressDlg(getActivity(), "");
        Request.getBusinessZoneFindRegionalManagerShopNum(mAreaCode, new CallbackObject<ManagerShopResponse>() {
            @Override
            public void onFailure(String msg) {
                ProgressDialogUtil.dismissProgressDlg();
            }

            @Override
            public void onSuccess(ManagerShopResponse data) {
                ProgressDialogUtil.dismissProgressDlg();
                showManagerShopInfo(data);
                Log.d("Response Data" , ParseUtil.toJson(data));
            }

            @Override
            public void onNetError(int code, String msg) {
                ProgressDialogUtil.dismissProgressDlg();

            }
        });
    }

    /**
     * 显示区域商家信息
     * @param data
     */
    private void showManagerShopInfo(ManagerShopResponse data) {
        if (data != null) {
            mTvShopCount.setText(data.shop_total_num);
            mAdapter.setDatas(data.listX);
        }
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

    private class BusinessAdapter extends QuickAdapter<ManagerShopNumBean> {

        @Override
        protected void fillView(QuickAdapterHolder holder, int pos, ManagerShopNumBean data) {
            TextView mTvName = holder.findViewById(R.id.tv_city);
            TextView mTvCount = holder.findViewById(R.id.tv_num);
            mTvName.setText(data.getAreaName());
            mTvCount.setText(data.getShopNum());
        }

        @Override
        protected int getLayoutId(int type) {
            return R.layout.business_list_item;
        }
    }

}
