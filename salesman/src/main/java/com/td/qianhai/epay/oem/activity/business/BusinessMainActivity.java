package com.td.qianhai.epay.oem.activity.business;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.share.app.entity.response.AreaInfoBean;
import com.share.app.entity.response.Constans;
import com.share.app.entity.response.ManagerInfoResponse;
import com.share.app.network.CallbackObject;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.QuickAdapter;
import com.td.qianhai.epay.oem.adapter.QuickAdapterHolder;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.views.CircleImageView;
import com.td.qianhai.epay.oem.views.MyTabView;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Snow on 2017/7/17.
 */

public class BusinessMainActivity extends FragmentActivity {

    private MyTabView tabView;
    private List<Map<String, Integer>> mTitle  = new ArrayList<>(2);
    private List<Fragment> mFragments = new ArrayList<>(2);

    private ScrollView mScrollView;

    private OneButtonDialogWarn warnDialog;

    private CircleImageView mCircleImageView;
    private TextView mTvName;
    private TextView mTvProvince, mTvCity, mTvArea;

    private ManagerInfoResponse mManagerInfo;

    private QuickAdapter.OnItemClickListener<AreaInfoBean> mAreaInfoBeanOnItemClickListener = null;

    private AreaAchievementFragment mAreaAchievementFragment;//区域总业绩界面
    private AreaBusinessFragment mAreaBusinessFragment;//区域商户数量界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_main_activity);
        initData();
        initView();
        setEvent();
    }

    private void initData(){
        requestManagerInfo();
        mAreaAchievementFragment = new AreaAchievementFragment();
        mAreaBusinessFragment = new AreaBusinessFragment();
    }

    /**
     * 请求区域经理信息
     */
    private void requestManagerInfo(){
        ProgressDialogUtil.showProgressDlg(this, "加载数据...");
        Request.getBusinessZoneGetRegionalManagerInfo(MyCacheUtil.getshared(this).getString(Constans.Login.USERID, ""),
                new CallbackObject<ManagerInfoResponse>() {
                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        showWarningDialog(msg);
                    }

                    @Override
                    public void onSuccess(ManagerInfoResponse data) {
                        mScrollView.setVisibility(View.VISIBLE);
                        mManagerInfo = data;
                        mAreaAchievementFragment.setManagerInfo(mManagerInfo);
                        mAreaBusinessFragment.setManagerInfo(mManagerInfo);
                        // 2017/7/25 显示区域经理信息
                        showManagerInfo(data);
                        // TODO: 2017/7/25 获取区域营业数据
                        List<AreaInfoBean> areas = mManagerInfo.getAreaInfo();
                        // 查询所有地区
                        AreaInfoBean[] areaInfoBeans = areas.toArray(new AreaInfoBean[areas.size()]);
                        mAreaAchievementFragment.queryManagerTradeInfo(areaInfoBeans);
                        mAreaBusinessFragment.queryAreaBusiness(areaInfoBeans);
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        showWarningDialog("请求服务器失败！！！");
                    }
                });
    }

    private void showWarningDialog(String msg) {
        warnDialog = new OneButtonDialogWarn(BusinessMainActivity.this,
                R.style.CustomDialog, "提示", msg , "确定",
                new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        warnDialog.dismiss();
                    }
                });
        warnDialog.show();
    }

    private void initView(){
        initTitle();
        initTabView();
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mScrollView.setVisibility(View.GONE);
        mCircleImageView = (CircleImageView) findViewById(R.id.img);
        mTvName = getView(R.id.tv_name);
        mTvProvince = getView(R.id.tv_province);
        mTvCity = getView(R.id.tv_city);
        mTvArea = getView(R.id.tv_area);
    }

    private void initTabView(){
        mTitle.add(getTitle("区域总业绩", null));
        mTitle.add(getTitle("区域商户数量", null));
        mFragments.add(mAreaAchievementFragment);
        mFragments.add(mAreaBusinessFragment);
        tabView = getView(R.id.tabFavorites);
        tabView.createView(mTitle, mFragments, getSupportFragmentManager());
    }

    private Map<String, Integer> getTitle(String title, Integer value) {
        Map<String, Integer> map = new HashMap<>();
        map.put(title, value);
        return map;
    }

    private void initTitle(){
        TextView tvTitle = getView(R.id.tv_title_contre);
        tvTitle.setText("业务专区");
    }

    private void setEvent(){
        getView(R.id.layout_btn_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mManagerInfo != null) {
                    showAreaSelectWindow();
                }
            }
        });
        getView(R.id.bt_title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //区域选框item点击事件
        mAreaInfoBeanOnItemClickListener = new QuickAdapter.OnItemClickListener<AreaInfoBean>() {
            @Override
            public void onItemClick(AreaInfoBean data) {
                ProgressDialogUtil.showProgressDlg(BusinessMainActivity.this, "数据加载中...");
                mAreaAchievementFragment.queryManagerTradeInfo(data);
                mAreaBusinessFragment.queryAreaBusiness(data);
            }
        };
    }

    /**
     * 显示区域经理信息
     * @param response
     */
    private void showManagerInfo(ManagerInfoResponse response) {
        if (response != null) {
            Glide.with(this).load(response.getPhoto()).error(R.drawable.share_s_public_head_small_big).into(mCircleImageView);
            mTvName.setText(response.getUser_name());
            mTvProvince.setText(response.getProvince_name());
            mTvCity.setText(response.getCity_name());
            mTvArea.setText("区域");
        }
    }

    /**
     * 显示地区选择框
     */
    private void showAreaSelectWindow() {
        PopupWindow window = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setContentView(initAreaSelectView(window));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAsDropDown(getView(R.id.view_divider));
    }

    /**
     * 创建地区选择界面
     * @param window
     * @return
     */
    private View initAreaSelectView(final PopupWindow window){
        View view = View.inflate(getApplicationContext(), R.layout.business_main_area_fragment, null);
        GridView mGridView = (GridView)view.findViewById(R.id.gridview);
        final AreaAdapter mAdapter = new AreaAdapter();
        mAdapter.setDatas(mManagerInfo.getAreaInfo());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAreaInfoBeanOnItemClickListener.onItemClick(mAdapter.getItem(position));
                window.dismiss();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        return view;
    }

    private <V extends View> V getView(@IdRes int id){
        return (V) findViewById(id);
    }

    private class AreaAdapter extends QuickAdapter<AreaInfoBean> {

        @Override
        protected void fillView(QuickAdapterHolder holder, int pos, final AreaInfoBean data) {
            TextView tvItem = holder.findViewById(R.id.btn_item);
            tvItem.setText(data.getArea_name());
        }

        @Override
        protected int getLayoutId(int type) {
            return R.layout.business_main_area_item;
        }
    }
}

