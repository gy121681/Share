package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.entity.response.ActivationCodeResponse;
import com.share.app.entity.response.Constans;
import com.share.app.network.CallbackObject;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.td.qianhai.epay.oem.activity.membermanager.UnActiveListActivity;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.ReturnActcodeBean;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnBackDialogClickListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class ActivationCodeManageActivity extends BaseActivity implements OnClickListener {


    private TextView has_been_act, has_been_generated, no_generation;

    private RelativeLayout agents_new_actcode, agents_actcode, agents_actcode1;

    private String phone, agtid;

    private OneButtonDialogWarn warnDialog;

    private ChooseDialog chooseDialog;

    private String[] type;

    private int positions;

    private MyEditDialog doubleWarnDialog;

    private boolean isrun = false;

    private boolean isThreadRun = false;

    private ArrayList<HashMap<String, Object>> mList;

    private int REQUEST_CONTACT = 1;

    private String paypwds, setnum;

    private String[] code;

    private LinearLayout lin_actcode;

    private String price, isretailers, issaleagt, isgeneralagent;

    private LinearLayout mLayoutUnActive;//未激活

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accode_manage);


    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//		phone = ((AppContext)getApplication()).getMobile();
        phone = MyCacheUtil.getshared(this).getString("Mobile", "");
        agtid = MyCacheUtil.getshared(this).getString("AGENTID", "");
        type = new String[]{"1个", "2个", "3个", "4个", "5个"};
        initview();
        if (!isrun) {
//			initdata();
            queryActivationCodeInfo();
        }
    }

    private void queryActivationCodeInfo() {
        String userId = MyCacheUtil.getshared(this).getString(Constans.Login.USERID, "");
        String start = "0";
        String size = "10";
        ProgressDialogUtil.showProgressDlg(this, "数据加载中...");
        Request.getMemberManagerActivationCodeInfo(userId, start, size,
                new CallbackObject<ActivationCodeResponse>() {
                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        showWaringDialog(msg);
                    }

                    @Override
                    public void onSuccess(ActivationCodeResponse data) {
                        ProgressDialogUtil.dismissProgressDlg();
                        showActivationInfo(data);
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        showWaringDialog("网络异常");
                    }
                });
    }

    private void showActivationInfo(ActivationCodeResponse data) {
        if (data != null) {
            has_been_act.setText(data.getActivationedCount());
            no_generation.setText(data.getUnActivationCount());
        }
    }

    private void initview() {
        // TODO Auto-generated method stub
        ((TextView) findViewById(R.id.tv_title_contre)).setText("代理商激活码管理");
        findViewById(R.id.bt_title_left).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        isretailers = MyCacheUtil.getshared(this).getString("ISRETAILERS", "");
        issaleagt = MyCacheUtil.getshared(this).getString("ISSALEAGT", "");
        isgeneralagent = MyCacheUtil.getshared(this).getString("ISGENERALAGENT", "");
        agents_actcode1 = (RelativeLayout) findViewById(R.id.agents_actcode1);
        agents_actcode1.setOnClickListener(this);
        has_been_act = (TextView) findViewById(R.id.has_been_act);
        has_been_generated = (TextView) findViewById(R.id.has_been_generated);
        no_generation = (TextView) findViewById(R.id.no_generation);
        agents_new_actcode = (RelativeLayout) findViewById(R.id.agents_new_actcode);
        agents_new_actcode.setOnClickListener(this);
        agents_actcode = (RelativeLayout) findViewById(R.id.agents_actcode);
        agents_actcode.setOnClickListener(this);
        lin_actcode = (LinearLayout) findViewById(R.id.lin_actcode);
        lin_actcode.setOnClickListener(this);
        findViewById(R.id.layout_unactive).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivationCodeManageActivity.this, UnActiveListActivity.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (v.getId()) {
            case R.id.agents_new_actcode:
                PopUpBox();
                break;
            case R.id.agents_actcode:
                //已激活会员查询
                intent = new Intent(ActivationCodeManageActivity.this, AgentListActivity.class);
//                intent.putExtra("num", setnum);
                intent.putExtra("level", Constans.MemberLevel.LEVEL_VIP);
                startActivity(intent);
//                if (isgeneralagent.equals("1") || issaleagt.equals("1") || isretailers.equals("1")) {
//                    Intent it1 = new Intent(ActivationCodeManageActivity.this, AgentListActivity.class);
//                    it1.putExtra("num", setnum);
//                    it1.putExtra("tag", "0");
//                    startActivity(it1);
//                } else {
//                    showWaringDialog("当前用户无此操作权限");
//                }
//			startActivityForResult(it1, REQUEST_CONTACT);
                break;

            case R.id.lin_actcode:
//                Intent it = new Intent(ActivationCodeManageActivity.this, ActCodeListActivity.class);
//                startActivity(it);
                break;
            case R.id.agents_actcode1:
                //普通会员已激活码查询
                intent = new Intent(ActivationCodeManageActivity.this, AgentListActivity.class);
//                intent.putExtra("num", setnum);
                intent.putExtra("level", Constans.MemberLevel.LEVEL_NORMAL);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void showWaringDialog(String msg) {
        warnDialog = new OneButtonDialogWarn(ActivationCodeManageActivity.this,
                R.style.CustomDialog, "提示",
                msg, "确定",
                new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {

                        warnDialog.dismiss();
                    }
                });
        warnDialog.setCancelable(false);
        warnDialog.setCanceledOnTouchOutside(false);
        warnDialog.show();
    }

    private void PopUpBox() {
        chooseDialog = new ChooseDialog(
                ActivationCodeManageActivity.this,
                R.style.CustomDialog,
                new OnBackDialogClickListener() {

                    @Override
                    public void OnBackClick(View v, String str,
                                            int position) {
                        positions = position + 1;
                        // TODO Auto-generated method stub
                        agents_new_actcode.setEnabled(false);
                        shwpaypwd();
                        chooseDialog.dismiss();
                    }
                }, "请选择生成激活码数量", type);
        chooseDialog.show();

    }

    protected void shwpaypwd() {

        doubleWarnDialog = new MyEditDialog(ActivationCodeManageActivity.this,
                R.style.MyEditDialog, "充值", "请输入支付密码", "确认", "取消", "",
                new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {

                        switch (v.getId()) {
                            case R.id.btn_right:
                                doubleWarnDialog.dismiss();
                                InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                break;
                            case R.id.btn_left:
                                break;
                            default:
                                break;
                        }

                    }
                },
                new onMyaddTextListener() {

                    @Override
                    public void refreshActivity(String paypwd) {

                        if (paypwd == null || paypwd.equals("")) {
                            Toast.makeText(getApplicationContext(), "请输入支付密码",
                                    Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(
//							ActivationCodeManageActivity.this,
//							"请输入支付密码！");
                            return;
                        }
                        if (paypwd.length() < 6 || paypwd.length() > 15) {
                            Toast.makeText(getApplicationContext(), "输入的密码长度有误,请输入6个数字、字母或特殊符号",
                                    Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(
//							ActivationCodeManageActivity.this,
//							"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
                            return;
                        }
                        paypwds = paypwd;

                        if (!isThreadRun) {
                            isThreadRun = true;
                            doubleWarnDialog.dismiss();
                            showLoadingDialog("正在加载中...");
                            new Thread(run).start();
                        }

//				InitActCode  itask = new InitActCode();
//				itask.execute(HttpUrls.ACTCODEINIT + "", phone,agtid,positions+"",paypwd);
//				mobile = charge_phone.getText().toString().trim();
//				PhoneChargeTask cardTask = new PhoneChargeTask();
//				cardTask.execute(HttpUrls.PHONE_CHARGE + "", mobile,paypwd,Operators,prdtypes,prdid,mercnum);

                    }
                });
        agents_new_actcode.setEnabled(true);
        doubleWarnDialog.setCancelable(false);
        doubleWarnDialog.setCanceledOnTouchOutside(false);
        doubleWarnDialog.show();


    }

    private void initdata() {

        Actcodemanage atask = new Actcodemanage();
        atask.execute(HttpUrls.ACTCODEMANAGE + "", phone, agtid);

    }

    class Actcodemanage extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isrun = true;
            showLoadingDialog("正在处理中。。。");
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2]};
            return NetCommunicate
                    .getAgentMidatc(HttpUrls.ACTCODEMANAGE, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            isrun = false;
            loadingDialogWhole.dismiss();
            if (result != null) {
                if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD).toString())) {
//					has_been_act.setText(result.get("ALLOTNUM").toString());
                    has_been_generated.setText(result.get("MAKENUM").toString());
                    no_generation.setText(result.get("UNMAKENUM").toString());
                    String a = result.get("ACTIVENUM").toString();
                    String b = result.get("ALLOTNUM").toString();
                    has_been_act.setText(b);//Integer.parseInt(a)+Integer.parseInt(b)+""

                    setnum = result.get("UNMAKENUM").toString();
                } else {
                    Toast.makeText(getApplicationContext(), result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(ActivationCodeManageActivity.this,
//							result.get(Entity.RSPMSG).toString());
                }
            }
            super.onPostExecute(result);
        }
    }

//	class InitActCode extends
//			AsyncTask<String, Integer, HashMap<String, Object>> {
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			isrun = true;
//			
//			showLoadingDialog("正在处理中。。。");
//		}
//
//		@Override
//		protected HashMap<String, Object> doInBackground(String... params) {
//			String[] values = { params[0], params[1], params[2],params[3], params[4] };
//			return NetCommunicate
//					.getAgentMidatc(HttpUrls.ACTCODEINIT, values);
//		}
//
//		@Override
//		protected void onPostExecute(HashMap<String, Object> result) {
//			loadingDialogWhole.dismiss();
//			if (result != null) {
//				isrun = false;
//				if (Entity.STATE_OK
//						.equals(result.get(Entity.RSPCOD).toString())) {
//					doubleWarnDialog.dismiss();
//					
//					warnDialog = new OneButtonDialogWarn(ActivationCodeManageActivity.this,
//							R.style.CustomDialog, "提示",
//							result.get(Entity.RSPMSG).toString(), "确定",
//							new OnMyDialogClickListener() {
//								@Override
//								public void onClick(View v) {
//									warnDialog.dismiss();
//								}
//							});
//					warnDialog.setCancelable(false);
//					warnDialog.setCanceledOnTouchOutside(false);
//					warnDialog.show();
//					
//					initdata();
//				} else {
//					doubleWarnDialog.dismiss();
//					ToastCustom.showMessage(ActivationCodeManageActivity.this,
//							result.get(Entity.RSPMSG).toString());
//				}
//			}
//			super.onPostExecute(result);
//		}
//	}


    Runnable run = new Runnable() {

        @Override
        public void run() {
            mList = new ArrayList<HashMap<String, Object>>();
            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            // ArrayList<HashMap<String, Object>> list2 = new
            // ArrayList<HashMap<String, Object>>();
            String[] values = {HttpUrls.ACTCODEINIT + "", phone, agtid, positions + "", paypwds};
            ReturnActcodeBean entitys = NetCommunicate.ActcodeBill(
                    HttpUrls.ACTCODEINIT, values);
            Message msg = new Message();
            if (entitys != null) {
                loadingDialogWhole.dismiss();
                list = entitys.list;
                if (list != null && list.size() != 0) {
                    mList.addAll(list);
                    msg.what = 1;
                    msg.obj = entitys.getRspmsg();
                } else {
                    msg.obj = entitys.getRspmsg();
                    msg.what = 2;
                }


            } else {
                msg.obj = entitys.getRspmsg();
                loadingDialogWhole.dismiss();
                msg.what = 3;

            }

            isThreadRun = false;
            loadingDialogWhole.dismiss();
            handler.sendMessage(msg);
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String msgs = msg.obj.toString();
            switch (msg.what) {
                case 1:

                    code = new String[mList.size()];

                    for (int i = 0; i < mList.size(); i++) {
                        code[i] = mList.get(i).get("ACTCODE").toString();
                    }

                    chooseDialog = new ChooseDialog(
                            ActivationCodeManageActivity.this,
                            R.style.CustomDialog,
                            new OnBackDialogClickListener() {

                                @Override
                                public void OnBackClick(View v, String str,
                                                        int position) {

                                    // TODO Auto-generated method stub
                                    chooseDialog.dismiss();
                                }
                            }, "已生成激活码", code);
                    chooseDialog.show();


                    chooseDialog.setOnDismissListener(new OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface arg0) {
                            // TODO Auto-generated method stub
                            initdata();
                        }
                    });

                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), msgs,
                            Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(ActivationCodeManageActivity.this,
//						msgs);
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), msgs,
                            Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(ActivationCodeManageActivity.this,
//						msgs);
                    break;
                default:
                    break;
            }
        }

        ;
    };

}
