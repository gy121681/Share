package com.td.qianhai.epay.oem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.CardListAdapter;
import com.td.qianhai.epay.oem.adapter.ViewPagerAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.BankListBean;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class NewPayMainActivity extends BaseActivity implements
        OnPageChangeListener, OnClickListener {

    private ViewPager viewPager;// 页卡内容
    private ViewPagerAdapter pageradapter;
    // private ImageView imageView,go_back;// 动画图片
// private TextView textView1, textView2;
    private ArrayList<View> views;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private View view1, view2;// 各个页卡
    private int screenX, screenY;
    private ListView listview, listview1;
    private LinearLayout lin1, lin2;
    // 界面管理器
    private DisplayMetrics display;
    private LayoutInflater inflater;
    private RadioButton card_1, card_2;
    private int defaulttypeX = 0;
    private int defaulttypeJ = 0;
    private int iscardtype = 0;

    private TextView delete_propty, addcard_tv, delete_propty1, tv_reblance, tv_agreement;

    private EditText regist_blance;

    private LinearLayout add_creditcard, add_creditcard1, lin_savcard;

    private ArrayList<HashMap<String, Object>> mlist;//
    private ArrayList<HashMap<String, Object>> mlist1;//
    private String mobile;

    private CardListAdapter adapter;
    private CardListAdapter adapter1;

    private ChooseDialog chooseDialog;

//private String balace;

    private String clslogno, names, mobiles, cardid;

    private String attStr, sts, idcards = "", idnames = "";

    private String idcardpic;

    private File cardPicFile;

    private String custId;
    private int iscamera = 0;

    private boolean isruns = true;

    private String unbindcard;

//private String tag ;

    private OneButtonDialogWarn warnDialog;


    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main_activity);
        AppContext.getInstance().addActivity(this);
        display = this.getResources().getDisplayMetrics();
        inflater = getLayoutInflater();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenX = metrics.widthPixels;
        screenY = metrics.heightPixels;
        custId = MyCacheUtil.getshared(this).getString("Mobile", "");
        mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
        attStr = MyCacheUtil.getshared(this).getString("MERSTS", "");
        sts = MyCacheUtil.getshared(this).getString("STS", "");
        Intent it = getIntent();

//balace = it.getStringExtra("balance");

//tag = it.getStringExtra("tag");

        initview();
        initchargelist();
        initdata();

        mlist = new ArrayList<HashMap<String, Object>>();
        mlist1 = new ArrayList<HashMap<String, Object>>();
        adapter = new CardListAdapter(NewPayMainActivity.this, mlist, 0);
        adapter1 = new CardListAdapter(NewPayMainActivity.this, mlist1, 0);
        listview.setAdapter(adapter);
        listview1.setAdapter(adapter1);
    }

    private void initdata() {

    }

    private void takePicture(int code) {

        File dir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/DCIM/");// 设置临时文件的存放目录
        if (!dir.exists()) {
            dir.mkdir();
        }
// 系统相机
// Intent intent = new Intent(
// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
// if (code == FRONT_CODE) {
// localTempImgFileName = idcardpic;
//
// Log.e("", "localTempImgFileName1 = = =" + localTempImgFileName);
// } else if (code == SCENS_CODE) {
//
// localTempImgFileName = custpic;
// Log.e("", "localTempImgFileName2 = = =" + localTempImgFileName);
// } else {
// localTempImgFileName = bankpic;
// Log.e("", "localTempImgFileName2 = = =" + localTempImgFileName);
// }
        File f = new File(dir, idcardpic);
        Log.e("fileNam", dir.getAbsolutePath() + idcardpic);
        Uri u = Uri.fromFile(f);
// intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
// intent.setDataAndType(u, "image/*"); // 格式
// intent.putExtra("crop", "true"); // 发送裁剪信号
// intent.putExtra("aspectX", 4); // X方向上的比例
// intent.putExtra("aspectY", 3); // Y方向上的比例
// intent.putExtra("outputX", 800f); // 裁剪区的宽
// intent.putExtra("outputY", 400f); // 裁剪区的高
// intent.putExtra("scale", false); // 是否保留比例
// intent.putExtra(MediaStore.EXTRA_OUTPUT, u); // 将URI指向相应的file:///
// intent.putExtra("return-data", false); // 是否将数据保留在Bitmap中返回
// intent.putExtra("outputFormat",
// Bitmap.CompressFormat.JPEG.toString());
// intent.putExtra("noFaceDetection", true); // no face detection
// intent.putExtra("circleCrop", false); // 圆形裁剪区域
// startActivityForResult(intent, code);

        Intent intent = new Intent(this, CameraActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putString("fileName", idcardpic);
        bundle.putString("tag", code + "");
        intent.putExtras(bundle);
        NewRealNameAuthenticationActivity.iscameras = false;
        startActivityForResult(intent, code);

    }

    class OrderTask1 extends
            AsyncTask<String, Integer, HashMap<String, Object>> {
        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new Builder(NewPayMainActivity.this);
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Window mWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.dimAmount = 0f;
            dialog.setContentView(R.layout.load);
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3],
                    params[4], params[5], params[6], params[7], params[8],
                    params[9], params[10], params[11], params[12], params[13],
                    params[14], params[15], params[16]};
            return NetCommunicate.getPay(HttpUrls.EPAY, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            dialog.dismiss();
            if (result != null) {
                if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {

                    clslogno = result.get("CLSLOGNO").toString();
                    Intent it = new Intent(NewPayMainActivity.this,
                            LastOderAvtivity.class);
                    int imgid = 0;
                    if (cardid.equals("CMBCHINACREDIT")) {
                        imgid = R.drawable.ps_cmb;
                    } else if (cardid.equals("ABCCREDIT")) {
                        imgid = R.drawable.ps_abc;
                    } else if (cardid.equals("BCCBCREDIT")) {
                        imgid = R.drawable.ps_bjb;
                    } else if (cardid.equals("BOCCREDIT")) {
                        imgid = R.drawable.ps_boc;
                    } else if (cardid.equals("CCBCREDIT")) {
                        imgid = R.drawable.ps_ccb;
                    } else if (cardid.equals("EVERBRIGHTCREDIT")) {
                        imgid = R.drawable.ps_cebb;
                    } else if (cardid.equals("CIBCREDIT")) {
                        imgid = R.drawable.ps_cib;
                    } else if (cardid.equals("ECITICCREDIT")) {
                        imgid = R.drawable.ps_citic;
                    } else if (cardid.equals("CMBCCREDIT")) {
                        imgid = R.drawable.ps_cmbc;
                    } else if (cardid.equals("BOCOCREDIT")) {
                        imgid = R.drawable.ps_comm;
                    } else if (cardid.equals("HXBCREDIT")) {
                        imgid = R.drawable.ps_hxb;
                    } else if (cardid.equals("GDBCREDIT")) {
                        imgid = R.drawable.ps_gdb;
                    } else if (cardid.equals("PSBCCREDIT")) {
                        imgid = R.drawable.ps_psbc;
                    } else if (cardid.equals("ICBCCREDIT")) {
                        imgid = R.drawable.ps_icbc;
                    } else if (cardid.equals("PINGANCREDIT")) {
                        imgid = R.drawable.ps_spa;
                    } else if (cardid.equals("SPDBCREDIT")) {
                        imgid = R.drawable.ps_spdb;
                    } else if (cardid.equals("BSBCREDIT")) {
                        imgid = R.drawable.ps_bsb;
                    } else if (cardid.equals("BOSHCREDIT")) {
                        imgid = R.drawable.ps_sh;
                    } else {
                        imgid = R.drawable.ps_unionpay;
                    }

                    // String mobiles = e_pay2.getText().toString();;
                    // String names = e_pay5.getText().toString();
                    it.putExtra("mobile", mobiles);
                    it.putExtra("name", names);
                    it.putExtra("imgid", imgid);
                    it.putExtra("clslogno", clslogno);
                    startActivity(it);
                    finish();
                } else {
                    ToastCustom.showMessage(NewPayMainActivity.this,
                            result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT);
                }
            }
            super.onPostExecute(result);
        }
    }

    class OrderTask2 extends
            AsyncTask<String, Integer, HashMap<String, Object>> {
        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new Builder(NewPayMainActivity.this);
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Window mWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.dimAmount = 0f;
            dialog.setContentView(R.layout.load);
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3],
                    params[4], params[5], params[6], params[7], params[8],
                    params[9], params[10], params[11], params[12], params[13],};
            return NetCommunicate.getPay(HttpUrls.EPAYSAV, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            dialog.dismiss();
            if (result != null) {
                if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {

                    clslogno = result.get("CLSLOGNO").toString();
                    Intent it = new Intent(NewPayMainActivity.this,
                            LastOderAvtivity.class);
                    int imgid = 0;
                    if (cardid.equals("CMBCHINACREDIT")) {
                        imgid = R.drawable.ps_cmb;
                    } else if (cardid.equals("ABCCREDIT")) {
                        imgid = R.drawable.ps_abc;
                    } else if (cardid.equals("BCCBCREDIT")) {
                        imgid = R.drawable.ps_bjb;
                    } else if (cardid.equals("BOCCREDIT")) {
                        imgid = R.drawable.ps_boc;
                    } else if (cardid.equals("CCBCREDIT")) {
                        imgid = R.drawable.ps_ccb;
                    } else if (cardid.equals("EVERBRIGHTCREDIT")) {
                        imgid = R.drawable.ps_cebb;
                    } else if (cardid.equals("CIBCREDIT")) {
                        imgid = R.drawable.ps_cib;
                    } else if (cardid.equals("ECITICCREDIT")) {
                        imgid = R.drawable.ps_citic;
                    } else if (cardid.equals("CMBCCREDIT")) {
                        imgid = R.drawable.ps_cmbc;
                    } else if (cardid.equals("BOCOCREDIT")) {
                        imgid = R.drawable.ps_comm;
                    } else if (cardid.equals("HXBCREDIT")) {
                        imgid = R.drawable.ps_hxb;
                    } else if (cardid.equals("GDBCREDIT")) {
                        imgid = R.drawable.ps_gdb;
                    } else if (cardid.equals("PSBCCREDIT")) {
                        imgid = R.drawable.ps_psbc;
                    } else if (cardid.equals("ICBCCREDIT")) {
                        imgid = R.drawable.ps_icbc;
                    } else if (cardid.equals("PINGANCREDIT")) {
                        imgid = R.drawable.ps_spa;
                    } else if (cardid.equals("SPDBCREDIT")) {
                        imgid = R.drawable.ps_spdb;
                    } else if (cardid.equals("BSBCREDIT")) {
                        imgid = R.drawable.ps_bsb;
                    } else if (cardid.equals("BOSHCREDIT")) {
                        imgid = R.drawable.ps_sh;
                    } else {
                        imgid = R.drawable.ps_unionpay;
                    }

                    // String mobiles = e_pay2.getText().toString();;
                    // String names = e_pay5.getText().toString();
                    it.putExtra("mobile", mobiles);
                    it.putExtra("name", names);
                    it.putExtra("imgid", imgid);
                    it.putExtra("clslogno", clslogno);
                    startActivity(it);
                    finish();
                } else {
                    ToastCustom.showMessage(NewPayMainActivity.this,
                            result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT);
                }
            }
            super.onPostExecute(result);
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
                rateedit(unbindcard);

                break;

            default:
                break;
        }
    }


    private void rateedit(String unbindcard2) {

        UnbindcardTask task = new UnbindcardTask();
        task.execute(HttpUrls.UNBINDCARD + "", mobile, unbindcard);

    }

    private void initadapter() {
        pageradapter = new ViewPagerAdapter(views);

        viewPager.setAdapter(pageradapter);

    }


    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        mlist.clear();
        mlist1.clear();
        initchargelist();
    }

    private void initchargelist() {

        showLoadingDialog("正在查询中...");
        new Thread(run).start();

    }

    private void initchargelist1() {

        showLoadingDialog("正在查询中...");
        new Thread(run1).start();

    }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            String[] values = {String.valueOf(HttpUrls.BANKCARDLIST), mobile, "02"};
            BankListBean entitys = NetCommunicate.getBankList(
                    HttpUrls.BANKCARDLIST, values);

            Message msg = new Message();
            if (entitys != null) {
                list = entitys.list;
                mlist.addAll(list);
                if (mlist.size() <= 0 || mlist == null) {

                    msg.what = 2;
                } else {
                    msg.what = 1;
                    idcards = mlist.get(0).get("CREDCODE").toString();
                    idnames = mlist.get(0).get("CARDNAME").toString();
                }

            } else {
                loadingDialogWhole.dismiss();
                msg.what = 3;
            }
            loadingDialogWhole.dismiss();
            handler.sendMessage(msg);
        }
    };

    Runnable run1 = new Runnable() {

        @Override
        public void run() {
            isruns = false;
            ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String, Object>>();
            String[] values = {String.valueOf(HttpUrls.BANKCARDLIST), mobile, "01"};
            BankListBean entitys = NetCommunicate.getBankList(
                    HttpUrls.BANKCARDLIST, values);

            Message msg = new Message();
            if (entitys != null) {
                list1 = entitys.list;
                mlist1.addAll(list1);
                if (mlist1.size() <= 0 || mlist1 == null) {

                    msg.what = 2;
                } else {
                    msg.what = 1;
                    idcards = mlist1.get(0).get("CREDCODE").toString();
                    idnames = mlist1.get(0).get("CARDNAME").toString();
                }

            } else {
                loadingDialogWhole.dismiss();
                msg.what = 3;
            }
            loadingDialogWhole.dismiss();
            handler1.sendMessage(msg);
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    adapter.notifyDataSetChanged();
//		if(tag!=null&&tag.equals("0")){
//			return;
//		}
                    delete_propty.setVisibility(View.VISIBLE);

                    break;
                case 2:
                    adapter.notifyDataSetChanged();
//		if(tag!=null&&tag.equals("0")){
//			return;
//		}
//		delete_propty.setVisibility(View.GONE);
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "网络异常",
                            Toast.LENGTH_SHORT).show();
                    // ToastCustom.showMessage(MyBankCardAvtivity.this,
                    // "网络异常");
                    break;
                default:
                    break;
            }
        }

        ;
    };


    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    adapter1.notifyDataSetChanged();
//		if(tag!=null&&tag.equals("0")){
//			return;
//		}
                    delete_propty1.setVisibility(View.VISIBLE);

                    break;
                case 2:
                    adapter1.notifyDataSetChanged();
//		if(tag!=null&&tag.equals("0")){
//			return;
//		}
//		 delete_propty1.setVisibility(View.GONE);

                    break;
                case 3:
//		delete_propty.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "网络异常",
                            Toast.LENGTH_SHORT).show();
                    // ToastCustom.showMessage(MyBankCardAvtivity.this,
                    // "网络异常");
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void initview() {


        idcardpic = "701734" + "_" + getStringDateMerge() + "_" + "B.jpg";
//if(tag!=null&&tag.equals("0")){
//	((TextView) findViewById(R.id.tv_title_contre)).setText("预留手机变更");
//}else if(tag!=null&&tag.equals("1")){
//	((TextView) findViewById(R.id.tv_title_contre)).setText("解绑银行卡");
//}else{
        ((TextView) findViewById(R.id.tv_title_contre)).setText("支付中心");
//}
        findViewById(R.id.bt_title_left).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });

        view1 = inflater.inflate(R.layout.banklistview, null);
        view2 = inflater.inflate(R.layout.banklistview, null);

        views = new ArrayList<View>();

        viewPager = (ViewPager) findViewById(R.id.viewpagers);

        viewPager.setOnPageChangeListener(this);

        card_1 = (RadioButton) findViewById(R.id.card_1);
        card_1.setChecked(true);
        card_2 = (RadioButton) findViewById(R.id.card_2);

        card_1.setOnClickListener(new MyOnClickListener(0));
        card_2.setOnClickListener(new MyOnClickListener(1));

        lin1 = (LinearLayout) view1.findViewById(R.id.lin);
        lin2 = (LinearLayout) view2.findViewById(R.id.lin);
        lin_savcard = (LinearLayout) view2.findViewById(R.id.lin_savcard);
        tv_agreement = (TextView) view2.findViewById(R.id.tv_agreement);
        tv_agreement.setText(Html.fromHtml("<u>《钱海财富储蓄卡理财协议》</u>"));
        regist_blance = (EditText) view2.findViewById(R.id.regist_blance);
        tv_reblance = (TextView) view2.findViewById(R.id.tv_reblance);

        views.add(lin1);
        views.add(lin2);

        listview = (ListView) view1.findViewById(R.id.mycardlist);
        listview1 = (ListView) view2.findViewById(R.id.mycardlist);
        delete_propty = (TextView) view1.findViewById(R.id.delete_proptys1);
        delete_propty1 = (TextView) view2.findViewById(R.id.delete_proptys1);
        add_creditcard = (LinearLayout) view1.findViewById(R.id.add_bankcard);
        add_creditcard1 = (LinearLayout) view2.findViewById(R.id.add_bankcard);

        addcard_tv = (TextView) view2.findViewById(R.id.addcard_tv);
        addcard_tv.setText("添加储蓄卡");


        tv_agreement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent();
                intent.setClass(NewPayMainActivity.this, OnlineWeb.class);
                intent.putExtra("urlStr", "http://wechat.qhno1.com/html/regularAgreement.html");
                intent.putExtra("title", "");
                startActivity(intent);
            }
        });

//delete_propty.setVisibility(View.GONE);

        regist_blance.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

                if (s.length() > 0) {
                    String str = regist_blance.getText().toString();
                    double b = Double.parseDouble(str);
                    String r = String.format("%.2f", b * (0.18 / 12));
                    tv_reblance.setText(r + "元");
                } else {
                    tv_reblance.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        add_creditcard.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//		if (MyCacheUtil.getshared(PayMainActivity.this)
//				.getString("Txnsts", "").equals("1")) {
//			takePicture(200);
//			iscamera = 1;
//		} else {
                Intent it = new Intent(NewPayMainActivity.this,
                        NewEpayActivity.class);
                it.putExtra("idcards", idcards);
                it.putExtra("idnames", idnames);
                it.putExtra("cardtype", "0");
                startActivity(it);
//		}
            }
        });

        add_creditcard1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//		if (MyCacheUtil.getshared(PayMainActivity.this)
//				.getString("Txnsts", "").equals("1")) {
//			takePicture(201);
//			iscamera = 2;
//		} else {
                Intent it = new Intent(NewPayMainActivity.this,
                        NewSavpayActivity.class);
//			it.putExtra("balance", balace);
                it.putExtra("idcards", idcards);
                it.putExtra("idnames", idnames);
                it.putExtra("cardtype", "1");
                startActivity(it);
//		}
            }
        });

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long id) {
                // TODO Auto-generated method stub

                String cardtel = null;
                String c_num = null;
                String c_name = null;
                String c_card = null;
                String banknum = null;
                String b_year = null;
                String b_month = null;
                String cvv = null;
                String bakname = null;

                if (mlist.get((int) id).get("CARDTEL") != null) {
                    cardtel = mlist.get((int) id).get("CARDTEL").toString();
                    mobiles = cardtel;
                } else {
                    return;
                }
                if (mlist.get((int) id).get("CREDCODE") != null) {
                    c_num = mlist.get((int) id).get("CREDCODE").toString();
                } else {
                    return;
                }
                if (mlist.get((int) id).get("CARDNAME") != null) {
                    c_name = mlist.get((int) id).get("CARDNAME").toString();
                    names = c_name;
                } else {
                    return;
                }
                if (mlist.get((int) id).get("CARDCODE") != null) {
                    c_card = mlist.get((int) id).get("CARDCODE").toString();
                } else {
                    return;
                }
                if (mlist.get((int) id).get("FRPID") != null) {
                    banknum = mlist.get((int) id).get("FRPID").toString();
                    cardid = banknum;
                } else {
                    return;
                }
                if (mlist.get((int) id).get("EXPIREYEAR") != null) {
                    b_year = mlist.get((int) id).get("EXPIREYEAR").toString();
                } else {
                    return;
                }
                if (mlist.get((int) id).get("EXPIREMONTH") != null) {
                    b_month = mlist.get((int) id).get("EXPIREMONTH").toString();
                } else {
                    return;
                }
                if (mlist.get((int) id).get("CVV") != null) {
                    cvv = mlist.get((int) id).get("CVV").toString();
                } else {
                    return;
                }
                if (mlist.get((int) id).get("ISSUER") != null) {
                    bakname = mlist.get((int) id).get("ISSUER").toString();
                } else {
                    return;
                }


                Intent it = new Intent();
                it.putExtra("FRPID", banknum);
                it.putExtra("CARDCODE", c_card);
                it.putExtra("ISSUER", bakname);
                it.putExtra("MOBILE", cardtel);
                it.putExtra("CRDFLG", "02");
                it.setClass(NewPayMainActivity.this, NewOrderPayActivity.class);
                startActivity(it);
                finish();
//		OrderTask1 otask = new OrderTask1();
//
//		otask.execute(HttpUrls.EPAY + "", mobile, "02", String
//				.valueOf((int) (Double.parseDouble(balace) * 100)),
//				cardtel, "IDCARD", c_num, c_name, c_card, banknum,
//				b_year, b_month, cvv, bakname, "3", "0", "");

            }
        });


        listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long id) {
                // TODO Auto-generated method stub

                unbindcard = mlist.get((int) id).get("CARDCODE").toString();
                SpannableString msp = new SpannableString("您确定解绑该卡吗?");
                showDoubleWarnDialog(msp);
                return true;
            }
        });

        listview1.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long id) {
                // TODO Auto-generated method stub
                unbindcard = mlist1.get((int) id).get("CARDCODE").toString();
                SpannableString msp = new SpannableString("您确定解绑该卡吗");
                showDoubleWarnDialog(msp);
                return true;
            }
        });


        listview1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long id) {
                // TODO Auto-generated method stub
                String cardtel = null;
                String c_num = null;
                String c_name = null;
                String c_card = null;
                String banknum = null;
                String b_year = null;
                String b_month = null;
                String cvv = null;
                String bakname = null;

                if (mlist1.get((int) id).get("CARDTEL") != null) {
                    cardtel = mlist1.get((int) id).get("CARDTEL").toString();
                    mobiles = cardtel;
                } else {
                    return;
                }
                if (mlist1.get((int) id).get("CREDCODE") != null) {
                    c_num = mlist1.get((int) id).get("CREDCODE").toString();
                } else {
                    return;
                }
                if (mlist1.get((int) id).get("CARDNAME") != null) {
                    c_name = mlist1.get((int) id).get("CARDNAME").toString();
                    names = c_name;
                } else {
                    return;
                }
                if (mlist1.get((int) id).get("CARDCODE") != null) {
                    c_card = mlist1.get((int) id).get("CARDCODE").toString();
                } else {
                    return;
                }
                if (mlist1.get((int) id).get("FRPID") != null) {
                    banknum = mlist1.get((int) id).get("FRPID").toString();
                    cardid = banknum;
                } else {
                    return;
                }

                if (mlist1.get((int) id).get("ISSUER") != null) {
                    bakname = mlist1.get((int) id).get("ISSUER").toString();
                } else {
                    return;
                }

                Intent it = new Intent();
                it.putExtra("FRPID", banknum);
                it.putExtra("CARDCODE", c_card);
                it.putExtra("ISSUER", bakname);
                it.putExtra("MOBILE", cardtel);
                it.putExtra("CRDFLG", "01");
                it.setClass(NewPayMainActivity.this, NewOrderPayActivity.class);
                startActivity(it);
                finish();
//		OrderTask2 otask = new OrderTask2();
//
//		otask.execute(HttpUrls.EPAYSAV + "", mobile, "02", String
//				.valueOf((int) (Double.parseDouble(balace) * 100)),
//				cardtel, "IDCARD", c_num, c_name, c_card, bakname,banknum , "2", "0", "");

            }
        });
        initadapter();


    }


    /**
     * 头标点击监听
     */
    private class MyOnClickListener implements OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

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
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量

        Animation animation = new TranslateAnimation(one * currIndex, one
                * arg0, 0, 0);// 显然这个比较简洁，只有一行代码。
        currIndex = arg0;
// animation.setFillAfter(true);// True:图片停在动画结束位置
// animation.setDuration(300);
// imageView.startAnimation(animation);

        if (arg0 == 0) {
            card_1.setChecked(true);
            iscardtype = 0;
            // textView2.setTextColor(Color.GRAY);
            // textView1.setTextColor(Color.BLACK);
        } else {
//	if(tag!=null&&tag.equals("0")){
//		lin_savcard.setVisibility(View.GONE);
//	}else{
//		lin_savcard.setVisibility(View.VISIBLE);
//	}

            if (isruns) {
                initchargelist1();
            }

            card_2.setChecked(true);
            iscardtype = 1;
            // textView1.setTextColor(Color.GRAY);0
            // textView2.setTextColor(Color.BLACK);
        }
    }

    public String getStringDateMerge() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        super.onActivityResult(arg0, arg1, data);

        if (arg1 != RESULT_OK) {
            return;
        } else if (arg0 == 200 || arg0 == 201 && arg1 == RESULT_OK) {

            // CameraActivity2.isdown = false;

//	File dir = 
            getBitmap();

            RealNameAuthTask task = new RealNameAuthTask();

            task.execute(HttpUrls.UPLODPIC + "", idcardpic);
        }
    }

    private File getBitmap() {
        File dir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/DCIM/" + idcardpic);// 设置存放目录
        Log.e("", "      = = = = = = =dir = = = = =" + dir);
        Log.e("", "      = = = = = = =filenames = = = = =" + idcardpic);
// File f = new File(dir.getAbsoluteFile(), localTempImgFileName);
        if (dir.isFile()) {
            Log.e("", "有文件");
            Bitmap bitmap = getimage(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/DCIM/" + idcardpic);
            saveBitmap2file(bitmap, idcardpic);
        } else {
            Log.e("", "没有文件");
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕
        opts.inSampleSize = 4;
/* 下面两个字段需要组合使用 */
// opts.inJustDecodeBounds = false;
// opts.inPurgeable = true;
// opts.inInputShareable = true;
        Bitmap cbitmap = BitmapFactory.decodeFile(dir.getAbsolutePath());

        cardPicFile = dir;
        return dir;
    }

    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 600f;// 这里设置高度为800f
        float ww = 400f;// 这里设置宽度为480f
// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = 4;// 设置缩放比例
// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inPurgeable = true;
        try {
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        } catch (Exception e) {
            // TODO: handle exception
            newOpts.inSampleSize = 1;
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        }

// return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        return bitmap;
    }

    private static boolean saveBitmap2file(Bitmap bmp, String filename) {
        CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + "/DCIM/"
                    + filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            default:
                break;
        }
    }

    /**
     * 充值传银行卡
     */
    class RealNameAuthTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog("正在上传照片中。。。");
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1]};
            File[] files = {cardPicFile};
            return NetCommunicate.getUpload(HttpUrls.UPLODPIC,
                    // 198110,
                    values, files);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            loadingDialogWhole.dismiss();
            if (result != null) {
                if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
                    Toast.makeText(getApplicationContext(),
                            result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT).show();
                    // ToastCustom.showMessage(
                    // MyBankCardAvtivity.this,
                    // result.get(Entity.RSPMSG).toString());
                    Intent it = new Intent();
                    if (iscamera == 1) {
                        it.setClass(NewPayMainActivity.this, EpayActivity.class);
                    } else if (iscamera == 2) {
                        it.setClass(NewPayMainActivity.this, SavpayActivity.class);
                    } else {
                        it.setClass(NewPayMainActivity.this, EpayActivity.class);
                    }
                    if (result.get("IMGPATH") != null) {
                        it.putExtra("url", result.get("IMGPATH").toString());
                    }
                    startActivity(it);
                    finish();

                } else {
                    Intent it = new Intent();
                    if (iscamera == 1) {
                        it.setClass(NewPayMainActivity.this, EpayActivity.class);
                    } else if (iscamera == 2) {
                        it.setClass(NewPayMainActivity.this, SavpayActivity.class);
                    } else {
                        it.setClass(NewPayMainActivity.this, EpayActivity.class);
                    }
                    if (result.get("IMGPATH") != null) {
                        it.putExtra("url", result.get("IMGPATH").toString());
                    }
                    startActivity(it);
                    finish();
                }
            }
            super.onPostExecute(result);
        }
    }

    class UnbindcardTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog("正在处理中。。。");
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2]};
            return NetCommunicate.getAgentMidatc(HttpUrls.UNBINDCARD,
                    values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            loadingDialogWhole.dismiss();
            if (result != null) {
                if (Entity.STATE_OK
                        .equals(result.get(Entity.RSPCOD).toString())) {
                    warnDialog = new OneButtonDialogWarn(NewPayMainActivity.this,
                            R.style.CustomDialog, "提示",
                            result.get(Entity.RSPMSG).toString(), "确定",
                            new OnMyDialogClickListener() {
                                @Override
                                public void onClick(View v) {
                                    warnDialog.dismiss();
                                    finish();
                                }
                            });
                    warnDialog.setCancelable(false);
                    warnDialog.setCanceledOnTouchOutside(false);
                    warnDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT).show();
                    // ToastCustom.showMessage(AgentListActivity.this,
                    // result.get(Entity.RSPMSG).toString());
                }
            }
            super.onPostExecute(result);
        }
    }

}
