package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class AgentManageActivity extends BaseActivity implements
        OnClickListener {

    private RelativeLayout lin_new_agent, lin_agent_maneger, to_avertising,
            lin_activation_code, buy_code;
    private String phone, newphone, ratenum, agtid = "", mercnum;
    private boolean isrun = false;
    private TextView agent_username, agent_type, tv_usedactcod, tv_leftshramt, tv_totshramt, tv_grade, tv_to_change;
    private OneButtonDialogWarn warnDialog;
    private String minfeerate, isretailers, issaleagt, isgeneralagent;
    private String curl;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        AppContext.getInstance().addActivity(this);
        curl = MyCacheUtil.getshared(this).getString("CURROL", "");
        setContentView(R.layout.agentmanage_activity);


    }

    @Override
    protected void onStart() {
// TODO Auto-generated method stub
        super.onStart();
//phone = ((AppContext)getApplication()).getMobile();
        phone = MyCacheUtil.getshared(this).getString("Mobile", "");
        mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");
//agtid = ((AppContext)getApplication()).getAgentid();
        agtid = MyCacheUtil.getshared(this).getString("AGENTID", "");
        isretailers = MyCacheUtil.getshared(this).getString("ISRETAILERS", "");
        issaleagt = MyCacheUtil.getshared(this).getString("ISSALEAGT", "");
        isgeneralagent = MyCacheUtil.getshared(this).getString("ISGENERALAGENT", "");
        newphone = "13510585449";
        ratenum = "0.69";

        initview();
        if (!isrun) {
            initdata();
        }
    }

    private void initdata() {

        AgentInfoTask atask = new AgentInfoTask();
        atask.execute(HttpUrls.AGENTINFO + "", phone, agtid, mercnum);


    }

    private void initview() {
        ((TextView) findViewById(R.id.tv_title_contre)).setText("费率收益");
        findViewById(R.id.bt_title_left).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        lin_new_agent = (RelativeLayout) findViewById(R.id.lin_new_agents);
        lin_agent_maneger = (RelativeLayout) findViewById(R.id.lin_agent_maneger);
        lin_activation_code = (RelativeLayout) findViewById(R.id.lin_activation_code);
        buy_code = (RelativeLayout) findViewById(R.id.buy_code);
        to_avertising = (RelativeLayout) findViewById(R.id.to_avertising);
        to_avertising.setOnClickListener(this);
        buy_code.setOnClickListener(this);
        lin_new_agent.setOnClickListener(this);
        lin_agent_maneger.setOnClickListener(this);
        lin_activation_code.setOnClickListener(this);
        agent_username = (TextView) findViewById(R.id.agent_usernames);
        agent_username.setOnClickListener(this);
        agent_type = (TextView) findViewById(R.id.agent_type);
        tv_usedactcod = (TextView) findViewById(R.id.tv_usedactcod);
        tv_leftshramt = (TextView) findViewById(R.id.tv_leftshramt);
        tv_totshramt = (TextView) findViewById(R.id.tv_totshramt);
        tv_grade = (TextView) findViewById(R.id.tv_grade);
        tv_to_change = (TextView) findViewById(R.id.tv_to_change);
        tv_to_change.setOnClickListener(this);
        if (agtid == null || agtid.equals("")) {
            lin_agent_maneger.setVisibility(View.GONE);
            lin_activation_code.setVisibility(View.GONE);
        }
        if (curl.equals("0")) {

        } else {

        }

    }

    @Override
    public void onClick(View v) {
// TODO Auto-generated method stub
        Intent it = new Intent();
        switch (v.getId()) {
            case R.id.lin_new_agents:
                it.setClass(AgentManageActivity.this, NewAgentActivity.class);
                startActivity(it);
                break;

            case R.id.lin_agent_maneger:
                it.setClass(AgentManageActivity.this, MyCircleActivity1.class);
                it.putExtra("tag", "1");
                it.putExtra("min", minfeerate);
                startActivity(it);
                break;

            case R.id.lin_activation_code:
                it.setClass(AgentManageActivity.this, ActivationCodeManageActivity.class);
                startActivity(it);

                break;
            case R.id.agent_usernames:
//	it.setClass(AgentManageActivity.this, MyProfitActivity.class);
//	startActivity(it);
                break;
            case R.id.to_avertising:
                Intent intent = new Intent();
                intent.setClass(AgentManageActivity.this, HaiGouAvtivity.class);
                intent.putExtra("url", HttpUrls.TOAVERTISING);
                intent.putExtra("title", "广告发布");
                intent.putExtra("tag", "0");
                startActivity(intent);
                break;
            case R.id.tv_to_change:

                SpannableString msp = new SpannableString("您确定将当前金额存入钱包吗?");
                showDoubleWarnDialog(msp);

                break;
            case R.id.buy_code:
                if (isgeneralagent.equals("1") || issaleagt.equals("1") || isretailers.equals("1")) {
                    it.setClass(AgentManageActivity.this, BuyCodeActivity.class);
                    startActivity(it);
                } else {
                    warnDialog = new OneButtonDialogWarn(AgentManageActivity.this,
                            R.style.CustomDialog, "提示",
                            "请先升级会员等级再操作", "确定",
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

                break;
            default:
                break;
        }
    }


    @Override
    protected void doubleWarnOnClick(View v) {
// TODO Auto-generated method stub
        super.doubleWarnOnClick(v);
        switch (v.getId()) {
            case R.id.btn_left:

                doubleWarnDialog.dismiss();

                break;
            case R.id.btn_right:
                doubleWarnDialog.dismiss();
                DepositPurseTask PurseTask = new DepositPurseTask();
                PurseTask.execute(HttpUrls.DEPOSITPURSE + "", phone, agtid);
                break;

            default:
                break;
        }
    }

    class AgentInfoTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isrun = true;
            showLoadingDialog("正在加载中。。。");
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3]};
            return NetCommunicate
                    .getAgentMidatc(HttpUrls.AGENTINFO, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            isrun = false;
            loadingDialogWhole.dismiss();
            if (result != null) {
                if (Entity.STATE_OK
                        .equals(result.get(Entity.RSPCOD).toString())) {
//			agent_username.setText(result.get("AGTNAM").toString());
                    String t = result.get("NOCARAGTTYP").toString();
                    if (t.equals("1")) {
                        agent_type.setText("普通");
                        tv_grade.setText("LV0");
                    } else if (t.equals("2")) {
                        agent_type.setText("男爵");
                        tv_grade.setText("LV1");
                    } else if (t.equals("3")) {
                        agent_type.setText("伯爵");
                        tv_grade.setText("LV2");
                    } else if (t.equals("4")) {
                        agent_type.setText("公爵");
                        tv_grade.setText("LV3");
                    }

                    tv_usedactcod.setText(result.get("USEDACTCOD").toString());

                    String leftshramt = result.get("LEFTSHRAMT").toString();
                    String totshramt = result.get("TOTSHRAMT").toString();

                    if (totshramt.equals("0")) {
                        agent_username.setText("暂无收益");
                    } else {
                        agent_username.setText(Double.parseDouble(totshramt)
                                / 100 + "");
                    }

                    if (leftshramt.equals("0")) {
                        tv_leftshramt.setText("暂无收益");
                    } else {
                        tv_leftshramt.setText(Double.parseDouble(leftshramt) / 100 + "");
                    }
                    if (leftshramt.equals("0")) {
                        tv_totshramt.setText("0.0");
                    } else {
                        tv_totshramt.setText(Double.parseDouble(leftshramt) / 100 + "");
                    }

                    minfeerate = result.get("MINFEERATE").toString();
                    String maxfeerate = result.get("MAXFEERATE").toString();

                    int min = (int) (Double.parseDouble(minfeerate) * 100);
                    int max = (int) (Double.parseDouble(maxfeerate) * 100);
                    List<String> list = new ArrayList<String>();
                    for (int i = min; i <= max; i++) {
                        list.add(String.valueOf((double) i / 100));
                    }
                    String[] a = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).length() == 3) {
                            a[i] = list.get(i) + "0%";
                        } else {
                            a[i] = list.get(i) + "%";
                        }

                    }
                    ((AppContext) getApplication()).setRatelist(a);
                } else {
                    Toast.makeText(getApplicationContext(), result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(AgentManageActivity.this,
//					result.get(Entity.RSPMSG).toString());
                    finish();
                }
            }
            super.onPostExecute(result);
        }
    }

    class DepositPurseTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isrun = true;
            showLoadingDialog("正在加载中。。。");
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2]};
            return NetCommunicate.getVerification(HttpUrls.DEPOSITPURSE, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            isrun = false;
            loadingDialogWhole.dismiss();
            if (result != null) {
                if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
                    warnDialog = new OneButtonDialogWarn(AgentManageActivity.this,
                            R.style.CustomDialog, "提示",
                            "存入钱包成功", "确定",
                            new OnMyDialogClickListener() {
                                @Override
                                public void onClick(View v) {

                                    onStart();
                                    warnDialog.dismiss();
                                }
                            });
                    warnDialog.setCancelable(false);
                    warnDialog.setCanceledOnTouchOutside(false);
                    warnDialog.show();

                } else {
                    Toast.makeText(getApplicationContext(),
                            result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT).show();
                    // ToastCustom.showMessage(AgentManageActivity.this,
                    // result.get(Entity.RSPMSG).toString());
                }
            }
            super.onPostExecute(result);
        }
    }
}
