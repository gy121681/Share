package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.entity.response.BankInfoResponse;
import com.share.app.entity.response.Constans;
import com.share.app.entity.response.MsgResponse;
import com.share.app.entity.response.SalesmanInfoResponse;
import com.share.app.event.SalesmanInfoRefreshEvent;
import com.share.app.model.SalesmanInfoModel;
import com.share.app.network.CallbackObject;
import com.share.app.network.Request;
import com.share.app.utils.BankLogoUtils;
import com.share.app.utils.ProgressDialogUtil;
import com.share.app.utils.T;
import com.share.app.utils.WaringDialogUtils;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.CityEntity;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.ProvinceEntity;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.utils.DateUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 银行卡变更申请
 *
 * @author liangge
 */
public class RequestCardInfoChangeActivity extends BaseActivity {

    /**
     * 用户姓名、银行卡号
     */
    private EditText uName, uBankCardNo;
    /**
     * 银行名称、银行开户省份、银行开户城市、银行开户支付
     */
    private TextView tvBankName, tvBankProvince, tvBankCity, tvBankBranch, et_update_bank;
    /**
     * 提交按钮
     */
    private LinearLayout requestButton;
    /**
     * 输入法开关判断
     */
    private boolean isOpen;
    /**
     * 磁卡数据、二磁、三磁、手机号码、卡种、卡类型
     */
    private String cardData, track2, track3, phonenumber, dcflag, cardType;
    /**
     * 终端号、、开户行省份ID、开户行城市ID、开户行支行ID、开户支行名称、银行卡号、银行名称、银行开户省份名、银行开户城市名
     */
    private String psamId, issno, bankProvinceid, bankCityid, bankBranchid,
            bankBranchName, actNo, custId, bankname, bankProvincename, acname,
            bankCityname;
    /**
     * 后台list数据集合
     */
    private ArrayList<HashMap<String, Object>> list;
    /**
     * 返回、title中间内容
     */
    private TextView bt_Back, tv_title, et_update_bank1;
    private ImageView im_bank, im_bank1;

    private String mRealName;
    private String mPersonNO;
    private String mBankName;
    private String mBankCardNO;

    private EditText mEdtMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_bank_card_info_apply);
        AppContext.getInstance().addActivity(this);
        phonenumber = MyCacheUtil.getshared(this).getString("Mobile", "");
        custId = MyCacheUtil.getshared(this).getString("Mobile", "");
        SalesmanInfoResponse salesmanInfo = SalesmanInfoModel.getInstance().getSalesmanInfo();
        if (salesmanInfo != null) {
            mRealName = salesmanInfo.getReal_name();
            mPersonNO = salesmanInfo.getPerson_no();
            mBankName = salesmanInfo.getBank_name();
            mBankCardNO = salesmanInfo.getBank_no();
        }
//		phonenumber = ((AppContext) getApplication()).getCustId();
//		custId = ((AppContext) this.getApplication()).getCustId();
        psamId = AppContext.getInstance().getPsamId();
//		BussinessInfoTask task = new BussinessInfoTask();
//		task.execute(HttpUrls.BUSSINESS_INFO + "", custId, "4","0");
//		GetBankProvinceTask task2 = new GetBankProvinceTask();
//		task2.execute(HttpUrls.QUERY_BANK_INFO + "", custId);
        initView();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2:
                    // 刷卡获取银行名称
                    cardData = (String) msg.obj;
                    // etCardNo.setText(getCardNo(cardData.split("d")[0]));
                    uBankCardNo.setText(cardData.split("d")[0]);
                    actNo = uBankCardNo.getText().toString();
                    uBankCardNo.setEnabled(false);
                    tvBankName.setEnabled(false);
                    track2 = cardData.substring(0, 48).replace("f", "")
                            .replace("d", "D");
                    track3 = cardData.substring(48, cardData.length())
                            .replace("f", "").replace("d", "D");
//				BankProvinceTask task = new BankProvinceTask();
//				Log.e("", "运行了   = = == ");
//				task.execute(HttpUrls.QUERY_BANK_NAME + "", "", track2,
//						track3);
                    break;
                case 6:
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    isOpen = imm.isActive();
                    if (isOpen) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    uBankCardNo.clearFocus();
                    list = null;
                    cardData = (String) msg.obj;
                    // etCardNo.setText(getCardNo(cardData.split("d")[0]));
                    uBankCardNo.setText(cardData.split("d")[0]);
//				uBankCardNo.setEnabled(true);
                    // char[] charBin = cardData.toCharArray();
                    // String bankName = BankInfo.getNameOfBank(charBin, 0);

                    cardData = cardData
                            + "d49121202369991430fffffffffff996222024000079840084d1561560000000000001003236999010000049120d000000000000d000000000000d00000000fffffffffffffff";

                    track2 = cardData.substring(0, 48).replace("f", "")
                            .replace("d", "D");
                    track3 = cardData.substring(48, cardData.length())
                            .replace("f", "").replace("d", "D");
                    System.out.println("cardData:" + cardData);
                    System.out.println("track2:" + track2);
                    System.out.println("track3:" + track3);
                    if ((String) msg.obj != null) {
                        BankProvinceTask task2 = new BankProvinceTask();
                        task2.execute(HttpUrls.QUERY_BANK_NAME + "", "psamIdIsNull",
                                track2, track3);
                    }
                    break;
            }
        }

        ;
    };

    /**
     * 初始化View
     */
    private void initView() {
        et_update_bank1 = (TextView) findViewById(R.id.et_update_bank1);
        im_bank1 = (ImageView) findViewById(R.id.im_bank1);
        uName = (EditText) findViewById(R.id.et_update_bank_apply_name);
        uBankCardNo = (EditText) findViewById(R.id.et_update_bank_apply_card_no);
        tvBankName = (TextView) findViewById(R.id.tv_update_bank_apply_bank_name);
        et_update_bank = (TextView) findViewById(R.id.et_update_bank);
        tvBankProvince = (TextView) findViewById(R.id.tv_update_bank_apply_bank_province);
        tvBankCity = (TextView) findViewById(R.id.tv_update_bank_apply_bank_city);
        tvBankBranch = (TextView) findViewById(R.id.tv_update_bank_apply_bank_branch);
        requestButton = (LinearLayout) findViewById(R.id.btn_update_bank_apply_submit);
        mEdtMobile = (EditText) findViewById(R.id.edt_mobile);
        tv_title = (TextView) findViewById(R.id.tv_title_contre);
        im_bank = (ImageView) findViewById(R.id.im_bank);
        tv_title.setText("银行卡变更申请");
        bt_Back = (TextView) findViewById(R.id.bt_title_left);
        bt_Back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        et_update_bank.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent it = new Intent(RequestCardInfoChangeActivity.this, SelectBankActivity.class);
                it.putExtra("tag", "3");
                startActivityForResult(it, 2);
            }
        });

//		uName.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				if (s.length() > 0) {
//					uName.setText("");
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});

        /**
         * 点击银行卡把其他的信息先清除掉
         */
        uBankCardNo.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                if (hasFocus == false) {
                    String cardNo = uBankCardNo.getText().toString();
                    if (cardNo != null && !cardNo.trim().equals("")) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        isOpen = imm.isActive();
                        if (isOpen) {
                            imm.toggleSoftInput(
                                    InputMethodManager.SHOW_IMPLICIT,
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        queryBankInfo(cardNo);
//                        Message message = Message.obtain(mHandler);
//                        message.what = 6;
//                        message.obj = uBankCardNo.getText().toString();
//                        message.sendToTarget();
                    }
                }
//				} else {
//					bankBranchName = "";
//					tvBankProvince.setHint("选择省份");
//					tvBankCity.setHint("选择城市");
//					tvBankBranch.setHint("选择支行");
//					list = null;
//					bankProvinceid = null;
//					bankCityid = null;
//					bankBranchid = null;
//					tvBankName.setText("");
//				}
            }
        });

        tvBankName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                uBankCardNo.clearFocus();
            }
        });

        /**
         * 姓名判断
         */
//		uName.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stubet_update_bank_apply_card_no
//				if (!hasFocus) {
//					String name = uName.getText().toString();
//					if (name == null || (name != null && name.equals(""))) {
////						uName.setError("请输入用户姓名");
////					} else if (!ChineseUtil.checkNameChese(name)) {
////						uName.setError("用户姓名必须全为中文");
//					}
//				} else {
//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					isOpen = imm.isActive();
//					if (isOpen) {
//						Log.v("resule", "进入到了姓名判断的焦点");
//						imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
//					}
//				}
//			}
//		});

        tvBankProvince.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (DateUtil.isFastDoubleClick()) {
                    return;
                } else {
                    if (tvBankName.getText().toString() == null || tvBankName.getText().toString().equals("") || tvBankName.getText().toString().equals("未知")) {
                        ToastCustom.showMessage(
                                RequestCardInfoChangeActivity.this, "请先填写正确的银行卡号",
                                Toast.LENGTH_SHORT);
                        return;
                    }
                    if (tvBankName.getText().toString().equals("")) {
                        BankProvinceTask task = new BankProvinceTask();
                        task.execute(HttpUrls.QUERY_BANK_NAME + "", psamId,
                                track2, track3);
                    }
                    bankCityid = null;
                    tvBankCity.setHint("选择城市");
                    tvBankBranch.setHint("选择支行");
                    // 查询银行卡名称
//					getBankInfo(list);
                    // 查询银行卡名称
                    // getBankInfo(list);
                    Intent intent = new Intent(RequestCardInfoChangeActivity.this,
                            SelectListNameActivity.class);
                    Bundle bundle = new Bundle();
                    ArrayList carrier = new ArrayList();
                    carrier.add(list);
                    bundle.putString("titleContent", "银行卡开户省份");
                    bundle.putParcelableArrayList("carrier", carrier);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 9);
                    overridePendingTransition(0, 0);
                }
            }
        });
        tvBankCity.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (DateUtil.isFastDoubleClick()) {
                    return;
                } else {
                    if (bankProvinceid == null
                            || (bankProvinceid != null && bankProvinceid
                            .equals(""))) {
                        ToastCustom.showMessage(
                                RequestCardInfoChangeActivity.this,
                                "请先选择开户行省份", Toast.LENGTH_SHORT);
                        return;
                    }
                    tvBankBranch.setText("选择支行");
                    BankCityTask task = new BankCityTask();
                    task.execute(HttpUrls.QUERY_BANK_CITY + "", "1", "200",
                            bankProvinceid);
                }
            }
        });
        tvBankBranch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (DateUtil.isFastDoubleClick()) {
                    return;
                } else {
                    if (bankCityid == null
                            || (bankCityid != null && bankCityid.equals(""))) {
                        ToastCustom.showMessage(
                                RequestCardInfoChangeActivity.this,
                                "请先选择开户行城市", Toast.LENGTH_SHORT);
                        return;
                    }
                    BankBranchTask task = new BankBranchTask();
                    task.execute(HttpUrls.QUERY_BANK_BRANCH + "", "1", "200",
                            issno, bankProvinceid, bankCityid);
                }
            }
        });

        requestButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (DateUtil.isFastDoubleClick()) {
                    return;
                } else {
                    judgmentInfo();
                }
            }
        });
        showBindBankInfo();
    }

    private void queryBankInfo(String cardInfo) {
        Request.getServiceQueryBankCardInfo(cardInfo, MyCacheUtil.getshared(this).getString(Constans.Login.USERID, ""),
                new CallbackObject<BankInfoResponse>() {
                    @Override
                    public void onFailure(String msg) {

                    }

                    @Override
                    public void onSuccess(BankInfoResponse data) {
                        // TODO: 2017/7/28 显示银行卡信息。
                    }

                    @Override
                    public void onNetError(int code, String msg) {

                    }
                });
    }

    /**
     * 显示绑定银行卡信息
     */
    private void showBindBankInfo() {
        uName.setText(mRealName);
        uName.setEnabled(false);
        et_update_bank1.setText("尾号 " + mBankCardNO.substring(mBankCardNO.length() - 4));
        im_bank1.setImageResource(BankLogoUtils.getLogo(mBankName));
        im_bank1.setVisibility(View.VISIBLE);
    }

    /**
     * 判断输入的信息完整性
     */
    private void judgmentInfo() {
        String name = null;
        name = uName.getText().toString();

//		if (name == null || (name != null && name.equals(""))) {
//			name = acname;
//			if(acname.equals("")){
//				uName.setEnabled(true) ;
//			}

//			if(acname == null || (acname != null && name.equals(""))){
//				uName.setText("请输入用户姓名");
//				return;
//			} else if (!ChineseUtil.checkNameChese(name)) {
//				uName.setText("用户姓名必须全为中文");
//				return;
//			}
//		}else{/

        String cardNo = uBankCardNo.getText().toString();
        if (cardNo == null || (cardNo != null && cardNo.equals(""))) {
            ToastCustom.showMessage(this, "请输入银行卡号", Toast.LENGTH_SHORT);
            return;
        } else if (!checkBankCard(cardNo)) {
            ToastCustom.showMessage(this, "银行卡号不正确", Toast.LENGTH_SHORT);
            return;
        }

        String mobile = mEdtMobile.getText().toString().trim();
        if (!isMobileNO(mobile)) {
            T.showShort(this, "请输入有效的手机号");
            return;
        }
        requestBankInfo();

        // String bankCode = tvBankBranch.getText().toString();
//		if (bankBranchid == null
//				|| (bankBranchid != null && bankBranchid.equals(""))) {
//			bankBranchid = "";
////			ToastCustom.showMessage(this, "请选择开户银行支行", Toast.LENGTH_SHORT);
////			return;
//		}

//		String name2 = uName.getText().toString();
//		String cardNo2 = uBankCardNo.getText().toString();
//		String bankname2 = tvBankName.getText().toString();
//		String bankProvincename2 = tvBankProvince.getText().toString();
//		String bankCityname2 = tvBankCity.getText().toString();
//		String bankBranchName2 = tvBankBranch.getText().toString();

//		if (name2 != null && name2.equals(name) && cardNo2 != null
//				&& cardNo2.equals(acname)
//				&& bankProvincename2 != null
//				&& bankProvincename2.equals(bankProvincename)
//				&& bankCityname2 != null && bankCityname2.equals(bankCityname)
//				&& bankBranchName2 != null
//				&& bankBranchName2.equals(bankBranchName)
//				) {
//			Toast.makeText(getApplicationContext(), "您要修改的信息和原来的信息一致，请核对后修改!", Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}

//		if(bankname2==null||bankname2.equals("")){
//			Toast.makeText(this, "银行卡信息不完整", Toast.LENGTH_SHORT)
//			.show();
//			return;
//		}
//		if(bankProvincename2==null||bankProvincename2.equals("")){
//			Toast.makeText(this, "银行卡信息不完整", Toast.LENGTH_SHORT)
//			.show();
//			return;
//		}
//		if(bankCityname2==null||bankCityname2.equals("")){
//			Toast.makeText(this, "银行卡信息不完整", Toast.LENGTH_SHORT)
//			.show();
//			return;
//		}
//		BankCardInfoChangeTask task = new BankCardInfoChangeTask();
//
//		task.execute(HttpUrls.APPLY_BANKCARD_INFO_CHANGES + "", phonenumber,
//				bankname, "", "", "", name,
//				cardNo);

    }

    /**
     * 使用新api请求银行卡信息
     */
    private void requestBankInfo() {
        final String cardNo = uBankCardNo.getText().toString().trim();
        ProgressDialogUtil.showProgressDlg(this, "");
        Request.getServiceQueryBankCardInfo(cardNo, MyCacheUtil.getshared(this).getString(Constans.Login.USERID, ""),
                new CallbackObject<BankInfoResponse>() {
                    @Override
                    public void onSuccess(BankInfoResponse data) {
                        if (data != null) {
                            if (data.result.nature.contains("借记") || data.result.nature.contains("储蓄")) {
                                changeBankCard(cardNo, data.result.bank);
                            } else {
                                WaringDialogUtils.showWaringDialog(getActivity(), "请使用借记卡或者储蓄卡", null);
                                ProgressDialogUtil.dismissProgressDlg();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(getActivity(), msg, null);
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(getActivity(), "网络异常", null);
                    }
                });
    }

    /**
     * 变更银行卡
     *
     * @param bankName
     */
    private void changeBankCard(String cardNo, String bankName) {
        String mobile = mEdtMobile.getText().toString().trim();
        Request.getSalesmanChangeUserBankCard(MyCacheUtil.getshared(this).getString(Constans.Login.USERID, ""),
                cardNo, bankName, mRealName, mPersonNO, mobile,
                new CallbackObject<MsgResponse>() {
                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(getActivity(), msg, null);
                    }

                    @Override
                    public void onSuccess(MsgResponse data) {
                        ProgressDialogUtil.dismissProgressDlg();
                        EventBus.getDefault().post(new SalesmanInfoRefreshEvent());
                        toast("操作成功");
                        finish();
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(getActivity(), "网络异常", null);
                    }
                });

    }


//	private void getBankInfo(ArrayList<HashMap<String, Object>> list) {
//
//		ProvinceDialog dialog = new ProvinceDialog(1,
//				RequestCardInfoChangeActivity.this, list,
//				new OnProvinceDialogListener() {
//
//					@Override
//					public void back(HashMap<String, Object> map) {
//						bankProvinceid = map.get("PROVID").toString();
//						tvBankProvince.setText(map.get("PROVNAM").toString());
//
//					}
//				});
//		dialog.setTitle("银行卡开户省份");
//		dialog.show();
//
//	}

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    private char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    private boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

//	private void isIsson(String isson) {
//		// 判断邮政不可用
//
//		AlertDialog.Builder builder = new Builder(
//				RequestCardInfoChangeActivity.this);
//		builder.setMessage("暂不支持邮政卡！");
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}

    /**
     * 银行卡信息变更申请
     *
     * @author liang
     */
    class BankCardInfoChangeTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showLoadingDialog("正在提交资料...");
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            // TODO Auto-generated method stub
            String[] values = {params[0], params[1], params[2], params[3],
                    params[4], params[5], params[6], params[7]};
            return NetCommunicate.get(HttpUrls.APPLY_BANKCARD_INFO_CHANGES,
                    values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            loadingDialogWhole.dismiss();
            if (result != null) {

                // 提交成功
                if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
                    ToastCustom.showMessage(RequestCardInfoChangeActivity.this, "申请已提交,请上传资料等待后台审核",
                            Toast.LENGTH_SHORT);
                    Intent intent = new Intent(
                            RequestCardInfoChangeActivity.this,
                            NewRealNameAuthenticationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("intentObj",
                            "RequestCardInfoChangeActivity");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    ToastCustom.showMessage(RequestCardInfoChangeActivity.this, result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT);
                }

            } else {

                showSingleWarnDialog("提交失败");
            }
            super.onPostExecute(result);
        }

    }

    /**
     * 银行卡开户行查询
     *
     * @author Administrator
     */
    class BankProvinceTask extends AsyncTask<String, Integer, ProvinceEntity> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog("正在查询中...");
            super.onPreExecute();
        }

        @Override
        protected ProvinceEntity doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3]};
            return NetCommunicate.getQueryProvince(HttpUrls.QUERY_BANK_NAME,
                    values);
        }

        @Override
        protected void onPostExecute(ProvinceEntity result) {
            loadingDialogWhole.dismiss();
            if (result != null) {
                if (Entity.STATE_OK.equals(result.getRspcod())) {
                    issno = result.getIssno();
                    dcflag = result.getDcflag().toString();
                    System.out.println("dcflag" + dcflag);
                    if (dcflag != null && dcflag != "") {
                        if (dcflag.equals("01")) {
                            cardType = "(借记卡)";
                        } else if (dcflag.equals("02")) {
                            cardType = "(信用卡)";
                            uBankCardNo.setText("");
                            tvBankName.setText("");
                            uBankCardNo.setEnabled(false);
                            list = null;
                            bankProvinceid = null;
                            bankCityid = null;
                            bankBranchName = null;
                            showSingleWarnDialog("暂不支持信用卡注册！");
                            return;
                        } else {
                            cardType = "";
                        }
                    }

                    if (result.getIssnam() == null || result.getIssnam().equals("null")) {
                        bankname = "未知";
                        tvBankName
                                .setText(bankname + cardType);
                    } else {
                        bankname = result.getIssnam().toString();
                        tvBankName
                                .setText(bankname + cardType);
                    }

                    tvBankProvince.setHint("选择省份");
                    tvBankCity.setHint("选择城市");
                    tvBankBranch.setHint("选择支行");
                    Log.v("result", "成功获取银行信息");
                    Log.v("result", "bankname:" + bankname);
                    if (bankname == null || bankname == "") {
                        tvBankName
                                .setText("未知");
                        ToastCustom.showMessage(RequestCardInfoChangeActivity.this, "银行卡信息获取失败 ，请检卡号查输入是否正确", Toast.LENGTH_SHORT);

                        list = null;
                        bankProvinceid = null;
                        bankCityid = null;
                        Log.v("result", "list==null");
                    }
//					else if (result.getIssno().equals("01000000")) {
//						uBankCardNo.setText("");
//						tvBankName.setText("");
//						uBankCardNo.setEnabled(false);
//						list = null;
//						bankProvinceid = null;
//						bankCityid = null;
//						bankname = null;
//						isIsson(result.getIssno());
//					} 
                    else {
                        list = result.list;

                        if (result.getIssnam().toString() == null || result.getIssnam().toString().equals("null")) {
                            tvBankName.setText("未知");

                        } else {
                            tvBankName.setText(result.getIssnam().toString()
                                    + cardType);
                        }

                    }
                } else {
                    showSingleWarnDialog(result.getRspmsg().toString());
                }
            }
            super.onPostExecute(result);
        }

    }

    /**
     * 获取城市Task
     *
     * @author liangge
     */
    class BankCityTask extends AsyncTask<String, Integer, CityEntity> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog("正在查询中...");
            super.onPreExecute();
        }

        @Override
        protected CityEntity doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3]};
            return NetCommunicate
                    .getQueryCity(HttpUrls.QUERY_BANK_CITY, values);
        }

        @Override
        protected void onPostExecute(CityEntity result) {
            loadingDialogWhole.dismiss();
            if (result != null) {

                if (Entity.STATE_OK.equals(result.getRspcod())) {
//					ProvinceDialog dialog = new ProvinceDialog(2,
//							RequestCardInfoChangeActivity.this, result.list,
//							new OnProvinceDialogListener() {
//
//								@Override
//								public void back(HashMap<String, Object> map) {
//									bankCityid = map.get("CITYID").toString();
//									tvBankCity.setText(map.get("CITYNAM")
//											.toString());
//
//								}
//							});
//					dialog.setTitle("银行卡开户城市");
//					dialog.show();

                    Intent intent = new Intent(RequestCardInfoChangeActivity.this,
                            SelectListNameActivity.class);
                    Bundle bundle = new Bundle();
                    ArrayList carrier = new ArrayList();
                    carrier.add(result.list);
                    bundle.putString("titleContent", "银行卡开户城市");
                    bundle.putParcelableArrayList("carrier", carrier);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 8);
                    overridePendingTransition(0, 0);
                }
            }
            super.onPostExecute(result);
        }

    }

    /**
     * 获取支行Task
     *
     * @author liangge
     */
    class BankBranchTask extends AsyncTask<String, Integer, CityEntity> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog("正在查询中...");
            super.onPreExecute();
        }

        @Override
        protected CityEntity doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3],
                    params[4], params[5]};
            return NetCommunicate.getQueryCity(HttpUrls.QUERY_BANK_BRANCH,
                    values);
        }

        @Override
        protected void onPostExecute(CityEntity result) {
            loadingDialogWhole.dismiss();
            if (result != null) {

                if (Entity.STATE_OK.equals(result.getRspcod())) {
                    if (result.list.size() == 0) {
                        ToastCustom.showMessage(
                                RequestCardInfoChangeActivity.this,
                                "没有该城市支行信息", Toast.LENGTH_SHORT);
                        return;
                    }
                    Intent intent = new Intent(RequestCardInfoChangeActivity.this,
                            SelectListNameActivity.class);
                    Bundle bundle = new Bundle();
                    ArrayList carrier = new ArrayList();
                    carrier.add(result.list);
                    bundle.putString("titleContent", "银行卡开户支行");
                    bundle.putParcelableArrayList("carrier", carrier);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 7);
                    overridePendingTransition(0, 0);
                }
            }
            super.onPostExecute(result);
        }
    }

    // @Override
    // protected void onPause() {
    // flag = false;
    // swiper.stopGetCardData();
    // super.onPause();
    // }

    /**
     * 查看商户资料Task
     *
     * @author Administrator
     */
    class BussinessInfoTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog("正在加载中...");
            super.onPreExecute();
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

                        acname = result.get("ACTNAM").toString();
                        switch (acname.length()) {
                            case 2:
                                String setnames = acname.substring(0, 1);
                                uName.setHint(setnames + "*");
                                break;
                            case 3:
                                String setnames1 = acname.substring(0, 1);
                                String getnames1 = acname.substring(acname.length() - 1);
                                uName.setHint(setnames1 + "*" + getnames1);
                                break;
                            case 4:
                                String setnames2 = acname.substring(0, 1);
                                String getnames2 = acname.substring(acname.length() - 1);
                                uName.setHint(setnames2 + "**" + getnames2);
                                break;
                            case 5:
                                String setnames3 = acname.substring(0, 1);
                                String getnames3 = acname.substring(acname.length() - 1);
                                uName.setHint(setnames3 + "***" + getnames3);
                                break;
                            case 6:
                                String setnames4 = acname.substring(0, 1);
                                String getnames4 = acname.substring(acname.length() - 1);
                                uName.setHint(setnames4 + "****" + getnames4);
                                break;
                            case 7:
                                String setnames5 = acname.substring(0, 1);
                                String getnames5 = acname.substring(acname.length() - 1);
                                uName.setHint(setnames5 + "*****" + getnames5);
                                break;
                            case 8:
                                String setnames6 = acname.substring(0, 1);
                                String getnames6 = acname.substring(acname.length() - 1);
                                uName.setHint(setnames6 + "******" + getnames6);
                                break;
                            case 9:
                                String setnames7 = acname.substring(0, 1);
                                String getnames7 = acname.substring(acname.length() - 1);
                                uName.setHint(setnames7 + "*******" + getnames7);
                                break;
                            case 10:
                                String setnames8 = acname.substring(0, 1);
                                String getnames8 = acname.substring(acname.length() - 1);
                                uName.setHint(setnames8 + "********" + getnames8);
                                break;
                            default:
                                uName.setHint(acname);
                                break;
                        }
                    }
                    if (result.get("ACTNO") != null) {
                        String str = result.get("ACTNO").toString();
                        et_update_bank1.setText("尾号 " + str.substring(str.length() - 4));
                    }
                    // tvBankName.setText(result.get("OPNBNK").toString());

                    if (result.get("OPNBNK") != null) {
                        im_bank1.setVisibility(View.VISIBLE);
                        String banckname = result.get("OPNBNK").toString();
                        if (banckname.substring(0, 2).equals("招商")) {
                            im_bank1.setImageResource(R.drawable.ps_cmb);
                        } else if (banckname.substring(0, 2).equals("农业")) {
                            im_bank1.setImageResource(R.drawable.ps_abc);
                        } else if (banckname.substring(0, 2).equals("农行")) {
                            im_bank1.setImageResource(R.drawable.ps_abc);
                        } else if (banckname.substring(0, 2).equals("北京")) {
                            im_bank1.setImageResource(R.drawable.ps_bjb);
                        } else if (banckname.substring(0, 2).equals("中国")) {
                            if (banckname.substring(0, 4).equals("中国建设")) {
                                im_bank1.setImageResource(R.drawable.ps_ccb);
                            } else {
                                im_bank1.setImageResource(R.drawable.ps_boc);
                            }
                        } else if (banckname.substring(0, 2).equals("建设")) {
                            im_bank1.setImageResource(R.drawable.ps_ccb);
                        } else if (banckname.substring(0, 2).equals("光大")) {
                            im_bank1.setImageResource(R.drawable.ps_cebb);
                        } else if (banckname.substring(0, 2).equals("兴业")) {
                            im_bank1.setImageResource(R.drawable.ps_cib);
                        } else if (banckname.substring(0, 2).equals("中信")) {
                            im_bank1.setImageResource(R.drawable.ps_citic);
                        } else if (banckname.substring(0, 2).equals("民生")) {
                            im_bank1.setImageResource(R.drawable.ps_cmbc);
                        } else if (banckname.substring(0, 2).equals("交通")) {
                            im_bank1.setImageResource(R.drawable.ps_comm);
                        } else if (banckname.substring(0, 2).equals("华夏")) {
                            im_bank1.setImageResource(R.drawable.ps_hxb);
                        } else if (banckname.substring(0, 4).equals("广东发展")) {
                            im_bank1.setImageResource(R.drawable.ps_gdb);
                        } else if (banckname.substring(0, 2).equals("广发")) {
                            im_bank1.setImageResource(R.drawable.ps_gdb);
                        } else if (banckname.substring(0, 2).equals("邮政")) {
                            im_bank1.setImageResource(R.drawable.ps_psbc);
                        } else if (banckname.substring(0, 2).equals("邮储")) {
                            im_bank1.setImageResource(R.drawable.ps_psbc);
                        } else if (banckname.substring(0, 2).equals("工商")) {
                            im_bank1.setImageResource(R.drawable.ps_icbc);
                        } else if (banckname.substring(0, 2).equals("平安")) {
                            im_bank1.setImageResource(R.drawable.ps_spa);
                        } else if (banckname.substring(0, 2).equals("浦东")) {
                            im_bank1.setImageResource(R.drawable.ps_spdb);
                        } else if (banckname.substring(0, 2).equals("工商")) {
                            im_bank1.setImageResource(R.drawable.ps_icbc);
                        } else {
                            im_bank1.setImageResource(R.drawable.ps_unionpay);
                        }
                    } else {
                    }

                } else {
                    showSingleWarnDialog(result.get(Entity.RSPMSG).toString());
                }
            }
            super.onPostExecute(result);
        }

    }

    /**
     * 获取省份Task
     *
     * @author liangge
     */
    class GetBankProvinceTask extends
            AsyncTask<String, Integer, ProvinceEntity> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ProvinceEntity doInBackground(String... params) {
            String[] values = {params[0], params[1]};
            return NetCommunicate.getQueryProvince(HttpUrls.QUERY_BANK_INFO,
                    values);
        }

        @Override
        protected void onPostExecute(ProvinceEntity result) {
            loadingDialogWhole.dismiss();
            if (result != null) {

                if (Entity.STATE_OK.equals(result.getRspcod())) {
                    // 获取id标识
                    issno = result.getIssno();
                    bankProvinceid = result.getProvid();
                    bankCityid = result.getCityid();
                    bankBranchid = result.getBkno();
                    // 获取中文名
                    bankname = result.getIssnam();
//					tvBankName.setText(result.getIssnam());
                    // 获取省中文名
//					tvBankProvince.setText(result.getPronam());
                    bankProvincename = result.getPronam();
                    // 获取城市中文名
//					tvBankCity.setText(result.getCitynam());
                    bankCityname = result.getCitynam();
                    // 获取支行中文名
//					tvBankBranch.setText(result.getBenelx());
                    bankBranchName = result.getBenelx();
                    list = result.list;
                }
            }
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            im_bank.setVisibility(View.VISIBLE);
            String banks = data.getExtras().getString("result");
            et_update_bank.setText(banks);
            String imgurl = data.getExtras().getString("img");
            im_bank.setVisibility(View.VISIBLE);
            FinalBitmap.create(RequestCardInfoChangeActivity.this).display(im_bank,
                    HttpUrls.HOST_POSM + imgurl,
                    im_bank.getWidth(),
                    im_bank.getHeight(), null, null);
//				Bitmap bit = null;
//				try {
//					 bit = GetImageUtil.iscace( im_bank,HttpUrls.HOST_POSM+imgurl);
//				} catch (Exception e) {
//					// TODO: handle exception\
//					Log.e("", ""+e.toString());
//				}
//				if(bit!=null){
//					im_bank.setImageBitmap(bit);
//				}else{
//				}
        }
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (null != bundle) {
                if (resultCode == 9) {
                    bankProvinceid = bundle.getString("companyId");
                    tvBankProvince.setText(bundle.getString("companyName"));
                } else if (resultCode == 8) {
                    bankCityid = bundle.getString("companyId");
                    tvBankCity.setText(bundle.getString("companyName"));
                } else if (resultCode == 7) {
                    bankBranchid = bundle.getString("companyId");
                    tvBankBranch.setText(bundle.getString("companyName"));
                }
            }
        }
    }

}
