package com.shareshenghuo.app.user;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.baozi.Zxing.CaptureActivity;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.shareshenghuo.app.user.BuildConfig;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.DobusinessFragment;
import com.shareshenghuo.app.user.fragment.HomeFragment;
import com.shareshenghuo.app.user.fragment.MineFragment;
import com.shareshenghuo.app.user.fragment.ShopListFragment;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
import com.shareshenghuo.app.user.network.request.VersionRequest;
import com.shareshenghuo.app.user.network.response.VersionRespone;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ApkUtil;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.UpdateService;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends FragmentActivity implements OnTouchListener {

    public FragmentTabHost fTabHost;
    private static String[] tabTags;
    //	public  Class[] tabFragments = {HomeFragment.class, ShopListFragment.class, DobusinessFragment.class, LifeCFragment.class, MineFragment.class};
    public static Class[] tabFragments;
    private static String[] tabTitles;
    private static int[] tabIcons;
    private TwoButtonDialog downloadDialog;
    public String shop_type_id = "";
    public String shop_type_name = "";
    public int positions;
    public List<CategoryInfo> item;
    public boolean tag;
    public static String name = "";
    public static Activity activity;

    static {
        if (BuildConfig.isApply) {
            tabTags = new String[]{"tab_home", "tab_shop_list", "tab_mine"};
            tabFragments = new Class[]{HomeFragment.class, ShopListFragment.class, MineFragment.class};
            tabTitles = new String[]{"首页", "商家", "我的"};
            tabIcons = new int[]{R.drawable.tab_home, R.drawable.tab_shop, R.drawable.tab_cart, R.drawable.tab_mine};
        } else {
            tabTags = new String[]{"tab_home", "tab_shop_list", "tab_cart", "tab_mine"};
            tabFragments = new Class[]{HomeFragment.class, ShopListFragment.class, DobusinessFragment.class, MineFragment.class};
            tabTitles = new String[]{"首页", "商家", "分红", "我的"};
            tabIcons = new int[]{R.drawable.tab_home, R.drawable.tab_shop, R.drawable.tab_cart, R.drawable.tab_mine};
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        checkVersion();
//        if(!getIntent().getBooleanExtra("logout", false))
//        	startActivity(new Intent(this, LoadingActivity.class));

        initTabHost();
//        new Thread(new Runnable() {
        activity = this;
//			@Override
//			public void run() {
        // TODO Auto-generated method stub

//			}
//		}).start();


        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        UserInfoManager.updateUserInfo(this);
        CityManager.getInstance(getApplicationContext()).requestLocation();
        UserInfo userInfo = UserInfoManager.getUserInfo(this);
        if (userInfo != null) {
            if (TextUtils.isEmpty(userInfo.province_code) ||
                    TextUtils.isEmpty(userInfo.city_code) ||
                    TextUtils.isEmpty(userInfo.area_code)) {
                startActivity(new Intent(MainActivity.this, PerfectInfoActivity.class));
            }
        }


        if (UserInfoManager.isLogin(this)) {
            EMChatManager.getInstance().login("c" + UserInfoManager.getUserId(this), "123456", new EMCallBack() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onProgress(int arg0, String arg1) {
                }

                @Override
                public void onError(int arg0, String arg1) {
                }
            });
        }


    }

    public void initTabHost() {
        fTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (int i = 0; i < tabFragments.length; i++) {
            TabSpec spec = fTabHost.newTabSpec(tabTags[i]);
            spec.setIndicator(getTabView(i));
            fTabHost.addTab(spec, tabFragments[i], null);
            fTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }

        fTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    public View getTabView(int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_tab_main, null);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivMainTabIcon);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvMainTabTitle);
        ivIcon.setImageResource(tabIcons[i]);
        if (TextUtils.isEmpty(tabTitles[i])) {
            tvTitle.setVisibility(View.GONE);
            ivIcon.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            tvTitle.setText(tabTitles[i]);
        }
        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!UserInfoManager.isLogin(this) &&
                (fTabHost.getTabWidget().getChildAt(3) == v)) {// || fTabHost.getTabWidget().getChildAt(4)==v)) {
            T.showShort(this, "您当前未登录，请先登录");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CityManager.getInstance(getApplicationContext()).stopLocation();
    }

    public Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }


    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);

//		if(data.getStringExtra("result").equals("-1")){
//			Log.e("", "adsasd");
//			return;
//		}
        if (resCode == RESULT_OK) {
            switch (reqCode) {
                case CaptureActivity.REQ_SCAN_QR:

                    try {
                        Intent it = new Intent(this, ShopDetailActivity.class);
                        it.putExtra("shopId", Integer.parseInt(data.getStringExtra("result")));
                        startActivity(it);
                    } catch (Exception e1) {
//					T.showShort(this, "鏃犳硶璇嗗埆闈炲晢瀹朵簩缁寸爜");
//					e.printStackTrace();

                        String shopid = data.getStringExtra("result");
                        String id = "";
                        String discountType = "";
                        String mchId = "";
                        try {
                            id = URLRequest(shopid).get("shopid").toString();

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        try {
                            discountType = URLRequest(shopid).get("discounttype").toString();

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        try {
                            mchId = URLRequest(shopid).get("mchid").toString();

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        if (TextUtils.isEmpty(id) && TextUtils.isEmpty(mchId)) {
                            Intent about = new Intent(this, WebLoadActivity.class);
                            about.putExtra("title", "");
                            about.putExtra("url", shopid);
                            startActivity(about);
                            return;
                        }
                        Intent it = new Intent(this, PaymentnewActivity.class);
                        it.putExtra("shopId", id);
                        it.putExtra("discountType", discountType);
                        it.putExtra("mchId", mchId);
                        startActivity(it);
//					try {
//						Intent it = new Intent(this, ShopDetailActivity.class);
//						it.putExtra("shopId", Integer.parseInt(data.getStringExtra("result")));
//						startActivity(it);
//					} catch(Exception e) {
//						T.showShort(this, "无法识别非商家二维码");
//						e.printStackTrace();
//					}
                    }


                    break;

                case MineFragment.REQ_PERSONAL_CENTER:
//				Intent login = new Intent(this, LoginActivity.class);
//				login.putExtra("logout", true);
//				startActivity(login);
                    break;


                default:

                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    private static boolean isExit = false;

    private void exitBy2Click() {
        if (!isExit) {
            isExit = true;
            T.showShort(this, "再按一次退出程序");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 1500);
        } else {
            finish();
        }
    }

    public void checkVersion() {
        VersionRequest req = new VersionRequest();
        req.client = "1";
        RequestParams params = new RequestParams("utf-8");
        try {
            params.setBodyEntity(new StringEntity(req.toJson()));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        new HttpUtils().send(HttpMethod.POST, Api.URL_VERSION_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                final VersionRespone bean = new Gson().fromJson(resp.result, VersionRespone.class);
                if (Api.SUCCEED == bean.result_code) {
                    try {
                        if (bean.data.version_code > ApkUtil.getVersionCode(MainActivity.this)) {
                            if (bean.data.status == 1) {
                                // 强制升级
                                upgrade(bean.data.version_url, getPackageName() + bean.data.version_no);
                            } else {
                                // 建议升级

                                downloadDialog = new TwoButtonDialog(MainActivity.this, R.style.CustomDialog,
                                        "版本升级", "发现新版本：" + bean.data.version_no, "取消", "升级", true, new OnMyDialogClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        switch (v.getId()) {
                                            case R.id.Button_OK:
                                                downloadDialog.dismiss();
                                                break;
                                            case R.id.Button_cancel:

                                                /*****update service*******/
                                                Intent intent = new Intent(MainActivity.this, UpdateService.class);
                                                intent.putExtra("Key_App_Name", "ermagy" + bean.data.version_no);
                                                intent.putExtra("Key_Down_Url", bean.data.version_url);
                                                startService(intent);
//													new UpDate(MainActivity.this, bean.data.version_url,bean.data.version_no);
//													upgrade(bean.data.version_url, MainActivity.this.getPackageName()+bean.data.version_no);
                                                downloadDialog.dismiss();
                                            default:
                                                break;
                                        }
                                    }
                                });
                                downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode,
                                                         KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                                            return true;
                                        } else {
                                            return true; // 默认返回 false
                                        }
                                    }
                                });
                                downloadDialog.setCanceledOnTouchOutside(false);

                                downloadDialog.show();
                            }

//								new AlertDialog.Builder(MainActivity.this).setTitle("发现新版本："+bean.data.version_no)
//								.setMessage(bean.data.update_content)
//								.setPositiveButton("取消", null)
//								.setNegativeButton("升级", new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										upgrade(bean.data.version_url, getPackageName()+bean.data.version_no);
//									}
//								}).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
            }
        });
    }

    public void upgrade(String url, String name) {
        ProgressDialogUtil.showProgressDlg(this, "升级中");
        ProgressDialogUtil.setCancelable(false);
        new HttpUtils().download(url, Environment.getExternalStorageDirectory() + "/" + name,
                new RequestCallBack<File>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ProgressDialogUtil.dismissProgressDlg();
                        T.showShort(MainActivity.this, "下载失败");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> resp) {
                        ProgressDialogUtil.dismissProgressDlg();
                        if (resp.statusCode == 200 && resp.result != null) {
                            ApkUtil.installAPK(MainActivity.this, resp.result);
                        }
                    }
                });
    }
}
