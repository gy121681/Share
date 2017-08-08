package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.IntegralAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.IntegralBean;
import com.shareshenghuo.app.shop.network.request.IntegralRquest;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.response.IntegralResponse;
import com.shareshenghuo.app.shop.network.response.NumResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.Util;

public class IntegralFragment1 extends BaseFragment implements OnRefreshListener2<ListView>{

    private PullToRefreshListView lvData;
    private IntegralAdapter adapter;
    private TextView tv_num, tv_title, tv_title1, tv_num1;
    private int pageNo = 1;
    private int pageSize = 10;

    LinearLayout kejili_layou;//
    LinearLayout ll;// 当前积分布局
    RelativeLayout kejilijifen_layout, daijilijifen_layout;// 新消费积分,静态积分
    TextView daijili_text, kejili_text;// 待激励可激励
    TextView kjljf_t, djljf_t;// 待激励可激励

    private int integralType_num = 1;// 0可1待激励

    @Override
    protected int getLayoutId() {
        return R.layout.integral_activity;
    }

    @Override
    protected void init(View rootView) {
        initView();
        // loadData();
    }

    // private PullToRefreshListView lvData;
    // private IntegralAdapter adapter;
    // private TextView tv_num,tv_title,tv_title1,tv_num1;
    // private int pageNo = 1;
    // private int pageSize = 10;
    // private String integral,totalIntegral;
    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // setContentView(R.layout.integral_activity);
    //
    // integral = getIntent().getStringExtra("integral");
    // totalIntegral = getIntent().getStringExtra("totalIntegral");
    //
    // }

    public void initView() {
        // kejili_layou = getView(R.id.kejili_layou);
        // kejili_layou.setVisibility(View.GONE);//可激励静态积分按钮布局

        // initTopBar("公益积分", "产业链积分");

        kejilijifen_layout = getView(R.id.kejilijifen_layout);// 新消费积分
        daijilijifen_layout = getView(R.id.daijilijifen_layout);// 静态积分

        daijili_text = getView(R.id.daijili_text);// 静态积分
        kejili_text = getView(R.id.kejili_text);

        kjljf_t = getView(R.id.kjljf_t);
        djljf_t = getView(R.id.djljf_t);

        ll = getView(R.id.ll);

//		tv_title = getView(R.id.tv_title);
//		tv_title1 = getView(R.id.tv_title1);
//		tv_title.setText("当前积分  ");
//		tv_title1.setText("累计积分  ");
        tv_num = getView(R.id.tv_num);
        tv_num1 = getView(R.id.tv_num1);
        lvData = getView(R.id.lvShop);
        lvData.setMode(Mode.BOTH);
        lvData.setOnRefreshListener(this);

        daijilijifen_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//				daijilijifen_layout
//						.setBackgroundResource(R.drawable.bg_spinner_content);
//				kejilijifen_layout.setBackgroundResource(R.drawable.bai);
                kjljf_t.setTextColor(getResources().getColor(R.color.text_black));
                kejili_text.setTextColor(getResources().getColor(R.color.text_black));
                djljf_t.setTextColor(getResources().getColor(R.color.text_orange));
                daijili_text.setTextColor(getResources().getColor(R.color.text_orange));
                integralType_num = 1;
                pageNo = 1;
                pageSize = 10;
                loadData(integralType_num);
            }
        });// 待激励
        kejilijifen_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//				daijilijifen_layout.setBackgroundResource(R.drawable.bai);
//				kejilijifen_layout
//						.setBackgroundResource(R.drawable.bg_spinner_content);

                kjljf_t.setTextColor(getResources().getColor(R.color.text_orange));
                kejili_text.setTextColor(getResources().getColor(R.color.text_orange));
                djljf_t.setTextColor(getResources().getColor(R.color.text_black));
                daijili_text.setTextColor(getResources().getColor(R.color.text_black));
                integralType_num = 0;
                pageNo = 1;
                pageSize = 10;
                loadData(integralType_num);
            }
        });// 可激励

        kejilijifen_layout.performClick();// 触发一下可激励的查询

        getStatisticsData();
    }

    public void getStatisticsData() {

        // ProgressDialogUtil.showProgressDlg(getActivity(), "");
        NumRequest req = new NumRequest();
        try {
            req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id + "";

        } catch (Exception e) {
            // TODO: handle exception
        }
        req.userType = "2";

        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ProgressDialogUtil.dismissProgressDlg();
                        // T.showNetworkError(IntegralDivisionActivity.this);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp) {
                        ProgressDialogUtil.dismissProgressDlg();
                        NumResponse bean = new Gson().fromJson(resp.result,
                                NumResponse.class);
                        System.out.println("===秀儿积分全部的:" + resp.result);
                        if (Api.SUCCEED == bean.result_code) {
                            // tv_num.setText(Util.getnum(bean.data.integral +
                            // "",
                            // false));
                            // tv_num1.setText(Util.getnum(bean.data.totalIntegral
                            // + "", false));
                            // &&bean.data.userKeepIntegral!=null
                            if (bean.data.integral != null) {
                                // tv_num.setText(Util.getnum(
                                // bean.data.integralNew + "", false));
                                // tv_num1.setText(Util.getnum(
                                // bean.data.totalIntegralNew + "",
                                // false));//累计积分
                                String i_temp = "0";
                                try {
                                    i_temp = bean.data.userKeepIntegral
                                            .toString().trim();
                                } catch (Exception e) {
                                }

                                long k = Long.parseLong(bean.data.integral
                                        .toString().trim());
                                long i = Long.parseLong(i_temp);
                                long ki = k + i;
                                String a = Util.getnum(ki + "", false);
                                tv_num1.setText(a + "");// 累计积分

                                daijili_text.setText(Util.getnum(
                                        bean.data.userKeepIntegral + "", false));// 待激励
                                kejili_text.setText(Util.getnum(
                                        bean.data.integral + "", false));// 可激励

                            }

                            // lvData.setAdapter(new DataReportAdapter(activity,
                            // bean.data));

                        }
                    }
                });
    }

    public void loadData(int integralType_) {
        // List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
        // for (int i = 0; i < 10; i++) {
        // ExcitationBean data = new ExcitationBean();
        // data.amount = "交易金额: 200元";
        // data.time = "结算时间: 2016-8-15";
        // data.num = "+30";
        // bean.add(data);
        // }
        // updateView(bean);
        IntegralRquest req = new IntegralRquest();
        req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
        req.queryType = "0";
        req.userType = "2";
        req.operbType = "";
        req.opersType = "";
        req.startDate = "";
        req.endDate = "";
        req.pageNo = pageNo+"";
        req.pageSize = pageSize+"";
        req.integralType = integralType_ + "";// 0可1待

        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson()));
            System.out.println("====秀儿秀点:" + req.toJson());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new HttpUtils().send(HttpMethod.POST, Api.INTEGRALLIST, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        lvData.onRefreshComplete();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp) {
                        Log.e("", "" + resp.result);
                        System.out.println("=====秀儿积分,返回数据:" + resp.result);
                        lvData.onRefreshComplete();
                        IntegralResponse bean = new Gson().fromJson(
                                resp.result, IntegralResponse.class);
                        if (Api.SUCCEED == bean.result_code)
                            if (bean.data != null) {
                                updateView(bean.data);
                            }
                    }
                });
    }

    public void updateView(List<IntegralBean> data) {
        if (pageNo == 1 || adapter == null) {
            adapter = new IntegralAdapter(activity, data);
            lvData.setAdapter(adapter);
        }
        if (pageNo > 1) {
            adapter.getmData().addAll(data);
            adapter.notifyDataSetChanged();
        }
        pageNo++;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageNo = 1;
        loadData(integralType_num);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        loadData(integralType_num);
    }

}