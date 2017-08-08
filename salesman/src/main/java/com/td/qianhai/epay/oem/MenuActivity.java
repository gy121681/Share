package com.td.qianhai.epay.oem;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.entity.response.Constans;
import com.td.qianhai.epay.oem.activity.business.BusinessMainActivity;
import com.td.qianhai.epay.oem.adapter.QuickAdapter;
import com.td.qianhai.epay.oem.adapter.QuickAdapterHolder;
import com.td.qianhai.epay.oem.adapter.VerticalAdapter;
import com.td.qianhai.epay.oem.adapter.VerticalViewPager;
import com.td.qianhai.epay.oem.advertising.AdGallery;
import com.td.qianhai.epay.oem.advertising.AdGallery.OnAdItemClickListener;
import com.td.qianhai.epay.oem.advertising.AdGalleryHelper;
import com.td.qianhai.epay.oem.advertising.Advertisement;
import com.td.qianhai.epay.oem.advertising.Image3DSwitchView;
import com.td.qianhai.epay.oem.advertising.Image3DSwitchView.OnImageSwitchListener;
import com.td.qianhai.epay.oem.advertising.Image3DView;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.LevelBean;
import com.td.qianhai.epay.oem.beans.RichTreasureBean;
import com.td.qianhai.epay.oem.mail.utils.DESKey;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.qrcode.CaptureActivity;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.DateUtil;
import com.td.qianhai.fragmentmanager.FmMainActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 主菜单页面
 *
 * @author liangge
 */
public class MenuActivity extends Fragment implements OnClickListener, OnAdItemClickListener {
    /**
     * 商户ID、商户认证状态、商户认证状态显示、用户手机、用户密码
     */
    private String custid, attStr, attValue, userPsw, psamId, isvip;
    private ArrayList<View> pageViews;
    private VerticalViewPager viewPager;
    private int getWidgetId;
    /**
     * page第一页
     */
    private View firstView;
    /**
     * page第二页
     */
    private View secondview;
    /**
     * 界面视图
     */
    private View view;


    private boolean isok = false;
    private List<LevelBean> level;

    /**
     * （钱包）致富宝实体
     */
    private RichTreasureBean treasureBean;
    /**
     * 用户手机号
     */
    private String phone;

    private int ImageIt = 0;

    /**
     * 分享
     */
    private UMSocialService mController;

    private OneButtonDialogWarn warnDialog;

    private Button btnBalanceQuery, btnBossReceive, btnDealRecord,
            btnRemittance, btnCreditCard, btnHelp, btnRealName,
            btnExpress, btnremittance, phone_recharge, credit_card_payments, btn_business,
            btn_plane_ticket;
    /**
     * 是否设置支付密码
     */
    private String lognum;

    private boolean flags = false;

    private AdGallery adGallery;
    // 网络图片
    private Advertisement[] data;
    // 测试图片
    private static final int[] dataids = {R.drawable.a_bg, R.drawable.a_bg1,
            R.drawable.a_bg4};

    private RelativeLayout adContainer, re_pro;
    private AdGalleryHelper adGalleryHelper;

    private LayoutInflater inflater;

    private Activity con;

    private Context context;

    private String sts;

    private boolean usertag = false;

    public static boolean phonetag = false;

    public static boolean bankcardtag = false;

    private String STS;

    private LinearLayout tv_ss, tv_dow, share_button;

    private Image3DSwitchView imageSwitchView;

    private RadioGroup mRadioGroup; // 滚动标记组件

    private String appid, userid, oemid;

    private boolean isrun = true;

    private Editor editor;

    private int clickCount = 0;

    private TextView tv_textadvertising, tv_dimiss;

    private List<String> aa = new ArrayList<String>();

    private int protag = 0;

    private ArrayList<HashMap<String, Object>> mlist;

    // private Intent intent;
    // private Bundle bundle;
    /**
     * 1、体验帐号
     */
    // private int identify;

    @SuppressLint("HandlerLeak")
    private Handler menHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 6:
                    Intent intent = new Intent();
                    if (attStr.equals("0")) {

                        switch (getWidgetId) {
                            case R.id.btn_credit_card_payments:


                                intent.setClass(con, HaiGouAvtivity.class);
                                intent.putExtra("title", "运营中心");
                                intent.putExtra("url", "http://javapay.qhno1.com/agtMessage/attract");
                                intent.putExtra("tag", "1");
                                startActivity(intent);


//						ToastCustom.showMessage(con.getApplicationContext(), "此功能即将开通！",
//								Toast.LENGTH_SHORT);
                                // ToastCustom.showMessage(con, "此功能即将上线！",
                                // Toast.LENGTH_SHORT);
//						intent.setClass(con, CreditToActivity.class);
//						startActivity(intent);
//						intent.setClass(con, ShouKuanBAvtivity.class);
//						intent.putExtra("tag", "0");
//						startActivity(intent);
                                return;
                            case R.id.btn_menu_balance_query:
                                intent.setClass(con, BalanceDetailsAcitvity1.class);
                                startActivity(intent);
                                return;

                            case R.id.btn_business:
//						intent.setClass(con, MoreMenuActivity.class);
//						intent.putExtra("tag", "1");
//						startActivity(intent);
                                intent.setClass(con, ScreeningActivity.class);
                                startActivity(intent);
                                return;
                            case R.id.btn_menu_boss_receive:

//						ShouKuanBAvtivity1
                                intent.setClass(con, MoreMenuActivity.class);
                                intent.putExtra("tag", "0");
                                startActivity(intent);
//						intent.setClass(con, OrderPayActivity.class);
//						startActivity(intent);
                                return;
                            case R.id.btn_menu_deal_records:
                                intent.setClass(con, CreditToActivity.class);
                                startActivity(intent);
//						intent.setClass(con,
//								RichTreasureDealRecordsActivity.class);
//						startActivity(intent);
                                return;
                            // case R.id.btn_menu_help:
                            // intent.setClass(con, HelpActivity.class);
                            // startActivity(intent);
                            // return;
                            case R.id.btn_menu_remittance:
                                intent.setClass(con, HaiGouAvtivity.class);
                                intent.putExtra("title", "一元夺宝");
                                intent.putExtra("url", HttpUrls.HG_URL);
                                intent.putExtra("tag", "0");
                                startActivity(intent);
//						intent.setClass(con, TransferAccountsActivity.class);
//						startActivity(intent);
                                return;
                            case R.id.btn_menu_mobile_recharge:

                                ToastCustom.showMessage(con, "此功能即将开通！",
                                        Toast.LENGTH_SHORT);
                                return;
                            case R.id.btn_menu_water_rate:
                                ToastCustom.showMessage(con, "此功能即将开通！",
                                        Toast.LENGTH_SHORT);
                                return;
                            case R.id.btn_menu_power_rate:
                                ToastCustom.showMessage(con, "此功能即将开通！",
                                        Toast.LENGTH_SHORT);
                                return;
                            case R.id.btn_menu_gas_rate:

                                intent.setClass(con, MoreMenuActivity.class);
                                intent.putExtra("tag", "2");
                                startActivity(intent);

//						if (sts.equals("3")) {
//							warnDialog = new OneButtonDialogWarn(con,
//									R.style.CustomDialog, "提示", "银行卡变更申请正在审核中",
//									"确定", new OnMyDialogClickListener() {
//										@Override
//										public void onClick(View v) {
//											warnDialog.dismiss();
//
//										}
//									});
//							warnDialog.show();
//						} else {
//
//							if (bankcardtag) {
//								ToastCustom.showMessage(con, "您的银行卡变更申请已审核成功");
//								bankcardtag = false;
//							}
//
//							intent.setClass(con, WithdrawalActivity.class);
//							startActivity(intent);
//						}

                                return;
                            case R.id.btn_menu_plane_ticket:
                                ToastCustom.showMessage(con, "此功能即将开通！",
                                        Toast.LENGTH_SHORT);
                                return;
                            case R.id.btn_menu_train_ticket:
                                ToastCustom.showMessage(con, "此功能即将开通！",
                                        Toast.LENGTH_SHORT);
                                return;
                            case R.id.btn_menu_lottery_ticket:
                                ToastCustom.showMessage(con, "此功能即将开通！",
                                        Toast.LENGTH_SHORT);
                                return;

                            case R.id.btn_express_ticket:
                                ToastCustom.showMessage(con, "此功能即将开通！",
                                        Toast.LENGTH_SHORT);
                                return;
                            case R.id.btn_plane_ticket:
                                intent.setClass(con, HaiGouAvtivity.class);
                                intent.putExtra("url", HttpUrls.MALL + inithtml1());
                                intent.putExtra("title", "商城");
                                intent.putExtra("tag", "1");
                                startActivity(intent);
//						ToastCustom.showMessage(con, "此功能即将开通！",
//								Toast.LENGTH_SHORT);

//						intent.setClass(con, HaiGouAvtivity.class);
//						intent.putExtra("url",HttpUrls.MALL+inithtml1());
//						intent.putExtra("tag","1");
//						intent.setClass(con, OnlineWeb.class);
//						intent.putExtra("urlStr",
//								HttpUrls.MALL+inithtml1());
//						intent.putExtra("titleStr","商城");
//						startActivity(intent);
                                return;
                            case R.id.tv_dow:
                                intent.setClass(con, MyQrcard.class);
                                startActivity(intent);
                                return;

                            case R.id.phone_recharges:
//						intent.setClass(con, PhonereChargeActivity.class);
//						startActivity(intent);
                                intent.setClass(con, ConvenienceServicesActivity.class);
                                startActivity(intent);
                                return;
                        }
                    } else {
                        intent.setClass(con, UserActivity.class);
                        startActivity(intent);
                        AppContext.getInstance().exit();
                        Toast.makeText(con.getApplicationContext(), "请重新登录",
                                Toast.LENGTH_SHORT).show();
                    }

                    // default:
                    // break;
            }
        }
    };

    private String inithtml1() {
        // TODO Auto-generated method stub


        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("PHONENUMBER", phone);
            jsonObj.put("PASSWORD", userPsw);
            jsonObj.put("OEMID", oemid);
            jsonObj.put("APPMARKNUMBER", HttpUrls.APPNUM);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String aa = null;
        try {
            aa = DESKey.AES_Encode(jsonObj.toString(),
                    "f15f1ede25a2471998ee06edba7d2e29");
            aa = URLEncoder.encode(aa);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return aa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_menu);
        con = getActivity();

        appid = ((AppContext) con.getApplication()).getAppid();
        userid = ((AppContext) con.getApplication()).getUserid();
//		custid = ((AppContext) con.getApplication()).getCustId();
        isvip = MyCacheUtil.getshared(con).getString("ISSENIORMEMBER", "");
        oemid = MyCacheUtil.getshared(con).getString("OEMID", "");
        userPsw = MyCacheUtil.getshared(con).getString("pwd", "");
        editor = MyCacheUtil.setshared(con);
        custid = MyCacheUtil.getshared(con).getString("Mobile", "");
        attStr = MyCacheUtil.getshared(con).getString("MERSTS", "");
//		phone = ((AppContext) con.getApplication()).getMobile();
        phone = MyCacheUtil.getshared(con).getString("Mobile", "");
        // PushManager.startWork(context, phone, appid);
        context = this.getActivity();
        inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        // inflater = con.getLayoutInflater();
        view = inflater.inflate(R.layout.activity_menu, null);
        loadMore();
        initView();
        playFlipAnimation2();


    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        sts = MyCacheUtil.getshared(con).getString("STS", "");
        if (isok) {
            GetMerStsTask1 stsTask = new GetMerStsTask1();
            stsTask.execute("199002", custid, userPsw, "11111111", "",
                    "2", "", "", "1", HttpUrls.APPNUM, "", "", "", "", "", "", "");
        }


    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        sts = MyCacheUtil.getshared(con).getString("STS", "");

    }

    private void initView() {
        pageViews = new ArrayList<View>();

        // LayoutInflater inflater = activity.getLayoutInflater();    activity_menu_first
        firstView = inflater.inflate(R.layout.activity_menu1, null);
        secondview = inflater.inflate(R.layout.activity_menu_second, null);

        tv_ss = (LinearLayout) view.findViewById(R.id.tv_ss);

        tv_ss.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent it1 = new Intent(getActivity(), CaptureActivity.class);
                startActivity(it1);
            }
        });

//		tv_dimiss = (TextView) view.findViewById(R.id.tv_dimiss);
        re_pro = (RelativeLayout) view.findViewById(R.id.re_pro);
//		tv_dimiss.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				re_pro.setVisibility(View.GONE);
//			}
//		});

        tv_dow = (LinearLayout) view.findViewById(R.id.tv_dow);
        tv_dow.setOnClickListener(this);
        btnBalanceQuery = (Button) firstView
                .findViewById(R.id.btn_menu_balance_query);
        btnBalanceQuery.setOnClickListener(this);
        btnBossReceive = (Button) firstView
                .findViewById(R.id.btn_menu_boss_receive);
        btnBossReceive.setOnClickListener(this);
        btnDealRecord = (Button) firstView
                .findViewById(R.id.btn_menu_deal_records);
        btn_business = (Button) firstView.findViewById(R.id.btn_business);
        btn_business.setOnClickListener(this);
        btnDealRecord.setOnClickListener(this);
        btnremittance = (Button) firstView
                .findViewById(R.id.btn_menu_remittance);
        btnremittance.setOnClickListener(this);
//		btn_menu_rates = (Button) firstView.findViewById(R.id.btn_menu_rates);
//		btn_menu_rates.setOnClickListener(this);
        phone_recharge = (Button) firstView.findViewById(R.id.phone_recharges);
        phone_recharge.setOnClickListener(this);
        tv_textadvertising = (TextView) view.findViewById(R.id.tv_textadvertising);
        // btnRealName = (Button)
        // firstView.findViewById(R.id.btn_menu_real_name);
        // btnRealName.setOnClickListener(this);
        btnRemittance = (Button) firstView.findViewById(R.id.btn_menu_gas_rate);
        btnRemittance.setOnClickListener(this);
        share_button = (LinearLayout) view.findViewById(R.id.share_buttons);

        credit_card_payments = (Button) firstView
                .findViewById(R.id.btn_credit_card_payments);
        credit_card_payments.setOnClickListener(this);

        btn_plane_ticket = (Button) firstView
                .findViewById(R.id.btn_plane_ticket);
        btn_plane_ticket.setOnClickListener(this);
        // btnExpress = (Button)
        // firstView.findViewById(R.id.btn_express_ticket);
        // btnExpress.setOnClickListener(this);
        // btnMenuAccount = (Button) firstView
        // .findViewById(R.id.btn_menu_account_manage);
        // btnMenuAccount.setOnClickListener(this);
        // btnHelp = (Button) firstView.findViewById(R.id.btn_menu_help);
        // btnHelp.setOnClickListener(this);
//		pageViews.add(firstView);
//		pageViews.add(secondview);
        // pageViews.add(secondview);

        pageViews.add(createThirdView());
        viewPager = (VerticalViewPager) view.findViewById(R.id.guidePages);
        VerticalAdapter vAdapter1 = new VerticalAdapter(pageViews);
        viewPager.setAdapter(vAdapter1);
//		viewPager.setAdapter(fragmentAdapter);

//		viewPager.setOnPageChangeListener(new GuidePageChangeListener());

        share_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                initshare();

            }

        });

        if (AppContext.isscreenstatus) {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Intent it = new Intent(con, IntroductionActivity.class);
                    startActivity(it);
                    AppContext.isscreenstatus = false;
                }
            }, 1000);

        }
    }

    private View inflateView(@LayoutRes int layoutId) {
        return inflater.inflate(layoutId, null);
    }

    private View createThirdView() {
        View view = inflateView(R.layout.activity_menu_third_grid);
        GridView mMenus = (GridView) view.findViewById(R.id.menus_grid_view);
        QuickAdapter<MenuData> menuAdapter = new QuickAdapter<MenuData>() {
            @Override
            protected void fillView(QuickAdapterHolder holder, int pos, final MenuData data) {
                holder.getItemView().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.mClickListener != null) {
                            data.mClickListener.onClick(v);
                        }
                    }
                });
                TextView title = holder.findViewById(R.id.tv_content);
                title.setText(data.title);
                title.setCompoundDrawablesWithIntrinsicBounds(0, data.drawRes, 0, 0);
                title.setEnabled(data.enable);
            }

            @Override
            protected int getLayoutId(int type) {
                return R.layout.activity_menu_third_grid_item;
            }
        };
        menuAdapter.setDatas(createMenus());
        mMenus.setAdapter(menuAdapter);
        return view;
    }

    private List<MenuData> createMenus() {
        List<MenuData> datas = new ArrayList<>();
        final String type = MyCacheUtil.getshared(getActivity()).getString(Constans.Login.TYPE, "");
        final String agentType = MyCacheUtil.getshared(getActivity()).getString(Constans.Login.AGENTTYPE, "");
        datas.add(new MenuDataBuilder()
                .setType(1)
                .setTitle("服务中心")
                .setDrawableRes(R.drawable.selector_homepage_icon_service)
                .setEnable(type.equals(Constans.Role.NORMAl) || type.equals(Constans.Role.AGENT))
                .setOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type.equals(Constans.Role.NORMAl) || type.equals(Constans.Role.AGENT)) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), OnlineWeb.class);
                            intent.putExtra("titleStr", "服务中心");
                            intent.putExtra("urlStr", HttpUrls.SHARE_WEB_SERVICES_CENTER);
                            startActivity(intent);
                        } else {
                            showWarningDialog("只有代理商和业务员才能进入", null);
                        }
                    }
                })
                .build());
        datas.add(new MenuDataBuilder()
                .setType(2)
                .setTitle("会员管理")
                .setDrawableRes(R.drawable.selector_homepage_icon_member)
                .setEnable(type.equals(Constans.Role.AGENT) && agentType.equals(Constans.AgentType.TYPE_AREA))
                .setOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        if (type.equals(Constans.Role.AGENT) && agentType.equals(Constans.AgentType.TYPE_AREA)) {
                            intent.setClass(con, MoreMenuActivity.class);
                            intent.putExtra("tag", "2");
                            startActivity(intent);
                        } else {
                            showWarningDialog("只有区级代理商才能进入", null);
                        }
                    }
                })
                .build());
        datas.add(new MenuDataBuilder()
                .setType(3)
                .setTitle("业务专区")
                .setDrawableRes(R.drawable.selector_homepage_icon_business)
                .setEnable(type.equals(Constans.Role.AREA_MANAGER))
                .setOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        if (type.equals(Constans.Role.AREA_MANAGER)) {
                            intent.setClass(getActivity(), BusinessMainActivity.class);
                            startActivity(intent);
                        } else {
                            showWarningDialog("只有区域经理才能进入", null);
                        }
                    }
                })
                .build());
        datas.add(new MenuDataBuilder()
                .setType(4)
                .setTitle("发展商户")
                .setDrawableRes(R.drawable.selector_homepage_icon_develop_seller)
                .setEnable(type.equals(Constans.Role.NORMAl))
                .setOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type.equals(Constans.Role.NORMAl)) {
                            Intent it = new Intent(getActivity(), AddShopActivity.class);
                            it.putExtra("handle_id", MyCacheUtil.getshared(getActivity()).getString(Constans.Login.USERID, ""));
                            it.putExtra("tag", "0");
                            startActivity(it);
                        } else {
                            showWarningDialog("只有业务员才能进入", null);
                        }
                    }
                })
                .build());
        datas.add(new MenuDataBuilder()
                .setType(5)
                .setTitle("我的商户")
                .setDrawableRes(R.drawable.selector_homepage_icon_my_seller)
                .setEnable(type.equals(Constans.Role.NORMAl))
                .setOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type.equals(Constans.Role.NORMAl)) {
                            Intent intent = new Intent();
                            intent.setClass(con, MoreMenuActivity.class);
                            intent.putExtra("tag", "1");
                            startActivity(intent);
                        } else {
                            showWarningDialog("只有业务员才能进入", null);
                        }
                    }
                })
                .build());
        datas.add(new MenuDataBuilder()
                .setType(6)
                .setTitle("我的收益")
                .setDrawableRes(R.drawable.selector_homepage_icon_profit)
                .setEnable(type.equals(Constans.Role.NORMAl) || type.equals(Constans.Role.AGENT))
                .setOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type.equals(Constans.Role.NORMAl) || type.equals(Constans.Role.AGENT)) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), OnlineWeb.class);
                            intent.putExtra("titleStr", "我的收益");
                            intent.putExtra("urlStr", HttpUrls.SHARE_WEB_LUCRE + MyCacheUtil.getshared(getActivity()).getString(Constans.Login.USERID, ""));
                            startActivity(intent);
                        } else {
                            showWarningDialog("只有代理商和业务员才能进入", null);
                        }
                    }
                })
                .build());
        return datas;
    }

    // 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            // TODO Auto-generated method stub
            ((ViewPager) arg0).addView(pageViews.get(arg1));
            return pageViews.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }
    }

    // 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
        }
    }

    @Override
    public void onClick(View v) {

        if (DateUtil.isFastDoubleClick()) {
            return;
        } else {
            Intent intent = new Intent();
            getWidgetId = v.getId();
            if (custid == null || attStr == null) {
                intent.setClass(con, UserActivity.class);
                startActivity(intent);
                AppContext.getInstance().exit();
                ToastCustom.showMessage(con, "请重新登录");
                return;

            } else if (custid != null && attStr.equals("0") && sts.equals("0")) {
                if (v.getId() == R.id.btn_plane_ticket) {

                    intent.setClass(con, HaiGouAvtivity.class);
                    intent.putExtra("url", HttpUrls.MALL + inithtml1());
                    intent.putExtra("title", "商城");
                    intent.putExtra("tag", "1");

//					intent.setClass(con, OnlineWeb.class);
//					intent.putExtra("urlStr",
//							HttpUrls.MALL+inithtml1());
//					intent.putExtra("titleStr","商城");
                    startActivity(intent);
                    return;
                }

                if (isvip.equals("0")) {
                    warnDialog = new OneButtonDialogWarn(con,
                            R.style.CustomDialog, "提示",
                            "尊敬的用户,请先升级会员再进行操作", "确定",
                            new OnMyDialogClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(con, PremiumUpgradeActivity.class);
                                    startActivity(it);
                                    warnDialog.dismiss();
                                }
                            });
                    warnDialog.show();
                    return;

                }
                menHandler.sendEmptyMessage(6);
            } else {
                if (v.getId() == R.id.btn_menu_balance_query) {

                    intent.setClass(con, BalanceDetailsAcitvity1.class);
                    startActivity(intent);
                    return;
                }
                if (v.getId() == R.id.btn_menu_gas_rate) {
                    if (isvip.equals("0")) {
                        warnDialog = new OneButtonDialogWarn(con,
                                R.style.CustomDialog, "提示",
                                "尊敬的用户,请先升级会员再进行操作", "确定",
                                new OnMyDialogClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent it = new Intent(con, PremiumUpgradeActivity.class);
                                        startActivity(it);
                                        warnDialog.dismiss();
                                    }
                                });
                        warnDialog.show();
                        return;
                    }
                }

//				if (v.getId() == R.id.btn_menu_boss_receive) {
//					if (MyCacheUtil.getshared(con).getString("Txnsts", "").equals("0")&& sts.equals("0")) {
//						if(isvip.equals("0")){
//							warnDialog = new OneButtonDialogWarn(con,
//									R.style.CustomDialog, "提示",
//									"尊敬的用户,请先升级会员再进行操作", "确定",
//									new OnMyDialogClickListener() {
//										@Override
//										public void onClick(View v) {
//											Intent it = new Intent(con,DistributorActivity.class);
//											startActivity(it);
//											warnDialog.dismiss();
//										}
//									});
//							warnDialog.show();
//							return;
//							
//						}
////						intent.setClass(con, OrderPayActivity.class);
////						startActivity(intent);
//						return;
//
//					} else if (MyCacheUtil.getshared(con).getString("Txnsts", "").equals("1") && !sts.equals("0")) {
//						if(isvip.equals("0")){
//							warnDialog = new OneButtonDialogWarn(con,
//									R.style.CustomDialog, "提示",
//									"尊敬的用户,请先升级会员再进行操作", "确定",
//									new OnMyDialogClickListener() {
//										@Override
//										public void onClick(View v) {
//											Intent it = new Intent(con,DistributorActivity.class);
//											startActivity(it);
//											warnDialog.dismiss();
//										}
//									});
//							warnDialog.show();
//							return;
//						}
////						intent.setClass(con, OrderPayActivity.class);
////						startActivity(intent);
//						return;
//					}
//				}else 
                if (v.getId() == R.id.btn_plane_ticket) {

                    intent.setClass(con, HaiGouAvtivity.class);
                    intent.putExtra("url", HttpUrls.MALL + inithtml1());
                    intent.putExtra("title", "商城");
                    intent.putExtra("tag", "1");
                    startActivity(intent);
//					intent.setClass(con, OnlineWeb.class);
//					intent.putExtra("urlStr",
//							HttpUrls.MALL+inithtml1());
//					intent.putExtra("titleStr","商城");
//					startActivity(intent);
                    return;
                }


//					if(v.getId() ==R.id.btn_business){
//						intent.setClass(con, MoreMenuActivity.class);
//						intent.putExtra("tag", "1");
//						startActivity(intent);
//					return;
//				}
//					
//					if(v.getId() ==R.id.btn_menu_gas_rate){
//						intent.setClass(con, MoreMenuActivity.class);
//						intent.putExtra("tag", "2");
//						startActivity(intent);
//					return;
//				}
//					if(v.getId() ==R.id.btn_menu_boss_receive){
//						intent.setClass(con, MoreMenuActivity.class);
//						intent.putExtra("tag", "0");
//						startActivity(intent);
//					return;
//				}


//				else if(v.getId() ==R.id.btn_menu_balance_query){
//					intent.setClass(con, BalanceDetailsAcitvity1.class);
//					startActivity(intent);
//					return;
//				}

//				else if (v.getId() == R.id.btn_menu_balance_query) {
//					intent.setClass(con, BalanceDetailsAcitvity.class);
//					startActivity(intent);
//					return;
//				} else if (v.getId() == R.id.btn_menu_rates) {
//					intent.setClass(con, RateMianActivity.class);
//					startActivity(intent);
//					return;
//				}
                // else if (v.getId() == R.id.btn_menu_help) {
                // intent.setClass(con, HelpActivity.class);
                // startActivity(intent);
                // }
                // else if(v.getId() == R.id.btn_menu_real_name){
                // intent.setClass(activity,
                // RealNameAuthenticationActivity.class);
                // startActivity(intent);
                // }
                // else{
                if (isrun) {
                    psamId = ((AppContext) con.getApplication()).getPsamId();
                    GetMerStsTask stsTask = new GetMerStsTask();
                    stsTask.execute("199002", custid, userPsw, "11111111", "",
                            "2", "", "", "1", HttpUrls.APPNUM, "", "", "");
                    isrun = false;
                }

                // }
            }
        }
    }


    class GetMerStsTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3],
                    params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12]};
            return NetCommunicate.get(HttpUrls.LOGIN, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            if (result != null) {
                isrun = true;
                if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)
                        && result.get("STS").toString().equals("0")) {

                    if (result.get("ISSENIORMEMBER") != null) {
                        isvip = result.get("ISSENIORMEMBER").toString();
                    }

                    attStr = result.get("MERSTS").toString();

                    ((AppContext) con.getApplication()).setMerSts(attStr);
                    editor.putString("Txnsts", result.get("TXNSTS").toString());
                    editor.putString("MERSTS", attStr);
                    editor.commit();
                    ((AppContext) con.getApplication()).setTxnsts(result.get("TXNSTS").toString());
                    if (result.get("LOGNUM") != null) {
                        lognum = result.get("LOGNUM").toString();
                    }
                    sts = result.get("STS").toString();
                    // ((AppContext)
                    // con.getApplicationContext()).setSts(result.get("STS").toString());
                    Log.e("", "sta" + result.get("STS").toString());
                    if (phonetag) {
                        // ToastCustom.showMessage(con, "手机号变更申请已审核成功,请重新登录");
                        // FmMainActivity.context.finish();
                        // Intent it = new Intent(con,UserActivity.class);
                        // startActivity(it);
                        // ((AppContext)
                        // con.getApplication()).setSts(result.get("STS").toString());
                        // phonetag = false;
                    }
                    // else if(bankcardtag){
                    //
                    // ToastCustom.showMessage(con, "您的银行卡信息修改已审核成功");
                    // ((AppContext)
                    // con.getApplication()).setSts(result.get("STS").toString());
                    // bankcardtag = false;
                    // }

                    ((AppContext) con.getApplication()).setSts(result
                            .get("STS").toString());
                    editor.putString("STS", result.get("STS").toString());
                    editor.commit();

                    if (lognum != null && lognum.equals("0")) {
                        warnDialog = new OneButtonDialogWarn(con,
                                R.style.CustomDialog, "提示",
                                "您的资料审核已通过,请重新登录并设置支付密码", "确定",
                                new OnMyDialogClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FmMainActivity.context.finish();
                                        Intent it = new Intent(con,
                                                UserActivity.class);
                                        con.finish();
                                        startActivity(it);
                                        warnDialog.dismiss();
                                    }
                                });
                        warnDialog.setCancelable(false);
                        warnDialog.setCanceledOnTouchOutside(false);
                        warnDialog.show();

                    } else {
                        if (isvip.equals("0")) {
                            warnDialog = new OneButtonDialogWarn(con,
                                    R.style.CustomDialog, "提示",
                                    "尊敬的用户,请先升级会员再进行操作", "确定",
                                    new OnMyDialogClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent it = new Intent(con, PremiumUpgradeActivity.class);
                                            startActivity(it);
                                            warnDialog.dismiss();
                                        }
                                    });
                            warnDialog.show();
                            return;

                        }
                        menHandler.sendEmptyMessage(6);
                    }
                } else {

                    if (result.get("IDCARDPICSTA") != null && result.get("CUSTPICSTA") != null && result.get("FRYHKIMGPATHSTA") != null) {
                        String a = result.get("IDCARDPICSTA").toString();
                        String b = result.get("CUSTPICSTA").toString();
                        String c = result.get("FRYHKIMGPATHSTA").toString();
                        ((AppContext) con.getApplication()).setStateaudit(a + b + c);
                    }


                    if (result.get(Entity.RSPCOD).toString().equals("000002")) {
                        warnDialog = new OneButtonDialogWarn(con,
                                R.style.CustomDialog, "提示", "手机号变更申请成功,请重新登录",
                                "确定", new OnMyDialogClickListener() {
                            @Override
                            public void onClick(View v) {
                                FmMainActivity.context.finish();
                                Intent it = new Intent(con,
                                        UserActivity.class);
                                startActivity(it);
                                warnDialog.dismiss();
                            }
                        });
                        warnDialog.setCancelable(false);
                        warnDialog.setCanceledOnTouchOutside(false);
                        warnDialog.show();

                    } else {
                        // ToastCustom.showMessage(con,
                        // result.get(Entity.RSPMSG).toString());
                    }

                    if (result.get("STS") != null) {
                        editor.putString("STS", result.get("STS").toString());
                        editor.commit();
                        if (result.get("STS").equals("1")) {
                            usertag = true;
                            warnDialog = new OneButtonDialogWarn(con,
                                    R.style.CustomDialog, "提示", "用户信息正在审核中",
                                    "确定", new OnMyDialogClickListener() {
                                @Override
                                public void onClick(View v) {
                                    warnDialog.dismiss();
                                }
                            });
                            warnDialog.show();

                        } else if (result.get("STS").equals("-1")) {
                            warnDialog = new OneButtonDialogWarn(con,
                                    R.style.CustomDialog, "提示", result.get(
                                    Entity.RSPMSG).toString(), "确定",
                                    new OnMyDialogClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            warnDialog.dismiss();
                                            Intent it = new Intent(
                                                    con,
                                                    AuthenticationActivity.class);
                                            it.putExtra("intentObj",
                                                    "MenuActivity");
                                            startActivity(it);
                                        }
                                    });
                            warnDialog.show();

                        } else if (result.get("STS").equals("3")) {
                            bankcardtag = true;
                            menHandler.sendEmptyMessage(6);

                        } else if (result.get("STS").equals("-2")) {

                            warnDialog = new OneButtonDialogWarn(con,
                                    R.style.CustomDialog, "提示", result.get(
                                    Entity.RSPMSG).toString(), "确定",
                                    new OnMyDialogClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            warnDialog.dismiss();
                                            Intent it = new Intent(
                                                    con,
                                                    RequestCardInfoChangeActivity.class);
                                            startActivity(it);
                                        }
                                    });
                            warnDialog.show();

                        } else if (result.get("STS").equals("4")) {
                            phonetag = true;
                            warnDialog = new OneButtonDialogWarn(con,
                                    R.style.CustomDialog, "提示", "手机变更申请正在审核中",
                                    "确定", new OnMyDialogClickListener() {
                                @Override
                                public void onClick(View v) {
                                    warnDialog.dismiss();
                                }
                            });
                            warnDialog.show();
                        } else {
                            ((AppContext) con.getApplication()).setTxnsts(result.get("TXNSTS").toString());
                            editor.putString("Txnsts", result.get("TXNSTS").toString());
                            editor.commit();
//							((AppContext)con.getApplication()).setTxnsts(result.get("STS").toString());

                            if (isvip.equals("0")) {
                                warnDialog = new OneButtonDialogWarn(con,
                                        R.style.CustomDialog, "提示",
                                        "尊敬的用户,请先升级会员再进行操作", "确定",
                                        new OnMyDialogClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent it = new Intent(con, PremiumUpgradeActivity.class);
                                                startActivity(it);
                                                warnDialog.dismiss();
                                            }
                                        });
                                warnDialog.show();
                                return;

                            }
//							if(result.get("TXNSTS").toString().equals("0")){


                            warnDialog = new OneButtonDialogWarn(con,
                                    R.style.CustomDialog, "提示",
                                    "为了您账户安全！请补全资料进行实名认证再进行操作", "确定",
                                    new OnMyDialogClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent it = new Intent(con, AuthenticationActivity.class);
                                            startActivity(it);
                                            warnDialog.dismiss();
                                        }
                                    });
                            warnDialog.show();

//							}else {
//								
//								if(isvip.equals("0")){
//									warnDialog = new OneButtonDialogWarn(con,
//											R.style.CustomDialog, "提示",
//											"尊敬的用户,请先升级普通会员再进行操作", "确定",
//											new OnMyDialogClickListener() {
//												@Override
//												public void onClick(View v) {
//													Intent it = new Intent(con,DistributorActivity.class);
//													startActivity(it);
//													warnDialog.dismiss();
//												}
//											});
//									warnDialog.show();
//									return;
//									
//								}
//								
//									warnDialog = new OneButtonDialogWarn(con,
//											R.style.CustomDialog, "提示",
//											"尊敬的用户,请先收款再操作", "确定",
//											new OnMyDialogClickListener() {
//												@Override
//												public void onClick(View v) {
//													Intent it = new Intent(
//															con,
//															OrderPayActivity.class);
//													startActivity(it);
//													warnDialog.dismiss();
//												}
//											});
//									warnDialog.show();
//							}
                        }
                    }
                }
            } else {
                ToastCustom.showMessage(con, "网络状况不佳");
            }
            super.onPostExecute(result);
        }
    }


    private void realizeFunc2() {
        mRadioGroup = (RadioGroup) view
                .findViewById(R.id.home_pop_gallery_mark1);
        imageSwitchView = (Image3DSwitchView) view
                .findViewById(R.id.image_switch_view);
        imageSwitchView.setCurrentImage(0);
        imageSwitchView.settime(5000);

        for (int i = 0; i < level.size(); i++) {
            // RelativeLayout.LayoutParams l = new
            // RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
            // LayoutParams.WRAP_CONTENT);
            // l.setMargins(5, 0, 5, 0);

            Image3DView view = new Image3DView(con, null);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));


//			FinalBitmap.create(con).display(view,
//					HttpUrls.HOST_POSM + level.get(i).getUrl(),
//					view.getWidth(),
//					view.getHeight(), null, null);

            try {
                new GetImageUtil(con, view, HttpUrls.HOST_POSM + level.get(i).getUrl(), "bm");
            } catch (Exception e) {
                // TODO: handle exception
            }


//			FinalBitmap.create(con).display(view, HttpUrls.HOST_POSM+level.get(i).getUrl());
//			FinalBitmap.create(con).display(view,
//					HttpUrls.HOST_POSM+level.get(i).getUrl());
//			Log.e("", ""+HttpUrls.HOST_POSM+level.get(i).getUrl());
//			view.setBackgroundResource1(dataids[i]);
            view.setScaleType(null);

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    String url = level.get(ImageIt).getIntent();

                    if (url == null || url.equals("null") || url.equals("")) {
                        return;
                    }
                    try {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url1 = Uri.parse(url);
                        intent.setData(content_url1);
                        startActivity(intent);
                    } catch (Exception e) {
                        // TODO: handle exception
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url1 = Uri.parse("http://" + url);
                        intent.setData(content_url1);
                        startActivity(intent);
                    }
                }
            });
            imageSwitchView.addView(view);

            RadioButton _rb = new RadioButton(con);
            _rb.setLayoutParams(new LayoutParams(20, LayoutParams.WRAP_CONTENT));
            _rb.setId(0x1234 + i);
            _rb.setButtonDrawable(con.getResources().getDrawable(
                    R.drawable.gallery_selector));
            // _rb.setLayoutParams(l);
            _rb.setPadding(4, 0, 4, 0);
            mRadioGroup.addView(_rb);
        }

        imageSwitchView.setOnImageSwitchListener(new OnImageSwitchListener() {
            @Override
            public void onImageSwitch(int currentImage) {
                mRadioGroup.check(mRadioGroup.getChildAt(currentImage).getId());
                ImageIt = currentImage;
            }
        });
    }


    private void realizeFunc1() {
        mRadioGroup = (RadioGroup) view
                .findViewById(R.id.home_pop_gallery_mark1);
        imageSwitchView = (Image3DSwitchView) view
                .findViewById(R.id.image_switch_view);
        imageSwitchView.setCurrentImage(0);
        imageSwitchView.settime(5000);

        for (int i = 0; i < dataids.length; i++) {
            // RelativeLayout.LayoutParams l = new
            // RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
            // LayoutParams.WRAP_CONTENT);
            // l.setMargins(5, 0, 5, 0);

            Image3DView view = new Image3DView(con, null);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            view.setBackgroundResource(dataids[i]);
            view.setScaleType(null);
            imageSwitchView.addView(view);

            RadioButton _rb = new RadioButton(con);
            _rb.setLayoutParams(new LayoutParams(20, LayoutParams.WRAP_CONTENT));
            _rb.setId(0x1234 + i);
            _rb.setButtonDrawable(con.getResources().getDrawable(
                    R.drawable.gallery_selector));
            // _rb.setLayoutParams(l);
            _rb.setPadding(4, 0, 4, 0);
            mRadioGroup.addView(_rb);
        }

        imageSwitchView.setOnImageSwitchListener(new OnImageSwitchListener() {
            @Override
            public void onImageSwitch(int currentImage) {
                mRadioGroup.check(mRadioGroup.getChildAt(currentImage).getId());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageSwitchView != null) {
            imageSwitchView.clear();
        }

    }

    private void initshare() {
        // TODO Auto-generated method stub
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        mController.getConfig().setPlatforms(
//				SHARE_MEDIA.RENREN,
//				SHARE_MEDIA.TENCENT, 
                SHARE_MEDIA.QQ,
//				SHARE_MEDIA.QZONE,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//				SHARE_MEDIA.SINA, 
                SHARE_MEDIA.SMS);

        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        mController
                .setShareContent("打造全方位的共赢平台" + HttpUrls.SHEARURL + inithtml());
        String appID = "wx67b3a23ca6de80f8";
        String appSecret = "1a35ae5ac3d57848c743a5655d11cb4b";

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(getActivity(), appID, appSecret);
        // WeiXinShareContent weixinContent = new WeiXinShareContent();
        // weixinContent.setTargetUrl("http://180.166.124.95:8092/posm/upload/QH_W_V1.2.apk");
        wxHandler.addToSocialSDK();
        UMImage aa = new UMImage(getActivity(), R.drawable.ico);
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appID,
                appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();


        String setphone = phone.substring(0, 3);
        String getphone = phone.substring(phone.length() - 4);
        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("打造全方位的共赢平台      ");
        circleMedia.setTitle(HttpUrls.APPNAME);
        circleMedia.setShareImage(aa);
        circleMedia.setTargetUrl(HttpUrls.SHEARURL + inithtml());
        mController.setShareMedia(circleMedia);

        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();


        WeiXinShareContent weixinContent = new WeiXinShareContent();
        // 设置分享文字
        weixinContent.setShareContent("打造全方位的共赢平台");
        // 设置title
        weixinContent.setTitle(HttpUrls.APPNAME);
        // 设置分享内容跳转URL
        weixinContent
                .setTargetUrl(HttpUrls.SHEARURL + inithtml());
        // 设置分享图片m
        weixinContent.setShareImage(aa);

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
                "1104744982", "MCXWLlnpU2S4ULld");
        qqSsoHandler.addToSocialSDK();

        // 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
                "1104744982", "MCXWLlnpU2S4ULld");
        qZoneSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        // 设置分享文字
        qqShareContent.setShareContent("打造全方位的共赢平台");
        // 设置分享title
        qqShareContent.setTitle(HttpUrls.APPNAME);
        // 设置分享图片
        qqShareContent.setShareImage(aa);
        // 设置点击分享内容的跳转链接
        qqShareContent
                .setTargetUrl(HttpUrls.SHEARURL + inithtml());

        QZoneShareContent qzone = new QZoneShareContent();
        // 设置分享文字
        qzone.setShareContent("打造全方位的共赢平台");
        // 设置点击消息的跳转URL
        qzone.setTargetUrl(HttpUrls.SHEARURL + inithtml());
        // 设置分享内容的标题
        qzone.setTitle(HttpUrls.APPNAME);
        // 设置分享图片
        qzone.setShareImage(aa);

        mController.setShareMedia(qzone);

        mController.setShareMedia(qqShareContent);

        mController.setShareMedia(weixinContent);

        mController.openShare(getActivity(), false);
    }

    private String inithtml() {
        // TODO Auto-generated method stub

//		JSONObject jsonObj = new JSONObject();
//		try {
//			jsonObj.put("PHONENUMBER", phone);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        String aa = null;
        try {
            aa = DESKey.AES_Encode(phone,
                    "f15f1ede25a2471998ee06edba7d2e29");
//			aa = URLEncoder.encode(aa);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return aa;
    }


    private void loadMore() {
        mlist = new ArrayList<HashMap<String, Object>>();
        new Thread(run).start();
    }

    Runnable run = new Runnable() {

        @Override
        public void run() {

            String[] values = {HttpUrls.BANNER + "", phone, oemid,};
            ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.BANNER,
                    values, HttpKeys.BANNER_BACK);

            Message msg = new Message();

            if (list != null) {
                mlist.addAll(list);
                if (list.size() <= 0 || list == null) {
                    msg.what = 2;
                } else {
                    msg.what = 1;
                }
            } else {

                msg.what = 3;
            }
            handler.sendMessage(msg);
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            isok = true;
            switch (msg.what) {
                case 1:
                    level = new ArrayList<LevelBean>();
                    for (int i = 0; i < mlist.size(); i++) {
                        LevelBean l = new LevelBean();
                        if (mlist.get(i).get("IMG_URL") != null) {

                            l.setUrl(mlist.get(i).get("IMG_URL").toString());
                        }
                        if (mlist.get(i).get("FORWARD_URL") != null) {

                            l.setIntent(mlist.get(i).get("FORWARD_URL").toString());
                        }
                        if (mlist.get(i).get("CONTENT") != null) {
                            l.setIsshowin(true);

                        } else {
                            l.setIsshowin(false);
                        }
                        if (mlist.get(i).get("AGTMERCNUM") != null) {
                            l.setAgtmercnum(mlist.get(i).get("AGTMERCNUM").toString());
                        }
                        if (mlist.get(i).get("CONTENT") != null) {
                            l.setContent(mlist.get(i).get("CONTENT").toString());
                        }
                        if (mlist.get(i).get("NICKNAME") != null) {
                            l.setNickname(mlist.get(i).get("NICKNAME").toString());
                        }
                        if (mlist.get(i).get("ADDRESS") != null) {
                            l.setAddress(mlist.get(i).get("ADDRESS").toString());
                        }
                        if (mlist.get(i).get("AGTPHONE") != null) {
                            l.setAgtphone(mlist.get(i).get("AGTPHONE").toString());
                        }
                        if (mlist.get(i).get("FONTCOLOR") != null) {
                            l.setFontcolor(mlist.get(i).get("FONTCOLOR").toString());
                        }
                        if (mlist.get(i).get("LINKURL") != null) {
                            l.setUrl(mlist.get(i).get("LINKURL").toString());
                        }

                        level.add(l);
                    }
                    if (level.get(0).isIsshowin()) {
                        realizeFunc();

                    } else {
                        realizeFunc();
//					realizeFunc2();
                    }

                    break;
                case 2:
                    realizeFunc1();
//				if (moreView != null) {
//				}
//				 Toast.makeText(getApplicationContext(),"加载完毕",
//				 Toast.LENGTH_SHORT).show();
                    break;
                case 3:
//				Toast.makeText(getApplicationContext(), "网络不给力,请检查网络设置",
//						Toast.LENGTH_SHORT).show();
//				listView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    class GetMerStsTask1 extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3],
                    params[4], params[5], params[6], params[7], params[8],
                    params[9], params[10], params[11], params[12], params[13],
                    params[14], params[15], params[16]};
            return NetCommunicate.get(HttpUrls.LOGIN, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            if (result != null) {
                isrun = true;
                if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
                    if (result.get("NOTICEMESSAGE") != null) {
                        editor.putString("NOTICEMESSAGE",
                                result.get("NOTICEMESSAGE").toString());
                        if (result.get("NOTICEMESSAGE").toString().length() > 0) {
                            tv_textadvertising.setText(result.get(
                                    "NOTICEMESSAGE").toString());
                        } else {
                            tv_textadvertising.setText("暂无通知");
                        }
                    } else {

                    }

                } else {
                    // ToastCustom.showMessage(con, "网络状况不佳");
                }
                super.onPostExecute(result);
            }
        }
    }

    public void realizeFunc() {
        imageSwitchView = (Image3DSwitchView) view
                .findViewById(R.id.image_switch_view);
        imageSwitchView.setVisibility(View.GONE);
        adContainer = (RelativeLayout) view.findViewById(R.id.ad_container);
        adGalleryHelper = new AdGalleryHelper(context, level, 5000, true);
        adContainer.addView(adGalleryHelper.getLayout());
        adGallery = adGalleryHelper.getAdGallery();
        adGallery.setAdOnItemClickListener(this);

    }

    @Override
    public void setItemClick(int position) {
        String url = level.get(position).getIntent();
        if (url != null) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url1 = Uri.parse(url);
                intent.setData(content_url1);
                startActivity(intent);
            } catch (Exception e) {
                // TODO: handle exception
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url1 = Uri.parse("http://" + url);
                intent.setData(content_url1);
                startActivity(intent);
            }

        }
        //调用系统浏览器访问对应广告图的链接
    }

    @SuppressLint("NewApi")
    private void playFlipAnimation2() {

        clickCount++;
        String text = MyCacheUtil.getshared(con).getString("NOTICEMESSAGE", "");

        if (text.length() > 0) {
            tv_textadvertising.setText(text);
        } else {
            tv_textadvertising.setText("暂无通知");
        }

//        final AnimatorSet animatorSetOut = (AnimatorSet) AnimatorInflater
//                .loadAnimator(con, R.anim.card_flip_left_out);
//
////        final AnimatorSet animatorSetIn = (AnimatorSet) AnimatorInflater
////                .loadAnimator(con, R.anim.card_flip_left_in);
//
//        animatorSetOut.setTarget(tv_textadvertising);
////        animatorSetIn.setTarget(tv_textadvertising);
//
//        String text = MyCacheUtil.getshared(con).getString("NOTICEMESSAGE", "");
//        
//        if(text.length()>0){
//        if(text.length()<=20){
//        	aa.add(text.substring(0, text.length()));
//        }else if(text.length()>=20&&text.length()<40){
//        	aa.add(text.substring(0, 20));
//        	aa.add(text.substring(20, text.length()));
//        }else if(text.length()>=40&&text.length()<60){
//        	aa.add(text.substring(0, 20));
//        	aa.add(text.substring(20, 40));
//        	aa.add(text.substring(40, text.length()));
//        }else if(text.length()>=60&&text.length()<70){
//        	aa.add(text.substring(0, 20));
//        	aa.add(text.substring(20, 40));
//        	aa.add(text.substring(40, 60));
//        	aa.add(text.substring(60, text.length()));
//        }
//        }else{
//        	re_pro.setVisibility(View.GONE);
//        }
////        tv_textadvertising.setText(aa.get(0));
//        
//
//        animatorSetOut.addListener(new AnimatorListenerAdapter() {
//
//            @Override
//            public void onAnimationEnd(Animator animation) {// 翻转90度之后，换图
//            	
//            	for (int i = protag; i < aa.size(); i++) {
//            		 tv_textadvertising.setText("  "+aa.get(protag));
//            		 protag ++;
//            		 if(protag==aa.size()){
//            			 protag = 0;
//            		 }
//            		break;
//       			}
//                if (clickCount % 2 == 0) {
////                	animatorSetIn.start();
////                    imageView.setImageResource(R.drawable.image1);
//
//                } else {
//
////                    imageView.setImageResource(R.drawable.image2);
//                }
////                animatorSetIn.start();
//               
//            }
//        	
//        });
//        
//        
//        
//    	final Handler handlers = new Handler();
//    	Runnable runnable = new Runnable() {
//    		@Override
//    		public void run() {
//    			animatorSetOut.start();
//    			handlers.postDelayed(this, 5000);// 20是延时时长
//    		}
//
//    	};
//    	handlers.postDelayed(runnable, 0);
//       
////        animatorSetIn.addListener(new AnimatorListenerAdapter() {
////
////            @Override
////            public void onAnimationEnd(Animator animation) {
////                // TODO
////            }
////        });
////        animatorSetOut.start();
    }


    private void showWarningDialog(String msg, @Nullable final OnMyDialogClickListener l) {
        warnDialog = new OneButtonDialogWarn(getActivity(),
                R.style.CustomDialog, "提示", msg, "确定",
                new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (l != null) {
                            l.onClick(v);
                        }
                        warnDialog.dismiss();
                    }
                });
        warnDialog.show();
    }

    private class MenuDataBuilder {
        private MenuData mData;

        public MenuDataBuilder() {
            mData = new MenuData();
            mData.type = 0;
            mData.title = "";
            mData.drawRes = 0;
            mData.enable = true;
            mData.mClickListener = null;
        }

        public MenuDataBuilder setType(int type) {
            mData.type = type;
            return this;
        }

        public MenuDataBuilder setTitle(String title) {
            mData.title = title;
            return this;
        }

        public MenuDataBuilder setDrawableRes(@DrawableRes int res) {
            mData.drawRes = res;
            return this;
        }

        public MenuDataBuilder setEnable(boolean enable) {
            mData.enable = enable;
            return this;
        }

        public MenuDataBuilder setOnClick(View.OnClickListener l) {
            mData.mClickListener = l;
            return this;
        }

        public MenuData build() {
            return mData;
        }
    }

    private class MenuData {
        public int type;
        public String title;
        public
        @DrawableRes
        int drawRes;
        public boolean enable = true;
        public View.OnClickListener mClickListener;
    }


}