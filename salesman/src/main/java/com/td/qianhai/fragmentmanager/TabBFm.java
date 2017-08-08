package com.td.qianhai.fragmentmanager;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.entity.response.Constans;
import com.share.app.utils.WaringDialogUtils;
import com.td.qianhai.epay.oem.AccountManageActivity;
import com.td.qianhai.epay.oem.AgentManageActivity;
import com.td.qianhai.epay.oem.AuthenticationActivity;
import com.td.qianhai.epay.oem.BusinessCenterActivity;
import com.td.qianhai.epay.oem.DeviceListActivity;
import com.td.qianhai.epay.oem.DistributorActivity;
import com.td.qianhai.epay.oem.HaiGouAvtivity;
import com.td.qianhai.epay.oem.MyCircleActivity;
import com.td.qianhai.epay.oem.MyProfitActivitys;
import com.td.qianhai.epay.oem.MyPromotionActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.RateMianActivity;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.unlock.SetUnlockPasswordActivity;
import com.td.qianhai.epay.oem.views.dialog.LoadingDialogWhole;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.TwoButtonDialogStyle2;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

/**
 * tab页3(个人)
 *
 * @author Administrator
 */
public class TabBFm extends Fragment implements OnClickListener {

    private RelativeLayout turnover, st_printing, wechat_list, vip_manager, to_avertisings, qr_activitys, rate_btn, hands_pwd, revenue_management, circle_btn, re_rate_up, real_name_authentication, real_distribution, relalay_vip;

    private View view;

    private ImageView mImageView;

    private Activity con;

    private LoadingDialogWhole loadingDialogWhole;

    private String mobile;

    private TwoButtonDialogStyle2 doubleWarnDialog;

    private String attStr, tagsts, isvip;

    private ImageView a_propty;

    private OneButtonDialogWarn warnDialog;

    private String curl, isretailers, isgeneralagent;

//	private ImgListView sListView;

    private TextView tv_distribution, tv_agent, tv_vip;

    /**
     * 初始化填充值，控制图片被隐藏的边缘值
     */
    private static final int PADDING = -100;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        con = getActivity();
        view = inflater.inflate(R.layout.tab_b, null);
//		mobile = AppContext.getInstance().getMobile();
        mobile = MyCacheUtil.getshared(con).getString("Mobile", "");
        attStr = MyCacheUtil.getshared(con).getString("MERSTS", "");
        tagsts = MyCacheUtil.getshared(con).getString("STS", "");
        isretailers = MyCacheUtil.getshared(con).getString("ISRETAILERS", "");
        curl = MyCacheUtil.getshared(con).getString("CURROL", "");
        isvip = MyCacheUtil.getshared(con).getString("ISSENIORMEMBER", "");
        isgeneralagent = MyCacheUtil.getshared(con).getString("ISGENERALAGENT", "");

//		attStr = AppContext.getInstance().getMerSts();
//		tagsts = AppContext.getInstance().getSts();
        initview();


//		sListView.setImageId(R.drawable.top_img)


        // 隐藏图片的初始边界
        // 保证下拉能出现放大图片的效果
    }

    private void initview() {
        st_printing = (RelativeLayout) view.findViewById(R.id.st_printing);
        qr_activitys = (RelativeLayout) view.findViewById(R.id.aaaa);
        circle_btn = (RelativeLayout) view.findViewById(R.id.circle_btn);
        relalay_vip = (RelativeLayout) view.findViewById(R.id.relalay_vip);
        vip_manager = (RelativeLayout) view.findViewById(R.id.vip_manager);
        to_avertisings = (RelativeLayout) view.findViewById(R.id.to_avertisings);
        wechat_list = (RelativeLayout) view.findViewById(R.id.wechat_list);
        turnover = (RelativeLayout) view.findViewById(R.id.turnover);
        view.findViewById(R.id.business_center).setOnClickListener(this);
        turnover.setOnClickListener(this);
        st_printing.setOnClickListener(this);
        wechat_list.setOnClickListener(this);
        to_avertisings.setOnClickListener(this);
        vip_manager.setOnClickListener(this);
        relalay_vip.setOnClickListener(this);
//		tv_vip = (TextView) view.findViewById(R.id.tv_vip);
        circle_btn.setOnClickListener(this);
//		real_distribution = (RelativeLayout) view.findViewById(R.id.real_distribution);
//		real_distribution.setOnClickListener(this);
        tv_agent = (TextView) view.findViewById(R.id.tv_agent);
        tv_distribution = (TextView) view.findViewById(R.id.tv_distribution);
        a_propty = (ImageView) view.findViewById(R.id.a_propty);
//		if(tagsts.equals("2")){
//			a_propty.setVisibility(View.VISIBLE);
//			AnimationUtil.BtnSpecialAnmations1(con, a_propty,700);
//		}
//		p_out = (TextView) view.findViewById(R.id.p_out);
//		p_out.setOnClickListener(this);
        real_name_authentication = (RelativeLayout) view.findViewById(R.id.real_name_authentication);
        real_name_authentication.setOnClickListener(this);
        rate_btn = (RelativeLayout) view.findViewById(R.id.rate_btn);
        rate_btn.setOnClickListener(this);
        re_rate_up = (RelativeLayout) view.findViewById(R.id.re_rate_up);
        re_rate_up.setOnClickListener(this);
        hands_pwd = (RelativeLayout) view.findViewById(R.id.hands_pwd);
        hands_pwd.setOnClickListener(this);
        revenue_management = (RelativeLayout) view.findViewById(R.id.revenue_management);
        revenue_management.setOnClickListener(this);

//		if(tagsts.equals("3")||tagsts.equals("0")||tagsts.equals("4")){
//			if(curl.equals("2")){
//				tv_distribution.setText("分销商管理");
//			}else{
//				tv_distribution.setText("申请分销商");
//			}
//		}else{
//			real_distribution.setVisibility(View.GONE);
//		}

//		if(tagsts.equals("3")||tagsts.equals("0")||tagsts.equals("4")){
////			if(isretailers.equals("1")){
////				rate_btn.setVisibility(View.VISIBLE);
////			}else{
////				rate_btn.setVisibility(View.GONE);
////			}
//			if(curl.equals("1")||curl.equals("2")){
//				
//			}else{
//				rate_btn.setVisibility(View.GONE);
//			}
//		}else{
//			to_avertisings.setVisibility(View.GONE);
//				rate_btn.setVisibility(View.GONE);
//			
//		}

//		if(tagsts.equals("3")||tagsts.equals("0")||tagsts.equals("4")){
//			if(isvip.equals("1")){
//				tv_vip.setText("高级会员管理");
//				
//			}else{
//				tv_vip.setText("申请高级会员");
//			}
//		}else{
//			relalay_vip.setVisibility(View.GONE);
//		}

//		if(!UnlockLoginActivity.isjp){
//			hands_pwd.setVisibility(View.GONE);
//		}else{
//			hands_pwd.setVisibility(View.VISIBLE);
//		}
//		if(!attStr.equals("0")){
//			hands_pwd.setVisibility(View.GONE);
//		}

        if (tagsts.equals("0") || tagsts.equals("3") || tagsts.equals("4")) {
            real_name_authentication.setVisibility(View.GONE);
        }
        qr_activitys.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent it = new Intent(con, AccountManageActivity.class);
                a_propty.setVisibility(View.INVISIBLE);
                startActivity(it);

            }
        });


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // 相当于Fragment的onResume

        } else {
            // 相当于Fragment的onPause
        }
    }


    /**
     * 查看商户资料Task
     *
     * @author Administrator
     */
    class BussinessInfoTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog("正在查询中...");
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3]};
            return NetCommunicate.get(HttpUrls.BUSSINESS_INFO, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            loadingDialogWhole.dismiss();
            if (result != null) {
                if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
                    if (result.get("ACTNAM") != null) {
                        AppContext.getInstance().setUsername(result.get("ACTNAM").toString());
                    }
//					((AppContext)con.getApplication()).setUsername(result.get("ACTNAM"));

//					showView(result);
                } else {
//					ToastCustom.showMessage(BussinessInfoActivity.this, result
//							.get(Entity.RSPMSG).toString(), Toast.LENGTH_SHORT);
                }
            }
            super.onPostExecute(result);
        }
    }


    protected void showLoadingDialog(String msg) {
        loadingDialogWhole = new LoadingDialogWhole(con, R.style.CustomDialog,
                msg);
        loadingDialogWhole.setCancelable(false);
        loadingDialogWhole
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
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
        loadingDialogWhole.setCanceledOnTouchOutside(false);
        loadingDialogWhole.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.rate_btn:
                if (tagsts.equals("4")) {
                    Toast.makeText(con, "抱歉 ,手机号变更中不可使用代理功能", Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(getActivity(), "抱歉 ,手机号变更中不可使用代理功能");

                } else {
//				if(!isgeneralagent.equals("1")){
//					warnDialog = new OneButtonDialogWarn(con,
//							R.style.CustomDialog, "提示", "您还不是代理商,请先升级", "确定",
//							new OnMyDialogClickListener() {
//								@Override
//								public void onClick(View v) {
//									warnDialog.dismiss();
//									
//								}
//							});
//					warnDialog.show();
//					return;
//				}
                    if (curl.equals("1") || curl.equals("2")) {
                        intent.setClass(con, AgentManageActivity.class);
                        startActivity(intent);
                    } else {
//					UpAgentActivity
                        intent.setClass(con, AgentManageActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.hands_pwd:
                if (attStr.equals("0")) {

                    SpannableString msps = new SpannableString("重置密码将清空之前的手势密码！");
                    showDoubleWarnDialog(msps);


                } else {
                    Toast.makeText(con, "未设手势密码", Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(getActivity(), "未设手势密码");
                }
                break;
            case R.id.circle_btn:
                intent.setClass(con, MyCircleActivity.class);
                startActivity(intent);
                break;
            case R.id.real_name_authentication:
                if (MyCacheUtil.getshared(con).getString("STS", "").equals("1")) {
                    Toast.makeText(con, "实名认证信息审核中,待审核通过后重试", Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(getActivity(), "实名认证信息审核中,待审核通过后重试");
                } else {
                    intent.setClass(con, AuthenticationActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.re_rate_up:
//			if(tagsts.equals("0")||tagsts.equals("3")){
                intent.setClass(con, RateMianActivity.class);
                startActivity(intent);
//			}else{
//				ToastCustom.showMessage(getActivity(), "抱歉 ,您当前不可进行费率升级");
//			}
                break;
            case R.id.st_printing:
                Intent it = new Intent(con, DeviceListActivity.class);
                startActivity(it);
                break;

            case R.id.turnover:
                intent.setClass(con, BusinessCenterActivity.class);
                startActivity(intent);
                break;
//		case R.id.relalay_vip:
//			if(isvip.equals("1")){
//				intent.setClass(con, HaiGouAvtivity.class);
//				intent.putExtra("url",HttpUrls.SUPVIP);
//				intent.putExtra("tag","0");
//				startActivity(intent);
//			}else{
//				intent.setClass(con, HaiGouAvtivity.class);
//				intent.putExtra("url",HttpUrls.APPLYSUPVIP);
//				intent.putExtra("tag","0");
//				startActivity(intent);
//			}


//		case R.id.real_distribution:
//			
//			if (tagsts.equals("4")) {
//				ToastCustom.showMessage(getActivity(), "抱歉 ,手机号变更中不可使用分销商功能");
//				
//			}else{
//			if(curl.equals("2")){
//				intent.setClass(con, HaiGouAvtivity.class);
//				intent.putExtra("url",HttpUrls.DISTINDEX);
//				intent.putExtra("tag","0");
//				startActivity(intent);
//			}else{
//				intent.setClass(con, HaiGouAvtivity.class);
//				intent.putExtra("url",HttpUrls.NEWAGENCY);
//				intent.putExtra("tag","0");
//				startActivity(intent);
//			}
//			}
//			break;

            case R.id.revenue_management:
                intent.setClass(con, MyProfitActivitys.class);
                startActivity(intent);
                break;
            case R.id.wechat_list:
                intent.setClass(con, MyPromotionActivity.class);
                startActivity(intent);

                break;
            case R.id.to_avertisings:
                intent.setClass(con, HaiGouAvtivity.class);
                intent.putExtra("url", HttpUrls.TOAVERTISING);
                intent.putExtra("title", "广告发布");
                intent.putExtra("tag", "0");
                startActivity(intent);
                break;
            case R.id.vip_manager:
                intent.setClass(con, DistributorActivity.class);
                startActivity(intent);
                break;
            case R.id.business_center://营业中心
                //  2017/7/12 营业中心
                String type = MyCacheUtil.getshared(getActivity()).getString(Constans.Login.TYPE, "");
                if (TextUtils.equals("1", type)) {
                    //代理商
                    intent.setClass(con, BusinessCenterActivity.class);
                    startActivity(intent);
                } else {
                    WaringDialogUtils.showWaringDialog(getActivity(), "该用户不是代理商", null);
                }
                break;
            default:
                break;
        }
    }


    /***
     * 双按钮提示dialog
     *
     * @param msg
     */
    protected void showDoubleWarnDialog(SpannableString msg) {
        doubleWarnDialog = new TwoButtonDialogStyle2(getActivity(),
                R.style.CustomDialog, "提示", msg, "确定", "取消",
                new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        doubleWarnOnClick(v);
                    }
                });
        doubleWarnDialog.setCancelable(false);
        doubleWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
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
        doubleWarnDialog.setCanceledOnTouchOutside(false);
        doubleWarnDialog.show();
    }


//	protected void doubleWarnOnClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_right:
//			
//			AppContext.getInstance().setCustId(null);
//			AppContext.getInstance().setPsamId(null);
//			AppContext.getInstance().setMacKey(null);
//			AppContext.getInstance().setPinKey(null);
//			AppContext.getInstance().setMerSts(null);
//			AppContext.getInstance().setMobile(null);
//			AppContext.getInstance().setEncryPtkey(null);
//			AppContext.getInstance().setStatus(null);
//			AppContext.getInstance().setCustPass(null);
//			AppContext.getInstance().setVersionSerial(null);
//			doubleWarnDialog.dismiss();
//			AppContext.getInstance().exit();
//			
//			Intent it = new Intent(getActivity(),UserActivity.class);
//			
//			startActivity(it);
//			MainActivity.cache.putString("usermobile", "");
//			MainActivity.cache.putString("userpwd", "");
//			MainActivity.cache.commit();
//			break;
//		case R.id.btn_left:
//			doubleWarnDialog.dismiss();
//			AppContext.getInstance().setCustId(null);
//			AppContext.getInstance().setPsamId(null);
//			AppContext.getInstance().setMacKey(null);
//			AppContext.getInstance().setPinKey(null);
//			AppContext.getInstance().setMerSts(null);
//			AppContext.getInstance().setMobile(null);
//			AppContext.getInstance().setEncryPtkey(null);
//			AppContext.getInstance().setStatus(null);
//			AppContext.getInstance().setCustPass(null);
//			AppContext.getInstance().setVersionSerial(null);
//			AppContext.getInstance().exit();
//
//			break;
//		default:
//			break;
//		}
//	}

    protected void doubleWarnOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                doubleWarnDialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), SetUnlockPasswordActivity.class);
                intent.putExtra("refresh", "refresh");
                startActivity(intent);
                break;
            case R.id.btn_left:
                doubleWarnDialog.dismiss();

                break;
            default:
                break;
        }
    }


//	@Override
//	protected void doubleWarnOnClick(View v) {
//		// TODO Auto-generated method stub
//		super.doubleWarnOnClick(v);
//		switch (v.getId()) {
//		case R.id.btn_left:
//			AppContext.getInstance().setCustId(null);
//			AppContext.getInstance().setPsamId(null);
//			AppContext.getInstance().setMacKey(null);
//			AppContext.getInstance().setPinKey(null);
//			AppContext.getInstance().setMerSts(null);
//			AppContext.getInstance().setMobile(null);
//			AppContext.getInstance().setEncryPtkey(null);
//			AppContext.getInstance().setStatus(null);
//			AppContext.getInstance().setCustPass(null);
//			AppContext.getInstance().setVersionSerial(null);
//			doubleWarnDialog.dismiss();
//			AppContext.getInstance().exit();
//			
//			Intent it = new Intent(getActivity(),UserActivity.class);
//			
//			startActivity(it);
//			break;
//		case R.id.btn_right:
//			doubleWarnDialog.dismiss();
//			break;
//
//		default:
//			break;
//		}
//	}

}

